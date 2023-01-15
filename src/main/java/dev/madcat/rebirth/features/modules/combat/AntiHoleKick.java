

package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.block.state.*;
import net.minecraft.util.*;
import dev.madcat.rebirth.util.*;

public class AntiHoleKick extends Module
{
    private final Setting<Boolean> rotate;
    private int obsidian;
    
    public AntiHoleKick() {
        super("AntiHoleKick", "AntiHoleKick", Category.COMBAT, true, false, false);
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", true));
        this.obsidian = -1;
    }
    
    @Override
    public void onUpdate() {
        this.obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
        if (this.obsidian == -1) {
            return;
        }
        final BlockPos pos = new BlockPos(AntiHoleKick.mc.player.posX, AntiHoleKick.mc.player.posY, AntiHoleKick.mc.player.posZ);
        if (this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.PISTON) {
            this.perform(pos.add(0, 1, -1));
            this.perform(pos.add(0, 2, -1));
            this.perform(pos.add(0, 2, 0));
        }
        if (this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.PISTON) {
            this.perform(pos.add(0, 1, 1));
            this.perform(pos.add(0, 2, 1));
            this.perform(pos.add(0, 2, 0));
        }
        if (this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.PISTON) {
            this.perform(pos.add(-1, 1, 0));
            this.perform(pos.add(-1, 2, 0));
            this.perform(pos.add(0, 2, 0));
        }
        if (this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.PISTON) {
            this.perform(pos.add(1, 1, 0));
            this.perform(pos.add(1, 2, 0));
            this.perform(pos.add(0, 2, 0));
        }
    }
    
    private void switchToSlot(final int slot) {
        AntiHoleKick.mc.player.inventory.currentItem = slot;
        AntiHoleKick.mc.playerController.updateController();
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return AntiHoleKick.mc.world.getBlockState(block);
    }
    
    private void perform(final BlockPos pos) {
        final int old = AntiHoleKick.mc.player.inventory.currentItem;
        this.switchToSlot(this.obsidian);
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        this.switchToSlot(old);
    }
}
