package me.bopis.king.features.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.bopis.king.event.events.Render3DEvent;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.modules.client.ClickGui;
import me.bopis.king.features.setting.Setting;
import me.bopis.king.util.ColorUtil;
import me.bopis.king.util.MathUtil;
import me.bopis.king.util.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;

public class StorageESP
        extends Module {
    private final Setting <Float> range = this.register(new Setting<Float>("Range", Float.valueOf(50.0f), Float.valueOf(1.0f), Float.valueOf(300.0f)));
    private final Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", false));
    private final Setting<Boolean> chest = this.register(new Setting<Boolean>("Chest", true));
    private final Setting<Boolean> dispenser = this.register(new Setting<Boolean>("Dispenser", false));
    private final Setting<Boolean> shulker = this.register(new Setting<Boolean>("Shulker", true));
    private final Setting<Boolean> echest = this.register(new Setting<Boolean>("Ender Chest", true));
    private final Setting<Boolean> furnace = this.register(new Setting<Boolean>("Furnace", false));
    private final Setting<Boolean> hopper = this.register(new Setting<Boolean>("Hopper", false));
    private final Setting<Boolean> cart = this.register(new Setting<Boolean>("Minecart", false));
    private final Setting<Boolean> frame = this.register(new Setting<Boolean>("Item Frame", false));
    private final Setting<Boolean> box = this.register(new Setting<Boolean>("Box", false));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue()));
    private final Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue()));

    public StorageESP() {
        super("StorageESP", "Highlights Containers.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        int color;
        BlockPos pos;
        HashMap<BlockPos, Integer> positions = new HashMap<BlockPos, Integer>();
        for (TileEntity tileEntity : StorageESP.mc.world.loadedTileEntityList) {
            if (!(tileEntity instanceof TileEntityChest && this.chest.getValue() != false || tileEntity instanceof TileEntityDispenser && this.dispenser.getValue() != false || tileEntity instanceof TileEntityShulkerBox && this.shulker.getValue() != false || tileEntity instanceof TileEntityEnderChest && this.echest.getValue() != false || tileEntity instanceof TileEntityFurnace && this.furnace.getValue() != false) && (!(tileEntity instanceof TileEntityHopper) || !this.hopper.getValue().booleanValue()) || !(StorageESP.mc.player.getDistanceSq(pos = tileEntity.getPos()) <= MathUtil.square(this.range.getValue().floatValue())) || (color = this.getTileEntityColor(tileEntity)) == -1) continue;
            positions.put(pos, color);
        }
        for (Entity entity : StorageESP.mc.world.loadedEntityList) {
            if ((!(entity instanceof EntityItemFrame) || !this.frame.getValue().booleanValue()) && (!(entity instanceof EntityMinecartChest) || !this.cart.getValue().booleanValue()) || !(StorageESP.mc.player.getDistanceSq(pos = entity.getPosition()) <= MathUtil.square(this.range.getValue().floatValue())) || (color = this.getEntityColor(entity)) == -1) continue;
            positions.put(pos, color);
        }
        for (Map.Entry entry : positions.entrySet()) {
            BlockPos blockPos = (BlockPos)entry.getKey();
            color = (Integer)entry.getValue();
            RenderUtil.drawBoxESP(blockPos, this.colorSync.getValue() != false ? ClickGui.getInstance().getCurrentColor() : new Color(color), false, new Color(color), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }

    private int getTileEntityColor(TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest) {
            return ColorUtil.toRGBA(Color.blue);
        }
        if (tileEntity instanceof TileEntityShulkerBox) {
            return ColorUtil.toRGBA(Color.red);
        }
        if (tileEntity instanceof TileEntityEnderChest) {
            return ColorUtil.toRGBA(Color.pink);
        }
        if (tileEntity instanceof TileEntityFurnace) {
            return ColorUtil.toRGBA(Color.gray);
        }
        if (tileEntity instanceof TileEntityHopper) {
            return ColorUtil.toRGBA(Color.orange);
        }
        if (tileEntity instanceof TileEntityDispenser) {
            return ColorUtil.toRGBA(Color.magenta);
        }
        return -1;
    }

    private int getEntityColor(Entity entity) {
        if (entity instanceof EntityMinecartChest) {
            return ColorUtil.toRGBA(Color.cyan);
        }
        if (entity instanceof EntityItemFrame && ((EntityItemFrame)entity).getDisplayedItem().getItem() instanceof ItemShulkerBox) {
            return ColorUtil.toRGBA(Color.red);
        }
        if (entity instanceof EntityItemFrame && !(((EntityItemFrame)entity).getDisplayedItem().getItem() instanceof ItemShulkerBox)) {
            return ColorUtil.toRGBA(Color.yellow);
        }
        return -1;
    }
}

