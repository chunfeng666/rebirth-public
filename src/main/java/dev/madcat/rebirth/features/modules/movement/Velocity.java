
package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import dev.madcat.rebirth.event.events.*;

public class Velocity extends Module
{
    public Setting<Boolean> antiKnockBack;
    public Setting<Boolean> noEntityPush;
    public Setting<Boolean> noBlockPush;
    public Setting<Boolean> noWaterPush;
    
    public Velocity() {
        super("Velocity", "Anti push and knock back.", Module.Category.MOVEMENT, true, false, false);
        this.antiKnockBack = (Setting<Boolean>)this.register(new Setting("KnockBack", true));
        this.noEntityPush = (Setting<Boolean>)this.register(new Setting("No PlayerPush", true));
        this.noBlockPush = (Setting<Boolean>)this.register(new Setting("No BlockPush", true));
        this.noWaterPush = (Setting<Boolean>)this.register(new Setting("No LiquidPush", true));
    }
    
    @SubscribeEvent
    public void onPacketReceived(final PacketEvent.Receive event) {
        if (fullNullCheck()) {
            return;
        }
        if (this.antiKnockBack.getValue()) {
            if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                event.setCanceled(true);
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public void onPush(final PushEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0 && this.noEntityPush.getValue() && event.entity.equals((Object)Velocity.mc.player)) {
            event.x = -event.x * 0.0;
            event.y = -event.y * 0.0;
            event.z = -event.z * 0.0;
        }
        else if (event.getStage() == 1 && this.noBlockPush.getValue()) {
            event.setCanceled(true);
        }
        else if (event.getStage() == 2 && this.noWaterPush.getValue() && Velocity.mc.player != null && Velocity.mc.player.equals((Object)event.entity)) {
            event.setCanceled(true);
        }
    }
}
