package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.event.events.DeathEvent;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BopisHack extends Module {
    public BopisHack() {
        super("bopis", "bopis", Module.Category.MISC, true, false, false);
    }
    
    @SubscribeEvent
    public void onDeathEvent(DeathEvent event) {
    	this.disable();
    }

    @Override
    public void onUpdate() {
        mc.player.sendChatMessage("/kill");
    }

    @Override
    public void onDisable() {
        this.enable();
    }
}