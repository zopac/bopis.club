package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.item.ItemExpBottle;

public class FastPlace extends Module {
    private Setting<Item> Items;


    public FastPlace() {
        super("FastPlace", "Fast everything.", Module.Category.PLAYER, true, false, false);
        this.Items = (Setting<Item>) this.register(new Setting("Items", Item.XP));
    }

    public void onUpdate() {

        if (FastPlace.fullNullCheck()) {
            return;
        }
        if (Items.getValue().equals(Item.XP)) {
            if (Items.getValue().equals(Item.XP) && InventoryUtil.holdingItem(ItemExpBottle.class)) {
                FastPlace.mc.rightClickDelayTimer = 0;
            }
        } else {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
    }

    private enum Item {
        XP,
        ALL;
    }
}

