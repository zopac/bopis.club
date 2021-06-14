package me.bopis.king.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.bopis.king.Bopis;
import me.bopis.king.event.events.PacketEvent;
import me.bopis.king.features.Feature;
import me.bopis.king.features.command.Command;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReloadManager
        extends Feature {
    public String prefix;

    public void init(String prefix) {
        this.prefix = prefix;
        MinecraftForge.EVENT_BUS.register(this);
        if (!ReloadManager.fullNullCheck()) {
            Command.sendMessage(ChatFormatting.RED + "bopis has been unloaded. Type " + prefix + "reload to reload.");
        }
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketChatMessage packet;
        if (event.getPacket() instanceof CPacketChatMessage && (packet = event.getPacket()).getMessage().startsWith(this.prefix) && packet.getMessage().contains("reload")) {
            Bopis.load();
            event.setCanceled(true);
        }
    }
}

