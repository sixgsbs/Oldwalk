package com.oldwalk.mixin;

import com.oldwalk.OldWalkConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "tickHeadTurn", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float oldwalk$rotateBackwards(float yRot) {
        if (!OldWalkConfig.rotateBackwardsWalking) return yRot;
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player) || self.attackAnim > 0.0F) return yRot;
        double dx = self.getX() - self.xo;
        double dz = self.getZ() - self.zo;
        if (dx * dx + dz * dz > 0.0025F) {
            return (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
        }
        return yRot;
    }

    @Inject(method = "getMaxHeadRotationRelativeToBody", at = @At("HEAD"), cancellable = true)
    private void oldwalk$headLimit(CallbackInfoReturnable<Float> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player)) return;
        boolean blocking = OldWalkConfig.uncapBlockingHeadRotation && self.isBlocking();
        if (OldWalkConfig.rotateBackwardsWalking || blocking) {
            cir.setReturnValue(75.0F);
        }
    }
}
