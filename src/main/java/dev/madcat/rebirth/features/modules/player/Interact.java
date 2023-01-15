

package dev.madcat.rebirth.features.modules.player;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Interact extends Module
{
    private static Interact INSTANCE;
    
    public Interact() {
        super("Interact", "Allows you to place at build height", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }
    
    public static Interact getInstance() {
        if (Interact.INSTANCE == null) {
            Interact.INSTANCE = new Interact();
        }
        return Interact.INSTANCE;
    }
    
    private void setInstance() {
        Interact.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final CPacketPlayerTryUseItemOnBlock packet;
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && (packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket()).getPos().getY() >= 255 && packet.getDirection() == EnumFacing.UP) {
            packet.placedBlockDirection = EnumFacing.DOWN;
        }
    }
    
    static {
        Interact.INSTANCE = new Interact();
    }
}
