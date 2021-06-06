package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatSuffix extends Module {
    public ChatSuffix() {
        super("ChatSuffix", "Appends your message", Module.Category.MISC, false, false, false);
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        String suffix = "bopis.club";
        if (event.getMessage().startsWith("/") || event.getMessage().startsWith(".")
                || event.getMessage().startsWith(",") || event.getMessage().startsWith("-")
                || event.getMessage().startsWith("$") || event.getMessage().startsWith("*")) return;
        event.setMessage(event.getMessage() + suffix); // Adds the suffix to the end of the message
    }
}