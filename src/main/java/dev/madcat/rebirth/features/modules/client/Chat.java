

package dev.madcat.rebirth.features.modules.client;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.client.gui.*;
import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.network.play.client.*;
import dev.madcat.rebirth.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import java.util.*;
import java.text.*;
import net.minecraft.util.text.*;
import com.mojang.realmsclient.gui.*;

public class Chat extends Module
{
    private static Chat INSTANCE;
    public Setting<Boolean> clean;
    public Setting<Boolean> infinite;
    public Setting<Boolean> time;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    public boolean check;
    
    public Chat() {
        super("BetterChat", "Modifies your chat", Category.CLIENT, true, false, false);
        this.clean = (Setting<Boolean>)this.register(new Setting("NoChatBackground", false, "Cleans your chat"));
        this.infinite = (Setting<Boolean>)this.register(new Setting("InfiniteChat", false, "Makes your chat infinite."));
        this.time = (Setting<Boolean>)this.register(new Setting("ChatTime", false, "Makes your chat infinite."));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 20, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 20, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 20, 0, 255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 35, 0, 255));
        this.setInstance();
    }
    
    public static Chat getInstance() {
        if (Chat.INSTANCE == null) {
            Chat.INSTANCE = new Chat();
        }
        return Chat.INSTANCE;
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (Chat.mc.currentScreen instanceof GuiChat) {
            RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
        }
    }
    
    private void setInstance() {
        Chat.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            final String s = ((CPacketChatMessage)event.getPacket()).getMessage();
            this.check = !s.startsWith(Rebirth.commandManager.getPrefix());
        }
    }
    
    @SubscribeEvent
    public void onClientChatReceived(final ClientChatReceivedEvent event) {
        final Date date = new Date();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
        final String strDate = dateFormatter.format(date);
        TextComponentString time = new TextComponentString("");
        if (this.time.getValue()) {
            time = new TextComponentString(ChatFormatting.RED + "<" + ChatFormatting.GRAY + strDate + ChatFormatting.RED + ">" + ChatFormatting.RESET + " ");
        }
        event.setMessage(time.appendSibling(event.getMessage()));
    }
    
    static {
        Chat.INSTANCE = new Chat();
    }
}
