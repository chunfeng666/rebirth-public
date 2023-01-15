

package dev.madcat.rebirth.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.resources.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = { Locale.class }, priority = 100)
public class MixinLocaleFont
{
    @Inject(method = { "checkUnicode" }, at = { @At("HEAD") }, cancellable = true)
    public void checkUnicode(final CallbackInfo ci) {
        ci.cancel();
    }
}
