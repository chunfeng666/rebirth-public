
package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import java.util.*;
import net.minecraftforge.client.event.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AutoQueue extends Module
{
    Map<String, String> QA;
    
    public AutoQueue() {
        super("AutoQueue", "Automatically queue in 2b2t.xin", Category.MISC, true, false, false);
        this.QA = new HashMap<String, String>() {
            {
                this.put("\u9f99\u86cb", "A");
                this.put("\u4f20\u9001", "B");
                this.put("\u5927\u7bb1\u5b50", "C");
                this.put("\u5c0f\u7bb1\u5b50", "B");
                this.put("HIM", "A");
                this.put("\u95ea\u7535\u51fb\u4e2d", "B");
                this.put("\u5b98\u65b9\u8bd1\u540d", "C");
                this.put("\u94bb\u77f3", "A");
                this.put("\u706b\u7130\u5f39", "B");
                this.put("\u5357\u74dc", "A");
                this.put("\u4ec0\u4e48\u52a8\u7269", "B");
                this.put("\u7f8a\u9a7c", "B");
                this.put("\u6316\u6398", "C");
                this.put("\u51cb\u96f6", "C");
                this.put("\u5708\u5730", "B");
                this.put("\u65e0\u9650\u6c34", "C");
                this.put("\u672b\u5f71\u4e4b\u773c", "A");
                this.put("\u7ea2\u77f3\u706b\u628a", "B");
                this.put("\u51e0\u9875", "A");
                this.put("\u933e", "B");
                this.put("\u9644\u9b54\u91d1", "B");
            }
        };
    }
    
    @SubscribeEvent
    public void onClientChatReceived(final ClientChatReceivedEvent event) {
        final String msg = event.getMessage().getUnformattedText();
        final Map.Entry<String, String> Answer = this.QA.entrySet().stream().filter(p -> msg.contains(p.getKey())).findFirst().orElse(null);
        if (Answer != null) {
            AutoQueue.mc.player.connection.sendPacket((Packet)new CPacketChatMessage((String)Answer.getValue()));
        }
    }
}
