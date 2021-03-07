package net.kerbaras.betterservers.mixin;

import net.kerbaras.betterservers.gui.IWindow;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Window.class)
public class WindowMixin implements IWindow {
    @Unique
    private int forcedScale = UNFORCED_SCALE;

    @Override
    public void betterservers_setForcedScale(int forcedScale) {
        this.forcedScale = forcedScale;
    }

    @Inject(method = "calculateScaleFactor", at = @At("HEAD"), cancellable = true)
    private void onCalculateScaleFactor(CallbackInfoReturnable<Integer> ci) {
        if (forcedScale != UNFORCED_SCALE) {
            ci.setReturnValue(forcedScale);
        }
    }
}
