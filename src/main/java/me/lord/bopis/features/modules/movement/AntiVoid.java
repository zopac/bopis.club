package me.lord.bopis.features.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.lord.bopis.features.command.Command;
import me.lord.bopis.features.modules.Module;
import me.lord.bopis.features.setting.Setting;
import me.lord.bopis.util.Util;

public class AntiVoid extends Module {
    public Setting<Mode> mode = register(new Setting<Mode>("Mode", Mode.BOUNCE));
    public Setting<Boolean> display = register(new Setting<Boolean>("Display", true));

    public AntiVoid() {
        super("AntiVoid", "Glitches you up from void.", Module.Category.MOVEMENT, false, false, false);
    }

    @Override
    public void onUpdate() {
        double yLevel = Util.mc.player.posY;
        if (yLevel <= .5) {
            Command.sendMessage(ChatFormatting.RED + "Player " + ChatFormatting.GREEN + Util.mc.player.getName() + ChatFormatting.RED + " is in the void!");
            if (mode.getValue().equals(Mode.BOUNCE)) {
                Util.mc.player.moveVertical = 10;
                Util.mc.player.jump();
            }
            if (mode.getValue().equals(Mode.LAUNCH)) {
                Util.mc.player.moveVertical = 100;
                Util.mc.player.jump();
            }
        } else {
            Util.mc.player.moveVertical = 0;
        }
    }

    @Override
    public void onDisable() {
        Util.mc.player.moveVertical = 0;
    }

    @Override
    public String getDisplayInfo() {
        if (display.getValue()) {
            if (mode.getValue().equals(Mode.BOUNCE)) {
                return "Bounce";
            }
            if (mode.getValue().equals(Mode.LAUNCH)) {
                return "Launch";
            }
        }
        return null;
    }

        public enum Mode {
            BOUNCE,
            LAUNCH,
        }
    }