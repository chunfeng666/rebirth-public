

package dev.madcat.rebirth.util;

import net.minecraft.client.*;
import java.util.*;

public interface Globals
{
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final Random random = new Random();
    public static final char SECTIONSIGN = '��';
    
    default boolean nullCheck() {
        return Globals.mc.player == null || Globals.mc.world == null;
    }
}
