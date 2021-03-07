package net.kerbaras.betterservers.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IconButtonWidget extends AbstractPressableButtonWidget {
    private static final int ICON_SIZE = 64;

    private static final Text MESSAGE = new LiteralText("");
    private final Identifier icon;
    private final Text tooltip;
    private final Runnable onPress;

    public IconButtonWidget(int x, int y, int width, int height, Identifier icon, Text tooltip, Runnable onPress) {
        super(x, y, width, height, MESSAGE);
        this.icon = icon;
        this.tooltip = tooltip;
        this.onPress = onPress;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.getTextureManager().bindTexture(WIDGETS_LOCATION);
        RenderSystem.color4f(1, 1, 1, alpha);
        int imageY = getYImage(isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        drawTexture(matrices, x, y, width / 2, height, 0, 46 + imageY * 20, width / 2, 20, 256, 256);
        drawTexture(matrices, x + width / 2, y, width / 2, height, width / 2f, 46 + imageY * 20, width / 2, 20, 256, 256);

        mc.getTextureManager().bindTexture(icon);
        RenderSystem.enableDepthTest();
        drawTexture(matrices, x, y, width, height, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

        if (isHovered()) {
            Screen currentScreen = mc.currentScreen;
            if (currentScreen != null) {
                currentScreen.renderTooltip(matrices, tooltip, mouseX, mouseY);
            }
        }
    }

    @Override
    public void onPress() {
        onPress.run();
    }
}
