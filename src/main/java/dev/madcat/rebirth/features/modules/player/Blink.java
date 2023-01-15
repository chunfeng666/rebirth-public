

package dev.madcat.rebirth.features.modules.player;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import java.util.*;
import net.minecraft.network.*;
import net.minecraft.client.entity.*;
import net.minecraft.util.math.*;
import java.util.concurrent.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Blink extends Module
{
    private static Blink INSTANCE;
    private final Setting<Boolean> cPacketPlayer;
    private final Setting<Mode> autoOff;
    private final Setting<Integer> timeLimit;
    private final Setting<Integer> packetLimit;
    private final Setting<Float> distance;
    private final Timer timer;
    private final Queue<Packet<?>> packets;
    private EntityOtherPlayerMP entity;
    private int packetsCanceled;
    private BlockPos startPos;
    
    public Blink() {
        super("Blink", "Fake lag.", Module.Category.PLAYER, true, false, false);
        this.cPacketPlayer = (Setting<Boolean>)this.register(new Setting("CPacketPlayer", true));
        this.autoOff = (Setting<Mode>)this.register(new Setting("AutoOff", Mode.MANUAL));
        this.timeLimit = (Setting<Integer>)this.register(new Setting("Time", 20, 1, 500, v -> this.autoOff.getValue() == Mode.TIME));
        this.packetLimit = (Setting<Integer>)this.register(new Setting("Packets", 20, 1, 500, v -> this.autoOff.getValue() == Mode.PACKETS));
        this.distance = (Setting<Float>)this.register(new Setting("Distance", 10.0f, 1.0f, 100.0f, v -> this.autoOff.getValue() == Mode.DISTANCE));
        this.timer = new Timer();
        this.packets = new ConcurrentLinkedQueue<Packet<?>>();
        this.packetsCanceled = 0;
        this.startPos = null;
        this.setInstance();
    }
    
    public static Blink getInstance() {
        if (Blink.INSTANCE == null) {
            Blink.INSTANCE = new Blink();
        }
        return Blink.INSTANCE;
    }
    
    private void setInstance() {
        Blink.INSTANCE = this;
    }
    
    public void onEnable() {
        if (!fullNullCheck()) {
            (this.entity = new EntityOtherPlayerMP((World)Blink.mc.world, Blink.mc.session.getProfile())).copyLocationAndAnglesFrom((Entity)Blink.mc.player);
            this.entity.rotationYaw = Blink.mc.player.rotationYaw;
            this.entity.rotationYawHead = Blink.mc.player.rotationYawHead;
            this.entity.inventory.copyInventory(Blink.mc.player.inventory);
            Blink.mc.world.addEntityToWorld(6942069, (Entity)this.entity);
            this.startPos = Blink.mc.player.getPosition();
        }
        else {
            this.disable();
        }
        this.packetsCanceled = 0;
        this.timer.reset();
    }
    
    public void onUpdate() {
        if (nullCheck() || (this.autoOff.getValue() == Mode.TIME && this.timer.passedS(this.timeLimit.getValue())) || (this.autoOff.getValue() == Mode.DISTANCE && this.startPos != null && Blink.mc.player.getDistanceSq(this.startPos) >= MathUtil.square(this.distance.getValue())) || (this.autoOff.getValue() == Mode.PACKETS && this.packetsCanceled >= this.packetLimit.getValue())) {
            this.disable();
        }
    }
    
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }
    
    @SubscribeEvent
    public void onSendPacket(final PacketEvent.Send event) {
        if (Blink.mc.world != null && !Blink.mc.isSingleplayer()) {
            final Object packet = event.getPacket();
            if (this.cPacketPlayer.getValue() && packet instanceof CPacketPlayer) {
                event.setCanceled(true);
                this.packets.add((Packet<?>)packet);
                ++this.packetsCanceled;
            }
            if (!this.cPacketPlayer.getValue()) {
                if (packet instanceof CPacketChatMessage || packet instanceof CPacketConfirmTeleport || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus) {
                    return;
                }
                this.packets.add((Packet<?>)packet);
                event.setCanceled(true);
                ++this.packetsCanceled;
            }
        }
    }
    
    public void onDisable() {
        if (!fullNullCheck()) {
            Blink.mc.world.removeEntity((Entity)this.entity);
            while (!this.packets.isEmpty()) {
                Blink.mc.player.connection.sendPacket((Packet)this.packets.poll());
            }
        }
        this.startPos = null;
    }
    
    public String getDisplayInfo() {
        if (this.packets != null) {
            return this.packets.size() + "";
        }
        return null;
    }
    
    static {
        Blink.INSTANCE = new Blink();
    }
    
    public enum Mode
    {
        MANUAL, 
        TIME, 
        DISTANCE, 
        PACKETS;
    }
}
