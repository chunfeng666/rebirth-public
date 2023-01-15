

package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.*;
import java.util.*;
import net.minecraft.block.state.*;

public class AutoCity extends Module
{
    public static EntityPlayer target;
    private final Setting<Float> range;
    
    public AutoCity() {
        super("AutoCity", "All aspects AutoCity is good", Category.COMBAT, true, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", 5.0f, 1.0f, 8.0f));
    }
    
    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        AutoCity.target = this.getTarget(this.range.getValue());
        if (AutoCity.target == null) {
            return;
        }
        final BlockPos feet = new BlockPos(AutoCity.target.posX, AutoCity.target.posY, AutoCity.target.posZ);
        if (!this.detection(AutoCity.target)) {
            if (InstantMine.getInstance().db.getValue()) {
                if (this.getBlock(feet.add(0, 1, 2)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 0, 1));
                }
                else if (this.getBlock(feet.add(0, 1, -2)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 0, -1));
                }
                else if (this.getBlock(feet.add(2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(1, 0, 0));
                }
                else if (this.getBlock(feet.add(-2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(-1, 0, 0));
                }
                else if (this.getBlock(feet.add(2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(2, 0, 0));
                }
                else if (this.getBlock(feet.add(-2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(-2, 0, 0));
                }
                else if (this.getBlock(feet.add(0, 1, -2)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 0, -2));
                }
                else if (this.getBlock(feet.add(0, 1, 2)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 0, 2));
                }
                else if (this.getBlock(feet.add(2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(2, 0, 0));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(feet.add(1, 0, 0));
                    }
                }
                else if (this.getBlock(feet.add(-2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(-2, 0, 0));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(feet.add(-1, 0, 0));
                    }
                }
                else if (this.getBlock(feet.add(0, 1, -2)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 0, -2));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(feet.add(0, 0, -1));
                    }
                }
                else if (this.getBlock(feet.add(0, 1, 2)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 0, 2));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(feet.add(0, 0, 1));
                    }
                }
                else if (this.getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 1, 1));
                }
                else if (this.getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 1, 1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 0, 1));
                }
                else if (this.getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 1, -1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 0, -1));
                }
                else if (this.getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(1, 0, 0));
                }
                else if (this.getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(-1, 0, 0));
                }
                else if (this.getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(1, 1, 0));
                }
                else if (this.getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(-1, 1, 0));
                }
                else if (this.getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 1, -1));
                }
                else if (this.getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(1, 1, 0));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(feet.add(1, 0, 0));
                    }
                }
                else if (this.getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(-1, 1, 0));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(feet.add(-1, 0, 0));
                    }
                }
                else if (this.getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 1, -1));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(feet.add(0, 0, -1));
                    }
                }
                else if (this.getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 1, 1));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(feet.add(0, 0, 1));
                    }
                }
                else if (this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(-2, 1, 0));
                }
                else if (this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(2, 1, 0));
                }
                else if (this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 1, 2)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 1, 2));
                }
                else if (this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 1, -2)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 1, -2));
                }
                else if (this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(-1, 2, 0));
                }
                else if (this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(1, 2, 0));
                }
                else if (this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 2, 1));
                }
                else if (this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(feet.add(0, 2, -1));
                }
            }
            else if (this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(1, 1, 0));
            }
            else if (this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(1, 0, 0));
            }
            else if (this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(-1, 1, 0));
            }
            else if (this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(-1, 0, 0));
            }
            else if (this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(0, 1, -1));
            }
            else if (this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(0, 0, -1));
            }
            else if (this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(0, 1, 1));
            }
            else if (this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(0, 0, 1));
            }
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (AutoCity.target != null) {
            return AutoCity.target.getName();
        }
        return null;
    }
    
    private void surroundMine(final BlockPos position) {
        if (InstantMine.breakPos2 != null && InstantMine.breakPos2.equals((Object)position)) {
            return;
        }
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals((Object)position)) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(AutoCity.target.posX, AutoCity.target.posY, AutoCity.target.posZ)) && AutoCity.mc.world.getBlockState(new BlockPos(AutoCity.target.posX, AutoCity.target.posY, AutoCity.target.posZ)).getBlock() != Blocks.AIR) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(AutoCity.mc.player.posX, AutoCity.mc.player.posY + 2.0, AutoCity.mc.player.posZ))) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(AutoCity.mc.player.posX, AutoCity.mc.player.posY - 1.0, AutoCity.mc.player.posZ))) {
                return;
            }
            if (AutoCity.mc.player.rotationPitch <= 90.0f && AutoCity.mc.player.rotationPitch >= 80.0f) {
                return;
            }
            if (AutoCity.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
        }
        AutoCity.mc.playerController.onPlayerDamageBlock(position, BlockUtil.getRayTraceFacing(position));
    }
    
    private boolean detection(final EntityPlayer player) {
        return (AutoCity.mc.world.getBlockState(new BlockPos(player.posX + 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR & AutoCity.mc.world.getBlockState(new BlockPos(player.posX + 1.2, player.posY + 1.0, player.posZ)).getBlock() == Blocks.AIR) || (AutoCity.mc.world.getBlockState(new BlockPos(player.posX - 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR & AutoCity.mc.world.getBlockState(new BlockPos(player.posX - 1.2, player.posY + 1.0, player.posZ)).getBlock() == Blocks.AIR) || (AutoCity.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ + 1.2)).getBlock() == Blocks.AIR & AutoCity.mc.world.getBlockState(new BlockPos(player.posX, player.posY + 1.0, player.posZ + 1.2)).getBlock() == Blocks.AIR) || (AutoCity.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ - 1.2)).getBlock() == Blocks.AIR & AutoCity.mc.world.getBlockState(new BlockPos(player.posX, player.posY + 1.0, player.posZ - 1.2)).getBlock() == Blocks.AIR) || (AutoCity.mc.world.getBlockState(new BlockPos(player.posX + 2.2, player.posY, player.posZ)).getBlock() == Blocks.AIR & AutoCity.mc.world.getBlockState(new BlockPos(player.posX + 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR) || (AutoCity.mc.world.getBlockState(new BlockPos(player.posX - 2.2, player.posY, player.posZ)).getBlock() == Blocks.AIR & AutoCity.mc.world.getBlockState(new BlockPos(player.posX - 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR) || (AutoCity.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ + 2.2)).getBlock() == Blocks.AIR & AutoCity.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ + 1.2)).getBlock() == Blocks.AIR) || (AutoCity.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ - 2.2)).getBlock() == Blocks.AIR & AutoCity.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ - 1.2)).getBlock() == Blocks.AIR);
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (final EntityPlayer player : AutoCity.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, range)) {
                if (Rebirth.speedManager.getPlayerSpeed(player) > 10.0) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = AutoCity.mc.player.getDistanceSq((Entity)player);
                }
                else {
                    if (AutoCity.mc.player.getDistanceSq((Entity)player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = AutoCity.mc.player.getDistanceSq((Entity)player);
                }
            }
        }
        return target;
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return AutoCity.mc.world.getBlockState(block);
    }
}
