package me.alpha432.oyvey.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class PlaceBlockUtil implements Util {

    public static void placeBlock(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.VALUES) {
            final BlockPos neighbor = pos.offset(side);
            final IBlockState neighborState = mc.world.getBlockState(neighbor);
            if (neighborState.getBlock().canCollideCheck(neighborState, false)) {
                final boolean sneak = !mc.player.isSneaking() && neighborState.getBlock().onBlockActivated(mc.world, pos, mc.world.getBlockState(pos), mc.player, EnumHand.MAIN_HAND, side, 0.5f, 0.5f, 0.5f);
                if (sneak) {
                    mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                }
                mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(neighbor, side.getOpposite(), EnumHand.MAIN_HAND, 0.5F, 0.5F, 0.5F));
                mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                if (sneak) {
                    mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                }
            }
        }
    }

}
