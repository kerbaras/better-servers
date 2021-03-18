package net.kerbaras.betterservers.mixin;

import net.kerbaras.betterservers.gui.IWindow;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Window.class)
public class WindowMixin implements IWindow {
    @Unique
    private double forcedScale = UNFORCED_SCALE;

    @Override
    public void betterservers_setForcedScale(double forcedScale) {
        this.forcedScale = forcedScale;
    }

    @ModifyVariable(method = "setScaleFactor", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double modifyScaleFactor(double scaleFactor) {
        if (forcedScale == UNFORCED_SCALE) {
            return scaleFactor;
        } else {
            return forcedScale;
        }
    }
}
