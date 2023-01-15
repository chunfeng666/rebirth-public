
package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraftforge.client.event.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import dev.madcat.rebirth.event.events.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.client.entity.*;

public class NoSlow extends Module
{
    public Setting<Boolean> slow;
    public Setting<Boolean> shift;
    public Setting<Boolean> web;
    private final Setting<Float> speed;
    private final Setting<Float> speed2;
    
    public NoSlow() {
        super("NoSlow", "No item use slow down.", Module.Category.MOVEMENT, true, false, false);
        this.slow = (Setting<Boolean>)this.register(new Setting("Slow", true));
        this.shift = (Setting<Boolean>)this.register(new Setting("Sneak", false));
        this.web = (Setting<Boolean>)this.register(new Setting("Web", false));
        this.speed = (Setting<Float>)this.register(new Setting("Factor", 10.0f, 1.0f, 10.0f, v -> this.web.getValue()));
        this.speed2 = (Setting<Float>)this.register(new Setting("Factor", 0.0f, 1.0f, 1.0f, v -> this.web.getValue()));
    }
    
    @SubscribeEvent
    public void Slow(final InputUpdateEvent event) {
        if (!NoSlow.mc.player.isSneaking() && NoSlow.mc.player.isHandActive() && this.slow.getValue() && !NoSlow.mc.player.isRiding()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveStrafe *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.moveForward *= 5.0f;
            return;
        }
        if (this.shift.getValue() && NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
            final MovementInput movementInput3 = event.getMovementInput();
            movementInput3.moveStrafe *= 5.0f;
            final MovementInput movementInput4 = event.getMovementInput();
            movementInput4.moveForward *= 5.0f;
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 1 && this.web.getValue() && NoSlow.mc.player.isInWeb) {
            final double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
            NoSlow.mc.player.motionX = calc[0];
            NoSlow.mc.player.motionZ = calc[1];
            final EntityPlayerSP player = NoSlow.mc.player;
            player.motionY -= this.speed2.getValue();
        }
    }
}
