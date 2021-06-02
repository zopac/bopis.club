package me.lord.bopis.features.modules.misc;

import me.lord.bopis.DiscordPresence;
import me.lord.bopis.features.modules.Module;
import me.lord.bopis.features.setting.Setting;

public class RPC extends Module
{
    public static RPC INSTANCE;
    public Setting<Boolean> showIP;
    public Setting<String> state;

    public RPC() {
        super("RPC", "Discord rich presence", Category.MISC, false, false, false);
        this.showIP = (Setting<Boolean>)this.register(new Setting("ShowIP", true, "Shows the server IP in your discord presence."));
        this.state = (Setting<String>)this.register(new Setting("State", "Zori 1.2.2", "Sets the state of the DiscordRPC."));
        RPC.INSTANCE = this;
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();
    }

    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }
}