package me.alpha432.oyvey.features.modules.combat;

import java.util.Comparator;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.alpha432.oyvey.Bopis;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BadlionTessellator;
import me.alpha432.oyvey.util.ColorUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BedAura extends Module {
    public BedAura() {
        super("BedAura", "hiugedawihodjw", Module.Category.COMBAT, true, false, false);
    }
    
    public Setting<Integer> placedelay = register(new Setting<Integer>("Place Delay", 15, 1, 20));
    public Setting<Integer> range = register(new Setting<Integer>("Range", 7, 1, 9));
    public Setting<Boolean> announceUsage = register(new Setting("Announce", true));
    public Setting<Boolean> placeesp = register(new Setting("Bed ESP", true));

    private int playerHotbarSlot = -1;
    private int lastHotbarSlot = -1;
    private EntityPlayer closestTarget;
    private String lastTickTargetName;
    private int bedSlot = -1;
    private BlockPos placeTarget;
    private float rotVar;
    private int blocksPlaced;
    private double diffXZ;
    private boolean firstRun;

    private boolean nowTop = false;


    @Override
    public void onEnable() {

        if (mc.player == null) {
            this.toggle();
            return;
        }

        MinecraftForge.EVENT_BUS.register(this);
        //this is gay ^
        //this should already happen on the onEnable() in module.class (since its overriding it)
        //but it doesnt


        firstRun = true;


        blocksPlaced = 0;


        playerHotbarSlot = mc.player.inventory.currentItem;
        lastHotbarSlot = -1;


    }

    @Override
    public void onDisable() {

        if (mc.player == null) {
            return;
        }

        MinecraftForge.EVENT_BUS.unregister(this);

        if (lastHotbarSlot != playerHotbarSlot && playerHotbarSlot != -1) {
            mc.player.inventory.currentItem = playerHotbarSlot;
        }

        playerHotbarSlot = -1;
        lastHotbarSlot = -1;

        if (announceUsage.getValue()) {
            Command.sendMessage(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "BedAura" + TextFormatting.BLUE + "]" + ChatFormatting.RED.toString() + " Disabled" + ChatFormatting.RESET.toString() + "!");
        }

        blocksPlaced = 0;


    }

    @Override
    public void onUpdate() {

        if (mc.player == null) {
            return;
        }
        if(mc.player.dimension == 0) {
            Command.sendMessage("You are in the overworld!");
            this.toggle();
        }
        try {
            findClosestTarget();
        } catch(NullPointerException npe) {}
        //yeah if someone knows a more appropriate way to deal with npe crashes
        //please message me on discord
        //im gonna be averaging like 3 try/catches per module at this rate
        //as might as well literally just surround the entire fucking module in a try catch

        if(closestTarget == null && mc.player.dimension != 0) {
            if(firstRun) {
                firstRun = false;
                if(announceUsage.getValue()) {
                    Command.sendMessage(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "BedAura" + TextFormatting.BLUE + "]" + TextFormatting.WHITE + " enabled, " + TextFormatting.WHITE + "waiting for target.");
                }
            }
        }

        if (firstRun && closestTarget != null && mc.player.dimension != 0) {
            firstRun = false;
            lastTickTargetName = closestTarget.getName();
            if (announceUsage.getValue()) {
                Command.sendMessage(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "BedAura" + TextFormatting.BLUE + "]" + TextFormatting.WHITE + " enabled" + TextFormatting.WHITE + ", target: " + ChatFormatting.BLUE.toString() + lastTickTargetName);
            }
        }

        if(closestTarget != null && lastTickTargetName != null) {
            if (!lastTickTargetName.equals(closestTarget.getName())) {
                lastTickTargetName = closestTarget.getName();
                if (announceUsage.getValue()) {
                    Command.sendMessage(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "BedAura" + TextFormatting.BLUE + "]" + TextFormatting.WHITE + " New target: " + ChatFormatting.BLUE.toString() + lastTickTargetName);
                }
            }
        }
        try {
            diffXZ = mc.player.getPositionVector().distanceTo(closestTarget.getPositionVector());
        } catch(NullPointerException npe) {

        }

        try {
            if(closestTarget != null) {
                placeTarget = new BlockPos(closestTarget.getPositionVector().add(1, 1, 0));
                nowTop = false;
                rotVar = 90;

                BlockPos block1 = placeTarget;
                if(!canPlaceBed(block1)) {
                    placeTarget = new BlockPos(closestTarget.getPositionVector().add(-1, 1, 0));
                    rotVar = -90;
                    nowTop = false;
                }
                BlockPos block2 = placeTarget;
                if(!canPlaceBed(block2)) {
                    placeTarget = new BlockPos(closestTarget.getPositionVector().add(0, 1, 1));
                    rotVar = 180;
                    nowTop = false;
                }
                BlockPos block3 = placeTarget;
                if(!canPlaceBed(block3)) {
                    placeTarget = new BlockPos(closestTarget.getPositionVector().add(0, 1, -1));
                    rotVar = 0;
                    nowTop = false;
                }
                BlockPos block4 = placeTarget;
                if(!canPlaceBed(block4)) {
                    placeTarget = new BlockPos(closestTarget.getPositionVector().add(0, 2, -1));
                    rotVar = 0;
                    nowTop = true;
                }
                BlockPos blockt1 = placeTarget;
                if(nowTop && !canPlaceBed(blockt1)) {
                    placeTarget = new BlockPos(closestTarget.getPositionVector().add(-1, 2, 0));
                    rotVar = -90;
                }
                BlockPos blockt2 = placeTarget;
                if(nowTop && !canPlaceBed(blockt2)) {
                    placeTarget = new BlockPos(closestTarget.getPositionVector().add(0, 2, 1));
                    rotVar = 180;
                }
                BlockPos blockt3 = placeTarget;
                if(nowTop && !canPlaceBed(blockt3)) {
                    placeTarget = new BlockPos(closestTarget.getPositionVector().add(1, 2, 0));
                    rotVar = 90;
                }
            }

            mc.world.loadedTileEntityList.stream()
                    .filter(e -> e instanceof TileEntityBed)
                    .filter(e -> mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()) <= range.getValue())
                    .sorted(Comparator.comparing(e -> mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ())))
                    .forEach(bed -> {
                        if(mc.player.dimension != 0) {
                            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(bed.getPos(), EnumFacing.UP, EnumHand.OFF_HAND, 0, 0, 0));
                        }
                    });

            if((mc.player.ticksExisted % placedelay.getValue() == 0)  && closestTarget != null) {
                this.findBeds();
                mc.player.ticksExisted++;
                this.doDaMagic();
            }




        } catch(NullPointerException npe) {
            npe.printStackTrace();
        }
    }
    private void doDaMagic() {
        if(diffXZ <= range.getValue()) {
            for (int i = 0; i < 9; i++) {
                if (bedSlot != -1) {
                    break;
                }
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() instanceof ItemBed) {
                    bedSlot = i;
                    if(i != -1) {
                        mc.player.inventory.currentItem = bedSlot;
                    }
                    break;
                }
            }
            bedSlot = -1;
            if(blocksPlaced == 0 && mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem() instanceof ItemBed) {
                mc.player.connection.sendPacket(new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotVar, 0, mc.player.onGround));
                placeBlock(new BlockPos(this.placeTarget), EnumFacing.DOWN);
                mc.player.connection.sendPacket(new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                blocksPlaced = 1;
                nowTop = false;
            }

            blocksPlaced = 0;

        }
    }

    private void findBeds() {
        if (mc.currentScreen == null || !(mc.currentScreen instanceof GuiContainer)) {
            if (mc.player.inventory.getStackInSlot(0).getItem() != Items.BED) {
                for(int i = 9; i < 36; ++i) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, mc.player);
                        break;
                    }

                }

            }
        }
    }

    private boolean canPlaceBed(BlockPos pos) {
        return (mc.world.getBlockState(pos).getBlock() == Blocks.AIR || mc.world.getBlockState(pos).getBlock() == Blocks.BED) &&
                mc.world.getEntitiesWithinAABB((Class) Entity.class, new AxisAlignedBB(pos)).isEmpty();

    }

    private void findClosestTarget() {
        List<EntityPlayer> playerList = mc.world.playerEntities;
        closestTarget = null;
        for (EntityPlayer target : playerList) {
            if (target == mc.player) {
                continue;
            }
            if (Bopis.friendManager.isFriend(target.getName())) {
                continue;
            }
            if (!isLiving(target)) {
                continue;
            }
            if ((target).getHealth() <= 0) {
                continue;
            }
            if (closestTarget == null) {
                closestTarget = target;
                continue;
            }
            if (mc.player.getDistance(target) < mc.player.getDistance(closestTarget)) {
                closestTarget = target;
            }
        }
    }

    private void placeBlock(BlockPos pos, EnumFacing side) {
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static boolean isLiving(Entity e) {
        return e instanceof EntityLivingBase;
    }

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        if(placeTarget == null || mc.world == null || closestTarget == null) return;
        if(placeesp.getValue()) {try {
            float posx = placeTarget.getX();
            float posy = placeTarget.getY();
            float posz = placeTarget.getZ();
            BadlionTessellator.prepare("lines");
            BadlionTessellator.draw_cube_line(posx, posy, posz, ColorUtils.GenRainbow(), "all");
            if(rotVar == 90) {
                BadlionTessellator.draw_cube_line(posx - 1, posy, posz, ColorUtils.GenRainbow(), "all");
            }
            if(rotVar == 0) {
                BadlionTessellator.draw_cube_line(posx, posy, posz + 1, ColorUtils.GenRainbow(), "all");
            }
            if(rotVar == -90) {
                BadlionTessellator.draw_cube_line(posx + 1, posy, posz, ColorUtils.GenRainbow(), "all");
            }
            if(rotVar == 180) {
                BadlionTessellator.draw_cube_line(posx, posy, posz - 1, ColorUtils.GenRainbow(), "all");
            }
        } catch(Exception ignored) {
        }
            BadlionTessellator.release();
        }
    }

}
