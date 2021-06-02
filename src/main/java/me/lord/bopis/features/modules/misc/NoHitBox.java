package me.lord.bopis.features.modules.misc;

import me.lord.bopis.features.modules.Module;
import me.lord.bopis.features.setting.Setting;

public class NoHitBox extends Module {
    private static NoHitBox INSTANCE = new NoHitBox();
    public Setting<Boolean> pickaxe = register(new Setting<Boolean>("Pickaxe", true));
    public Setting<Boolean> crystal = register(new Setting<Boolean>("Crystal", true));
    public Setting<Boolean> gapple = register(new Setting<Boolean>("Gapple", false));

    public NoHitBox() {
        super("NoHitBox", "NoHitBox.", Module.Category.MISC, false, false, false);
        setInstance();
    }

    public static NoHitBox getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new NoHitBox();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

