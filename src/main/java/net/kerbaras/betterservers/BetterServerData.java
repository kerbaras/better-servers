package net.kerbaras.betterservers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.multiplayer.ServerData;
import org.jetbrains.annotations.Nullable;

public class BetterServerData {
    private String ip;
    private String name;
    private ServerData.ServerPackStatus resourcePackStatus;
    private String icon;
    private final List<String> categories = new ArrayList<>();

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

    public void setResourcePackStatus(ServerData.ServerPackStatus resourcePackStatus) {
        this.resourcePackStatus = resourcePackStatus;
    }

    public ServerData.ServerPackStatus getResourcePackStatus() {
        return resourcePackStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getIcon() {
        return this.icon;
    }

    public void setIcon(@Nullable String string) {
        this.icon = string;
    }

    public List<String> getCategories() {
        return this.categories;
    }
}
