package me.lord.bopis.features.modules.combat;

import me.lord.bopis.Bopis;
import me.lord.bopis.features.modules.Module;
import me.lord.bopis.features.modules.movement.ReverseStep;
import me.lord.bopis.features.setting.Setting;
import me.lord.bopis.manager.Mapping;
import me.lord.bopis.util.InventoryUtil;
import me.lord.bopis.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;

public class SelfFill extends Module {
    private BlockPos playerPos;
    private final Setting<Boolean> timerfill = register(new Setting("TimerFill", false));
    private final Setting<Boolean> toggleRStep = register(new Setting("ToggleRStep", true));
    public Setting<BLOCK> block = this.register(new Setting<Object>("Block", BLOCK.Obsidian));

    public enum BLOCK {
        Obsidian,
        Enderchest,
        Anvil

    }

    public SelfFill() {
        super("SelfFill", "SelfFills yourself in a hole.", Module.Category.COMBAT, true, false, true);
    }

    @Override
    public void onEnable() {

        if (timerfill.getValue()) {
            setTimer(50.0f);
        }
        if (toggleRStep.getValue()) {
            Bopis.moduleManager.getModuleByName("ReverseStep").isEnabled(); {
                ReverseStep.getInstance().disable();
            }
        }
        playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (block.equals(BLOCK.Obsidian)) {
            WorldUtil.placeBlock(playerPos, InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN));
        } else if (block.equals(BLOCK.Enderchest)) {
            WorldUtil.placeBlock(playerPos, InventoryUtil.findHotbarBlock(Blocks.ENDER_CHEST));
        } else if (block.equals(BLOCK.Anvil)) {
            WorldUtil.placeBlock(playerPos, InventoryUtil.findHotbarBlock(Blocks.ANVIL));
        }

        if (block.equals(BLOCK.Obsidian) && mc.world.getBlockState(playerPos).getBlock().equals(Blocks.OBSIDIAN)) {
            disable();
            return;
        } else if (block.equals(BLOCK.Enderchest) && mc.world.getBlockState(playerPos).getBlock().equals(Blocks.ENDER_CHEST)) {
            disable();
            return;
        } else if (block.equals(BLOCK.Anvil) && mc.world.getBlockState(playerPos).getBlock().equals(Blocks.ANVIL)) {
            disable();
            return;
        }
        mc.player.jump();
    }

    @Override
    public void onDisable() {
        if (toggleRStep.getValue()) {
            Bopis.moduleManager.getModuleByName("ReverseStep").isDisabled(); {
                ReverseStep.getInstance().enable();
            }
        }
        setTimer(1.0f);
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (mc.player.posY > playerPos.getY() + 1.04) {
             if (block.equals(BLOCK.Obsidian)) {
                 WorldUtil.placeBlock(playerPos, InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN));
             } else if (block.equals(BLOCK.Enderchest)) {
                 WorldUtil.placeBlock(playerPos, InventoryUtil.findHotbarBlock(Blocks.ENDER_CHEST));
             } else if (block.equals(BLOCK.Anvil)) {
                 WorldUtil.placeBlock(playerPos, InventoryUtil.findHotbarBlock(Blocks.ANVIL));
             }
            mc.player.jump();
            disable();
        }
    }

    private void setTimer(final float value) {
        try {
            final Field timer = Minecraft.class.getDeclaredField( Mapping.timer);
            timer.setAccessible(true);
            final Field tickLength = net.minecraft.util.Timer.class.getDeclaredField(Mapping.tickLength);
            tickLength.setAccessible(true);
            tickLength.setFloat(timer.get(mc), 50.0f / value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDisplayInfo() {
        if (timerfill.getValue()) {
            return "Timer";
        } else {
            return "Burrow";
        }
    }

}