package me.bopis.king.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.bopis.king.features.command.Command;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;

public class NickHider extends Module {
    public final Setting <String> NameString = register(new Setting<Object>("Name", "New Name Here..."));
    private static NickHider instance;

    public NickHider() {
        super("NickHider", "Changes name", Module.Category.CLIENT, false, false, false);
        instance = this;
    }

    @Override
    public void onEnable() {
        Command.sendMessage(ChatFormatting.GRAY + "Success! Name successfully changed to " + ChatFormatting.GREEN + NameString.getValue());
    }

    public static NickHider getInstance() {
        if (instance == null) {
            instance = new NickHider();
        }
        return instance;
    }
}