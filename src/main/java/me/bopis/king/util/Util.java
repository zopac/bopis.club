package me.bopis.king.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public interface Util {

    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static final Minecraft mc = Minecraft.getMinecraft();
}