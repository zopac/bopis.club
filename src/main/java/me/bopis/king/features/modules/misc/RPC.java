package me.bopis.king.features.modules.misc;

import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import me.bopis.king.util.DiscordUtil;

/**
 * @Author evaan on 6/16/2021
 * https://github.com/evaan
 */
public class RPC extends Module {
    public RPC() {super("RPC", "le discord le rpc", Category.MISC, false, false, false);}

    public Setting<String> text = register(new Setting<String>("Text", "bopis on top!"));

    public void onEnable() { DiscordUtil.start(); }
    public void onDisable() { DiscordUtil.end(); }
}