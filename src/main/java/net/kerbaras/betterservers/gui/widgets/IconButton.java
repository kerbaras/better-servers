package net.kerbaras.betterservers.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class IconButton extends AbstractButton {
    private static final int ICON_SIZE = 64;

    private static final Component MESSAGE = new TextComponent("");
    private final ResourceLocation icon;
    private final Runnable onPress;
    private final Component tooltip;
    private int padding = 0;

    public IconButton(int x, int y, int width, int height, ResourceLocation icon, Component tooltip, Runnable onPress) {
        super(x, y, width, height, MESSAGE);
        this.icon = icon;
        this.tooltip = tooltip;
        this.onPress = onPress;
    }

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float delta) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1, 1, 1, alpha);
        int imageY = getYImage(isHoveredOrFocused());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        blit(stack, x, y, width / 2, height, 0, 46 + imageY * 20, width / 2, 20, 256, 256);
        blit(stack, x + width / 2, y, width / 2, height, 200 - width / 2f, 46 + imageY * 20, width / 2, 20, 256, 256);

        RenderSystem.setShaderTexture(0, icon);
        RenderSystem.enableDepthTest();
        blit(stack, x + padding , y + padding, width - 2 * padding, height - 2 * padding, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

        if (isHoveredOrFocused()) {
            Screen currentScreen = mc.screen;
            if (currentScreen != null) {
                currentScreen.renderTooltip(stack, tooltip, mouseX, mouseY);
            }
        }
    }

    @Override
    public void onPress() {
        onPress.run();
    }

    public IconButton padding(int padding){
        this.padding = padding;
        return this;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        // TODO: narration
    }
}
