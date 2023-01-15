

package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class NoRotate extends Module
{
    private final Timer timer;
    private boolean cancelPackets;
    private boolean timerReset;
    
    public NoRotate() {
        super("NoRotate", "Dangerous to use might desync you.", Category.MISC, true, false, false);
        this.timer = new Timer();
        this.cancelPackets = true;
        this.timerReset = false;
    }
    
    @Override
    public void onLogout() {
        this.cancelPackets = false;
    }
    
    @Override
    public void onLogin() {
        this.timer.reset();
        this.timerReset = true;
    }
    
    @Override
    public void onUpdate() {
        if (this.timerReset && !this.cancelPackets) {
            this.cancelPackets = true;
            this.timerReset = false;
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && this.cancelPackets && event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            packet.yaw = NoRotate.mc.player.rotationYaw;
            packet.pitch = NoRotate.mc.player.rotationPitch;
        }
    }
}
