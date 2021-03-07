package net.kerbaras.betterservers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.ServerList;

import java.util.ArrayList;
import java.util.List;

public class BetterServerList {
    private final MinecraftClient mc;
    private final List<BetterServerInfo> servers = new ArrayList<>();


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
        for (int i = 0; i < vanillaServerList.size(); i++) {
            servers.add(BetterServerInfo.fromVanilla(vanillaServerList.get(i)));
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
}
