//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\23204\Desktop\cn×îÇ¿·´±àÒëÆ÷\1.12 stable mappings"!

//Decompiled by Procyon!

package dev.madcat.rebirth.event.events;

import dev.madcat.rebirth.event.*;

public class PlayerJumpEvent extends EventStage
{
    public double motionX;
    public double motionY;
    
    public PlayerJumpEvent(final double motionX, final double motionY) {
        this.motionX = motionX;
        this.motionY = motionY;
    }
}
