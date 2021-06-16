package me.bopis.king.features.modules.misc;

<<<<<<< Updated upstream:src/main/java/me/bopis/king/features/modules/misc/BopisHack.java
import me.bopis.king.features.modules.Module;
=======
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
>>>>>>> Stashed changes:src/main/java/me/alpha432/oyvey/features/modules/misc/BopisHack.java
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