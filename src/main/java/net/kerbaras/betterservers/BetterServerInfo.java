package net.kerbaras.betterservers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.network.ServerInfo;
import org.jetbrains.annotations.Nullable;

public class BetterServerInfo {
    private String ip;
    private String name;
    private ServerInfo.ResourcePackState resourcePackState;
    private String icon;
    private List<String> categories = new ArrayList<>();

    public static BetterServerInfo fromVanilla(ServerInfo serverInfo) {
        BetterServerInfo info = new BetterServerInfo();
        info.copyFromVanilla(serverInfo);
        return info;
    }

    private BetterServerInfo() {}

    public BetterServerInfo(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public void copyFromVanilla(ServerInfo serverInfo) {
        this.ip = serverInfo.address;
        this.name = serverInfo.name;
        this.resourcePackState = serverInfo.getResourcePack();
        this.icon = serverInfo.getIcon();
    }

    public ServerInfo toVanilla() {
        return null;
    }

    public void setResourcePackState(ServerInfo.ResourcePackState resourcePackState) {
        this.resourcePackState = resourcePackState;
    }

    public ServerInfo.ResourcePackState getResourcePackState() {
        return resourcePackState;
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

    public ArrayList<String> getCategories() {
        return (ArrayList<String>) this.categories;
    }
}
