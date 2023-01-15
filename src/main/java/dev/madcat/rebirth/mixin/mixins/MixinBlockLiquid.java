

package dev.madcat.rebirth.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;

@Mixin({ BlockLiquid.class })
public class MixinBlockLiquid extends Block
{
    protected MixinBlockLiquid(final Material materialIn) {
        super(materialIn);
    }
}
