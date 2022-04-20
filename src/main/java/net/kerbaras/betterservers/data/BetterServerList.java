package net.kerbaras.betterservers.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.kerbaras.betterservers.helper.ServerListHelper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class BetterServerList {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Minecraft mc;
    private final List<BetterServerData> servers = new ArrayList<>();
    private final HashMap<String, FilterData> savedViews = new HashMap<>();

    public BetterServerList(Minecraft mc) {
        this.mc = mc;
    }

    private static Path getBetterServersDir() {
        return FabricLoader.getInstance().getConfigDir().resolve("better_servers");
    }

    private static Path getBetterServersFile() {
        return getBetterServersDir().resolve("servers.json");
    }

    private static Path getSavedViewsFile() {
        return getBetterServersDir().resolve("saved_views.json");
    }

    public void load() {
        ServerList vanillaServerListObj = new ServerList(mc);

        // load vanilla
        vanillaServerListObj.load();

        List<ServerData> vanillaServerList = new ArrayList<>(vanillaServerListObj.size());
        for (int i = 0; i < vanillaServerListObj.size(); i++) {
            vanillaServerList.add(vanillaServerListObj.get(i));
        }

        // load our format
        servers.clear();
        loadFromDisk().ifPresent(candidates ->
                servers.addAll(ServerListHelper.load(vanillaServerList, candidates)));

        // load saved views
        savedViews.clear();
        loadViewsFromDisk().ifPresent(savedViews::putAll);
    }

    private Optional<List<BetterServerData>> loadFromDisk(){
        // check that our format matches vanilla, if it doesn't match try to merge the vanilla format into our format
        Path betterServersFile = getBetterServersFile();
        if (Files.exists(betterServersFile)) {
            try (Reader reader = Files.newBufferedReader(betterServersFile)) {
                return Optional.of(GSON.fromJson(reader, new TypeToken<ArrayList<BetterServerData>>(){}.getType()));
            } catch (IOException | JsonSyntaxException e) {
                LOGGER.error("Failed to read better servers file", e);
            }
        }
        return Optional.empty();
    }

    private Optional<Map<String, FilterData>> loadViewsFromDisk(){
        // check that our format matches vanilla, if it doesn't match try to merge the vanilla format into our format
        Path savedViewsFile = getSavedViewsFile();
        if (Files.exists(savedViewsFile)) {
            try (Reader reader = Files.newBufferedReader(savedViewsFile)) {
                return Optional.of(GSON.fromJson(reader, new TypeToken<Map<String, FilterData>>(){}.getType()));
            } catch (IOException | JsonSyntaxException e) {
                LOGGER.error("Failed to read saved views file", e);
            }
        }
        return Optional.empty();
    }

    public void save() {
        ServerList vanillaServerList = new ServerList(mc);

        // copy our data to vanilla
        int size = vanillaServerList.size();
        int index = 0;
        for (BetterServerData server : servers) {
            if (index < size)
                vanillaServerList.replace(index++, server.toVanilla());
            else
                vanillaServerList.add(server.toVanilla());
        }

        // save vanilla
        vanillaServerList.save();

        // save our format
        try {
            safeSave(servers, getBetterServersFile());
            safeSave(savedViews, getSavedViewsFile());
        } catch (IOException e){
            LOGGER.error("Failed to save data");
        }
    }

    private <T> void safeSave(T write, Path file) throws IOException {
        Path betterServersDir = getBetterServersDir();
        Files.createDirectories(betterServersDir);
        String name = FilenameUtils.getBaseName(file.toString());
        Path tempFile = Files.createTempFile(betterServersDir, name, ".json");
        try (Writer writer = Files.newBufferedWriter(tempFile)) {
            GSON.toJson(write, writer);
            writer.flush();
        }
        Util.safeReplaceFile(file, tempFile, getBetterServersDir().resolve(name.concat(".json_old")));
    }

    public void setSavedView(String name, FilterData data) {
       savedViews.put(name, data);
    }

    public void removeSavedView(String name) {
        savedViews.remove(name);
    }

    public Optional<FilterData> getSavedView(String name) {
        if (!savedViews.containsKey(name)) return Optional.empty();
        return Optional.of(savedViews.get(name));
    }

    public Set<String> getSavedViews() {
        return Collections.unmodifiableSet(savedViews.keySet());
    }

    public void addServer(BetterServerData server) {
        servers.add(server);
    }

    public void removeServer(BetterServerData server) {
        servers.remove(server);
    }

    public List<BetterServerData> getServers() {
        return Collections.unmodifiableList(servers);
    }
}
