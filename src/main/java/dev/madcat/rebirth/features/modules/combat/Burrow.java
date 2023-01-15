
package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.init.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.features.command.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraft.entity.item.*;
import net.minecraft.util.*;
import java.util.*;
import net.minecraft.block.state.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.block.*;

public class Burrow extends Module
{
    private Setting<Boolean> rotate;
    private boolean isSneaking;
    private BlockPos startPos;
    private boolean noFall;
    int oldSlot;
    private BlockPos originalPos;
    public boolean shouldEnable;
    
    public Burrow() {
        super("Burrow", "XD", Category.COMBAT, true, false, false);
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", true));
        this.isSneaking = false;
        this.startPos = null;
        this.noFall = false;
        this.oldSlot = -1;
        this.shouldEnable = true;
    }
    
    @Override
    public void onDisable() {
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (!this.noFall) {
            return;
        }
        this.noFall = false;
    }
    
    @Override
    public void onEnable() {
        final Vec3d a = AntiCev.mc.player.getPositionVector();
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 0)) != null) {
            EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 0)), true);
        }
        if (InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN) == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "Obsidian ?");
            this.disable();
            return;
        }
        this.originalPos = new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ);
        if (!Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) && !intersectsWithEntity(this.originalPos)) {
            if (Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.getPositionVector().add(0.0, 3.0, 0.0))).getBlock() != Blocks.AIR || Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.getPositionVector().add(0.0, 2.0, 0.0))).getBlock() != Blocks.AIR) {
                Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "From above stop you !");
                this.disable();
                return;
            }
            this.shouldEnable = true;
            this.oldSlot = Burrow.mc.player.inventory.currentItem;
        }
        else {
            this.shouldEnable = false;
            this.toggle();
        }
    }
    
    Entity checkCrystal(final Vec3d pos, final Vec3d[] list) {
        Entity crystal = null;
        final Vec3d[] var4 = list;
        for (int var5 = list.length, var6 = 0; var6 < var5; ++var6) {
            final Vec3d vec3d = var4[var6];
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
    
    @Override
    public void onTick() {
        if (Burrow.mc.player != null && Burrow.mc.world != null) {
            final int blockslot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
            if (blockslot != -1) {
                Burrow.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(blockslot));
            }
            EntityUtil.LocalPlayerfakeJump();
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Burrow.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true);
            if (this.oldSlot != -1) {
                Burrow.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.oldSlot));
            }
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 3.0, Burrow.mc.player.posZ, false));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ, true));
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Burrow.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.toggle();
        }
    }
    
    private static boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : Burrow.mc.world.loadedEntityList) {
            if (!entity.equals((Object)Burrow.mc.player) && !(entity instanceof EntityItem) && new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }
    
    public List<EnumFacing> getPossibleSides(final BlockPos pos) {
        final ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        if (Burrow.mc.world != null && pos != null) {
            for (final EnumFacing side : EnumFacing.values()) {
                final BlockPos neighbour = pos.offset(side);
                final IBlockState blockState = Burrow.mc.world.getBlockState(neighbour);
                if (blockState != null && blockState.getBlock().canCollideCheck(blockState, false) && !blockState.getMaterial().isReplaceable()) {
                    facings.add(side);
                }
            }
            return facings;
        }
        return facings;
    }
    
    public void rightClickBlock(final BlockPos pos, final Vec3d vec, final EnumHand hand, final EnumFacing direction, final boolean packet) {
        if (packet) {
            final float f = (float)(vec.x - pos.getX());
            final float f2 = (float)(vec.y - pos.getY());
            final float f3 = (float)(vec.z - pos.getZ());
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
        }
        else {
            Burrow.mc.playerController.processRightClickBlock(Burrow.mc.player, Burrow.mc.world, pos, direction, vec, hand);
        }
        Burrow.mc.player.swingArm(EnumHand.MAIN_HAND);
        Burrow.mc.rightClickDelayTimer = 4;
    }
    
    public boolean placeBlock(final BlockPos pos, final EnumHand hand, final boolean rotate, final boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = null;
        final Iterator<EnumFacing> iterator = this.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            side = iterator.next();
        }
        if (side == null) {
            return isSneaking;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = Burrow.mc.world.getBlockState(neighbour).getBlock();
        if (!Burrow.mc.player.isSneaking() && (BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock))) {
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Burrow.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            Burrow.mc.player.setSneaking(true);
            sneaking = true;
        }
        if (rotate) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        this.rightClickBlock(neighbour, hitVec, hand, opposite, true);
        Burrow.mc.player.swingArm(EnumHand.MAIN_HAND);
        Burrow.mc.rightClickDelayTimer = 4;
        return sneaking || isSneaking;
    }
}
