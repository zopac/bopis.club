package me.bopis.king.features.modules.combat;

import me.bopis.king.features.modules.*;
import me.bopis.king.features.setting.*;
import net.minecraft.init.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import java.util.*;
import net.minecraft.item.*;
import java.util.function.*;
import net.minecraft.client.gui.inventory.*;
import me.bopis.king.util.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.EnumHand;

public class AutoTotem extends Module {
    public Setting<AutoTotemItem> mode;
    public Setting<Float> health;
    public Setting<Float> holeHealth;
    public Setting<Boolean> swordGap;
    public Setting<Float> swordGapHealth;
    public Setting<Boolean> totemOnLethalCrystal;
    public Item offhandItem;
    public Item holdingItem;
    public Item lastItem;
    public int crystals;
    public int gapples;
    public int totems;

    public AutoTotem() {
        super("AutoTotem", "Automatically puts items into your offhand", Category.COMBAT, true, false, false);
        this.mode = (Setting<AutoTotemItem>)this.register(new Setting("Mode", AutoTotemItem.TOTEM));
        this.health = (Setting<Float>)this.register(new Setting("Health", 16.0f, 0.0f, 36.0f));
        this.holeHealth = (Setting<Float>)this.register(new Setting("HoleHealth", 6.0f, 0.1f, 36.0f));
        this.swordGap = (Setting<Boolean>)this.register(new Setting("SwordGap", true));
        this.swordGapHealth = (Setting<Float>)this.register(new Setting("SwordGapMinHealth", 6.0f, 0.1f, 36.0f, v -> this.swordGap.getValue()));
        this.totemOnLethalCrystal = (Setting<Boolean>)this.register(new Setting("LethalCrystalSwitch", true));
    }

    @Override
    public void onUpdate() {
        final float currentHealth = AutoTotem.mc.player.getHealth() + AutoTotem.mc.player.getAbsorptionAmount();
        if (this.isLethalCrystal(currentHealth) && this.totemOnLethalCrystal.getValue()) {
            this.lastItem = this.offhandItem;
            this.offhandItem = Items.TOTEM_OF_UNDYING;
        }
        else if (AutoTotem.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && AutoTotem.mc.gameSettings.keyBindUseItem.isKeyDown() && currentHealth > this.swordGapHealth.getValue()) {
            this.lastItem = this.offhandItem;
            this.offhandItem = Items.GOLDEN_APPLE;
        }
        else if (EntityUtil.isSafe((Entity)AutoTotem.mc.player)) {
            if (currentHealth <= this.holeHealth.getValue()) {
                this.offhandItem = Items.TOTEM_OF_UNDYING;
            }
            else {
                final String lowerCase = this.mode.currentEnumName().toLowerCase();
                switch (lowerCase) {
                    case "totem": {
                        this.offhandItem = Items.TOTEM_OF_UNDYING;
                        break;
                    }
                    case "crystal": {
                        this.offhandItem = Items.END_CRYSTAL;
                        break;
                    }
                    case "gapple": {
                        this.offhandItem = Items.GOLDEN_APPLE;
                        break;
                    }
                }
            }
        }
        else if (currentHealth <= this.health.getValue()) {
            this.offhandItem = Items.TOTEM_OF_UNDYING;
        }
        else {
            final String lowerCase2 = this.mode.currentEnumName().toLowerCase();
            switch (lowerCase2) {
                case "totem": {
                    this.offhandItem = Items.TOTEM_OF_UNDYING;
                    break;
                }
                case "crystal": {
                    this.offhandItem = Items.END_CRYSTAL;
                    break;
                }
                case "gapple": {
                    this.offhandItem = Items.GOLDEN_APPLE;
                    break;
                }
            }
        }
        this.doSwitch();
    }

    private boolean isLethalCrystal(final float health) {
        for (final Entity e : AutoTotem.mc.world.loadedEntityList) {
            if (e instanceof EntityEnderCrystal && health <= DamageUtil.calculateDamage(e, (Entity)AutoTotem.mc.player)) {
                return true;
            }
        }
        return false;
    }

    private void doSwitch() {
        this.holdingItem = AutoTotem.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem();
        this.crystals = AutoTotem.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        this.gapples = AutoTotem.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        this.totems = AutoTotem.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (this.holdingItem.equals(Items.END_CRYSTAL)) {
            this.crystals += AutoTotem.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        }
        else if (this.holdingItem.equals(Items.GOLDEN_APPLE)) {
            this.gapples += AutoTotem.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        }
        else if (this.holdingItem.equals(Items.TOTEM_OF_UNDYING)) {
            this.totems += AutoTotem.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        }
        if (AutoTotem.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        final int slot = InventoryUtil.findItemInventorySlot(this.offhandItem, false);
        if (slot != -1) {
            if (this.holdingItem == this.offhandItem) {
                return;
            }
            if (this.offhandItem.equals(Items.END_CRYSTAL) && this.crystals > 0) {
                this.switchItem(slot);
            }
            if (this.offhandItem.equals(Items.GOLDEN_APPLE) && this.gapples > 0) {
                this.switchItem(slot);
            }
            if (this.offhandItem.equals(Items.TOTEM_OF_UNDYING) && this.totems > 0) {
                this.switchItem(slot);
            }
        }
    }

    private void switchItem(final int slot) {
        int returnSlot = -1;
        if (slot == -1) {
            return;
        }
        AutoTotem.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
        AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
        for (int i = 0; i < 45; ++i) {
            if (AutoTotem.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                returnSlot = i;
                break;
            }
        }
        if (returnSlot != -1) {
            AutoTotem.mc.playerController.windowClick(0, (returnSlot < 9) ? (returnSlot + 36) : returnSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
        }
        AutoTotem.mc.playerController.updateController();
    }

    public enum AutoTotemItem
    {
        TOTEM,
        CRYSTAL,
        GAPPLE;
    }
}
