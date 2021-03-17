package net.kerbaras.betterservers.gui.widgets;

import net.kerbaras.betterservers.gui.screen.BetterMultiplayerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;

public class ServerListWidget extends AlwaysSelectedEntryListWidget<ServerListWidget.Entry> {


    private final BetterMultiplayerScreen screen;

    public ServerListWidget(BetterMultiplayerScreen screen, MinecraftClient client, int width, int height, int top, int bottom, int entryHeight) {
        super(client, width, height, top, bottom, entryHeight);
        this.screen = screen;
    }

    private void updateEntries() {
        this.clearEntries();
        this.servers.forEach(this::addEntry);
        this.addEntry(this.scanningEntry);
        this.lanServers.forEach(this::addEntry);
    }


    public class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {

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
    }
}
