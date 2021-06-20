package me.bopis.king.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.bopis.king.Bopis;
import me.bopis.king.event.events.ClientEvent;
import me.bopis.king.features.command.Command;
import me.bopis.king.features.gui.ClickGuiScreen;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import me.bopis.king.manager.HWIDManager;
import me.bopis.king.util.ColorUtil;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ClickGui extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public Setting <String> prefix = register(new Setting<>("Prefix", "-"));
    public Setting<Boolean> customFov = register(new Setting<>("CustomFov", false));
    public Setting<Float> fov = register(new Setting<>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f)));
    public Setting<Integer> red = register(new Setting<>("Red", 255, 0, 255));
    public Setting<Integer> green = register(new Setting<>("Green", 255, 0, 255));
    public Setting<Integer> blue = register(new Setting<>("Blue", 255, 0, 255));
    public Setting<Integer> hoverAlpha = register(new Setting<>("Alpha", 180, 0, 255));
    public Setting<Integer> topRed = register(new Setting<>("SecondRed", 0, 0, 255));
    public Setting<Integer> topGreen = register(new Setting<>("SecondGreen", 0, 0, 255));
    public Setting<Integer> topBlue = register(new Setting<>("SecondBlue", 150, 0, 255));
    public Setting<Integer> alpha = register(new Setting<>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> rainbow = register(new Setting<>("Rainbow", false));
    public Setting<rainbowMode> rainbowModeHud = register(new Setting<>("HRainbowMode", rainbowMode.Static, v -> rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = register(new Setting<>("ARainbowMode", rainbowModeArray.Static, v -> rainbow.getValue()));
    public Setting<Integer> rainbowHue = register(new Setting<>("Delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600), v -> rainbow.getValue()));
    public Setting<Float> rainbowBrightness = register(new Setting<>("Brightness ", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> rainbow.getValue()));
    public Setting<Float> rainbowSaturation = register(new Setting<>("Saturation", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> rainbow.getValue()));
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", false));
    public float hue;

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (customFov.getValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, fov.getValue());
        }
    }

    public Color getCurrentColor() {
        if (this.rainbow.getValue().booleanValue()) {
            return Color.getHSBColor(this.hue, (float) this.rainbowSaturation.getValue().intValue() / 255.0f, (float) this.rainbowBrightness.getValue().intValue() / 255.0f);
        }
        return new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
    }

    public int getCurrentColorHex() {
        if (this.rainbow.getValue()) {
            return Color.HSBtoRGB(this.hue, (float) this.rainbowSaturation.getValue().intValue() / 255.0f, (float) this.rainbowBrightness.getValue().intValue() / 255.0f);
        }
        return ColorUtil.toARGB(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
    }

    @SubscribeEvent
    public void onSettingChange( ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(prefix)) {
                Bopis.commandManager.setPrefix(prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + Bopis.commandManager.getPrefix());
            }
            Bopis.colorManager.setColor(red.getPlannedValue(), green.getPlannedValue(), blue.getPlannedValue(), hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(ClickGuiScreen.getClickGui());
    }

    @Override
    public void onLoad() {
        Bopis.colorManager.setColor(red.getValue(), green.getValue(), blue.getValue(), hoverAlpha.getValue());
        Bopis.commandManager.setPrefix(prefix.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof ClickGuiScreen)) {
            disable();
        }
    }

    public enum rainbowModeArray {
        Static,
        Up
    }

    public enum rainbowMode {
        Static,
        Sideway
    }
}

