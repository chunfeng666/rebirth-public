
package dev.madcat.rebirth.util;

import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.*;

@Cancelable
public class PacketSendEvent extends PacketEvent
{
    public PacketSendEvent(final Packet<?> packet, final MomentumEvent.Stage stage) {
        super((Packet)packet, stage);
    }
}
