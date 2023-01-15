

package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraftforge.client.event.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.client.entity.*;

public class Speed extends Module
{
    public Setting<Double> speed;
    private final Setting<Boolean> airMotion;
    private final Setting<Boolean> autoJump;
    private final Setting<Boolean> useTimer;
    private final Setting<Boolean> IceSpeed;
    public Setting<Float> slipperiness;
    int waitCounter;
    
    public Speed() {
        super("Speed", "placeholder", Module.Category.MOVEMENT, false, false, false);
        this.speed = (Setting<Double>)this.register(new Setting("Speed", 1.0, 0.0, 50.0));
        this.airMotion = (Setting<Boolean>)this.register(new Setting("InAirMotion", true));
        this.autoJump = (Setting<Boolean>)this.register(new Setting("AutoJump", true));
        this.useTimer = (Setting<Boolean>)this.register(new Setting("UseTimer", true));
        this.IceSpeed = (Setting<Boolean>)this.register(new Setting("IceSpeed", false));
        this.slipperiness = (Setting<Float>)this.register(new Setting("Slipperiness", 0.4f, 0.2f, 1.0f));
    }
    
    @SubscribeEvent
    public void onInput(final InputUpdateEvent event) {
        if (Speed.mc.player.isHandActive() && !Speed.mc.player.isRiding()) {
            final MovementInput movementInput = Speed.mc.player.movementInput;
            movementInput.moveStrafe /= 0.2f;
            final MovementInput movementInput2 = Speed.mc.player.movementInput;
            movementInput2.moveForward /= 0.2f;
        }
    }
    
    public void onTick() {
        if (Speed.mc.player.isRiding()) {
            return;
        }
        if (Speed.mc.player.capabilities != null && (Speed.mc.player.capabilities.isFlying || Speed.mc.player.isElytraFlying())) {
            return;
        }
        if (Speed.mc.player.isHandActive() && Speed.mc.player.getHeldItem(Speed.mc.player.getActiveHand()).getItem() instanceof ItemShield && (Speed.mc.player.movementInput.moveStrafe != 0.0f || (Speed.mc.player.movementInput.moveForward != 0.0f && Speed.mc.player.getItemInUseMaxCount() >= 8))) {
            Speed.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Speed.mc.player.getHorizontalFacing()));
        }
        if (this.IceSpeed.getValue()) {
            Blocks.ICE.slipperiness = this.slipperiness.getValue();
            Blocks.PACKED_ICE.slipperiness = this.slipperiness.getValue();
            Blocks.FROSTED_ICE.slipperiness = this.slipperiness.getValue();
        }
        if (this.useTimer.getValue()) {}
        final boolean boost = Math.abs(Speed.mc.player.rotationYawHead - Speed.mc.player.rotationYaw) < 90.0f;
        if (Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f) {
            if (!Speed.mc.player.isSprinting()) {
                Speed.mc.player.setSprinting(true);
            }
            if (Speed.mc.player.onGround) {
                if (this.waitCounter < 1) {
                    ++this.waitCounter;
                    return;
                }
                this.waitCounter = 0;
                final float yaw = getPlayerDirection();
                if (this.autoJump.getValue()) {
                    Speed.mc.player.jump();
                }
                final EntityPlayerSP player = Speed.mc.player;
                player.motionX -= MathHelper.sin(yaw) * 0.005f * this.speed.getValue();
                final EntityPlayerSP player2 = Speed.mc.player;
                player2.motionZ += MathHelper.cos(yaw) * 0.005f * this.speed.getValue();
            }
            else if (this.airMotion.getValue()) {
                final float direction = getPlayerDirection();
                final double currentSpeed = Math.sqrt(Speed.mc.player.motionX * Speed.mc.player.motionX + Speed.mc.player.motionZ * Speed.mc.player.motionZ);
                double speed = boost ? 1.0064 : 1.001;
                if (Speed.mc.player.motionY < 0.0) {
                    speed = 1.0;
                }
                Speed.mc.player.motionX = -Math.sin(direction) * speed * currentSpeed;
                Speed.mc.player.motionZ = Math.cos(direction) * speed * currentSpeed;
            }
        }
        else {
            Speed.mc.player.motionX = 0.0;
            Speed.mc.player.motionZ = 0.0;
        }
    }
    
    private static float getPlayerDirection() {
        float rotationYaw = Speed.mc.player.rotationYaw;
        if (Speed.mc.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (Speed.mc.player.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (Speed.mc.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Speed.mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (Speed.mc.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return rotationYaw * 0.017453292f;
    }
    
    public void onDisable() {
        if (this.useTimer.getValue()) {}
        if (this.IceSpeed.getValue()) {
            Blocks.ICE.slipperiness = 0.98f;
            Blocks.PACKED_ICE.slipperiness = 0.98f;
            Blocks.FROSTED_ICE.slipperiness = 0.98f;
        }
    }
}
