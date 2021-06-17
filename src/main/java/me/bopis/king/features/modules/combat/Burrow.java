package me.bopis.king.features.modules.combat;

import me.bopis.king.features.command.Command;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import me.bopis.king.util.BlockUtil;
import me.bopis.king.util.InventoryUtil;
import me.bopis.king.util.InventoryUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class Burrow extends Module {
    private final Setting <Integer> offset = this.register(new Setting <Integer>( "Offset", 2, - 5, 5 ));
    private final Setting <Boolean> packet = this.register(new Setting <Boolean>( "Packet", true));
    private final Setting <Boolean> rotate = this.register(new Setting <Boolean>( "Rotate", false));
    private final Setting <Mode> mode = this.register(new Setting <Mode>( "Mode", Mode.Obsidian));
    Block returnBlock = null;
    private BlockPos originalPos;
    private int oldSlot = - 1;

    public Burrow () {
        super("Burrow" , "TPs you into a block" , Category.COMBAT , true , false , false );
    }

    @Override
    public void onEnable () {
        if (fullNullCheck()) {
            return;
        }
        this.originalPos = new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ);
        switch (this.mode.getValue()) {
            case Obsidian: {
                this.returnBlock = Blocks.OBSIDIAN;
                break;
            }
            case EnderChest: {
                this.returnBlock = Blocks.ENDER_CHEST;
                break;
            }
            case Chest: {
                this.returnBlock = Blocks.CHEST;
            }
            case Anvil: {
                this.returnBlock = Blocks.ANVIL;
            }
            case EnchantingTable: {
                this.returnBlock = Blocks.ENCHANTING_TABLE;
            }
            case DragonEgg: {
                this.returnBlock = Blocks.DRAGON_EGG;
            }
            case Stone: {
                this.returnBlock = Blocks.STONE;
            }
            case Cobweb: {
                this.returnBlock = Blocks.WEB;
            }
            case Dispenser: {
                this.returnBlock = Blocks.DISPENSER;
            }
            case Dropper: {
                this.returnBlock = Blocks.DROPPER;
            }
            case Hopper: {
                this.returnBlock = Blocks.HOPPER;
            }
        }
        if (Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ)).getBlock().equals(this.returnBlock) || this.intersectsWithEntity (this.originalPos)) {
            this.toggle();
            return;
        }
        this.oldSlot = Burrow.mc.player.inventory.currentItem;
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        switch (this.mode.getValue()) {
            case Obsidian: {
                if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) break;
                Command.sendMessage("Can't find obsidian in hotbar!");
                this.disable();
                break;
            }
            case EnderChest: {
                if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) break;
                Command.sendMessage("Can't find enderchest in hotbar!");
                this.disable();
                break;
            }
            case Chest: {
                if (InventoryUtil.findHotbarBlock(BlockChest.class) != -1) break;
                Command.sendMessage("Can't find chest in hotbar!");
                this.disable();
                break;
            }
            case EnchantingTable: {
                if (InventoryUtil.findHotbarBlock(BlockEnchantmentTable.class) != -1) break;
                Command.sendMessage("Can't find anvil in hotbar!");
                this.disable();
            }
            case DragonEgg: {
                if (InventoryUtil.findHotbarBlock(BlockDragonEgg.class) != -1) break;
                Command.sendMessage("Can't find anvil in hotbar!");
                this.disable();
            }
            case Stone: {
                if (InventoryUtil.findHotbarBlock(BlockStone.class) != -1) break;
                Command.sendMessage("Can't find anvil in hotbar!");
                this.disable();
            }
            case Cobweb: {
                if (InventoryUtil.findHotbarBlock(BlockWeb.class) != -1) break;
                Command.sendMessage("Can't find anvil in hotbar!");
                this.disable();
            }
            case Dispenser: {
                if (InventoryUtil.findHotbarBlock(BlockDispenser.class) != -1) break;
                Command.sendMessage("Can't find anvil in hotbar!");
                this.disable();
            }
            case Dropper: {
                if (InventoryUtil.findHotbarBlock(BlockDropper.class) != -1) break;
                Command.sendMessage("Can't find anvil in hotbar!");
                this.disable();
            }
            case Hopper: {
                if (InventoryUtil.findHotbarBlock(BlockHopper.class) != -1) break;
                Command.sendMessage("Can't find anvil in hotbar!");
                this.disable();
            }

        }
        switch (this.mode.getValue()) {
            case Obsidian: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockObsidian.class));
                break;
            }
            case EnderChest: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockEnderChest.class));
                break;
            }
            case Chest: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockChest.class));
                break;
            }
            case Anvil: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockAnvil.class));
                break;
            }
            case EnchantingTable: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockEnchantmentTable.class));
                break;
            }
            case DragonEgg: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockDragonEgg.class));
                break;
            }
            case Stone: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockStone.class));
                break;
            }
            case Cobweb: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockWeb.class));
                break;
            }
            case Dropper: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockDropper.class));
                break;
            }
            case Dispenser: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockDispenser.class));
                break;
            }
            case Hopper: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockHopper.class));
            }
        }
            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.41999998688698, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.7531999805211997, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.00133597911214, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.16610926093821, Burrow.mc.player.posZ, true));
            BlockUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), packet.getValue(), false);
            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + (double) this.offset.getValue().intValue(), Burrow.mc.player.posZ, false));
            Burrow.mc.player.connection.sendPacket(new CPacketEntityAction(Burrow.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            Burrow.mc.player.setSneaking(false);
            BlockUtil.switchToSlot(this.oldSlot);
            this.toggle();
    }

    private boolean intersectsWithEntity ( BlockPos pos ) {
        for (Entity entity : Burrow.mc.world.loadedEntityList) {
            if (entity.equals(Burrow.mc.player) || entity instanceof EntityItem || ! new AxisAlignedBB (pos).intersects(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public enum Mode {
        Obsidian,
        EnderChest,
        Chest,
        Anvil,
        EnchantingTable,
        DragonEgg,
        Stone,
        Cobweb,
        Dropper,
        Dispenser,
        Hopper
    }
}