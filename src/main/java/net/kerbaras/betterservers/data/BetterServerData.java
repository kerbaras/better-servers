package net.kerbaras.betterservers.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.multiplayer.ServerData;
import org.jetbrains.annotations.Nullable;

public class BetterServerData {
    public String id = UUID.randomUUID().toString();
    public String ip;
    public String name;
    @Nullable public ServerData.ServerPackStatus resourcePackStatus;
    @Nullable public String icon;
    public final List<String> labels = new ArrayList<>();

    public static BetterServerData fromVanilla(ServerData serverData) {
        BetterServerData data = new BetterServerData();
        data.copyFromVanilla(serverData);
        return data;
    }

    private BetterServerData() {}

    public BetterServerData(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public void copyFromVanilla(ServerData serverData) {
        this.ip = serverData.ip;
        this.name = serverData.name;
        this.resourcePackStatus = serverData.getResourcePackStatus();
        this.icon = serverData.getIconB64();
    }

    public ServerData toVanilla() {
        ServerData data = new ServerData(name, ip, false);
        data.setResourcePackStatus(resourcePackStatus);
        data.setIconB64(icon);
        return data;
    }
}
