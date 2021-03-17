package net.kerbaras.betterservers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.options.ServerList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class BetterServerList {
    private final MinecraftClient mc;
    private final List<BetterServerInfo> servers = new ArrayList<>();
    private HashMap<String, Set<BetterServerInfo>> folders = new HashMap<>();


    public BetterServerList(MinecraftClient mc) {
        this.mc = mc;
    }

    public void load() {
        ServerList vanillaServerList = new ServerList(mc);
        servers.clear();

        // load vanilla
        vanillaServerList.loadFile();

        // load our format

        // check that our format matches vanilla, if it doesn't match try to merge the vanilla format into our format
        // TODO: this will change
        JSONObject metaFile = readMetaFile();
        for (int i = 0; i < vanillaServerList.size(); i++) {
            BetterServerInfo info = BetterServerInfo.fromVanilla(vanillaServerList.get(i));
            servers.add(info);
            if (metaFile == null)
                continue;
            JSONObject metadata = (JSONObject) metaFile.get(info.ip);
            info.applyMetadata(metadata);
            for (String category : info.getCategories()) {
                this.getOrCreateFolder(category).add(info);
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
    }

    private JSONObject readMetaFile(){
        File file = new File(mc.runDirectory, "servers.dat");
        try {
            FileReader reader = new FileReader(file);
            JSONParser jsonParser = new JSONParser();
            return (JSONObject) jsonParser.parse(reader);
        }catch (Exception e){
            return null;
        }
    }

    public Set<BetterServerInfo> getOrCreateFolder(String name){
        if (!this.folders.containsKey(name))
            this.folders.put(name, new HashSet<>());
        return this.folders.get(name);
    }
}
