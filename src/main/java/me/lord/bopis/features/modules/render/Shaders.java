package me.lord.bopis.features.modules.render;

import me.lord.bopis.features.modules.Module;
import me.lord.bopis.features.setting.Setting;
import me.lord.bopis.util.Util;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class Shaders extends Module {
    
    public Setting<Mode> shader = register(new Setting<Mode>("Mode", Mode.green));

    private static Shaders INSTANCE = new Shaders();
    public Shaders() {
        super("Shaders", "i dont even know anymore", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (OpenGlHelper.shadersSupported && Util.mc.getRenderViewEntity() instanceof EntityPlayer) {
            if ( Util.mc.entityRenderer.getShaderGroup() != null) {
                Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            try {
                Util.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/" + shader.getValue() + ".json"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if ( Util.mc.entityRenderer.getShaderGroup() != null && Util.mc.currentScreen == null) {
            Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override
    public String getDisplayInfo() {
        return shader.currentEnumName();
    }

    @Override
    public void onDisable() {
        if ( Util.mc.entityRenderer.getShaderGroup() != null) {
            Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
    public enum Mode{
        notch, antialias, art, bits, blobs, blobs2, blur, bumpy, color_convolve, creeper, deconverge, desaturate, entity_outline, flip, fxaa, green, invert, ntsc, outline, pencil, phosphor, scan_pincusion, sobel, spider, wobble
    }
}
