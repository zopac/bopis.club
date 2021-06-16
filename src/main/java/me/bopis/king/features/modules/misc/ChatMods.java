package me.bopis.king.features.modules.misc;

import me.bopis.king.event.events.PacketEvent;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

/**
 * @Author evaan on 6/16/2021
 * https://github.com/evaan
 */
public class ChatMods extends Module {
    public ChatMods() {
        super("ChatMods", "what the chat doin", Module.Category.MISC, true, false, false);
    }

    public Setting<Boolean> suffix = register(new Setting<Boolean>("Suffix", true));
    public Setting<String> suffixT = register(new Setting<String>("Suffix Text", "\uff5c \u0299\u1d0f\u1d18\u026a\ua731\u002e\u1d04\u029f\u1d1c\u0299"));
    public Setting<Boolean> greenText = register(new Setting<Boolean>("Green Text", false));

    @SubscribeEvent
    public void onSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith("/")) return;
            String message = "";
            if (greenText.getValue()) message = "> " + ((CPacketChatMessage) event.getPacket()).getMessage();
            else message = ((CPacketChatMessage) event.getPacket()).getMessage();
            if (suffix.getValue()) message += " " + suffixT.getValue();
            if (message.length() > 255) return;
            else ((CPacketChatMessage) event.getPacket()).message = message;
        }
    }
}
