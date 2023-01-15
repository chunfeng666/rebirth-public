

package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import net.minecraft.client.entity.*;

public class BlockFly extends Module
{
    private final Timer timer;
    public Setting<Boolean> rotation;
    private BlockPos pos;
    
    public BlockFly() {
        super("BlockFly", "Places Blocks underneath you.", Module.Category.MOVEMENT, true, false, false);
        this.timer = new Timer();
        this.rotation = (Setting<Boolean>)this.register(new Setting("Rotate", Boolean.FALSE));
    }
    
    public void onEnable() {
        this.timer.reset();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayerPost(final UpdateWalkingPlayerEvent event) {
        if (this.isOff() || fullNullCheck() || event.getStage() == 0) {
            return;
        }
        if (!BlockFly.mc.gameSettings.keyBindJump.isKeyDown()) {
            this.timer.reset();
        }
        final BlockPos playerBlock;
        if (BlockUtil.isScaffoldPos((playerBlock = EntityUtil.getPlayerPosWithEntity()).add(0, -1, 0))) {
            if (BlockUtil.isValidBlock(playerBlock.add(0, -2, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.UP);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.EAST);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.WEST);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(0, -1, -1))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.NORTH);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.EAST);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.WEST);
                }
                this.place(playerBlock.add(-1, -1, 1), EnumFacing.SOUTH);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.SOUTH);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.WEST);
            }
            else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.EAST);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.NORTH);
            }
        }
    }
    
    public void place(final BlockPos posI, final EnumFacing face) {
        BlockPos pos = posI;
        if (face == EnumFacing.UP) {
            pos = pos.add(0, -1, 0);
        }
        else if (face == EnumFacing.NORTH) {
            pos = pos.add(0, 0, 1);
        }
        else if (face == EnumFacing.SOUTH) {
            pos = pos.add(0, 0, -1);
        }
        else if (face == EnumFacing.EAST) {
            pos = pos.add(-1, 0, 0);
        }
        else if (face == EnumFacing.WEST) {
            pos = pos.add(1, 0, 0);
        }
        final int oldSlot = BlockFly.mc.player.inventory.currentItem;
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = BlockFly.mc.player.inventory.getStackInSlot(i);
            if (!InventoryUtil.isNull(stack) && stack.getItem() instanceof ItemBlock && Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock()) {
                newSlot = i;
                break;
            }
        }
        if (newSlot == -1) {
            return;
        }
        boolean crouched = false;
        final Block block;
        if (!BlockFly.mc.player.isSneaking() && BlockUtil.blackList.contains(block = BlockFly.mc.world.getBlockState(pos).getBlock())) {
            BlockFly.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockFly.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            crouched = true;
        }
        if (!(BlockFly.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            BlockFly.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(newSlot));
            BlockFly.mc.player.inventory.currentItem = newSlot;
            BlockFly.mc.playerController.updateController();
        }
        if (BlockFly.mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP player = BlockFly.mc.player;
            player.motionX *= 0.3;
            final EntityPlayerSP player2 = BlockFly.mc.player;
            player2.motionZ *= 0.3;
            BlockFly.mc.player.jump();
            if (this.timer.passedMs(1500L)) {
                BlockFly.mc.player.motionY = -0.28;
                this.timer.reset();
            }
        }
        if (this.rotation.getValue()) {
            final float[] angle = MathUtil.calcAngle(BlockFly.mc.player.getPositionEyes(BlockFly.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() - 0.5f), (double)(pos.getZ() + 0.5f)));
            BlockFly.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angle[0], (float)MathHelper.normalizeAngle((int)angle[1], 360), BlockFly.mc.player.onGround));
        }
        BlockFly.mc.playerController.processRightClickBlock(BlockFly.mc.player, BlockFly.mc.world, pos, face, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
        BlockFly.mc.player.swingArm(EnumHand.MAIN_HAND);
        BlockFly.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(oldSlot));
        BlockFly.mc.player.inventory.currentItem = oldSlot;
        BlockFly.mc.playerController.updateController();
        if (crouched) {
            BlockFly.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockFly.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
}
