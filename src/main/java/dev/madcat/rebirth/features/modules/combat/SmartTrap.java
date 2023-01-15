

package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.*;
import java.util.*;
import net.minecraft.block.state.*;
import dev.madcat.rebirth.features.modules.client.*;

public class SmartTrap extends Module
{
    public EntityPlayer target;
    private final Setting<Float> range;
    private final Setting<Boolean> web;
    private final Setting<Boolean> feet;
    
    public SmartTrap() {
        super("AutoTrap", "Automatically trap the enemy.", Category.COMBAT, true, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", 5.0f, 1.0f, 8.0f));
        this.web = (Setting<Boolean>)this.register(new Setting("WebHead", false));
        this.feet = (Setting<Boolean>)this.register(new Setting("Feet", false));
    }
    
    @Override
    public void onTick() {
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) {
            return;
        }
        final BlockPos people = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        if (InstantMine.breakPos != null && InstantMine.breakPos.equals((Object)new BlockPos((Vec3i)people.add(0, 2, 0)))) {
            return;
        }
        final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (obbySlot == -1) {
            return;
        }
        int webSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (webSlot == -1) {
            return;
        }
        if (this.web.getValue()) {
            webSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
            if (webSlot == -1) {
                webSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                if (webSlot == -1) {
                    return;
                }
            }
        }
        final int old = SmartTrap.mc.player.inventory.currentItem;
        if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)people.add(0, 2, 0)))) {
            return;
        }
        if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)people.add(0, 2, 0)))) {
            return;
        }
        if (this.getBlock(people.add(0, 2, 0)).getBlock() == Blocks.AIR) {
            if (this.getBlock(people.add(1, 2, 0)).getBlock() != Blocks.AIR || this.getBlock(people.add(0, 2, 1)).getBlock() != Blocks.AIR || this.getBlock(people.add(-1, 2, 0)).getBlock() != Blocks.AIR || this.getBlock(people.add(0, 2, -1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(webSlot);
                BlockUtil.placeBlock(people.add(0, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(1, 1, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(1, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(-1, 1, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(-1, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(0, 1, 1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 2, 1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(0, 1, -1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 2, -1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(1, 0, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(1, 1, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(-1, 0, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(-1, 1, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(0, 0, 1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 1, 1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(0, 0, -1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 1, -1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(0, 0, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 0, -1), EnumHand.MAIN_HAND, false, true, false);
                BlockUtil.placeBlock(people.add(0, 0, 1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
        }
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = range;
        for (final EntityPlayer player : SmartTrap.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, range) && !Rebirth.friendManager.isFriend(player.getName())) {
                if (SmartTrap.mc.player.posY - player.posY >= 5.0) {
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
    
    private IBlockState getBlock(final BlockPos block) {
        return SmartTrap.mc.world.getBlockState(block);
    }
    
    @Override
    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) {
            return null;
        }
        if (this.target != null) {
            return this.target.getName();
        }
        return null;
    }
    
    private void switchToSlot(final int slot) {
        SmartTrap.mc.player.inventory.currentItem = slot;
        SmartTrap.mc.playerController.updateController();
    }
}
