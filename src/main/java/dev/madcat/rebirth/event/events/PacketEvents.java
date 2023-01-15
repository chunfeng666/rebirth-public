//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\23204\Desktop\cn×îÇ¿·´±àÒëÆ÷\1.12 stable mappings"!

//Decompiled by Procyon!

package dev.madcat.rebirth.event.events;

import dev.madcat.rebirth.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.*;

@Cancelable
public class PacketEvents extends EventStage
{
    private final Packet<?> packet;
    
    public PacketEvents(final int stage, final Packet<?> packet) {
        super(stage);
        this.packet = packet;
    }
    
    public <T extends Packet<?>> T getPacket() {
        return (T)this.packet;
    }
    
    @Cancelable
    public static class Receive extends PacketEvents
    {
        public Receive(final int stage, final Packet<?> packet) {
            super(stage, packet);
        }
    }
    
    @Cancelable
    public static class Send extends PacketEvents
    {
        public Send(final int stage, final Packet<?> packet) {
            super(stage, packet);
        }
    }
}
