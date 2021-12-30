package net.kerbaras.betterservers.mixin;

import net.kerbaras.betterservers.gui.screen.BetterMultiplayerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @ModifyVariable(method = "setScreen", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private @Nullable Screen modifyScreen(@Nullable Screen screen) {
        if (screen instanceof JoinMultiplayerScreen) {
            return new BetterMultiplayerScreen(((JoinMultiplayerScreenAccessor) screen).getLastScreen());
        } else {
            return screen;
        }
    }
}
