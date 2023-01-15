//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\23204\Desktop\cn×îÇ¿·´±àÒëÆ÷\1.12 stable mappings"!

//Decompiled by Procyon!

package dev.madcat.rebirth.event.events;

import dev.madcat.rebirth.event.*;

public class KeyPressedEvent extends EventStage
{
    public boolean info;
    public boolean pressed;
    
    public KeyPressedEvent(final boolean info, final boolean pressed) {
        this.info = info;
        this.pressed = pressed;
    }
}
