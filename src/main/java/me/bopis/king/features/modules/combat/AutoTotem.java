package me.bopis.king.features.modules.combat;

import me.bopis.king.event.events.PacketEvent;
import me.bopis.king.event.events.ProcessRightClickBlockEvent;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import me.bopis.king.util.EntityUtil;
import me.bopis.king.util.InventoryUtil;
import me.bopis.king.util.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AutoTotem extends Module {
    private static AutoTotem instance;
    private final Queue< InventoryUtil.Task > taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    private final Timer timer = new Timer();
    private final Timer secondTimer = new Timer();
    public Setting <Boolean> crystal = register(new Setting<>("Crystal", true));
    public Setting<Float> crystalHealth = register(new Setting<>("CrystalHP", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> crystalHoleHealth = register(new Setting<>("CrystalHoleHP", Float.valueOf(3.5f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Boolean> gapple = register(new Setting<>("Gapple", true));
    public Setting<Boolean> armorCheck = register(new Setting<>("ArmorCheck", true));
    public Setting<Integer> actions = register(new Setting<>("Packets", 4, 1, 4));
    public Mode2 currentMode = Mode2.TOTEMS;
    public int totems = 0;
    public int crystals = 0;
    public int gapples = 0;
    public int lastTotemSlot = -1;
    public int lastGappleSlot = -1;
    public int lastCrystalSlot = -1;
    public int lastObbySlot = -1;
    public int lastWebSlot = -1;
    public boolean holdingCrystal = false;
    public boolean holdingTotem = false;
    public boolean holdingGapple = false;
    public boolean didSwitchThisTick = false;
    private boolean second = false;
    private boolean switchedForHealthReason = false;

    public AutoTotem() {
        super("AutoTotem", "Allows you to switch up your AutoTotem.", Module.Category.COMBAT, true, false, false);
        instance = this;
    }

    public static AutoTotem getInstance() {
        if (instance == null) {
            instance = new AutoTotem();
        }
        return instance;
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(ProcessRightClickBlockEvent event) {
        if (event.hand == EnumHand.MAIN_HAND && event.stack.getItem() == Items.END_CRYSTAL && AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && AutoTotem.mc.objectMouseOver != null && event.pos == AutoTotem.mc.objectMouseOver.getBlockPos()) {
            event.setCanceled(true);
            AutoTotem.mc.player.setActiveHand(EnumHand.OFF_HAND);
            AutoTotem.mc.playerController.processRightClick(AutoTotem.mc.player, AutoTotem.mc.world, EnumHand.OFF_HAND);
        }
    }

    @Override
    public void onUpdate() {
        if (timer.passedMs(50L)) {
            if (AutoTotem.mc.player != null && AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && AutoTotem.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && Mouse.isButtonDown(1)) {
                AutoTotem.mc.player.setActiveHand(EnumHand.OFF_HAND);
                AutoTotem.mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
            }
        } else if (AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && AutoTotem.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            AutoTotem.mc.gameSettings.keyBindUseItem.pressed = false;
        }
        if (AutoTotem.nullCheck()) {
            return;
        }
        doOffhand();
        if (secondTimer.passedMs(50L) && second) {
            second = false;
            timer.reset();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (!AutoTotem.fullNullCheck() && AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && AutoTotem.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && AutoTotem.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            CPacketPlayerTryUseItem packet;
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                CPacketPlayerTryUseItemOnBlock packet2 = event.getPacket();
                if (packet2.getHand() == EnumHand.MAIN_HAND) {
                    if (timer.passedMs(50L)) {
                        AutoTotem.mc.player.setActiveHand(EnumHand.OFF_HAND);
                        AutoTotem.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
                    }
                    event.setCanceled(true);
                }
            } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && (packet = event.getPacket()).getHand() == EnumHand.OFF_HAND && !timer.passedMs(50L)) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        if (AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            return "Crystal";
        }
        if (AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            return "Totem";
        }
        if (AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            return "Gapple";
        }
        return null;
    }

    public void doOffhand() {
        didSwitchThisTick = false;
        holdingCrystal = AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        holdingTotem = AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING;
        holdingGapple = AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE;
        totems = AutoTotem.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (holdingTotem) {
            totems += AutoTotem.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        }
        crystals = AutoTotem.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        if (holdingCrystal) {
            crystals += AutoTotem.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        }
        gapples = AutoTotem.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        if (holdingGapple) {
            gapples += AutoTotem.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        }
        doSwitch();
    }

    public void doSwitch() {
        currentMode = Mode2.TOTEMS;
        if (gapple.getValue() && AutoTotem.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && AutoTotem.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            currentMode = Mode2.GAPPLES;
        } else if (currentMode != Mode2.CRYSTALS && crystal.getValue() && ( EntityUtil.isSafe(AutoTotem.mc.player) && EntityUtil.getHealth(AutoTotem.mc.player, true) > crystalHoleHealth.getValue() || EntityUtil.getHealth(AutoTotem.mc.player, true) > crystalHealth.getValue())) {
            currentMode = Mode2.CRYSTALS;
        }
        if (currentMode == Mode2.CRYSTALS && crystals == 0) {
            setMode(Mode2.TOTEMS);
        }
        if (currentMode == Mode2.CRYSTALS && (!EntityUtil.isSafe(AutoTotem.mc.player) && EntityUtil.getHealth(AutoTotem.mc.player, true) <= crystalHealth.getValue() || EntityUtil.getHealth(AutoTotem.mc.player, true) <= crystalHoleHealth.getValue())) {
            if (currentMode == Mode2.CRYSTALS) {
                switchedForHealthReason = true;
            }
            setMode(Mode2.TOTEMS);
        }
        if (switchedForHealthReason && (EntityUtil.isSafe(AutoTotem.mc.player) && EntityUtil.getHealth(AutoTotem.mc.player, true) > crystalHoleHealth.getValue() || EntityUtil.getHealth(AutoTotem.mc.player, true) > crystalHealth.getValue())) {
            setMode(Mode2.CRYSTALS);
            switchedForHealthReason = false;
        }
        if (currentMode == Mode2.CRYSTALS && armorCheck.getValue() && (AutoTotem.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.AIR || AutoTotem.mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == Items.AIR || AutoTotem.mc.player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == Items.AIR || AutoTotem.mc.player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == Items.AIR)) {
            setMode(Mode2.TOTEMS);
        }
        if (AutoTotem.mc.currentScreen instanceof GuiContainer && !(AutoTotem.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        Item currentOffhandItem = AutoTotem.mc.player.getHeldItemOffhand().getItem();
        switch (currentMode) {
            case TOTEMS: {
                if (totems <= 0 || holdingTotem) break;
                lastTotemSlot = InventoryUtil.findItemInventorySlot(Items.TOTEM_OF_UNDYING, false);
                int lastSlot = getLastSlot(currentOffhandItem, lastTotemSlot);
                putItemInOffhand(lastTotemSlot, lastSlot);
                break;
            }
            case GAPPLES: {
                if (gapples <= 0 || holdingGapple) break;
                lastGappleSlot = InventoryUtil.findItemInventorySlot(Items.GOLDEN_APPLE, false);
                int lastSlot = getLastSlot(currentOffhandItem, lastGappleSlot);
                putItemInOffhand(lastGappleSlot, lastSlot);
                break;
            }
            default: {
                if (crystals <= 0 || holdingCrystal) break;
                lastCrystalSlot = InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL, false);
                int lastSlot = getLastSlot(currentOffhandItem, lastCrystalSlot);
                putItemInOffhand(lastCrystalSlot, lastSlot);
            }
        }
        for (int i = 0; i < actions.getValue(); ++i) {
            InventoryUtil.Task task = taskList.poll();
            if (task == null) continue;
            task.run();
            if (!task.isSwitching()) continue;
            didSwitchThisTick = true;
        }
    }

    private int getLastSlot(Item item, int slotIn) {
        if (item == Items.END_CRYSTAL) {
            return lastCrystalSlot;
        }
        if (item == Items.GOLDEN_APPLE) {
            return lastGappleSlot;
        }
        if (item == Items.TOTEM_OF_UNDYING) {
            return lastTotemSlot;
        }
        if (InventoryUtil.isBlock(item, BlockObsidian.class)) {
            return lastObbySlot;
        }
        if (InventoryUtil.isBlock(item, BlockWeb.class)) {
            return lastWebSlot;
        }
        if (item == Items.AIR) {
            return -1;
        }
        return slotIn;
    }

    private void putItemInOffhand(int slotIn, int slotOut) {
        if (slotIn != -1 && taskList.isEmpty()) {
            taskList.add(new InventoryUtil.Task(slotIn));
            taskList.add(new InventoryUtil.Task(45));
            taskList.add(new InventoryUtil.Task(slotOut));
            taskList.add(new InventoryUtil.Task());
        }
    }

    public void setMode(Mode2 mode) {
        currentMode = currentMode == mode ? Mode2.TOTEMS : mode;
    }

    public enum Mode2 {
        TOTEMS,
        GAPPLES,
        CRYSTALS
    }
}
