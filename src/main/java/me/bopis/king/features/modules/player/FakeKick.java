package me.bopis.king.features.modules.player;

import me.bopis.king.features.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;

public class FakeKick extends Module {
    public FakeKick() {
        super("FakeKick", "Say you were ddosed", Category.PLAYER, true, false, false);
    }

    public void onEnable() {
        Minecraft.getMinecraft().getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString("Internal Exception: java.lang.NullPointerException")));
        this.disable();
    }
}