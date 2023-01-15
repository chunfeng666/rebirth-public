

package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.util.math.*;
import dev.madcat.rebirth.features.modules.combat.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.util.*;
import java.util.*;

public class Nuker extends Module
{
    private final Setting<Integer> range;
    private Setting<Boolean> logout;
    private Setting<Boolean> hp;
    private final Setting<Integer> saferange;
    
    public Nuker() {
        super("Nuker", "Crazy dig.", Category.MISC, true, false, false);
        this.range = (Setting<Integer>)this.register(new Setting("Range", (T)5, (T)1, (T)6));
        this.logout = (Setting<Boolean>)this.register(new Setting("BreakAll", (T)false));
        this.hp = (Setting<Boolean>)this.register(new Setting("32kMode", (T)false));
        this.saferange = (Setting<Integer>)this.register(new Setting("SafeRange", (T)2, (T)0, (T)6, v -> !this.logout.getValue()));
    }
    
    @Override
    public void onTick() {
        for (final BlockPos blockPos : this.breakPos(this.range.getValue())) {
            if (Nuker.mc.player.getDistanceSq(InstantMine.breakPos) > MathUtil.square(6.0) && this.hp.getValue() && InstantMine.breakPos != null && Nuker.mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockHopper) {
                return;
            }
            if (Nuker.mc.player.getDistanceSq(InstantMine.breakPos) > MathUtil.square(6.0) && InstantMine.breakPos != null && Nuker.mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockShulkerBox) {
                return;
            }
            if (Nuker.mc.player.getDistanceSq(InstantMine.breakPos) > MathUtil.square(6.0) && this.hp.getValue() && InstantMine.breakPos != null && Nuker.mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockHopper) {
                return;
            }
            if (Nuker.mc.player.getDistanceSq(InstantMine.breakPos) > MathUtil.square(6.0) && this.logout.getValue() && Nuker.mc.world.getBlockState(InstantMine.breakPos).getBlock() != Blocks.AIR) {
                return;
            }
            if (!this.logout.getValue()) {
                if (Nuker.mc.player.getDistanceSq(blockPos) < MathUtil.square(this.saferange.getValue())) {
                    continue;
                }
                if (blockPos == null) {
                    continue;
                }
                if (!this.hp.getValue()) {
                    continue;
                }
                if (Nuker.mc.world.getBlockState(blockPos).getBlock() instanceof BlockHopper) {
                    Nuker.mc.player.swingArm(EnumHand.MAIN_HAND);
                    Nuker.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
                }
                else {
                    if (!(Nuker.mc.world.getBlockState(blockPos).getBlock() instanceof BlockShulkerBox)) {
                        continue;
                    }
                    Nuker.mc.player.swingArm(EnumHand.MAIN_HAND);
                    Nuker.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
                }
            }
            else {
                if (Nuker.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || Nuker.mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR) {
                    continue;
                }
                Nuker.mc.player.swingArm(EnumHand.MAIN_HAND);
                Nuker.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
            }
        }
    }
    
    private NonNullList<BlockPos> breakPos(final float placeRange) {
        final NonNullList positions = NonNullList.create();
        positions.addAll((Collection)BlockUtil.getSphere(new BlockPos(Math.floor(Nuker.mc.player.posX), Math.floor(Nuker.mc.player.posY), Math.floor(Nuker.mc.player.posZ)), placeRange, 0, false, true, 0));
        return (NonNullList<BlockPos>)positions;
    }
}
