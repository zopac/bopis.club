package me.bopis.king.features.modules.combat;

import me.bopis.king.Bopis;
import me.bopis.king.features.command.Command;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import me.bopis.king.util.BlockUtil;
import me.bopis.king.util.EntityUtil;
import me.bopis.king.util.HoleUtil;
import me.bopis.king.util.InventoryUtil;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class Burrow extends Module {
    private final Setting<Integer> offset = this.register(new Setting<Integer>("Offset", 2, -5, 5));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    public final Setting<Boolean> auto = this.register(new Setting("Auto", false));
    private final Setting<Boolean> autoDisable = this.register(new Setting<Boolean>("Disable", false));
    private final Setting<Double> range = this.register(new Setting<Double>("Range", 2.5, 1.0, 7.0, v -> this.auto.getValue()));
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Obsidian));
    Block returnBlock = null;
    private BlockPos originalPos;
    private int oldSlot = -1;
    private final Set<EntityPlayer> entities;

    public Burrow() {
        super("Burrow", "TPs you into a block", Category.COMBAT, true, false, false);
        this.entities = new HashSet<EntityPlayer>();
    }

    @Override
    public void onEnable() {
        fullNullCheck();
        this.originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
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
            case Dispenser: {
                this.returnBlock = Blocks.DISPENSER;
            }
            case Dropper: {
                this.returnBlock = Blocks.DROPPER;
            }
            case Hopper: {
                this.returnBlock = Blocks.HOPPER;
            }
            case Cake: {
                this.returnBlock = Blocks.CAKE;
            }
        }
        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(this.returnBlock) || this.intersectsWithEntity(this.originalPos)) {
            this.toggle();
            
        }
        this.oldSlot = mc.player.inventory.currentItem;
    }

    public void onUpdate() {
        if (auto.getValue() && HoleUtil.isInHole(mc.player)) {
            mc.world.loadedEntityList.stream()
                    .filter(e -> e instanceof EntityPlayer)
                    .filter(e -> e != mc.player)
                    .forEach(e -> {
                        if (Bopis.friendManager.isFriend(e.getName()))
                            return;

                        if (e.getDistance(mc.player) + 0.22f <= range.getValue()) {
                            dotheburrow();
                        }
                    });
        } else
            dotheburrow();
    }

    private void dotheburrow() {
        fullNullCheck();
        switch (this.mode.getValue()) {
            case Obsidian: {
                if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) break;
                Command.sendMessage("Can't find any obsidian in hotbar!");
                this.disable();
                break;
            }
            case EnderChest: {
                if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) break;
                Command.sendMessage("Can't find any ender chests in hotbar!");
                this.disable();
                break;
            }
            case Chest: {
                if (InventoryUtil.findHotbarBlock(BlockChest.class) != -1) break;
                Command.sendMessage("Can't find any chests in hotbar!");
                this.disable();
                break;
            }
            case Anvil: {
                if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) break;
                Command.sendMessage("Can't find any anvils in hotbar!");
                this.disable();
                break;
            }
            case EnchantingTable: {
                if (InventoryUtil.findHotbarBlock(BlockEnchantmentTable.class) != -1) break;
                Command.sendMessage("Can't find any enchanting tables in hotbar!");
                this.disable();
                break;
            }
            case DragonEgg: {
                if (InventoryUtil.findHotbarBlock(BlockDragonEgg.class) != -1) break;
                Command.sendMessage("Can't find any dragon eggs in hotbar!");
                this.disable();
                break;
            }
            case Stone: {
                if (InventoryUtil.findHotbarBlock(BlockStone.class) != -1) break;
                Command.sendMessage("Can't find any stone in hotbar!");
                this.disable();
                break;
            }
            case Dispenser: {
                if (InventoryUtil.findHotbarBlock(BlockDispenser.class) != -1) break;
                Command.sendMessage("Can't find any dispensers in hotbar!");
                this.disable();
                break;
            }
            case Dropper: {
                if (InventoryUtil.findHotbarBlock(BlockDropper.class) != -1) break;
                Command.sendMessage("Can't find any droppers in hotbar!");
                this.disable();
                break;
            }
            case Hopper: {
                if (InventoryUtil.findHotbarBlock(BlockHopper.class) != -1) break;
                Command.sendMessage("Can't find any hoppers in hotbar!");
                this.disable();
                break;
            }
            case Cake: {
                if (InventoryUtil.findHotbarBlock(BlockCake.class) != -1) break;
                Command.sendMessage("Can't find any cake in hotbar!");
                this.disable();
                break;
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
                break;
            }
            case Cake: {
                BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockCake.class));
                break;
            }
        }
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.41999998688698, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.7531999805211997, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.00133597911214, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.16610926093821, Burrow.mc.player.posZ, true));
        BlockUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + (double) this.offset.getValue().intValue(), Burrow.mc.player.posZ, false));
        Burrow.mc.player.connection.sendPacket(new CPacketEntityAction(Burrow.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        Burrow.mc.player.setSneaking(false);
        BlockUtil.switchToSlot(this.oldSlot);
        if (autoDisable.getValue()) {
            this.disable();
        }
    }


    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : Burrow.mc.world.loadedEntityList) {
            if (entity.equals(Burrow.mc.player) || entity instanceof EntityItem || !new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox()))
                continue;
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
        Dropper,
        Dispenser,
        Hopper,
        Cake
    }
}