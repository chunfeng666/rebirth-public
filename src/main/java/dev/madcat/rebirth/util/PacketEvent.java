

package dev.madcat.rebirth.util;

import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.*;

@Cancelable
public class PacketEvent extends MomentumEvent
{
    Packet<?> packet;
    
    public PacketEvent(final Packet<?> packet, final MomentumEvent.Stage stage) {
        super(stage);
        this.packet = packet;
    }
    
    public Packet<?> getPacket() {
        return this.packet;
    }
}
