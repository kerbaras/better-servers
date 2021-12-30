package net.kerbaras.betterservers.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kerbaras.betterservers.gui.screen.BetterMultiplayerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

public class ServerListWidget extends ObjectSelectionList<ServerListWidget.Entry> {

    private final BetterMultiplayerScreen screen;

    public ServerListWidget(BetterMultiplayerScreen screen, Minecraft client, int width, int height, int top, int bottom, int entryHeight) {
        super(client, width, height, top, bottom, entryHeight);
        this.screen = screen;
    }

    private void updateEntries() {
//        this.clearEntries();
//        this.servers.forEach(this::addEntry);
//        this.addEntry(this.scanningEntry);
//        this.lanServers.forEach(this::addEntry);
    }


    public class Entry extends ObjectSelectionList.Entry<Entry> {

        @Override
        public void render(PoseStack stack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {

        }

        @Override
        public void mouseMoved(double mouseX, double mouseY) {

        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return false;
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return false;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            return false;
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
            return false;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return false;
        }

        @Override
        public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
            return false;
        }

        @Override
        public boolean charTyped(char chr, int modifiers) {
            return false;
        }

        @Override
        public Component getNarration() {
            return null;
        }
    }
}
