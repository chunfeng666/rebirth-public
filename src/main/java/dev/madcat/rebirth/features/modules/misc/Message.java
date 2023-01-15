
package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.features.setting.*;
import org.apache.commons.lang3.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class Message extends Module
{
    private final Timer timer;
    private Setting<String> custom;
    private Setting<Integer> random;
    private Setting<Integer> delay;
    
    public Message() {
        super("Spammer", "Message", Category.MISC, true, false, false);
        this.timer = new Timer();
        this.custom = (Setting<String>)this.register(new Setting("Custom", (T)"Rebirth very cute:3"));
        this.random = (Setting<Integer>)this.register(new Setting("Random", (T)1, (T)1, (T)20));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)100, (T)0, (T)10000));
    }
    
    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        if (!this.timer.passedMs(this.delay.getValue())) {
            return;
        }
        Message.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(this.custom.getValue() + RandomStringUtils.randomAlphanumeric((int)this.random.getValue())));
        this.timer.reset();
    }
}
