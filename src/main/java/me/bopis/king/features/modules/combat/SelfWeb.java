package me.bopis.king.features.modules.combat;

import me.bopis.king.features.command.Command;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import me.bopis.king.util.BlockUtil;
import me.bopis.king.util.InventoryUtil;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class SelfWeb extends Module {
    public SelfWeb() {
        super("SelfWeb", "the", Category.COMBAT, true, false, false);
    }
    private final Setting <Boolean> rotate = this.register(new Setting <Boolean>( "Rotate", false));
    Block returnBlock = null;
    private BlockPos originalPos;
    private int oldSlot = - 1;

    @Override
    public void onEnable () {
        if (fullNullCheck()) {
            return;
        }
        this.originalPos = new BlockPos(SelfWeb.mc.player.posX, SelfWeb.mc.player.posY, SelfWeb.mc.player.posZ);
        this.returnBlock = Blocks.WEB;
        if (SelfWeb.mc.world.getBlockState(new BlockPos(SelfWeb.mc.player.posX, SelfWeb.mc.player.posY, SelfWeb.mc.player.posZ)).getBlock().equals(this.returnBlock) || this.intersectsWithEntity (this.originalPos)) {
            this.toggle();
            return;
        }
        this.oldSlot = SelfWeb.mc.player.inventory.currentItem;
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (InventoryUtil.findHotbarBlock(BlockWeb.class) != -1) {
            Command.sendMessage("Can't find cobweb in hotbar!");
            this.toggle();
        }
        BlockUtil.switchToSlot(InventoryUtil.findHotbarBlock(BlockWeb.class));
        SelfWeb.mc.player.connection.sendPacket(new CPacketPlayer.Position(SelfWeb.mc.player.posX, SelfWeb.mc.player.posY + 0.41999998688698, SelfWeb.mc.player.posZ, true));
        SelfWeb.mc.player.connection.sendPacket(new CPacketPlayer.Position(SelfWeb.mc.player.posX, SelfWeb.mc.player.posY + 0.7531999805211997, SelfWeb.mc.player.posZ, true));
        SelfWeb.mc.player.connection.sendPacket(new CPacketPlayer.Position(SelfWeb.mc.player.posX, SelfWeb.mc.player.posY + 1.00133597911214, SelfWeb.mc.player.posZ, true));
        SelfWeb.mc.player.connection.sendPacket(new CPacketPlayer.Position(SelfWeb.mc.player.posX, SelfWeb.mc.player.posY + 1.16610926093821, SelfWeb.mc.player.posZ, true));
        BlockUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        SelfWeb.mc.player.connection.sendPacket(new CPacketPlayer.Position(SelfWeb.mc.player.posX, SelfWeb.mc.player.posY + (double) -2, SelfWeb.mc.player.posZ, false));
        SelfWeb.mc.player.connection.sendPacket(new CPacketEntityAction(SelfWeb.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        SelfWeb.mc.player.setSneaking(false);
        BlockUtil.switchToSlot(this.oldSlot);
        this.toggle();
    }

    private boolean intersectsWithEntity ( BlockPos pos ) {
        for (Entity entity : SelfWeb.mc.world.loadedEntityList) {
            if (entity.equals(SelfWeb.mc.player) || entity instanceof EntityItem || ! new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }
}

