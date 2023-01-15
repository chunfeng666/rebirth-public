
package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.features.modules.client.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.*;
import java.util.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.init.*;

public class FeetPad extends Module
{
    public EntityPlayer target;
    public Setting<Boolean> rotate;
    private final Setting<Boolean> air;
    private final Setting<Float> range;
    private final Setting<Boolean> WEB;
    private final Setting<Boolean> feet2;
    
    public FeetPad() {
        super("FeetPad", "Automatically put red stones on the enemy's feet.", Category.COMBAT, true, false, false);
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", true));
        this.air = (Setting<Boolean>)this.register(new Setting("WhenSelfInAir", true));
        this.range = (Setting<Float>)this.register(new Setting("Range", 5.0f, 1.0f, 6.0f));
        this.WEB = (Setting<Boolean>)this.register(new Setting("Web", false));
        this.feet2 = (Setting<Boolean>)this.register(new Setting("PlaceRange+", false));
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
    
    @Override
    public void onUpdate() {
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) {
            return;
        }
        if (!this.air.getValue() && !FeetPad.mc.player.onGround) {
            return;
        }
        final BlockPos pos = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        this.perform(pos.add(0, 0, 0));
        if (!this.feet2.getValue()) {
            return;
        }
        this.perform(pos.add(1, 0, 0));
        this.perform(pos.add(-1, 0, 0));
        this.perform(pos.add(0, 0, 1));
        this.perform(pos.add(0, 0, -1));
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = range;
        for (final EntityPlayer player : FeetPad.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, range) && !Rebirth.friendManager.isFriend(player.getName())) {
                if (FeetPad.mc.player.posY - player.posY >= 5.0) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = FeetPad.mc.player.getDistanceSq((Entity)player);
                }
                else {
                    if (FeetPad.mc.player.getDistanceSq((Entity)player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = FeetPad.mc.player.getDistanceSq((Entity)player);
                }
            }
        }
        return target;
    }
    
    private void perform(final BlockPos pos2) {
        final int old = FeetPad.mc.player.inventory.currentItem;
        if (FeetPad.mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
            if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos2))) {
                return;
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos2))) {
                return;
            }
            if (this.WEB.getValue() && InventoryUtil.findHotbarBlock(BlockWeb.class) != -1) {
                FeetPad.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockWeb.class);
                FeetPad.mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                FeetPad.mc.player.inventory.currentItem = old;
                FeetPad.mc.playerController.updateController();
            }
            else if (InventoryUtil.findItemInHotbar(Items.REDSTONE) != -1) {
                FeetPad.mc.player.inventory.currentItem = InventoryUtil.findItemInHotbar(Items.REDSTONE);
                FeetPad.mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                FeetPad.mc.player.inventory.currentItem = old;
                FeetPad.mc.playerController.updateController();
            }
        }
    }
}
