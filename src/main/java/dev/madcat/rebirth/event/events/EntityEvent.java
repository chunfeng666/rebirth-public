//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\23204\Desktop\cn×îÇ¿·´±àÒëÆ÷\1.12 stable mappings"!

//Decompiled by Procyon!

package dev.madcat.rebirth.event.events;

import dev.madcat.rebirth.event.*;
import net.minecraft.entity.*;

public class EntityEvent extends EventStage
{
    private Entity entity;
    
    public EntityEvent(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public static class EntityCollision extends EntityEvent
    {
        double x;
        double y;
        double z;
        
        public EntityCollision(final Entity entity, final double x, final double y, final double z) {
            super(entity);
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public double getX() {
            return this.x;
        }
        
        public double getY() {
            return this.y;
        }
        
        public double getZ() {
            return this.z;
        }
        
        public void setX(final double x) {
            this.x = x;
        }
        
        public void setY(final double y) {
            this.y = y;
        }
        
        public void setZ(final double z) {
            this.z = z;
        }
    }
}
