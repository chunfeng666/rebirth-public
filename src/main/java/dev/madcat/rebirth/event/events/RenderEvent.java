//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\23204\Desktop\cn×îÇ¿·´±àÒëÆ÷\1.12 stable mappings"!

//Decompiled by Procyon!

package dev.madcat.rebirth.event.events;

import dev.madcat.rebirth.event.*;
import net.minecraft.util.math.*;
import net.minecraft.client.renderer.*;

public class RenderEvent extends EventStage
{
    private Vec3d renderPos;
    private Tessellator tessellator;
    private final float partialTicks;
    
    public void resetTranslation() {
        this.setTranslation(this.renderPos);
    }
    
    public Vec3d getRenderPos() {
        return this.renderPos;
    }
    
    public BufferBuilder getBuffer() {
        return this.tessellator.getBuffer();
    }
    
    public Tessellator getTessellator() {
        return this.tessellator;
    }
    
    public RenderEvent(final Tessellator paramTessellator, final Vec3d paramVec3d, final float ticks) {
        this.tessellator = paramTessellator;
        this.renderPos = paramVec3d;
        this.partialTicks = ticks;
    }
    
    public void setTranslation(final Vec3d paramVec3d) {
        this.getBuffer().setTranslation(-paramVec3d.x, -paramVec3d.y, -paramVec3d.z);
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
