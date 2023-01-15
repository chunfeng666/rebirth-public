

package dev.madcat.rebirth.util;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import org.lwjgl.input.*;

public class Wrapper
{
    public static final Minecraft mc;
    
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    
    public static World getWorld() {
        return (World)getMinecraft().world;
    }
    
    public static int getKey(final String keyname) {
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
