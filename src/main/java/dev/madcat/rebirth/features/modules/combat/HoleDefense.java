
package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import dev.madcat.rebirth.*;
import net.minecraft.block.state.*;

public class HoleDefense extends Module
{
    public HoleDefense() {
        super("HoleDefense", "HoleDefense", Category.COMBAT, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        final BlockPos pos = new BlockPos(HoleDefense.mc.player.posX, HoleDefense.mc.player.posY, HoleDefense.mc.player.posZ);
        if ((this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN | this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) && (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN | this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK) && (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN | this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK) && (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN | this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK) && Rebirth.moduleManager.isModuleEnabled("Surround")) {
            Rebirth.moduleManager.disableModule("Surround");
        }
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return HoleDefense.mc.world.getBlockState(block);
    }
}
