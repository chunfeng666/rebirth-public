

package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.*;
import java.util.*;
import net.minecraft.block.state.*;

public class CevSelect extends Module
{
    public static EntityPlayer target;
    private static CevSelect INSTANCE;
    private final Setting<Float> range;
    private final List godBlocks;
    private final Timer timer;
    
    public CevSelect() {
        super("CevSelect", "All aspects AutoCity is good", Category.COMBAT, true, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", 5.0f, 1.0f, 8.0f));
        this.godBlocks = Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK);
        this.timer = new Timer();
        this.setInstance();
    }
    
    public static CevSelect Instance() {
        if (CevSelect.INSTANCE == null) {
            CevSelect.INSTANCE = new CevSelect();
        }
        return CevSelect.INSTANCE;
    }
    
    private void setInstance() {
        CevSelect.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        CevSelect.target = this.getTarget(this.range.getValue());
        this.surroundMine();
        this.disable();
    }
    
    @Override
    public String getDisplayInfo() {
        if (CevSelect.target != null) {
            return CevSelect.target.getName();
        }
        return null;
    }
    
    private void surroundMine() {
        if (CevSelect.target == null) {
            return;
        }
        final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (obbySlot == -1) {
            return;
        }
        final int old = Flatten.mc.player.inventory.currentItem;
        final BlockPos feet = new BlockPos(CevSelect.target.posX, CevSelect.target.posY, CevSelect.target.posZ);
        if (this.getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 3, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
            this.switchToSlot(obbySlot);
            BlockUtil.placeBlock(feet.add(1, 1, 0), EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        else if (this.getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 3, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
            this.switchToSlot(obbySlot);
            BlockUtil.placeBlock(feet.add(-1, 1, 0), EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        else if (this.getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 3, 1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
            this.switchToSlot(obbySlot);
            BlockUtil.placeBlock(feet.add(0, 1, 1), EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        else if (this.getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 3, -1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
            this.switchToSlot(obbySlot);
            BlockUtil.placeBlock(feet.add(0, 1, -1), EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        if (this.getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 3, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
            this.surroundMine(feet.add(1, 1, 0));
        }
        else if (this.getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 3, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
            this.surroundMine(feet.add(-1, 1, 0));
        }
        else if (this.getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 3, 1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
            this.surroundMine(feet.add(0, 1, 1));
        }
        else if (this.getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 3, -1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
            this.surroundMine(feet.add(0, 1, -1));
        }
        else {
            CevSelect.target = null;
        }
    }
    
    private void surroundMine(final BlockPos position) {
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals((Object)position)) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(CevSelect.target.posX, CevSelect.target.posY, CevSelect.target.posZ)) && CevSelect.mc.world.getBlockState(new BlockPos(CevSelect.target.posX, CevSelect.target.posY, CevSelect.target.posZ)).getBlock() != Blocks.AIR) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(CevSelect.mc.player.posX, CevSelect.mc.player.posY + 2.0, CevSelect.mc.player.posZ))) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(CevSelect.mc.player.posX, CevSelect.mc.player.posY - 1.0, CevSelect.mc.player.posZ))) {
                return;
            }
            if (CevSelect.mc.player.rotationPitch <= 90.0f && CevSelect.mc.player.rotationPitch >= 80.0f) {
                return;
            }
            if (CevSelect.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
        }
        CevSelect.mc.playerController.onPlayerDamageBlock(position, BlockUtil.getRayTraceFacing(position));
    }
    
    private void switchToSlot(final int slot) {
        Flatten.mc.player.inventory.currentItem = slot;
        Flatten.mc.playerController.updateController();
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (final EntityPlayer player : CevSelect.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, range)) {
                if (Rebirth.speedManager.getPlayerSpeed(player) > 10.0) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = CevSelect.mc.player.getDistanceSq((Entity)player);
                }
                else {
                    if (CevSelect.mc.player.getDistanceSq((Entity)player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = CevSelect.mc.player.getDistanceSq((Entity)player);
                }
            }
        }
        return target;
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return CevSelect.mc.world.getBlockState(block);
    }
    
    static {
        CevSelect.INSTANCE = new CevSelect();
    }
}
