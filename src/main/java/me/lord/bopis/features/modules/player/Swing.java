package me.lord.bopis.features.modules.player;

import me.lord.bopis.event.events.Packet;
import me.lord.bopis.features.modules.Module;
import me.lord.bopis.features.setting.Setting;
import me.lord.bopis.util.Util;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Novola
 * @since 19/1/2020 (1/19/2020)
 */
public class Swing extends Module {
    private Setting<Hand> hand = register(new Setting("Hand", Hand.OFFHAND));

    public Swing() {
        super("Swing", "Changes the hand you swing with", Module.Category.RENDER, false, false, false);
    }

    public void onUpdate() {
        if ( Util.mc.world == null)
            return;
        if (hand.getValue().equals(Hand.OFFHAND)) {
            Util.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (hand.getValue().equals(Hand.MAINHAND)) {
            Util.mc.player.swingingHand = EnumHand.MAIN_HAND;
        }
    }

    @SubscribeEvent
    public void onPacket(final Packet event) {
        if (nullCheck() || event.getType() == Packet.Type.INCOMING) {
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