package me.lord.bopis.features.modules.render;

import me.lord.bopis.features.modules.Module;
import me.lord.bopis.features.setting.Setting;
import me.lord.bopis.util.Util;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;

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
        if(hand.getValue().equals(Hand.OFFHAND)) {
            Util.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if(hand.getValue().equals(Hand.MAINHAND)) {
            Util.mc.player.swingingHand = EnumHand.MAIN_HAND;
        }
        if(hand.getValue().equals(Hand.PACKETSWING)) {
            if ( Util.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && Util.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
                Util.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
                Util.mc.entityRenderer.itemRenderer.itemStackMainHand = Util.mc.player.getHeldItemMainhand();
            }
        }
    }

    public enum Hand {
        OFFHAND,
        MAINHAND,
        PACKETSWING
    }
}