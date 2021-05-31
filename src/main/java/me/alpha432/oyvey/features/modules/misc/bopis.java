package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;

public class bopis extends Module {
    public bopis() {
        super("bopis", "bopis", Module.Category.MISC, true, false, false);
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