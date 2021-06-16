package me.bopis.king.features.modules.misc;

import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BopisHack extends Module {
    public BopisHack() {
        super("bopis", "bopis", Module.Category.MISC, true, false, false);
    }
    public Setting<String> code = register(new Setting("Code", "/kill"));
    @SubscribeEvent
    public void onDisplayDeathScreen(GuiOpenEvent event) {
        if (AutoRespawn.mc.player.getHealth() <= 0.0f || AutoRespawn.mc.player.getHealth() > 0.0f) {
            event.setCanceled(true);
            AutoRespawn.mc.player.respawnPlayer();
        }
    }

    @Override
    public void onUpdate() {
        mc.player.sendChatMessage(code.getValueAsString());
    }

    @Override
    public void onDisable() {
        this.enable();
    }
}