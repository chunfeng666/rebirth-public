

package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.init.*;
import java.awt.*;
import net.minecraft.util.math.*;
import net.minecraft.block.state.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.*;
import java.util.*;

public class CityESP extends Module
{
    public EntityPlayer target;
    private final Setting<Float> range;
    
    public CityESP() {
        super("CityESP", "CityESP", Module.Category.RENDER, true, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", 7.0f, 1.0f, 12.0f));
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        this.target = this.getTarget(this.range.getValue());
        this.surroundRender1();
    }
    
    private void surroundRender1() {
        if (this.target == null) {
            return;
        }
        final Vec3d a = this.target.getPositionVector();
        final BlockPos people = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        if (this.getBlock(people.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, -1.0, 0.0, 0.0, true);
            }
            else if (this.getBlock(people.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundRender1(a, -1.0, 0.0, 0.0, false);
            }
        }
        if (this.getBlock(people.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 1.0, 0.0, 0.0, true);
            }
            else if (this.getBlock(people.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundRender1(a, 1.0, 0.0, 0.0, false);
            }
        }
        if (this.getBlock(people.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(-2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, -2.0, 0.0, 0.0, true);
            }
            else {
                this.surroundRender1(a, -2.0, 0.0, 0.0, false);
            }
        }
        if (this.getBlock(people.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(-2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(-2, 0, 0)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, -2.0, 1.0, 0.0, true);
            }
            else {
                this.surroundRender1(a, -2.0, 1.0, 0.0, false);
            }
        }
        if (this.getBlock(people.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(2, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 2.0, 0.0, 0.0, true);
            }
            else {
                this.surroundRender1(a, 2.0, 0.0, 0.0, false);
            }
        }
        if (this.getBlock(people.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(2, 0, 0)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 2.0, 1.0, 0.0, true);
            }
            else {
                this.surroundRender1(a, 2.0, 1.0, 0.0, false);
            }
        }
        if (this.getBlock(people.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 0.0, 0.0, 1.0, true);
            }
            else if (this.getBlock(people.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
                this.surroundRender1(a, 0.0, 0.0, 1.0, false);
            }
        }
        if (this.getBlock(people.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 0.0, 0.0, -1.0, true);
            }
            else if (this.getBlock(people.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
                this.surroundRender1(a, 0.0, 0.0, -1.0, false);
            }
        }
        if (this.getBlock(people.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 1, 2)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 0, 2)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 0.0, 0.0, 2.0, true);
            }
            else {
                this.surroundRender1(a, 0.0, 0.0, 2.0, false);
            }
        }
        if (this.getBlock(people.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 1, 2)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 0, 2)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 0.0, 1.0, 2.0, true);
            }
            else {
                this.surroundRender1(a, 0.0, 1.0, 2.0, false);
            }
        }
        if (this.getBlock(people.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 1, -2)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 0, -2)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 0.0, 0.0, -2.0, true);
            }
            else {
                this.surroundRender1(a, 0.0, 0.0, -2.0, false);
            }
        }
        if (this.getBlock(people.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 1, -2)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 0, -2)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 0.0, 1.0, -2.0, true);
            }
            else {
                this.surroundRender1(a, 0.0, 1.0, -2.0, false);
            }
        }
        if (this.getBlock(people.add(0, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 0, 0)).getBlock() != Blocks.BEDROCK) {
            RenderUtil.drawBoxESP(new BlockPos(a), new Color(255, 255, 0), false, new Color(255, 255, 0), 1.0f, false, true, 42, true);
        }
    }
    
    private void surroundRender1(final Vec3d pos, final double x, final double y, final double z, final boolean red) {
        final BlockPos position = new BlockPos(pos).add(x, y, z);
        if (CityESP.mc.world.getBlockState(position).getBlock() == Blocks.AIR) {
            return;
        }
        if (CityESP.mc.world.getBlockState(position).getBlock() == Blocks.FIRE) {
            return;
        }
        if (red) {
            RenderUtil.drawBoxESP(position, new Color(255, 147, 147), false, new Color(255, 147, 147), 1.0f, false, true, 80, true);
            return;
        }
        RenderUtil.drawBoxESP(position, new Color(118, 118, 255), false, new Color(118, 118, 255), 1.0f, false, true, 40, true);
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return CityESP.mc.world.getBlockState(block);
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = range;
        for (final EntityPlayer player : CityESP.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, range) && !Rebirth.friendManager.isFriend(player.getName())) {
                if (CityESP.mc.player.posY - player.posY >= 5.0) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = EntityUtil.mc.player.getDistanceSq((Entity)player);
                }
                else {
                    if (EntityUtil.mc.player.getDistanceSq((Entity)player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = EntityUtil.mc.player.getDistanceSq((Entity)player);
                }
            }
        }
        return target;
    }
}
