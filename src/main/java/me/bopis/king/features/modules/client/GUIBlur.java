package me.bopis.king.features.modules.client;

import me.bopis.king.features.modules.Module;
import me.bopis.king.util.Util;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIBlur extends Module {

    public GUIBlur() {
        super("GUIBlur", "nigga", Module.Category.CLIENT, true, false, false);
    }

    public void onDisable() {
        if (mc.world != null) {
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    public void onUpdate() {
        if (mc.world != null) {
            if (ClickGui.getInstance().isEnabled() || mc.currentScreen != null) {
                if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
                    if (mc.entityRenderer.getShaderGroup() != null) {
                        mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                    }
                    try {
                        Util.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (mc.entityRenderer.getShaderGroup() != null && mc.currentScreen == null) {
                    mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            } else if (mc.entityRenderer.getShaderGroup() != null) {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
    }
}