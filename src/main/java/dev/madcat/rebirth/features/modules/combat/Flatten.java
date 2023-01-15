

package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.block.*;
import dev.madcat.rebirth.features.modules.client.*;

public class Flatten extends Module
{
    public EntityPlayer target;
    private final Setting<Float> range;
    private final Setting<Boolean> negative;
    private final Setting<Boolean> air;
    public Setting<Boolean> rotate;
    BlockPos feet;
    
    public Flatten() {
        super("Flatten", "Automatically pave the road for the enemy.", Category.COMBAT, true, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)5.0f, (T)1.0f, (T)6.0f));
        this.negative = (Setting<Boolean>)this.register(new Setting("Chest Place", (T)false));
        this.air = (Setting<Boolean>)this.register(new Setting("WhenSelfInAir", (T)true));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true));
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) {
            return;
        }
        if (!this.air.getValue() && !Flatten.mc.player.onGround) {
            return;
        }
        this.feet = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        this.perform(this.feet.add(0, -1, 0));
        this.perform(this.feet.add(1, -1, 0));
        this.perform(this.feet.add(-1, -1, 0));
        this.perform(this.feet.add(0, -1, 1));
        this.perform(this.feet.add(0, -1, -1));
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = range;
        for (final EntityPlayer player : Flatten.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, range) && !Rebirth.friendManager.isFriend(player.getName())) {
                if (Flatten.mc.player.posY - player.posY >= 5.0) {
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
    
    private void perform(final BlockPos pos2) {
        final int old = Flatten.mc.player.inventory.currentItem;
        if (Flatten.mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
            if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos2))) {
                return;
            }
            if (new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)pos2))) {
                return;
            }
            if (new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)pos2.add(0, -1, 0)))) {
                return;
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos2))) {
                return;
            }
            if (this.negative.getValue() && InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) {
                Flatten.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
                Flatten.mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                Flatten.mc.player.inventory.currentItem = old;
                Flatten.mc.playerController.updateController();
            }
            else if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                Flatten.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                Flatten.mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                Flatten.mc.player.inventory.currentItem = old;
                Flatten.mc.playerController.updateController();
            }
        }
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
}
