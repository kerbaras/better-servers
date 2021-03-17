package net.kerbaras.betterservers.gui.screen;

import net.kerbaras.betterservers.BetterServerList;
import net.kerbaras.betterservers.gui.IWindow;
import net.kerbaras.betterservers.gui.widgets.IconButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.options.ServerList;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class BetterMultiplayerScreen extends Screen {
    private static final Identifier CLOSE_ICON = new Identifier("betterservers", "icons/close.png");
    private static final Identifier GRID_ICON = new Identifier("betterservers", "icons/appstore.png");
    private static final Identifier LIST_ICON = new Identifier("betterservers", "icons/bars.png");
    private static final Identifier CONNECT_ICON = new Identifier("betterservers", "icons/connect.png");
    private static final Identifier ADD_FOLDER_ICON = new Identifier("betterservers", "icons/folder-add.png");
    private static final Identifier EDIT_ICON = new Identifier("betterservers", "icons/edit.png");
    private static final Identifier DELETE_ICON = new Identifier("betterservers", "icons/minus.png");

    private boolean initialized = false;
    private final Screen parent;
    private BetterServerList serverList;
    private TextFieldWidget search;

    public BetterMultiplayerScreen(Screen parent) {
        super(new TranslatableText("multiplayer.title"));
        this.parent = parent;
    }

    @Override
    public void removed() {
        super.removed();
        assert client != null;
        //noinspection ConstantConditions
        ((IWindow) (Object) client.getWindow()).betterservers_setForcedScale(IWindow.UNFORCED_SCALE);
        client.onResolutionChanged();
        initialized = false;

        serverList.save();
    }

    @Override
    protected void init() {
        super.init();
        assert client != null;

        if (!initialized) {
            initialized = true;
            //noinspection ConstantConditions
            ((IWindow) (Object) client.getWindow()).betterservers_setForcedScale(1);
            client.onResolutionChanged();

            serverList = new BetterServerList(client);
            serverList.load();
        }


        this.addIconButton(0, 0, CLOSE_ICON, "gui.cancel", () -> client.openScreen(parent)).padding(2);
        this.addIconButton(1, 0, LIST_ICON, "betterservers.gui.list", () -> {}).padding(2);
        this.addIconButton(2, 0, GRID_ICON, "betterservers.gui.grid", () -> {}).padding(0);


        this.addIconButton(0, 1, CONNECT_ICON, "selectServer.direct", () -> {}).padding(1);
        this.addIconButton(1, 1, EDIT_ICON, "selectServer.edit", () -> {}).padding(1);
        this.addIconButton(2, 1, DELETE_ICON, "selectServer.delete", () -> {}).padding(1);
        this.addIconButton(3, 1, ADD_FOLDER_ICON, "selectServer.add", () -> {});

        this.search = new TextFieldWidget(this.textRenderer, 15, 15, this.getSearchWidth(), 24, new TranslatableText("selectWorld.search"));
        this.children.add(this.search);
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        search.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private IconButtonWidget addIconButton(int gridX, int gridY, Identifier icon, String tooltipTranslationKey, Runnable onPress) {
        final int SIZE = 24;
        return addButton(new IconButtonWidget(width - 15 - SIZE - gridX * (SIZE + 8), 15 + gridY * (SIZE + 12), SIZE, SIZE, icon, new TranslatableText(tooltipTranslationKey), onPress));
    }

    private int getSearchWidth() {
        return Math.max(width / 3, 200);
    }
}
