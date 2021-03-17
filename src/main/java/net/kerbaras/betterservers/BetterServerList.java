package net.kerbaras.betterservers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.options.ServerList;
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
    private static final Gson GSON = new Gson();

    private final MinecraftClient mc;
    private final List<BetterServerInfo> servers = new ArrayList<>();
    private final HashMap<String, Set<BetterServerInfo>> categories = new HashMap<>();


    public BetterServerList(MinecraftClient mc) {
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
        vanillaServerListObj.loadFile();

        List<ServerInfo> vanillaServerList = new ArrayList<>(vanillaServerListObj.size());
        for (int i = 0; i < vanillaServerListObj.size(); i++) {
            vanillaServerList.add(vanillaServerListObj.get(i));
        }

        Map<String, List<ServerInfo>> vanillaServerListByIp = vanillaServerList.stream().collect(Collectors.groupingBy(info -> info.address));
        Map<String, List<ServerInfo>> vanillaServerListByName = vanillaServerList.stream().collect(Collectors.groupingBy(info -> info.name));


        // load our format

        // check that our format matches vanilla, if it doesn't match try to merge the vanilla format into our format
        // TODO: this will change
        try (Reader reader = Files.newBufferedReader(getBetterServersFile())) {
            servers.addAll(GSON.fromJson(reader, new TypeToken<ArrayList<BetterServerInfo>>(){}.getType()));
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.error("Failed to read better servers file", e);
            // continue in this method to recreate better servers from vanilla
        }

        Map<String, List<BetterServerInfo>> betterServerListByIp = servers.stream().collect(Collectors.groupingBy(BetterServerInfo::getIp));
        Map<String, List<BetterServerInfo>> betterServerListByName = servers.stream().collect(Collectors.groupingBy(BetterServerInfo::getName));

        vanillaServerListByIp.forEach((address, infos) -> {
            List<BetterServerInfo> betterInfos = betterServerListByIp.get(address);
            if (infos.size() == 1 && betterInfos != null && betterInfos.size() == 1) {
                // matched by ip
                betterInfos.get(0).copyFromVanilla(infos.get(0));
            } else {
                // Now we're matching by name
                for (ServerInfo info : infos) {
                    List<BetterServerInfo> betterInfosOfName = betterServerListByName.get(info.name);
                    if (betterInfosOfName != null && betterInfosOfName.size() == 1) {
                        // matched by name
                        betterInfosOfName.get(0).copyFromVanilla(info);
                    } else {
                        servers.add(BetterServerInfo.fromVanilla(info));
                    }
                }
            }
        });

        for (BetterServerInfo server : servers) {
            for (String ctgy : server.getCategories()) {
                getOrCreateCategory(ctgy).add(server);
            }
        }
    }

    public void save() {
        ServerList vanillaServerList = new ServerList(mc);
        // copy our data to vanilla
        for (BetterServerInfo server : servers) {
            vanillaServerList.add(server.toVanilla());
        }

        // save vanilla
        vanillaServerList.saveFile();

        // save our format
        try (Writer writer = Files.newBufferedWriter(getBetterServersFile())) {
            GSON.toJson(servers, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to write better servers file", e);
        }
    }

    public Set<BetterServerInfo> getOrCreateCategory(String name) {
        if (!this.categories.containsKey(name))
            this.categories.put(name, new HashSet<>());
        return this.categories.get(name);
    }
}
