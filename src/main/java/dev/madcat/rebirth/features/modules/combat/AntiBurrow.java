

package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.*;
import java.util.*;
import net.minecraft.client.gui.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;

public class AntiBurrow extends Module
{
    private final Setting<Double> range;
    private final Setting<Boolean> toggle;
    public static BlockPos pos;
    
    public AntiBurrow() {
        super("AntiBurrow", "AntiBurrow", Category.COMBAT, true, false, false);
        this.range = (Setting<Double>)this.register(new Setting("Range", 5.0, 1.0, 8.0));
        this.toggle = (Setting<Boolean>)this.register(new Setting("Toggle", false));
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (final EntityPlayer player : AntiBurrow.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, range)) {
                if (Rebirth.speedManager.getPlayerSpeed(player) > 10.0) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = AntiBurrow.mc.player.getDistanceSq((Entity)player);
                }
                else {
                    if (AntiBurrow.mc.player.getDistanceSq((Entity)player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = AntiBurrow.mc.player.getDistanceSq((Entity)player);
                }
            }
        }
        return target;
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (AntiBurrow.mc.currentScreen instanceof GuiHopper) {
            return;
        }
        final EntityPlayer player = this.getTarget(this.range.getValue());
        if (this.toggle.getValue()) {
            this.toggle();
        }
        if (player == null) {
            return;
        }
        AntiBurrow.pos = new BlockPos(player.posX, player.posY + 0.5, player.posZ);
        if (AntiBurrow.pos == null) {
            return;
        }
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals((Object)AntiBurrow.pos)) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(AntiBurrow.mc.player.posX, AntiBurrow.mc.player.posY + 2.0, AntiBurrow.mc.player.posZ))) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(AntiBurrow.mc.player.posX, AntiBurrow.mc.player.posY - 1.0, AntiBurrow.mc.player.posZ))) {
                return;
            }
            if (AntiBurrow.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
        }
        if (AntiBurrow.mc.world.getBlockState(AntiBurrow.pos).getBlock() != Blocks.AIR && AntiBurrow.mc.world.getBlockState(AntiBurrow.pos).getBlock() != Blocks.WEB && AntiBurrow.mc.world.getBlockState(AntiBurrow.pos).getBlock() != Blocks.REDSTONE_WIRE && !this.isOnLiquid() && !this.isInLiquid() && AntiBurrow.mc.world.getBlockState(AntiBurrow.pos).getBlock() != Blocks.WATER && AntiBurrow.mc.world.getBlockState(AntiBurrow.pos).getBlock() != Blocks.LAVA) {
            AntiBurrow.mc.player.swingArm(EnumHand.MAIN_HAND);
            AntiBurrow.mc.playerController.onPlayerDamageBlock(AntiBurrow.pos, BlockUtil.getRayTraceFacing(AntiBurrow.pos));
        }
    }
    
    private boolean isOnLiquid() {
        final double y = AntiBurrow.mc.player.posY - 0.03;
        for (int x = MathHelper.floor(AntiBurrow.mc.player.posX); x < MathHelper.ceil(AntiBurrow.mc.player.posX); ++x) {
            for (int z = MathHelper.floor(AntiBurrow.mc.player.posZ); z < MathHelper.ceil(AntiBurrow.mc.player.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (AntiBurrow.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isInLiquid() {
        final double y = AntiBurrow.mc.player.posY + 0.01;
        for (int x = MathHelper.floor(AntiBurrow.mc.player.posX); x < MathHelper.ceil(AntiBurrow.mc.player.posX); ++x) {
            for (int z = MathHelper.floor(AntiBurrow.mc.player.posZ); z < MathHelper.ceil(AntiBurrow.mc.player.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, (int)y, z);
                if (AntiBurrow.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
}
