

package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import dev.madcat.rebirth.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import java.util.stream.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import java.util.*;
import net.minecraft.util.math.*;
import net.minecraft.block.state.*;
import dev.madcat.rebirth.util.*;

public class AntiCev extends Module
{
    public Setting<Boolean> rotate;
    public Setting<Boolean> packet;
    public Setting<Boolean> highCiv;
    public Setting<Boolean> oldCev;
    int CevHigh;
    BlockPos startPos;
    BlockPos crystal;
    
    public AntiCev() {
        super("AntiCev", "Anti straight line explosion and oblique angle explosion.", Category.COMBAT, true, false, false);
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", true));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", true));
        this.highCiv = (Setting<Boolean>)this.register(new Setting("HighCiv", false));
        this.oldCev = (Setting<Boolean>)this.register(new Setting("OldCev", false));
        this.CevHigh = 0;
    }
    
    @Override
    public void onTick() {
        if (!fullNullCheck() && InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            final Vec3d a = AntiCev.mc.player.getPositionVector();
            final BlockPos pos = new BlockPos(AntiCev.mc.player.posX, AntiCev.mc.player.posY, AntiCev.mc.player.posZ);
            final Entity target = this.getTarget();
            if (target != null) {
                this.crystal = new BlockPos(target.posX, target.posY, target.posZ);
            }
            else {
                this.crystal = null;
            }
            if (this.oldCev.getValue()) {
                if (this.crystal == null) {
                    return;
                }
                if (this.getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 3, 1)))) {
                    this.perform(pos.add(0, 1, 1));
                    this.perform(pos.add(0, 2, 1));
                }
                if (this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 3, -1)))) {
                    this.perform(pos.add(0, 1, -1));
                    this.perform(pos.add(0, 2, -1));
                }
                if (this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(1, 3, 0)))) {
                    this.perform(pos.add(1, 1, 0));
                    this.perform(pos.add(1, 2, 0));
                }
                if (this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(-1, 3, 0)))) {
                    this.perform(pos.add(-1, 1, 0));
                    this.perform(pos.add(-1, 2, 0));
                }
                if (this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 3, 0)))) {
                    AntiCev.mc.player.jump();
                    if (!AntiCev.mc.player.onGround) {
                        attackCrystal(pos.add(0, 3, 0));
                        this.perform(pos.add(0, 3, 0));
                        if (this.getBlock(pos.add(1, 3, 0)).getBlock() != Blocks.AIR) {
                            this.perform(pos.add(1, 4, 0));
                        }
                        else if (this.getBlock(pos.add(1, 2, 0)).getBlock() != Blocks.AIR) {
                            this.perform(pos.add(1, 3, 0));
                        }
                        else if (this.getBlock(pos.add(1, 1, 0)).getBlock() != Blocks.AIR) {
                            this.perform(pos.add(1, 2, 0));
                        }
                        else if (this.getBlock(pos.add(1, 0, 0)).getBlock() != Blocks.AIR) {
                            this.perform(pos.add(1, 1, 0));
                        }
                        this.perform(pos.add(0, 4, 0));
                    }
                }
                if (this.getBlock(pos.add(0, 3, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 4, 0)))) {
                    this.perform(pos.add(0, 2, 0));
                    this.perform(pos.add(0, 3, 0));
                }
                if (this.getBlock(pos.add(0, 3, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 4, 0)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 5, 0)))) {
                    this.perform(pos.add(0, 4, 0));
                    this.perform(pos.add(0, 3, 0));
                }
            }
            else {
                if (this.crystal != null && this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 3, 0)))) {
                    this.CevHigh = 1;
                }
                if (this.CevHigh == 1 && this.crystal != null && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 3, 0)))) {
                    AntiCev.mc.player.jump();
                    if (!AntiCev.mc.player.onGround) {
                        attackCrystal(pos.add(0, 3, 0));
                    }
                }
                if (this.CevHigh == 1 && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 3, 0)) == null) {
                    this.perform(pos.add(0, 3, 0));
                    if (this.getBlock(pos.add(0, 3, 0)).getBlock() != Blocks.AIR) {
                        this.CevHigh = 0;
                    }
                }
                if (this.crystal != null && this.getBlock(pos.add(0, 3, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 4, 0)))) {
                    this.CevHigh = 2;
                }
                if (this.CevHigh == 2 && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 4, 0)) == null) {
                    this.perform(pos.add(0, 4, 0));
                    if (this.getBlock(pos.add(0, 4, 0)).getBlock() != Blocks.AIR) {
                        this.CevHigh = 0;
                    }
                }
                if (this.crystal != null && this.getBlock(pos.add(0, 3, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 4, 0)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 5, 0)))) {
                    this.CevHigh = 3;
                }
                if (this.CevHigh == 3 && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 5, 0)) == null) {
                    this.perform(pos.add(0, 5, 0));
                    if (this.getBlock(pos.add(0, 5, 0)).getBlock() != Blocks.AIR) {
                        this.CevHigh = 0;
                    }
                }
                if (this.crystal == null) {
                    return;
                }
                if (Rebirth.moduleManager.isModuleEnabled("Surround") && this.highCiv.getValue()) {
                    if (this.getBlock(pos.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 3, 1)))) {
                        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
                        Rebirth.positionManager.setPositionPacket(this.startPos.getX() + 0.5, this.startPos.getY(), this.startPos.getZ() + 0.5, true, true, true);
                        attackCrystal(pos.add(0, 3, 1));
                        this.perform(pos.add(0, 3, 1));
                    }
                    if (this.getBlock(pos.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 3, -1)))) {
                        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
                        Rebirth.positionManager.setPositionPacket(this.startPos.getX() + 0.5, this.startPos.getY(), this.startPos.getZ() + 0.5, true, true, true);
                        attackCrystal(pos.add(0, 3, -1));
                        this.perform(pos.add(0, 3, -1));
                    }
                    if (this.getBlock(pos.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(1, 3, 0)))) {
                        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
                        Rebirth.positionManager.setPositionPacket(this.startPos.getX() + 0.5, this.startPos.getY(), this.startPos.getZ() + 0.5, true, true, true);
                        attackCrystal(pos.add(1, 3, 0));
                        this.perform(pos.add(1, 3, 0));
                    }
                    if (this.getBlock(pos.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(-1, 3, 0)))) {
                        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
                        Rebirth.positionManager.setPositionPacket(this.startPos.getX() + 0.5, this.startPos.getY(), this.startPos.getZ() + 0.5, true, true, true);
                        attackCrystal(pos.add(-1, 3, 0));
                        this.perform(pos.add(-1, 3, 0));
                    }
                }
            }
            if (this.crystal == null) {
                return;
            }
            if (this.getBlock(pos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, 1)))) {
                attackCrystal(pos.add(0, 2, 1));
                this.perform(pos.add(0, 2, 1));
                this.perform(pos.add(0, 3, 1));
                if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(pos.add(1, 1, 1)).getBlock() != Blocks.AIR) {
                        this.perform(pos.add(1, 2, 1));
                    }
                    else if (this.getBlock(pos.add(1, 0, 1)).getBlock() != Blocks.AIR) {
                        this.perform(pos.add(1, 1, 1));
                    }
                }
            }
            if (this.getBlock(pos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, -1)))) {
                attackCrystal(pos.add(0, 2, -1));
                this.perform(pos.add(0, 2, -1));
                this.perform(pos.add(0, 3, -1));
                if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(pos.add(-1, 1, -1)).getBlock() != Blocks.AIR) {
                        this.perform(pos.add(-1, 2, -1));
                    }
                    else if (this.getBlock(pos.add(-1, 0, -1)).getBlock() != Blocks.AIR) {
                        this.perform(pos.add(-1, 1, -1));
                    }
                }
            }
            if (this.getBlock(pos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(1, 2, 0)))) {
                attackCrystal(pos.add(1, 2, 0));
                this.perform(pos.add(1, 2, 0));
                this.perform(pos.add(1, 3, 0));
                if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(pos.add(1, 1, 1)).getBlock() != Blocks.AIR) {
                        this.perform(pos.add(1, 2, 1));
                    }
                    else if (this.getBlock(pos.add(1, 0, 1)).getBlock() != Blocks.AIR) {
                        this.perform(pos.add(1, 1, 1));
                    }
                }
            }
            if (this.getBlock(pos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.crystal).equals((Object)new BlockPos((Vec3i)pos.add(-1, 2, 0)))) {
                attackCrystal(pos.add(-1, 2, 0));
                this.perform(pos.add(-1, 2, 0));
                this.perform(pos.add(-1, 3, 0));
                if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(pos.add(-1, 1, -1)).getBlock() != Blocks.AIR) {
                        this.perform(pos.add(-1, 2, -1));
                    }
                    else if (this.getBlock(pos.add(-1, 0, -1)).getBlock() != Blocks.AIR) {
                        this.perform(pos.add(-1, 1, -1));
                    }
                }
            }
        }
    }
    
    public static void attackCrystal(final BlockPos pos) {
        for (final Entity crystal : (List)AntiCev.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> AntiCev.mc.player.getDistance(e))).collect(Collectors.toList())) {
            if (crystal instanceof EntityEnderCrystal) {
                if (crystal.getDistanceSq(pos) > 1.0) {
                    continue;
                }
                AntiCev.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(crystal));
                AntiCev.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            }
        }
    }
    
    private Entity getTarget() {
        Entity target = null;
        for (final Entity player : AntiCev.mc.world.loadedEntityList) {
            if (!(player instanceof EntityEnderCrystal)) {
                continue;
            }
            target = player;
        }
        return target;
    }
    
    Entity checkCrystal(final Vec3d pos, final Vec3d[] list) {
        Entity crystal = null;
        for (final Vec3d vec3d : list) {
            final BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            for (final Entity entity : AntiCev.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(position))) {
                if (entity instanceof EntityEnderCrystal) {
                    if (crystal != null) {
                        continue;
                    }
                    crystal = entity;
                }
            }
        }
        return crystal;
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return AntiCev.mc.world.getBlockState(block);
    }
    
    private void perform(final BlockPos pos2) {
        if (AntiCev.mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
            final int old = AntiCev.mc.player.inventory.currentItem;
            if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos2))) {
                return;
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos2))) {
                return;
            }
            if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                AntiCev.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                AntiCev.mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                AntiCev.mc.player.inventory.currentItem = old;
                AntiCev.mc.playerController.updateController();
            }
        }
    }
}
