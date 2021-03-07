package net.kerbaras.betterservers.mixin;

import net.kerbaras.betterservers.gui.screen.BetterMultiplayerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @ModifyVariable(method = "openScreen", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private @Nullable Screen modifyScreen(@Nullable Screen screen) {
        if (screen instanceof MultiplayerScreen) {
            return new BetterMultiplayerScreen(((MultiplayerScreenAccessor) screen).getParent());
        } else {
            return screen;
        }
    }
}
