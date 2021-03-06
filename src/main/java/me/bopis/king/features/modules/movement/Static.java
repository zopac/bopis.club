package me.bopis.king.features.modules.movement;

import me.bopis.king.features.modules.Module;
import me.bopis.king.features.setting.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class Static
        extends Module {
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.NOVOID));
    private final Setting<Boolean> disabler = this.register(new Setting<Object>("Disable", true, v -> this.mode.getValue() == Mode.ROOF));
    private final Setting<Boolean> ySpeed = this.register(new Setting<Object>("YSpeed", false, v -> this.mode.getValue() == Mode.STATIC));
    private final Setting<Float> speed = this.register(new Setting<Object>("Speed", 0.1f, 0.0f, 10.0f, v -> this.ySpeed.getValue() != false && this.mode.getValue() == Mode.STATIC));
    private final Setting<Float> height = this.register(new Setting<Object>("Height", 2.5f, 0f, 10f, v -> this.mode.getValue() == Mode.NOVOID));

    public Static() {
        super("Static", "Stops any movement. Glitches you up.", Module.Category.MOVEMENT, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (Static.fullNullCheck()) {
            return;
        }
        switch (this.mode.getValue()) {
            case STATIC: {
                Static.mc.player.capabilities.isFlying = false;
                Static.mc.player.motionX = 0.0;
                Static.mc.player.motionY = 0.0;
                Static.mc.player.motionZ = 0.0;
                if (!this.ySpeed.getValue().booleanValue()) break;
                Static.mc.player.jumpMovementFactor = this.speed.getValue().floatValue();
                if (Static.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Static.mc.player.motionY += this.speed.getValue().floatValue();
                }
                if (!Static.mc.gameSettings.keyBindSneak.isKeyDown()) break;
                Static.mc.player.motionY -= this.speed.getValue().floatValue();
                break;
            }
            case ROOF: {
                Static.mc.player.connection.sendPacket(new CPacketPlayer.Position(Static.mc.player.posX, 10000.0, Static.mc.player.posZ, Static.mc.player.onGround));
                if (!this.disabler.getValue().booleanValue()) break;
                this.disable();
                break;
            }
            case NOVOID: {
                if (Static.mc.player.noClip || !(Static.mc.player.posY <= (double) this.height.getValue().floatValue()))
                    break;
                RayTraceResult trace = Static.mc.world.rayTraceBlocks(Static.mc.player.getPositionVector(), new Vec3d(Static.mc.player.posX, 0.0, Static.mc.player.posZ), false, false, false);
                if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return;
                }
                Static.mc.player.setVelocity(0.0, 0.0, 0.0);
                if (Static.mc.player.getRidingEntity() == null) break;
                Static.mc.player.getRidingEntity().setVelocity(0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.mode.getValue() == Mode.ROOF) {
            return "Roof";
        }
        if (this.mode.getValue() == Mode.NOVOID) {
            return "NoVoid";
        }
        return null;
    }

    public enum Mode {
        STATIC,
        ROOF,
        NOVOID

    }
}

