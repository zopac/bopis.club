package me.lord.bopis.features.modules.movement;

import me.lord.bopis.features.modules.Module;
import me.lord.bopis.util.Util;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlowBypass extends Module {

    public NoSlowBypass() {
        super("NoSlowBypass", "crazy", Module.Category.MOVEMENT, true, false, false);
    }

    private boolean sneaking;

    @Override
    public void onUpdate() {
        if(mc.world != null) {
            Item item = Util.getPlayer().getActiveItemStack().getItem();

            if (sneaking && ((!Util.getPlayer().isHandActive() && item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion) || (!(item instanceof ItemFood) || !(item instanceof ItemBow) || !(item instanceof ItemPotion)))) {
                Util.getPlayer().connection.sendPacket(new CPacketEntityAction(Util.getPlayer(), CPacketEntityAction.Action.STOP_SNEAKING));
                sneaking = false;
            }
        }
    }

    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        if (!sneaking) {
            Util.getPlayer().connection.sendPacket(new CPacketEntityAction(Util.getPlayer(), CPacketEntityAction.Action.START_SNEAKING));
            sneaking = true;
        }
    }
}