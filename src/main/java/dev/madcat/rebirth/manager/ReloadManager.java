

package dev.madcat.rebirth.manager;

import dev.madcat.rebirth.features.*;
import net.minecraftforge.common.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.features.command.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.network.play.client.*;
import dev.madcat.rebirth.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class ReloadManager extends Feature
{
    public String prefix;
    
    public void init(final String prefix) {
        this.prefix = prefix;
        MinecraftForge.EVENT_BUS.register((Object)this);
        if (!fullNullCheck()) {
            Command.sendMessage(ChatFormatting.RED + "unloaded. Type " + prefix + "reload to reload.");
        }
    }
    
    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final CPacketChatMessage packet;
        if (event.getPacket() instanceof CPacketChatMessage && (packet = (CPacketChatMessage)event.getPacket()).getMessage().startsWith(this.prefix) && packet.getMessage().contains("reload")) {
            Rebirth.load();
            event.setCanceled(true);
        }
    }
}
