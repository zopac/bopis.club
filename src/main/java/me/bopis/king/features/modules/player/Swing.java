package me.bopis.king.features.modules.player;

import me.bopis.king.event.events.Packet;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Swing extends Module {
    private final Setting<Hand> hand = register(new Setting("Hand", Hand.OFFHAND));

    public Swing() {
        super("Swing", "Changes the hand you swing with", Category.PLAYER, false, false, false);
    }

    public void onUpdate() {
        if (mc.world == null)
            return;
        if (hand.getValue().equals(Hand.OFFHAND)) {
            mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (hand.getValue().equals(Hand.MAINHAND)) {
            mc.player.swingingHand = EnumHand.MAIN_HAND;
        }
    }

    @SubscribeEvent
    public void onPacket(final Packet event) {
        if (hand.getValue().equals(Hand.PACKETSWING) && fullNullCheck() || hand.getValue().equals(Hand.PACKETSWING) && event.getType() == Packet.Type.INCOMING) {
            return;
        }
        if (event.getPacket() instanceof CPacketAnimation) {
            event.setCanceled(true);
        }
    }

    public enum Hand {
        OFFHAND,
        MAINHAND,
        PACKETSWING
    }
}