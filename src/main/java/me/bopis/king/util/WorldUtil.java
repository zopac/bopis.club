package me.bopis.king.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class WorldUtil implements Util {
    public static void placeBlock(final BlockPos pos) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            if (!mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !isIntercepted(pos)) {
                final Vec3d vec = new Vec3d(pos.getX() + 0.5 + enumFacing.getXOffset() * 0.5, pos.getY() + 0.5 + enumFacing.getYOffset() * 0.5, pos.getZ() + 0.5 + enumFacing.getZOffset() * 0.5);
                final float[] old = {mc.player.rotationYaw,  mc.player.rotationPitch };
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation((float)Math.toDegrees(Math.atan2(vec.z - mc.player.posZ, vec.x - mc.player.posX)) - 90.0f, (float)(-Math.toDegrees(Math.atan2(vec.y - (mc.player.posY + mc.player.getEyeHeight()), Math.sqrt((vec.x - mc.player.posX) * (vec.x - mc.player.posX) + (vec.z - mc.player.posZ) * (vec.z - mc.player.posZ))))), mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketEntityAction((Entity) mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.playerController.processRightClickBlock(mc.player, mc.world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d((Vec3i)pos), EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity) mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(old[0], old[1],  mc.player.onGround));
                return;
            }
        }
    }

    public static void placeBlock(final BlockPos pos, final int slot) {
        if (slot == -1) {
            return;
        }
        final int prev = mc.player.inventory.currentItem;
        mc.player.inventory.currentItem = slot;
        placeBlock(pos);
        mc.player.inventory.currentItem = prev;
    }

    public static boolean isIntercepted(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    public static BlockPos GetLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static boolean canBreak(final BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock().blockHardness != -1.0f;
    }
}