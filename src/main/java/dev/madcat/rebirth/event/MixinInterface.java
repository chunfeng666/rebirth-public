
package dev.madcat.rebirth.event;

import net.minecraft.client.*;

public interface MixinInterface
{
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final boolean nullCheck = MixinInterface.mc.player == null || MixinInterface.mc.world == null;
}
