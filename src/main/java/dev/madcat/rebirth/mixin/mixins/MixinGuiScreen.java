
package dev.madcat.rebirth.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import dev.madcat.rebirth.features.modules.render.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ GuiScreen.class })
public class MixinGuiScreen extends Gui
{
    @Inject(method = { "renderToolTip" }, at = { @At("HEAD") }, cancellable = true)
    public void renderToolTipHook(final ItemStack stack, final int x, final int y, final CallbackInfo info) {
        if (Peek.getInstance().isOn() && stack.getItem() instanceof ItemShulkerBox) {
            Peek.getInstance().renderShulkerToolTip(stack, x, y, (String)null);
            info.cancel();
        }
    }
}
