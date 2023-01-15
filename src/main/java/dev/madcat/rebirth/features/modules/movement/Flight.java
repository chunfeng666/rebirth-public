
package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.client.entity.*;

public final class Flight extends Module
{
    private static Flight INSTANCE;
    private final Setting<Float> speed;
    private final Setting<Boolean> glide;
    
    public Flight() {
        super("Flight", "Allows you to fly.", Module.Category.MOVEMENT, false, false, false);
        this.speed = (Setting<Float>)this.register(new Setting("Speed", 1.0f, 0.5f, 10.0f));
        this.glide = (Setting<Boolean>)this.register(new Setting("Glide", true));
        Flight.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (Flight.mc.player == null || Flight.mc.world == null) {
            return;
        }
        Flight.mc.player.capabilities.isFlying = false;
        Flight.mc.player.motionX = 0.0;
        Flight.mc.player.motionY = 0.0;
        Flight.mc.player.motionZ = 0.0;
        Flight.mc.player.jumpMovementFactor = this.speed.getValue();
        if (this.glide.getValue() && !Flight.mc.player.onGround) {
            Flight.mc.player.motionY = -0.03150000050663948;
            final EntityPlayerSP player = Flight.mc.player;
            player.jumpMovementFactor *= 1.21337f;
        }
        if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP player2 = Flight.mc.player;
            player2.motionY += this.speed.getValue();
        }
        if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
            final EntityPlayerSP player3 = Flight.mc.player;
            player3.motionY -= this.speed.getValue();
        }
    }
    
    static {
        Flight.INSTANCE = new Flight();
    }
}
