
package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.item.*;
import java.util.stream.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import java.util.*;
import net.minecraft.entity.player.*;

public class TrapSelf extends Module
{
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> cev;
    private final Setting<Boolean> civ;
    private final Setting<Boolean> cev2;
    private final Setting<Boolean> head;
    private final Setting<Boolean> headb;
    private final Setting<Boolean> center;
    private BlockPos startPos;
    private int obsidian;
    
    public TrapSelf() {
        super("TrapSelf", "One Self Trap", Category.COMBAT, true, false, false);
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", false));
        this.cev = (Setting<Boolean>)this.register(new Setting("AntiCev", false));
        this.civ = (Setting<Boolean>)this.register(new Setting("AntiCev", false));
        this.cev2 = (Setting<Boolean>)this.register(new Setting("AntiCev2", false));
        this.head = (Setting<Boolean>)this.register(new Setting("TrapHead", false));
        this.headb = (Setting<Boolean>)this.register(new Setting("HeadButton", false));
        this.center = (Setting<Boolean>)this.register(new Setting("TPCenter", true));
        this.obsidian = -1;
    }
    
    @Override
    public void onEnable() {
        this.startPos = EntityUtil.getRoundedBlockPos((Entity)TrapSelf.mc.player);
        if (this.center.getValue()) {
            Rebirth.positionManager.setPositionPacket(this.startPos.getX() + 0.5, this.startPos.getY(), this.startPos.getZ() + 0.5, true, true, true);
        }
    }
    
    @Override
    public void onTick() {
        if (!this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)TrapSelf.mc.player))) {
            this.toggle();
        }
        if (fullNullCheck()) {
            return;
        }
        this.obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
        if (this.obsidian == -1) {
            return;
        }
        final BlockPos pos = new BlockPos(TrapSelf.mc.player.posX, TrapSelf.mc.player.posY, TrapSelf.mc.player.posZ);
        if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR) {
            this.place(pos.add(1, 0, 0));
        }
        if (this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR) {
            this.place(pos.add(1, 1, 0));
        }
        if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR) {
            this.place(pos.add(-1, 0, 0));
        }
        if (this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
            this.place(pos.add(-1, 1, 0));
        }
        if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR) {
            this.place(pos.add(0, 0, 1));
        }
        if (this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR) {
            this.place(pos.add(0, 1, 1));
        }
        if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR) {
            this.place(pos.add(0, 0, -1));
        }
        if (this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR) {
            this.place(pos.add(0, 1, -1));
        }
        if (this.cev.getValue()) {
            if (this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR) {
                this.place(pos.add(0, 2, -1));
            }
            if (this.getBlock(pos.add(0, 3, -1)).getBlock() == Blocks.AIR) {
                this.place(pos.add(0, 3, -1));
            }
            if (this.getBlock(pos.add(0, 3, 0)).getBlock() == Blocks.AIR) {
                this.place(pos.add(0, 3, 0));
            }
        }
        if (this.cev2.getValue()) {
            if (this.getBlock(pos.add(0, 3, -1)).getBlock() == Blocks.AIR) {
                this.place(pos.add(0, 3, -1));
            }
            if (this.getBlock(pos.add(0, 4, -1)).getBlock() == Blocks.AIR) {
                this.place(pos.add(0, 4, -1));
            }
            if (this.getBlock(pos.add(0, 4, 0)).getBlock() == Blocks.AIR) {
                this.place(pos.add(0, 4, 0));
            }
        }
        if (this.head.getValue()) {
            if (this.headb.getValue()) {
                this.obsidian = InventoryUtil.findHotbarBlock(Blocks.WOODEN_BUTTON);
                if (this.obsidian == -1) {
                    this.obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                }
            }
            if (this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR) {
                this.place(pos.add(0, 2, -1));
            }
            if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                this.place(pos.add(0, 2, 0));
            }
            if (this.headb.getValue()) {
                this.obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                if (this.obsidian == -1) {
                    return;
                }
            }
        }
        if (this.civ.getValue()) {
            if (this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR) {
                this.place(pos.add(0, 2, -1));
            }
            if (this.getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.AIR) {
                this.place(pos.add(0, 2, 1));
            }
            if (this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR) {
                this.place(pos.add(1, 2, 0));
            }
            if (this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR) {
                this.place(pos.add(-1, 2, 0));
            }
        }
    }
    
    private void switchToSlot(final int slot) {
        TrapSelf.mc.player.inventory.currentItem = slot;
        TrapSelf.mc.playerController.updateController();
    }
    
    private void place(final BlockPos pos) {
        final int old = TrapSelf.mc.player.inventory.currentItem;
        this.switchToSlot(this.obsidian);
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        this.switchToSlot(old);
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return TrapSelf.mc.world.getBlockState(block);
    }
    
    public static void breakcrystal() {
        for (final Entity crystal : (List)TrapSelf.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> TrapSelf.mc.player.getDistance(e))).collect(Collectors.toList())) {
            if (crystal instanceof EntityEnderCrystal) {
                if (TrapSelf.mc.player.getDistance(crystal) > 4.0f) {
                    continue;
                }
                TrapSelf.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(crystal));
                TrapSelf.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.OFF_HAND));
            }
        }
    }
    
    private boolean isBurrowed(final EntityPlayer entityPlayer) {
        final BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY + 0.2), Math.floor(entityPlayer.posZ));
        return TrapSelf.mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || TrapSelf.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || TrapSelf.mc.world.getBlockState(blockPos).getBlock() == Blocks.CHEST;
    }
}
