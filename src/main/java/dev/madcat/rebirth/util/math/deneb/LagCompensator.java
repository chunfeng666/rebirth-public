

package dev.madcat.rebirth.util.math.deneb;

import net.minecraftforge.common.*;
import net.minecraft.client.*;
import net.minecraft.launchwrapper.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.util.*;
import net.minecraft.util.math.*;

public class LagCompensator implements EventListener
{
    public static LagCompensator INSTANCE;
    private final float[] tickRates;
    private int nextIndex;
    private long timeLastTimeUpdate;
    
    public LagCompensator() {
        this.nextIndex = 0;
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.tickRates = new float[20];
        this.reset();
    }
    
    public static int globalInfoPingValue() {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.getConnection() == null) {
            return 1;
        }
        if (mc.player == null) {
            return -1;
        }
        try {
            return mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
        }
        catch (NullPointerException npe) {
            LogWrapper.info("Caught NPE l25 CalcPing.java", (Object[])new Object[0]);
            return -1;
        }
    }
    
    @SubscribeEvent
    public void onPacketEvent(final PacketEvents event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            LagCompensator.INSTANCE.onTimeUpdate();
        }
    }
    
    public void reset() {
        this.nextIndex = 0;
        this.timeLastTimeUpdate = -1L;
        Arrays.fill(this.tickRates, 0.0f);
    }
    
    public float getTickRate() {
        float numTicks = 0.0f;
        float sumTickRates = 0.0f;
        for (final float tickRate : this.tickRates) {
            if (tickRate > 0.0f) {
                sumTickRates += tickRate;
                ++numTicks;
            }
        }
        return MathHelper.clamp(sumTickRates / numTicks, 0.0f, 20.0f);
    }
    
    public void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1L) {
            final float timeElapsed = (System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0f;
            this.tickRates[this.nextIndex % this.tickRates.length] = MathHelper.clamp(20.0f / timeElapsed, 0.0f, 20.0f);
            ++this.nextIndex;
        }
        this.timeLastTimeUpdate = System.currentTimeMillis();
    }
}
