package me.bopis.king.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.bopis.king.event.events.UpdateEvent;
import me.bopis.king.features.command.Command;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import me.bopis.king.util.ItemUtil;
import me.bopis.king.util.PlaceBlockUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Burrow extends Module {
    public Burrow() {
        super("Burrow", "dhiojpioawf joapfgw", Module.Category.COMBAT, true, false, false);
    }
    public Setting <Float> height = register(new Setting("Height", 5F, -5F, 5F));
    public Setting<Boolean> preferEChests = register(new Setting("EChests", false));
    public BlockPos startPos;
    private int obbySlot = -1;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            this.setEnabled(false);
            return;
        }

        obbySlot = ItemUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSlot = ItemUtil.findHotbarBlock(BlockEnderChest.class);
        if ((preferEChests.getValue() || obbySlot == -1) && eChestSlot != -1) {
            obbySlot = eChestSlot;
        } else {
            obbySlot = ItemUtil.findHotbarBlock(BlockObsidian.class);
            if (obbySlot == -1) {
                Command.sendMessage(ChatFormatting.RED + "Toggling, No obsidian.");
                this.setEnabled(false);
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (nullCheck()) {
            return;
        }

        int startSlot = mc.player.inventory.currentItem;

        mc.getConnection().sendPacket(new CPacketHeldItemChange(obbySlot));
        startPos = new BlockPos(mc.player.getPositionVector());
        mc.player.jump();
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.75, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16, mc.player.posZ, true));

        final boolean onEChest = mc.world.getBlockState(new BlockPos(mc.player.getPositionVector())).getBlock() == Blocks.ENDER_CHEST;
        PlaceBlockUtil.placeBlock(onEChest ? startPos.up() : startPos);
        mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + height.getValue(), mc.player.posZ, false));

        if (startSlot != -1)
            mc.getConnection().sendPacket(new CPacketHeldItemChange(startSlot));

        this.setEnabled(false);
    }

}
