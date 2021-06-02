package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.util.ItemUtil;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class InstantSelfFill extends Module {
    public InstantSelfFill() {
        super("InstantSelfFill", "does the thing i guess", Module.Category.COMBAT, true, false, false);
    }

    public Setting<BLOCK> block = this.register(new Setting<Object>("Block", BLOCK.Obsidian));

    public enum BLOCK {
        Obsidian,
        Enderchest,
        Anvil

    }

    private BlockPos originalPos;
    private int oldSlot = -1;

    @Override
    public void onEnable() {
        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) ||
                intersectsWithEntity(this.originalPos)) {
            toggle();
            return;
        }
        oldSlot = mc.player.inventory.currentItem;
    }

    @Override
    public void onUpdate() {
        if (block.equals(BLOCK.Obsidian) && ItemUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            Command.sendMessage("Can't find any obsidian in your hotbar!");
            toggle();
            return;
        } else if (block.equals(BLOCK.Enderchest) && ItemUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
            Command.sendMessage("Can't find any ender chests in your hotbar!");
            toggle();
            return;
        } else if (block.equals(BLOCK.Anvil) && ItemUtil.findHotbarBlock(BlockAnvil.class) == -1) {
            Command.sendMessage("Can't find any anvils in your hotbar!");
            toggle();
            return;
        }
        if (block.equals(BLOCK.Obsidian)) {
            ItemUtil.switchToSlot(ItemUtil.findHotbarBlock(BlockObsidian.class));
        } else if (block.equals(BLOCK.Enderchest)) {
            ItemUtil.switchToSlot(ItemUtil.findHotbarBlock(BlockEnderChest.class));
        } else if (block.equals(BLOCK.Anvil)) {
            ItemUtil.switchToSlot(ItemUtil.findHotbarBlock(BlockAnvil.class));
        }
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));
        ItemUtil.placeBlock(originalPos, EnumHand.MAIN_HAND, true, true, false);
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + -6.395812, mc.player.posZ, false));
        ItemUtil.switchToSlot(oldSlot);
        mc.player.setSneaking(false);
        toggle();
    }

    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        mc.player.setSneaking(false);
    }
}