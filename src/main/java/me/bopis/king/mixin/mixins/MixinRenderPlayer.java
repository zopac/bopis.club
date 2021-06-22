package me.bopis.king.mixin.mixins;

import me.bopis.king.Bopis;
import me.bopis.king.features.modules.render.TexturedChams;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderPlayer.class})

public class MixinRenderPlayer {
    @Inject(method = "renderEntityName", at = @At("HEAD"), cancellable = true)
    public void renderEntityNameHook(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo info) {
        if (Bopis.moduleManager.isModuleEnabled("NameTags"))
            info.cancel();
    }

    @Overwrite
    public ResourceLocation getEntityTexture(final AbstractClientPlayer entity) {
        if (Bopis.moduleManager.isModuleEnabled("TexturedChams")) {
            GlStateManager.color(TexturedChams.red.getValue() / 255.0f, TexturedChams.green.getValue() / 255.0f, TexturedChams.blue.getValue() / 255.0f, TexturedChams.alpha.getValue() / 255.0f);
            GlStateManager.resetColor();
            return new ResourceLocation("minecraft:steve_skin1.png");
        }
        GlStateManager.resetColor();
        return entity.getLocationSkin();
    }

}