
package dev.madcat.rebirth.event;

import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;

@Cancelable
public class PlayerDamageBlockEvent extends EventStage
{
    public BlockPos pos;
    public EnumFacing facing;
    
    public PlayerDamageBlockEvent(final int stage, final BlockPos pos, final EnumFacing facing) {
        super(stage);
        this.pos = pos;
        this.facing = facing;
    }
}
