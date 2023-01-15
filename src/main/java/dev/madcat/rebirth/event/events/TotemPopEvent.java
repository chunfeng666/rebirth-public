//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\23204\Desktop\cn×îÇ¿·´±àÒëÆ÷\1.12 stable mappings"!

//Decompiled by Procyon!

package dev.madcat.rebirth.event.events;

import dev.madcat.rebirth.event.*;
import net.minecraft.entity.player.*;

public class TotemPopEvent extends EventStage
{
    private final EntityPlayer entity;
    
    public TotemPopEvent(final EntityPlayer entity) {
        this.entity = entity;
    }
    
    public EntityPlayer getEntity() {
        return this.entity;
    }
}
