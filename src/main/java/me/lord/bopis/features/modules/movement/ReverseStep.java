package me.lord.bopis.features.modules.movement;

import me.lord.bopis.features.modules.Module;
import me.lord.bopis.util.Util;

public class ReverseStep extends Module {
    private static ReverseStep INSTANCE = new ReverseStep();

    public ReverseStep() {
        super("ReverseStep", "ReverseStep.", Module.Category.MOVEMENT, true, false, false);
        setInstance();
    }

    public static ReverseStep getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReverseStep();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if ( Util.mc.player == null || Util.mc.world == null || Util.mc.player.isInWater() || Util.mc.player.isInLava()) {
            return;
        }
        if ( Util.mc.player.onGround) {
            -- Util.mc.player.motionY;
        }
    }
}