

package dev.madcat.rebirth.util;

import net.minecraft.util.math.*;
import net.minecraft.util.*;

public class DamageBlockEvent extends EventStage
{
    private boolean cancelled;
    private BlockPos blockPos;
    private EnumFacing enumFacing;
    
    public DamageBlockEvent(final BlockPos blockPos, final EnumFacing enumFacing) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }
    
    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }
    
    public void setEnumFacing(final EnumFacing enumFacing) {
        this.enumFacing = enumFacing;
    }
}
