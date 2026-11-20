package com.oldwalk.mixin;

import com.oldwalk.OldWalkConfig;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Inject(
        method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V",
        at = @At("TAIL")
    )
    private void oldwalk$friendColor(Entity entity, EntityRenderState state, float partialTick, CallbackInfo ci) {
        if (!(entity instanceof Player) || state.nameTag == null || OldWalkConfig.friends.isEmpty()) return;
        Integer rgb = OldWalkConfig.friends.get(entity.getName().getString().toLowerCase(Locale.ROOT));
        if (rgb == null) return;
        int color = rgb;
        state.nameTag = state.nameTag.copy().withStyle(s -> s.withColor(color));
    }
}
