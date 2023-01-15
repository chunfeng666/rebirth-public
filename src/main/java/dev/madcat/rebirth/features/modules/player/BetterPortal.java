

package dev.madcat.rebirth.features.modules.player;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class BetterPortal extends Module
{
    public Setting<Boolean> portalChat;
    public Setting<Boolean> godmode;
    private static BetterPortal INSTANCE;
    
    public BetterPortal() {
        super("BetterPortal", "Tweaks for Portals.", Module.Category.PLAYER, true, false, false);
        this.portalChat = (Setting<Boolean>)this.register(new Setting("Chat", Boolean.TRUE, "Allows you to chat in portals."));
        this.godmode = (Setting<Boolean>)this.register(new Setting("GodMode", Boolean.FALSE, "Portal Godmode."));
        this.setInstance();
    }
    
    private void setInstance() {
        BetterPortal.INSTANCE = this;
    }
    
    public static BetterPortal getInstance() {
        if (BetterPortal.INSTANCE == null) {
            BetterPortal.INSTANCE = new BetterPortal();
        }
        return BetterPortal.INSTANCE;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && this.godmode.getValue() && event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCanceled(true);
        }
    }
    
    static {
        BetterPortal.INSTANCE = new BetterPortal();
    }
}
