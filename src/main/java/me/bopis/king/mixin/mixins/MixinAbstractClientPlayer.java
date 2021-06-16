package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.Bopis;
import me.alpha432.oyvey.features.modules.player.LiquidInteract;
import me.alpha432.oyvey.util.SkinStorageManipulationer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.UUID;

@Mixin(value = {AbstractClientPlayer.class})
public class MixinAbstractClientPlayer {
    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        // cape shit goes here
        UUID uuid = Objects.requireNonNull(Minecraft.getMinecraft().player.getGameProfile().getId());

        if (Bopis.capemanager.isOg(uuid)) {
            // callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/cape-old.png"));
        }

        if (Bopis.capemanager.isDonator(uuid)) {
            try {
                BufferedImage image = Bopis.capemanager.getCapeFromDonor(uuid);
                DynamicTexture texture = new DynamicTexture(image);
                SkinStorageManipulationer.WrappedResource wr = new SkinStorageManipulationer.WrappedResource(
                        FMLClientHandler.instance().getClient().getTextureManager().getDynamicTextureLocation(uuid.toString(), texture)
                );
                callbackInfoReturnable.setReturnValue(wr.location);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
