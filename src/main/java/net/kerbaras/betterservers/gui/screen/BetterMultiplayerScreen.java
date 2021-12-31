package net.kerbaras.betterservers.gui.screen;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kerbaras.betterservers.BetterServerList;
import net.kerbaras.betterservers.gui.IWindow;
import net.kerbaras.betterservers.gui.widgets.IconButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class BetterMultiplayerScreen extends Screen {
    private static final double SCALE_FOR_1080P = 1.5;

    private static final ResourceLocation CLOSE_ICON = new ResourceLocation("betterservers", "icons/close.png");
    private static final ResourceLocation GRID_ICON = new ResourceLocation("betterservers", "icons/appstore.png");
    private static final ResourceLocation LIST_ICON = new ResourceLocation("betterservers", "icons/bars.png");
    private static final ResourceLocation CONNECT_ICON = new ResourceLocation("betterservers", "icons/connect.png");
    private static final ResourceLocation ADD_FOLDER_ICON = new ResourceLocation("betterservers", "icons/folder-add.png");
    private static final ResourceLocation EDIT_ICON = new ResourceLocation("betterservers", "icons/edit.png");
    private static final ResourceLocation DELETE_ICON = new ResourceLocation("betterservers", "icons/minus.png");

    private boolean initialized = false;
    private boolean hasRescaled = false;
    private final Screen parent;
    private BetterServerList serverList;
    private EditBox search;

    public BetterMultiplayerScreen(Screen parent) {
        super(new TranslatableComponent("multiplayer.title"));
        this.parent = parent;
    }

    @Override
    public void removed() {
        super.removed();
        assert minecraft != null;
        ((IWindow) (Object) minecraft.getWindow()).betterservers_setForcedScale(IWindow.UNFORCED_SCALE);
        minecraft.resizeDisplay();
        initialized = false;

        serverList.save();
    }

    @Override
    protected void init() {
        super.init();
        assert minecraft != null;

        // Set custom window scale for this GUI
        if (!hasRescaled) {
            double forcedScale;
            Monitor monitor = minecraft.getWindow().findBestMonitor();
            if (monitor == null) {
                forcedScale = IWindow.UNFORCED_SCALE;
            } else {
                VideoMode videoMode = monitor.getCurrentMode();
                double ratioFromExpected = (videoMode.getWidth() / 1280.0 + videoMode.getHeight() / 720.0) * 0.5;
                forcedScale = SCALE_FOR_1080P * ratioFromExpected;
            }
            ((IWindow) (Object) minecraft.getWindow()).betterservers_setForcedScale(forcedScale);
            hasRescaled = true; // Prevents infinite recursion
            minecraft.resizeDisplay();
            hasRescaled = false;
        }

        if (!initialized) {
            initialized = true;

            serverList = new BetterServerList(minecraft);
            serverList.load();
        }


        this.addIconButton(0, 0, CLOSE_ICON, "gui.cancel", () -> minecraft.setScreen(parent)).padding(2);
        this.addIconButton(1, 0, LIST_ICON, "betterservers.gui.list", () -> {}).padding(2);
        this.addIconButton(2, 0, GRID_ICON, "betterservers.gui.grid", () -> {}).padding(0);


        this.addIconButton(0, 1, CONNECT_ICON, "selectServer.direct", () -> {}).padding(1);
        this.addIconButton(1, 1, EDIT_ICON, "selectServer.edit", () -> {}).padding(1);
        this.addIconButton(2, 1, DELETE_ICON, "selectServer.delete", () -> {}).padding(1);
        this.addIconButton(3, 1, ADD_FOLDER_ICON, "selectServer.add", () -> {});

        this.search = new EditBox(this.font, 15, 15, this.getSearchWidth(), 24, new TranslatableComponent("selectWorld.search"));
        this.addRenderableWidget(this.search);
        this.setInitialFocus(this.search);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return this.search.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return this.search.charTyped(chr, modifiers);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private IconButton addIconButton(int gridX, int gridY, ResourceLocation icon, String tooltipTranslationKey, Runnable onPress) {
        final int SIZE = 24;
        return addRenderableWidget(new IconButton(width - 15 - SIZE - gridX * (SIZE + 8), 15 + gridY * (SIZE + 12), SIZE, SIZE, icon, new TranslatableComponent(tooltipTranslationKey), onPress));
    }

    private int getSearchWidth() {
        return Math.max(width / 3, 200);
    }
}
