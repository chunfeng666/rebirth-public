
package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import org.lwjgl.input.*;
import net.minecraft.network.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import java.awt.*;
import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.item.*;
import java.util.stream.*;
import net.minecraft.entity.*;
import net.minecraft.network.play.client.*;
import java.util.*;
import dev.madcat.rebirth.event.*;

public class InstantMine extends Module
{
    private final Timer breakSuccess;
    private static InstantMine INSTANCE;
    private final Setting<Boolean> creativeMode;
    private final Setting<Boolean> ghostHand;
    private final Setting<Boolean> render;
    private final Setting<Integer> falpha;
    private final Setting<Boolean> render2;
    private final Setting<Integer> balpha;
    private final Setting<Boolean> crystal;
    private final Setting<Boolean> crystalp;
    public final Setting<Boolean> attackcrystal;
    public final Setting<Bind> bind;
    public Setting<Boolean> db;
    public final Setting<Float> health;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Integer> alpha2;
    private final List<Block> godBlocks;
    private boolean cancelStart;
    private boolean empty;
    private EnumFacing facing;
    public static BlockPos breakPos;
    int slotMain2;
    int swithc2;
    public static BlockPos breakPos2;
    double manxi;
    double manxi2;
    public final Timer imerS;
    public final Timer imerS2;
    static int ticked;
    
    public InstantMine() {
        super("InstantMine", "Crazy packet miner.", Category.COMBAT, true, false, false);
        this.breakSuccess = new Timer();
        this.creativeMode = (Setting<Boolean>)this.register(new Setting("CreativeMode", true));
        this.ghostHand = (Setting<Boolean>)this.register(new Setting("GhostHand", Boolean.TRUE, v -> this.creativeMode.getValue()));
        this.render = (Setting<Boolean>)this.register(new Setting("Fill", true));
        this.falpha = (Setting<Integer>)this.register(new Setting("FillAlpha", 30, 0, 255, v -> this.render.getValue()));
        this.render2 = (Setting<Boolean>)this.register(new Setting("Box", true));
        this.balpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 100, 0, 255, v -> this.render2.getValue()));
        this.crystal = (Setting<Boolean>)this.register(new Setting("Crystal", Boolean.TRUE));
        this.crystalp = (Setting<Boolean>)this.register(new Setting("Crystal on Break", Boolean.TRUE, v -> this.crystal.getValue()));
        this.attackcrystal = (Setting<Boolean>)this.register(new Setting("Attack Crystal", Boolean.TRUE, v -> this.crystal.getValue()));
        this.bind = (Setting<Bind>)this.register(new Setting("ObsidianBind", new Bind(-1), v -> this.crystal.getValue()));
        this.db = (Setting<Boolean>)this.register(new Setting("Silent Double", Boolean.TRUE));
        this.health = (Setting<Float>)this.register(new Setting("Health", 18.0f, 0.0f, 35.9f, v -> this.db.getValue()));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 255, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 255, 0, 255));
        this.alpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 150, 0, 255));
        this.alpha2 = (Setting<Integer>)this.register(new Setting("FillAlpha", 70, 0, 255));
        this.godBlocks = Arrays.asList(Blocks.AIR, (Block)Blocks.FLOWING_LAVA, (Block)Blocks.LAVA, (Block)Blocks.FLOWING_WATER, (Block)Blocks.WATER, Blocks.BEDROCK);
        this.cancelStart = false;
        this.empty = false;
        this.manxi = 0.0;
        this.manxi2 = 0.0;
        this.imerS = new Timer();
        this.imerS2 = new Timer();
        this.setInstance();
    }
    
    public static InstantMine getInstance() {
        if (InstantMine.INSTANCE != null) {
            return InstantMine.INSTANCE;
        }
        return InstantMine.INSTANCE = new InstantMine();
    }
    
    private void setInstance() {
        InstantMine.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
        if (InstantMine.mc.player.isCreative()) {
            return;
        }
        this.slotMain2 = InstantMine.mc.player.inventory.currentItem;
        if (InstantMine.ticked <= 86 && InstantMine.ticked >= 0) {
            ++InstantMine.ticked;
        }
        if (InstantMine.breakPos2 == null) {
            this.manxi2 = 0.0;
        }
        if (InstantMine.breakPos2 != null && (InstantMine.ticked >= 65 || (InstantMine.ticked >= 20 && InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.ENDER_CHEST))) {
            if (InstantMine.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.GOLDEN_APPLE || InstantMine.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.CHORUS_FRUIT) {
                if (!Mouse.isButtonDown(1)) {
                    if (InstantMine.mc.player.getHealth() + InstantMine.mc.player.getAbsorptionAmount() >= this.health.getValue()) {
                        if (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1 && this.db.getValue()) {
                            InstantMine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE)));
                            this.swithc2 = 1;
                            ++InstantMine.ticked;
                        }
                    }
                    else if (this.swithc2 == 1) {
                        InstantMine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.slotMain2));
                        this.swithc2 = 0;
                    }
                }
                else if (this.swithc2 == 1) {
                    InstantMine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.slotMain2));
                    this.swithc2 = 0;
                }
            }
            else if (InstantMine.mc.player.getHealth() + InstantMine.mc.player.getAbsorptionAmount() >= this.health.getValue()) {
                if (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1 && this.db.getValue()) {
                    InstantMine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE)));
                    this.swithc2 = 1;
                    ++InstantMine.ticked;
                }
            }
            else if (this.swithc2 == 1) {
                InstantMine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.slotMain2));
                this.swithc2 = 0;
            }
        }
        if (InstantMine.breakPos2 != null && InstantMine.mc.world.getBlockState(InstantMine.breakPos2).getBlock() == Blocks.AIR) {
            if (this.swithc2 == 1) {
                InstantMine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.slotMain2));
                this.swithc2 = 0;
            }
            InstantMine.breakPos2 = null;
            this.manxi2 = 0.0;
            InstantMine.ticked = 0;
        }
        if (InstantMine.ticked == 0) {
            this.manxi2 = 0.0;
            InstantMine.breakPos2 = null;
        }
        if (InstantMine.ticked >= 140) {
            if (this.swithc2 == 1) {
                InstantMine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.slotMain2));
                this.swithc2 = 0;
            }
            this.manxi2 = 0.0;
            InstantMine.breakPos2 = null;
            InstantMine.ticked = 0;
        }
        if (InstantMine.breakPos != null && InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.AIR && InstantMine.breakPos2 == null) {
            InstantMine.ticked = 0;
        }
        if (fullNullCheck()) {
            return;
        }
        if (!this.creativeMode.getValue()) {
            return;
        }
        if (!this.cancelStart) {
            return;
        }
        if (this.crystal.getValue() && this.attackcrystal.getValue() && InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.AIR) {
            attackcrystal();
        }
        if (this.bind.getValue().isDown() && this.crystal.getValue() && InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1 && InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.AIR) {
            final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            final int old = InstantMine.mc.player.inventory.currentItem;
            this.switchToSlot(obbySlot);
            BlockUtil.placeBlock(InstantMine.breakPos, EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        if (InventoryUtil.getItemHotbar(Items.END_CRYSTAL) != -1 && this.crystal.getValue() && InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.OBSIDIAN && !InstantMine.breakPos.equals((Object)AntiBurrow.pos)) {
            if (this.empty) {
                BlockUtil.placeCrystalOnBlock(InstantMine.breakPos, EnumHand.MAIN_HAND, true, false, true);
            }
            else if (!this.crystalp.getValue()) {
                BlockUtil.placeCrystalOnBlock(InstantMine.breakPos, EnumHand.MAIN_HAND, true, false, true);
            }
        }
        if (this.godBlocks.contains(InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock())) {
            return;
        }
        if (InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock() != Blocks.WEB) {
            if (this.ghostHand.getValue() && InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1 && InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE) != -1) {
                final int slotMain = InstantMine.mc.player.inventory.currentItem;
                if (InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock() != Blocks.OBSIDIAN) {
                    InstantMine.mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
                    InstantMine.mc.playerController.updateController();
                    InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, InstantMine.breakPos, this.facing));
                    InstantMine.mc.player.inventory.currentItem = slotMain;
                    InstantMine.mc.playerController.updateController();
                    return;
                }
                if (!this.breakSuccess.passedMs(1234L)) {
                    return;
                }
                InstantMine.mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
                InstantMine.mc.playerController.updateController();
                InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, InstantMine.breakPos, this.facing));
                InstantMine.mc.player.inventory.currentItem = slotMain;
                InstantMine.mc.playerController.updateController();
                return;
            }
        }
        else if (this.ghostHand.getValue() && InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD) != -1 && InventoryUtil.getItemHotbars(Items.DIAMOND_SWORD) != -1) {
            final int slotMain = InstantMine.mc.player.inventory.currentItem;
            InstantMine.mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD);
            InstantMine.mc.playerController.updateController();
            InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, InstantMine.breakPos, this.facing));
            InstantMine.mc.player.inventory.currentItem = slotMain;
            InstantMine.mc.playerController.updateController();
            return;
        }
        InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, InstantMine.breakPos, this.facing));
    }
    
    private void switchToSlot(final int slot) {
        InstantMine.mc.player.inventory.currentItem = slot;
        InstantMine.mc.playerController.updateController();
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (!InstantMine.mc.player.isCreative()) {
            if (InstantMine.breakPos2 != null) {
                final AxisAlignedBB axisAlignedBB = InstantMine.mc.world.getBlockState(InstantMine.breakPos2).getSelectedBoundingBox((World)InstantMine.mc.world, InstantMine.breakPos2);
                final double centerX = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0;
                final double centerY = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0;
                final double centerZ = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0;
                final double progressValX = getInstance().manxi2 * ((axisAlignedBB.maxX - centerX) / 10.0);
                final double progressValY = getInstance().manxi2 * ((axisAlignedBB.maxY - centerY) / 10.0);
                final double progressValZ = getInstance().manxi2 * ((axisAlignedBB.maxZ - centerZ) / 10.0);
                final AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
                if (InstantMine.breakPos != null) {
                    if (!InstantMine.breakPos2.equals((Object)InstantMine.breakPos)) {
                        RenderUtil.drawBBBox(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.alpha.getValue());
                        RenderUtil.drawBBFill(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha2.getValue()), this.alpha2.getValue());
                    }
                }
                else {
                    RenderUtil.drawBBBox(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.alpha.getValue());
                    RenderUtil.drawBBFill(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha2.getValue()), this.alpha2.getValue());
                }
            }
            if (this.creativeMode.getValue() && this.cancelStart) {
                if (this.godBlocks.contains(InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock())) {
                    this.empty = true;
                }
                if (this.imerS.passedMs(15L)) {
                    if (this.manxi <= 10.0) {
                        this.manxi += 0.11;
                    }
                    this.imerS.reset();
                }
                if (this.imerS2.passedMs(22L)) {
                    if (this.manxi2 <= 10.0 && this.manxi2 >= 0.0) {
                        this.manxi2 += 0.11;
                    }
                    this.imerS2.reset();
                }
                final AxisAlignedBB axisAlignedBB = InstantMine.mc.world.getBlockState(InstantMine.breakPos).getSelectedBoundingBox((World)InstantMine.mc.world, InstantMine.breakPos);
                final double centerX = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0;
                final double centerY = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0;
                final double centerZ = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0;
                final double progressValX = this.manxi * ((axisAlignedBB.maxX - centerX) / 10.0);
                final double progressValY = this.manxi * ((axisAlignedBB.maxY - centerY) / 10.0);
                final double progressValZ = this.manxi * ((axisAlignedBB.maxZ - centerZ) / 10.0);
                final AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
                if (this.render.getValue()) {
                    RenderUtil.drawBBFill(axisAlignedBB2, new Color(this.empty ? 0 : 255, this.empty ? 255 : 0, 0, 255), this.falpha.getValue());
                }
                if (this.render2.getValue()) {
                    RenderUtil.drawBBBox(axisAlignedBB2, new Color(this.empty ? 0 : 255, this.empty ? 255 : 0, 0, 255), this.balpha.getValue());
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (fullNullCheck()) {
            return;
        }
        if (InstantMine.mc.player.isCreative()) {
            return;
        }
        if (!(event.getPacket() instanceof CPacketPlayerDigging)) {
            return;
        }
        final CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
        if (packet.getAction() != CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            return;
        }
        event.setCanceled(this.cancelStart);
    }
    
    public static void attackcrystal() {
        for (final Entity crystal : (List)InstantMine.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> InstantMine.mc.player.getDistance(e))).collect(Collectors.toList())) {
            if (crystal instanceof EntityEnderCrystal) {
                if (crystal.getDistanceSq(InstantMine.breakPos) > 2.0) {
                    continue;
                }
                InstantMine.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(crystal));
                InstantMine.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            }
        }
    }
    
    @SubscribeEvent
    public void onBlockEvent(final PlayerDamageBlockEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (InstantMine.mc.player.isCreative()) {
            return;
        }
        if (!BlockUtil.canBreak(event.pos)) {
            return;
        }
        if (InstantMine.breakPos != null && InstantMine.breakPos.getX() == event.pos.getX() && InstantMine.breakPos.getY() == event.pos.getY() && InstantMine.breakPos.getZ() == event.pos.getZ()) {
            return;
        }
        if (InstantMine.ticked == 0) {
            InstantMine.ticked = 1;
        }
        if (this.manxi2 == 0.0) {
            this.manxi2 = 0.11;
        }
        if (InstantMine.breakPos != null && InstantMine.breakPos2 == null && InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock() != Blocks.AIR) {
            InstantMine.breakPos2 = InstantMine.breakPos;
        }
        if (InstantMine.breakPos == null && InstantMine.breakPos2 == null) {
            InstantMine.breakPos2 = event.pos;
        }
        this.manxi = 0.0;
        this.empty = false;
        this.cancelStart = false;
        InstantMine.breakPos = event.pos;
        this.breakSuccess.reset();
        this.facing = event.facing;
        if (InstantMine.breakPos == null) {
            return;
        }
        InstantMine.mc.player.swingArm(EnumHand.MAIN_HAND);
        InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, InstantMine.breakPos, this.facing));
        this.cancelStart = true;
        InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, InstantMine.breakPos, this.facing));
        event.setCanceled(true);
    }
    
    static {
        InstantMine.INSTANCE = new InstantMine();
        InstantMine.ticked = 0;
    }
}
