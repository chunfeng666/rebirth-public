

package dev.madcat.rebirth.mixin.mixins.accessors;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.multiplayer.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ ServerAddress.class })
public interface IServerAddress
{
    @Invoker("getServerAddress")
    default String[] getServerAddress(final String string) {
        throw new IllegalStateException("Mixin didnt transform this");
    }
}
