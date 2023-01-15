//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\23204\Desktop\cn×îÇ¿·´±àÒëÆ÷\1.12 stable mappings"!

//Decompiled by Procyon!

package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import dev.madcat.rebirth.features.modules.client.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.*;
import java.util.*;
import net.minecraft.block.state.*;
import net.minecraft.util.*;
import dev.madcat.rebirth.util.*;

public class PistonKicker extends Module
{
    public static EntityPlayer target;
    private final Setting<Float> range;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> disable;
    private final Setting<Boolean> debug;
    
    public PistonKicker() {
        super("PistonKicker", "Automatically kick out the enemy's hole.", Category.COMBAT, true, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)8.0f, (T)1.0f, (T)12.0f));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true));
        this.disable = (Setting<Boolean>)this.register(new Setting("Disable", (T)true));
        this.debug = (Setting<Boolean>)this.register(new Setting("Debug", (T)true));
    }
    
    @Override
    public void onUpdate() {
        if (InventoryUtil.findHotbarBlock(BlockPistonBase.class) == -1) {
            if (this.disable.getValue()) {
                this.toggle();
            }
            return;
        }
        if (InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK) == -1) {
            if (this.disable.getValue()) {
                this.toggle();
            }
            return;
        }
        PistonKicker.target = this.getTarget(this.range.getValue());
        if (PistonKicker.target == null) {
            if (this.disable.getValue()) {
                this.toggle();
            }
            return;
        }
        final BlockPos pos = new BlockPos(PistonKicker.target.posX, PistonKicker.target.posY, PistonKicker.target.posZ);
        final float[] angle = MathUtil.calcAngle(PistonKicker.mc.player.getPositionEyes(PistonKicker.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() + 0.5f), (double)(pos.getZ() + 0.5f)));
        if (angle[1] >= -71.0f && angle[1] <= 71.0f && angle[0] >= -51.0f && angle[0] <= 51.0f && this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR && (this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR | this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.PISTON)) {
            this.perform(pos.add(0, 1, 1));
            if (this.getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(0, 2, 1));
            }
            else if (this.getBlock(pos.add(0, 2, 1)).getBlock() != Blocks.REDSTONE_BLOCK) {
                if (this.getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.AIR) {
                    this.perform1(pos.add(1, 1, 1));
                }
                else if (this.getBlock(pos.add(1, 1, 1)).getBlock() != Blocks.REDSTONE_BLOCK) {
                    if (this.getBlock(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                        this.perform1(pos.add(0, 1, 2));
                    }
                    else if (this.getBlock(pos.add(0, 1, 2)).getBlock() != Blocks.REDSTONE_BLOCK) {
                        if (this.getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR) {
                            this.perform1(pos.add(-1, 1, 1));
                        }
                    }
                }
            }
        }
        else if (angle[1] >= -71.0f && angle[1] <= 71.0f && (angle[0] >= 129.0f | angle[0] <= -129.0f) && this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.AIR && (this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR | this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.PISTON)) {
            this.perform(pos.add(0, 1, -1));
            if (this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(0, 2, -1));
            }
            else if (this.getBlock(pos.add(0, 2, -1)).getBlock() != Blocks.REDSTONE_BLOCK) {
                if (this.getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.AIR) {
                    this.perform1(pos.add(1, 1, -1));
                }
                else if (this.getBlock(pos.add(1, 1, -1)).getBlock() != Blocks.REDSTONE_BLOCK) {
                    if (this.getBlock(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                        this.perform1(pos.add(0, 1, -2));
                    }
                    else if (this.getBlock(pos.add(0, 1, -2)).getBlock() != Blocks.REDSTONE_BLOCK) {
                        if (this.getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR) {
                            this.perform1(pos.add(-1, 1, -1));
                        }
                    }
                }
            }
        }
        else if (angle[1] >= -71.0f && angle[1] <= 71.0f && angle[0] <= -51.0f && angle[0] >= -129.0f && this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR && (this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR | this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.PISTON)) {
            this.perform(pos.add(1, 1, 0));
            if (this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(1, 2, 0));
            }
            else if (this.getBlock(pos.add(1, 2, 0)).getBlock() != Blocks.REDSTONE_BLOCK) {
                if (this.getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.AIR) {
                    this.perform1(pos.add(1, 1, 1));
                }
                else if (this.getBlock(pos.add(1, 1, 1)).getBlock() != Blocks.REDSTONE_BLOCK) {
                    if (this.getBlock(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                        this.perform1(pos.add(2, 1, 0));
                    }
                    else if (this.getBlock(pos.add(2, 1, 0)).getBlock() != Blocks.REDSTONE_BLOCK) {
                        if (this.getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.AIR) {
                            this.perform1(pos.add(1, 1, -1));
                        }
                    }
                }
            }
        }
        else if (angle[1] >= -71.0f && angle[1] <= 71.0f && angle[0] >= 51.0f && angle[0] <= 129.0f && this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR && (this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR | this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.PISTON)) {
            this.perform(pos.add(-1, 1, 0));
            if (this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR) {
                this.perform1(pos.add(-1, 2, 0));
            }
            else if (this.getBlock(pos.add(-1, 2, 0)).getBlock() != Blocks.REDSTONE_BLOCK) {
                if (this.getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR) {
                    this.perform1(pos.add(-1, 1, 1));
                }
                else if (this.getBlock(pos.add(-1, 1, 1)).getBlock() != Blocks.REDSTONE_BLOCK) {
                    if (this.getBlock(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                        this.perform1(pos.add(-2, 1, 0));
                    }
                    else if (this.getBlock(pos.add(-2, 1, 0)).getBlock() != Blocks.REDSTONE_BLOCK) {
                        if (this.getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR) {
                            this.perform1(pos.add(-1, 1, -1));
                        }
                    }
                }
            }
        }
        else if (this.disable.getValue()) {
            this.toggle();
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) {
            return null;
        }
        if (PistonKicker.target != null) {
            return PistonKicker.target.getName();
        }
        return null;
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (!this.debug.getValue()) {
            return;
        }
        if (InventoryUtil.findHotbarBlock(BlockPistonBase.class) == -1) {
            return;
        }
        if (InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK) == -1) {
            return;
        }
        PistonKicker.target = this.getTarget(this.range.getValue());
        if (PistonKicker.target == null) {
            return;
        }
        final BlockPos pos = new BlockPos(PistonKicker.target.posX, PistonKicker.target.posY, PistonKicker.target.posZ);
        final float[] angle = MathUtil.calcAngle(PistonKicker.mc.player.getPositionEyes(PistonKicker.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() + 0.5f), (double)(pos.getZ() + 0.5f)));
        PistonKicker.mc.fontRenderer.drawString(angle[0] + "   " + angle[1], 200.0f, 200.0f, 255, true);
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (final EntityPlayer player : PistonKicker.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, range)) {
                if (Rebirth.speedManager.getPlayerSpeed(player) > 10.0) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = PistonKicker.mc.player.getDistanceSq((Entity)player);
                }
                else {
                    if (PistonKicker.mc.player.getDistanceSq((Entity)player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = PistonKicker.mc.player.getDistanceSq((Entity)player);
                }
            }
        }
        return target;
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return PistonKicker.mc.world.getBlockState(block);
    }
    
    private void perform(final BlockPos pos) {
        final int old = AntiCity.mc.player.inventory.currentItem;
        if (PistonKicker.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
            PistonKicker.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockPistonBase.class);
            PistonKicker.mc.playerController.updateController();
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            PistonKicker.mc.player.inventory.currentItem = old;
            PistonKicker.mc.playerController.updateController();
        }
    }
    
    private void perform1(final BlockPos pos) {
        final int old = AntiCity.mc.player.inventory.currentItem;
        if (PistonKicker.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
            PistonKicker.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
            PistonKicker.mc.playerController.updateController();
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            PistonKicker.mc.player.inventory.currentItem = old;
            PistonKicker.mc.playerController.updateController();
        }
    }
}
