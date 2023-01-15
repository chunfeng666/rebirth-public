//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\23204\Desktop\cn×îÇ¿·´±àÒëÆ÷\1.12 stable mappings"!

//Decompiled by Procyon!

package dev.madcat.rebirth.event.events;

import dev.madcat.rebirth.event.*;
import net.minecraft.util.*;

public class TransformSideFirstPersonEvent extends EventStage
{
    private final EnumHandSide enumHandSide;
    
    public TransformSideFirstPersonEvent(final EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }
    
    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
