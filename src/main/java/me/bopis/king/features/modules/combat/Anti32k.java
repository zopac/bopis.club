package me.bopis.king.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.bopis.king.Bopis;
import me.bopis.king.features.command.Command;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

public class Anti32k extends Module {

    public Anti32k() {
        super("Anti32k", "tomfoolery", Module.Category.COMBAT, true, false, false);
    }

    private final Setting <Boolean> LogOut = register(new Setting<Boolean>("LogOut", true));

    private final Set<EntityPlayer> sword = Collections.newSetFromMap(new WeakHashMap<>());

    private boolean is32k(EntityPlayer player, ItemStack stack) {
        if (stack.getItem() instanceof net.minecraft.item.ItemSword) {
            NBTTagList enchants = stack.getEnchantmentTagList();
            for (int i = 0; i < enchants.tagCount(); i++) {
                if (enchants.getCompoundTagAt(i).getShort("lvl") >= Short.MAX_VALUE)
                    return true;
            }
        }
        return false ;
    }

    @Override
    public void onTick() {
        int once = 0;
        for (EntityPlayer player : mc.world.playerEntities) {
            int Distance = (int)mc.player.getDistance(player);
            if (player.equals(mc.player))
                continue;
            if (is32k(player, player.getHeldItem(EnumHand.MAIN_HAND)) && !this.sword.contains(player)) {
                Command.sendMessage(ChatFormatting.RED + player.getDisplayNameString() + " is holding a 32k");
                this.sword.add(player);
            }
            if (is32k(player, player.getHeldItem(EnumHand.MAIN_HAND))) {
                if(once>0){return;}
                once++;
                if(LogOut.getValue()) {
                    if(! Bopis.friendManager.isFriend(player.getName())){
                        if(Distance < 8){
                            Objects.requireNonNull ( Minecraft.getMinecraft ( ).getConnection ( ) ).handleDisconnect(new SPacketDisconnect(new TextComponentString(ChatFormatting.RED + "[32k Detect] Detected 32k near you")));
                        }
                    }
                }
            }
            if (!this.sword.contains(player))
                continue;
            if (is32k(player, player.getHeldItem(EnumHand.MAIN_HAND)))
                continue;
            Command.sendMessage(ChatFormatting.GREEN + player.getDisplayNameString() + " is no longer holding a 32k");
            this.sword.remove(player);
        }
    }
}
