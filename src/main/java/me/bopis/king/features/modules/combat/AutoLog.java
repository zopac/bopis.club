package me.bopis.king.features.modules.combat;

import me.bopis.king.Bopis;
import me.bopis.king.event.events.PacketEvent;
import me.bopis.king.features.Feature;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import me.bopis.king.util.DamageUtil;
import me.bopis.king.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoLog extends Module {
    public Setting<Boolean> fakeKick;
    public Setting<Boolean> disable;
    public Setting<Float> health;
    public Setting<Boolean> beds;
    public Setting<Float> bedrange;
    public Setting<Boolean> crystals;
    public Setting<Boolean> players;
    float playerhealth;

    public AutoLog() {
        super("AutoLog", "Automatically logs you out if certain conditions are met.", Category.COMBAT, true, false, false);
        this.fakeKick = (Setting<Boolean>) this.register(new Setting("FakeKick", false));
        this.disable = (Setting<Boolean>) this.register(new Setting("DisableOnLogout", false));
        this.health = (Setting<Float>) this.register(new Setting("Health", 16.0f, 0.1f, 36.0f));
        this.beds = (Setting<Boolean>) this.register(new Setting("Beds", false));
        this.bedrange = (Setting<Float>) this.register(new Setting("BedRange", 4.0f, 0.1f, 10.0f, v -> this.beds.getValue()));
        this.crystals = (Setting<Boolean>) this.register(new Setting("LethalCrystal", false));
        this.players = (Setting<Boolean>) this.register(new Setting("Players", false));
        this.playerhealth = -1.0f;
    }

    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        this.playerhealth = AutoLog.mc.player.getHealth() + AutoLog.mc.player.getAbsorptionAmount();
        if (this.playerhealth <= this.health.getValue()) {
            this.logout();
            return;
        }
        for (final Entity e : AutoLog.mc.world.loadedEntityList) {
            if (e instanceof EntityPlayer && this.players.getValue() && !e.equals(AutoLog.mc.player)) {
                if (Bopis.friendManager.isFriend((EntityPlayer) e)) {
                    continue;
                }
                this.logout();
            } else {
                if (!(e instanceof EntityEnderCrystal) || !this.crystals.getValue() || this.playerhealth - DamageUtil.calculateDamage(e, AutoLog.mc.player) >= this.health.getValue()) {
                    continue;
                }
                this.logout();
            }
        }
    }

    @SubscribeEvent
    public void onReceivePacket(final PacketEvent.Receive event) {
        if (fullNullCheck()) {
            return;
        }
        this.playerhealth = AutoLog.mc.player.getHealth() + AutoLog.mc.player.getAbsorptionAmount();
        final SPacketBlockChange packet;
        if (event.getPacket() instanceof SPacketBlockChange && this.beds.getValue() && (packet = event.getPacket()).getBlockState().getBlock() == Blocks.BED && AutoLog.mc.player.getDistanceSq(packet.getBlockPosition()) <= MathUtil.square(this.bedrange.getValue())) {
            this.logout();
        }
    }

    private void logout() {
        if (AutoLog.mc.isSingleplayer() || Feature.fullNullCheck()) {
            return;
        }
        if (this.fakeKick.getValue()) {
            AutoLog.mc.getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString("Internal Exception: java.lang.NullPointerException")));
        } else {
            AutoLog.mc.getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString("[AutoLog] Logged out")));
        }
        if (this.disable.getValue()) {
            this.toggle();
        }
    }
}
