package net.kerbaras.betterservers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class BetterServerList {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Minecraft mc;
    private final List<BetterServerData> servers = new ArrayList<>();
    private final HashMap<String, Set<BetterServerData>> categories = new HashMap<>();


    public BetterServerList(Minecraft mc) {
        this.mc = mc;
    }

    private static Path getBetterServersFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("better_servers").resolve("servers.json");
    }

    public void load() {
        ServerList vanillaServerListObj = new ServerList(mc);
        servers.clear();
        categories.clear();

        // load vanilla
        vanillaServerListObj.load();

        List<ServerData> vanillaServerList = new ArrayList<>(vanillaServerListObj.size());
        for (int i = 0; i < vanillaServerListObj.size(); i++) {
            vanillaServerList.add(vanillaServerListObj.get(i));
        }

        Map<String, List<ServerData>> vanillaServerListByIp = vanillaServerList.stream().collect(Collectors.groupingBy(info -> info.ip));
        Map<String, List<ServerData>> vanillaServerListByName = vanillaServerList.stream().collect(Collectors.groupingBy(info -> info.name));


        // load our format

        // check that our format matches vanilla, if it doesn't match try to merge the vanilla format into our format


        Path betterServersFile = getBetterServersFile();
        if (Files.exists(betterServersFile)) {
            try (Reader reader = Files.newBufferedReader(betterServersFile)) {
                servers.addAll(GSON.fromJson(reader, new TypeToken<ArrayList<BetterServerData>>(){}.getType()));
            } catch (IOException | JsonSyntaxException e) {
                LOGGER.error("Failed to read better servers file", e);
                // continue in this method to recreate better servers from vanilla
            }
        }

        Map<String, List<BetterServerData>> betterServerListByIp = servers.stream().collect(Collectors.groupingBy(BetterServerData::getIp));
        Map<String, List<BetterServerData>> betterServerListByName = servers.stream().collect(Collectors.groupingBy(BetterServerData::getName));

        // TODO: add some logging

        vanillaServerListByIp.forEach((address, datas) -> {
            List<BetterServerData> betterDatas = betterServerListByIp.get(address);
            if (datas.size() == 1 && betterDatas != null && betterDatas.size() == 1) {
                // matched by ip
                betterDatas.get(0).copyFromVanilla(datas.get(0));
            } else {
                // Now we're matching by name
                for (ServerData data : datas) {
                    List<BetterServerData> betterInfosOfName = betterServerListByName.get(data.name);
                    if (betterInfosOfName != null && betterInfosOfName.size() == 1) {
                        // matched by name
                        betterInfosOfName.get(0).copyFromVanilla(data);
                    } else {
                        servers.add(BetterServerData.fromVanilla(data));
                    }
                }
            }
        });

        for (BetterServerData server : servers) {
            for (String ctgy : server.getCategories()) {
                getOrCreateCategory(ctgy).add(server);
            }
        }
    }

    public void save() {
        ServerList vanillaServerList = new ServerList(mc);
        // copy our data to vanilla
        for (BetterServerData server : servers) {
            vanillaServerList.add(server.toVanilla());
        }

        // save vanilla
        vanillaServerList.save();

        // save our format
        Path betterServersFile = getBetterServersFile();
        try {
            Files.createDirectories(betterServersFile.getParent());
            try (Writer writer = Files.newBufferedWriter(betterServersFile)) {
                GSON.toJson(servers, writer);
                writer.flush();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to write better servers file", e);
        }
    }

    public Set<BetterServerData> getOrCreateCategory(String name) {
        if (!this.categories.containsKey(name))
            this.categories.put(name, new HashSet<>());
        return this.categories.get(name);
    }
}
