
package dev.madcat.rebirth.util;

import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.*;

@Cancelable
public class PacketReceiveEvent extends PacketEvent
{
    public PacketReceiveEvent(final Packet<?> packet, final MomentumEvent.Stage stage) {
        super((Packet)packet, stage);
    }
}
