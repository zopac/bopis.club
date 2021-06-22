package me.bopis.king.features.modules.player;

import com.mojang.authlib.GameProfile;
import me.bopis.king.Bopis;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FakePlayer extends Module {
    private static FakePlayer INSTANCE = new FakePlayer();
    private final List<EntityOtherPlayerMP> fakeEntities = new ArrayList<EntityOtherPlayerMP>();
    public Setting<Boolean> multi = this.register(new Setting<Boolean>("Multi", false));
    public List<Integer> fakePlayerIdList = new ArrayList<Integer>();
    private final Setting<Boolean> copyInv = this.register(new Setting<Boolean>("CopyInv", true));
    private final Setting<Integer> players = this.register(new Setting<Object>("Players", 1, 1, 9, v -> this.multi.getValue()));
    public static final String[][] multiPlayer = new String[][]{{"4538d5ab-ff77-407c-9a1e-1b713ef99a0d", "joe", "3", "0"}, {"4538d5ab-ff77-407c-9a1e-1b713ef99a0d", "joe", "-3", "0"}, {"4538d5ab-ff77-407c-9a1e-1b713ef99a0d", "joe", "0", "-3"}, {"4538d5ab-ff77-407c-9a1e-1b713ef99a0d", "joe", "0", "3"}, {"4538d5ab-ff77-407c-9a1e-1b713ef99a0d", "joe", "6", "0"}, {"4538d5ab-ff77-407c-9a1e-1b713ef99a0d", "joe", "-6", "0"}, {"4538d5ab-ff77-407c-9a1e-1b713ef99a0d", "joe", "0", "-6"}, {"4538d5ab-ff77-407c-9a1e-1b713ef99a0d", "joe", "0", "6"}, {"4538d5ab-ff77-407c-9a1e-1b713ef99a0d", "joe", "0", "9"}, {"4538d5ab-ff77-407c-9a1e-1b713ef99a0d", "joe", "0", "-9"}};
    private static final String[] singlePlayer = new String[]{"4538d5ab-ff77-407c-9a1e-1b713ef99a0d", "joe"};

    public FakePlayer() {
        super("FakePlayer", "Spawns in a fake player", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static FakePlayer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakePlayer();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (mc.player.equals(null)) {
            disable();
            return;
        }
        this.fakePlayerIdList = new ArrayList<Integer>();
        if (this.multi.getValue().booleanValue()) {
            int amount = 0;
            int entityId = -101;
            for (String[] data : multiPlayer) {
                this.addFakePlayer(data[0], data[1], entityId, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                if (++amount >= this.players.getValue()) {
                    return;
                }
                entityId -= amount;
            }
        } else {
            this.addFakePlayer(singlePlayer[0], singlePlayer[1], -100, 0, 0);
        }
    }

    @Override
    public void onDisable() {
        if (mc.player.equals(null)) {
            return;
        }
        for (int id : this.fakePlayerIdList) {
            FakePlayer.mc.world.removeEntityFromWorld(id);
        }
    }

    @Override
    public void onLogout() {
        if (isOn()) {
            disable();
        }
    }

    private void addFakePlayer(String uuid, String name, int entityId, int offsetX, int offsetZ) {
        GameProfile profile = new GameProfile(UUID.fromString(uuid), name);
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, profile);
        fakePlayer.copyLocationAndAnglesFrom(FakePlayer.mc.player);
        fakePlayer.posX += offsetX;
        fakePlayer.posZ += offsetZ;
        if (this.copyInv.getValue().booleanValue()) {
            for (PotionEffect potionEffect : Bopis.potionManager.getOwnPotions()) {
                fakePlayer.addPotionEffect(potionEffect);
            }
            fakePlayer.inventory.copyInventory(FakePlayer.mc.player.inventory);
        }
        fakePlayer.setHealth(FakePlayer.mc.player.getHealth() + FakePlayer.mc.player.getAbsorptionAmount());
        this.fakeEntities.add(fakePlayer);
        FakePlayer.mc.world.addEntityToWorld(entityId, fakePlayer);
        this.fakePlayerIdList.add(entityId);
    }
}

