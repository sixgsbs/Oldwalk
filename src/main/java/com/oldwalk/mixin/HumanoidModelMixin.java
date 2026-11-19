package com.oldwalk.mixin;

import com.oldwalk.OldWalkConfig;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart body;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightLeg;
    @Shadow @Final public ModelPart leftLeg;

    @Inject(
        method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
        at = @At("TAIL")
    )
    private void oldwalk$oldSneak(HumanoidRenderState state, CallbackInfo ci) {
        if (!OldWalkConfig.oldSneakAnimation || !state.isCrouching) return;
        this.head.y = 1.0F;
        this.body.y = 0.0F;
        this.rightArm.y = 2.0F;
        this.leftArm.y = 2.0F;
        this.rightLeg.y = 9.0F;
        this.leftLeg.y = 9.0F;
    }
}
