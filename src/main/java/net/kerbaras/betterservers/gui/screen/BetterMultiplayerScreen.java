package net.kerbaras.betterservers.gui.screen;

import net.kerbaras.betterservers.BetterServerList;
import net.kerbaras.betterservers.gui.IWindow;
import net.kerbaras.betterservers.gui.widgets.IconButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class BetterMultiplayerScreen extends Screen {
    private static final Identifier CLOSE_ICON = new Identifier("betterservers", "icons/close.png");

    private boolean initialized = false;
    private final Screen parent;
    private final BetterServerList serverList;

    public BetterMultiplayerScreen(Screen parent) {
        super(new TranslatableText("multiplayer.title"));
        this.parent = parent;
        this.serverList = new BetterServerList(client);
    }

    @Override
    public void removed() {
        super.removed();
        assert client != null;
        //noinspection ConstantConditions
        ((IWindow) (Object) client.getWindow()).betterservers_setForcedScale(IWindow.UNFORCED_SCALE);
        client.onResolutionChanged();
        initialized = false;
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
        }
        addIconButton(0, CLOSE_ICON, "gui.cancel", () -> client.openScreen(parent));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void addIconButton(int index, Identifier icon, String tooltipTranslationKey, Runnable onPress) {
        final int SIZE = 32;
        addButton(new IconButtonWidget(width - SIZE - 10, 50 + index * SIZE, SIZE, SIZE, icon, new TranslatableText(tooltipTranslationKey), onPress));
    }
}
