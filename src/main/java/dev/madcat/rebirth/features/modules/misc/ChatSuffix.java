

package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.event.events.*;
import dev.madcat.rebirth.manager.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class ChatSuffix extends Module
{
    public ChatSuffix() {
        super("ChatSuffix", "suffix", Category.MISC, true, false, false);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (ModuleManager.getModuleByName("AutoQueue").isEnabled()) {
            return;
        }
        final CPacketChatMessage packet;
        final String message;
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage && !(message = (packet = (CPacketChatMessage)event.getPacket()).getMessage()).startsWith("/")) {
            packet.message = message + " \u267f \u1d0d\u1d00\u1d05\u1d04\u1d00\u1d1b \u267f";
        }
    }
}
