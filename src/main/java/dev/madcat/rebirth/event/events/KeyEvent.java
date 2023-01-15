//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\23204\Desktop\cn×îÇ¿·´±àÒëÆ÷\1.12 stable mappings"!

//Decompiled by Procyon!

package dev.madcat.rebirth.event.events;

import dev.madcat.rebirth.event.*;

public class KeyEvent extends EventStage
{
    private final int key;
    
    public KeyEvent(final int key) {
        this.key = key;
    }
    
    public int getKey() {
        return this.key;
    }
}
