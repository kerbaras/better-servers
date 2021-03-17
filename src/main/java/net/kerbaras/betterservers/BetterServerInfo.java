package net.kerbaras.betterservers;

import java.util.ArrayList;
import net.minecraft.client.network.ServerInfo;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BetterServerInfo {
    public final String name;
    public final String ip;
    private ServerInfo.ResourcePackState resourcePackState;
    private String icon;
    private JSONArray categories;

    public static BetterServerInfo fromVanilla(ServerInfo serverInfo) {
        BetterServerInfo info = new BetterServerInfo(serverInfo.name, serverInfo.address);
        info.setIcon(serverInfo.getIcon());
        info.setResourcePackState(serverInfo.getResourcePack());
        return info;
    }

    public BetterServerInfo(String name, String ip){
        this.name = name;
        this.ip = ip;
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


    @Nullable
    public String getIcon() {
        return this.icon;
    }

    public void setIcon(@Nullable String string) {
        this.icon = string;
    }

    public void applyMetadata(JSONObject metadata) {
        if (metadata == null)
            return;
        // TODO: Handle metadata
        this.categories = (JSONArray) metadata.getOrDefault("categories", new JSONArray());
    }

    public ArrayList<String> getCategories() {
        return (ArrayList<String>) this.categories;
    }
}
