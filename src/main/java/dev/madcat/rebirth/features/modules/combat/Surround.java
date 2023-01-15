
package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.*;
import net.minecraft.init.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.features.modules.client.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.entity.item.*;
import java.util.stream.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import java.util.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;
import dev.madcat.rebirth.features.command.*;

public class Surround extends Module
{
    public static boolean isPlacing;
    private final Setting<Boolean> old;
    private final Setting<Boolean> face2;
    private final Setting<Boolean> face;
    private final Setting<Integer> blocksPerTick;
    private final Setting<Integer> delay;
    private final Setting<Boolean> noGhost;
    private final Setting<Boolean> breakcrystal;
    private final Setting<Double> safehealth;
    private final Setting<Boolean> center;
    private final Setting<Boolean> rotate;
    private final Timer timer;
    private final Timer retryTimer;
    private final Set<Vec3d> extendingBlocks;
    private final Map<BlockPos, Integer> retries;
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace;
    private boolean switchedItem;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements;
    private int extenders;
    BlockPos CrystalPos;
    BlockPos FeetPos;
    private int obbySlot;
    private boolean offHand;
    int CevHigh;
    
    public Surround() {
        super("Surround", "Surrounds you with Obsidian.", Category.COMBAT, true, false, false);
        this.old = (Setting<Boolean>)this.register(new Setting("OldMode", false));
        this.face2 = (Setting<Boolean>)this.register(new Setting("AntiFace", false, v -> !this.old.getValue()));
        this.face = (Setting<Boolean>)this.register(new Setting("AntiFace+", false, v -> !this.old.getValue()));
        this.blocksPerTick = (Setting<Integer>)this.register(new Setting("BlocksPerTick", 12, 1, 20, v -> this.old.getValue()));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", 0, 0, 250, v -> this.old.getValue()));
        this.noGhost = (Setting<Boolean>)this.register(new Setting("PacketPlace", true, v -> this.old.getValue()));
        this.breakcrystal = (Setting<Boolean>)this.register(new Setting("BreakCrystal", true, v -> this.old.getValue()));
        this.safehealth = (Setting<Double>)this.register(new Setting("Safe Health", 12.5, 1.0, 36.0, v -> this.breakcrystal.getValue() && this.old.getValue()));
        this.center = (Setting<Boolean>)this.register(new Setting("TPCenter", true));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", false));
        this.timer = new Timer();
        this.retryTimer = new Timer();
        this.extendingBlocks = new HashSet<Vec3d>();
        this.retries = new HashMap<BlockPos, Integer>();
        this.didPlace = false;
        this.placements = 0;
        this.extenders = 1;
        this.CrystalPos = null;
        this.obbySlot = -1;
        this.offHand = false;
    }
    
    @Override
    public void onEnable() {
        this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
        if (this.center.getValue()) {
            Rebirth.positionManager.setPositionPacket(this.startPos.getX() + 0.5, this.startPos.getY(), this.startPos.getZ() + 0.5, true, true, true);
        }
        this.retries.clear();
        this.retryTimer.reset();
    }
    
    @Override
    public void onTick() {
        if (this.startPos == null) {
            this.disable();
            return;
        }
        if (!this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player))) {
            this.disable();
            return;
        }
        if (this.old.getValue()) {
            if (this.breakcrystal.getValue() && Surround.mc.player.getHealth() >= this.safehealth.getValue() && this.isSafe == 0) {
                breakcrystal();
            }
            this.doFeetPlace();
            return;
        }
        final BlockPos pos = new BlockPos(Surround.mc.player.posX, Surround.mc.player.posY, Surround.mc.player.posZ);
        final EntityPlayer target = this.getTarget();
        if (target != null) {
            this.FeetPos = new BlockPos(target.posX, target.posY, target.posZ);
        }
        else {
            this.FeetPos = null;
        }
        final Entity crystal = this.getEndCrystal();
        if (crystal != null) {
            this.CrystalPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
            if (Surround.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && Surround.mc.world.getBlockState(pos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(3, 0, 0)))) {
                this.CevHigh = 1;
            }
            if (this.CevHigh == 1 && this.checkCrystal(EntityUtil.getVarOffsets(3, 0, 0)) == null) {
                this.perform(pos.add(3, 0, 0));
                if (Surround.mc.world.getBlockState(pos.add(3, 0, 0)).getBlock() != Blocks.AIR) {
                    this.CevHigh = 0;
                }
            }
            if (Surround.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && Surround.mc.world.getBlockState(pos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-3, 0, 0)))) {
                this.CevHigh = 2;
            }
            if (this.CevHigh == 2 && this.checkCrystal(EntityUtil.getVarOffsets(-3, 0, 0)) == null) {
                this.perform(pos.add(-3, 0, 0));
                if (Surround.mc.world.getBlockState(pos.add(-3, 0, 0)).getBlock() != Blocks.AIR) {
                    this.CevHigh = 0;
                }
            }
            if (Surround.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && Surround.mc.world.getBlockState(pos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 3)))) {
                this.CevHigh = 3;
            }
            if (this.CevHigh == 3 && this.checkCrystal(EntityUtil.getVarOffsets(0, 0, 3)) == null) {
                this.perform(pos.add(0, 0, 3));
                if (Surround.mc.world.getBlockState(pos.add(0, 0, 3)).getBlock() != Blocks.AIR) {
                    this.CevHigh = 0;
                }
            }
            if (Surround.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && Surround.mc.world.getBlockState(pos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -3)))) {
                this.CevHigh = 4;
            }
            if (this.CevHigh == 4 && this.checkCrystal(EntityUtil.getVarOffsets(0, 0, -3)) == null) {
                this.perform(pos.add(0, 0, -3));
                if (Surround.mc.world.getBlockState(pos.add(0, 0, -3)).getBlock() != Blocks.AIR) {
                    this.CevHigh = 0;
                }
            }
            if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1)))) {
                AttackCrystal(pos.add(0, 0, 1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1)))) {
                AttackCrystal(pos.add(0, 0, -1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0)))) {
                AttackCrystal(pos.add(1, 0, 0));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0)))) {
                AttackCrystal(pos.add(-1, 0, 0));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1)))) {
                AttackCrystal(pos.add(0, -1, 1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1)))) {
                AttackCrystal(pos.add(0, -1, -1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0)))) {
                AttackCrystal(pos.add(1, -1, 0));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0)))) {
                AttackCrystal(pos.add(-1, -1, 0));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 2)))) {
                AttackCrystal(pos.add(0, 0, 2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -2)))) {
                AttackCrystal(pos.add(0, 0, -2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 0)))) {
                AttackCrystal(pos.add(2, 0, 0));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 0)))) {
                AttackCrystal(pos.add(-2, 0, 0));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 2)))) {
                AttackCrystal(pos.add(0, 1, 2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -2)))) {
                AttackCrystal(pos.add(0, 1, -2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 1, 0)))) {
                AttackCrystal(pos.add(2, 1, 0));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 1, 0)))) {
                AttackCrystal(pos.add(-2, 1, 0));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 2)))) {
                AttackCrystal(pos.add(0, -1, 2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -2)))) {
                AttackCrystal(pos.add(0, -1, -2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(2, -1, 0)))) {
                AttackCrystal(pos.add(2, -1, 0));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, -1, 0)))) {
                AttackCrystal(pos.add(-2, -1, 0));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 1)))) {
                AttackCrystal(pos.add(1, 0, 1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -1)))) {
                AttackCrystal(pos.add(1, 0, -1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -1)))) {
                AttackCrystal(pos.add(-1, 0, -1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 1)))) {
                AttackCrystal(pos.add(-1, 0, 1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 1)))) {
                AttackCrystal(pos.add(1, -1, 1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, -1)))) {
                AttackCrystal(pos.add(1, -1, -1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, -1)))) {
                AttackCrystal(pos.add(-1, -1, -1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 1)))) {
                AttackCrystal(pos.add(-1, -1, 1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 1)))) {
                AttackCrystal(pos.add(2, 0, 1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, -1)))) {
                AttackCrystal(pos.add(2, 0, -1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, -1)))) {
                AttackCrystal(pos.add(-2, 0, -1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 1)))) {
                AttackCrystal(pos.add(-2, 0, 1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 2)))) {
                AttackCrystal(pos.add(1, 0, 2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -2)))) {
                AttackCrystal(pos.add(1, 0, -2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -2)))) {
                AttackCrystal(pos.add(-1, 0, -2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 2)))) {
                AttackCrystal(pos.add(-1, 0, 2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(2, -1, 1)))) {
                AttackCrystal(pos.add(2, -1, 1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(2, -1, -1)))) {
                AttackCrystal(pos.add(2, -1, -1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, -1, -1)))) {
                AttackCrystal(pos.add(-2, -1, -1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, -1, 1)))) {
                AttackCrystal(pos.add(-2, -1, 1));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 2)))) {
                AttackCrystal(pos.add(1, -1, 2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, -2)))) {
                AttackCrystal(pos.add(1, -1, -2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, -2)))) {
                AttackCrystal(pos.add(-1, -1, -2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 2)))) {
                AttackCrystal(pos.add(-1, -1, 2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 2)))) {
                AttackCrystal(pos.add(2, 0, 2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, -2)))) {
                AttackCrystal(pos.add(2, 0, -2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, -2)))) {
                AttackCrystal(pos.add(-2, 0, -2));
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 2)))) {
                AttackCrystal(pos.add(-2, 0, 2));
            }
        }
        if (Surround.mc.world.getBlockState(pos.add(0, -1, -1)).getBlock() != Blocks.AIR && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1)))) && Surround.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, -1));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -2, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1)))) && Surround.mc.world.getBlockState(pos.add(0, -1, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, -1, -1));
        }
        if (Surround.mc.world.getBlockState(pos.add(-1, -1, 0)).getBlock() != Blocks.AIR && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0)))) && Surround.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-1, 0, 0));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -2, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0)))) && Surround.mc.world.getBlockState(pos.add(-1, -1, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-1, -1, 0));
        }
        if (Surround.mc.world.getBlockState(pos.add(1, -1, 0)).getBlock() != Blocks.AIR && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0)))) && Surround.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(1, 0, 0));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -2, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0)))) && Surround.mc.world.getBlockState(pos.add(1, -1, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(1, -1, 0));
        }
        if (Surround.mc.world.getBlockState(pos.add(0, -1, 1)).getBlock() != Blocks.AIR && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1)))) && Surround.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, 1));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -2, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1)))) && Surround.mc.world.getBlockState(pos.add(0, -1, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, -1, 1));
        }
        else if (Surround.mc.world.getBlockState(pos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && this.checkCrystal(EntityUtil.getVarOffsets(0, 0, 2)) == null && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 2)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 2)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 2)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 2)))) && Surround.mc.world.getBlockState(pos.add(0, 0, 2)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, 2));
        }
        else if (Surround.mc.world.getBlockState(pos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && this.checkCrystal(EntityUtil.getVarOffsets(0, 0, -2)) == null && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -2)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -2)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -2)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -2)))) && Surround.mc.world.getBlockState(pos.add(0, 0, -2)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, -2));
        }
        else if (Surround.mc.world.getBlockState(pos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && this.checkCrystal(EntityUtil.getVarOffsets(2, 0, 0)) == null && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(2, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 0)))) && Surround.mc.world.getBlockState(pos.add(2, 0, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(2, 0, 0));
        }
        else if (Surround.mc.world.getBlockState(pos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && this.checkCrystal(EntityUtil.getVarOffsets(-3, 0, 0)) == null && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 0)))) && Surround.mc.world.getBlockState(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-2, 0, 0));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 1)))) && Surround.mc.world.getBlockState(pos.add(1, 0, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(1, 0, 1));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 1)))) && Surround.mc.world.getBlockState(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-1, 0, 1));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -1)))) && Surround.mc.world.getBlockState(pos.add(1, 0, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(1, 0, -1));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -1)))) && Surround.mc.world.getBlockState(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-1, 0, -1));
        }
        else if (Surround.mc.world.getBlockState(pos.add(0, 0, 2)).getBlock() != Blocks.AIR && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 2)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 2)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 2)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 2)))) && Surround.mc.world.getBlockState(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 1, 2));
            if (this.checkCrystal(EntityUtil.getVarOffsets(1, 1, 2)) == null && Surround.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(1, 0, 2));
                this.perform(pos.add(1, 1, 2));
            }
        }
        else if (Surround.mc.world.getBlockState(pos.add(0, 0, -2)).getBlock() != Blocks.AIR && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -2)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -2)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -2)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -2)))) && Surround.mc.world.getBlockState(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 1, -2));
            if (this.checkCrystal(EntityUtil.getVarOffsets(-1, 1, -2)) == null && Surround.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(-1, 0, -2));
                this.perform(pos.add(-1, 1, -2));
            }
        }
        else if (Surround.mc.world.getBlockState(pos.add(2, 0, 0)).getBlock() != Blocks.AIR && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 1, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 1, 0)))) && Surround.mc.world.getBlockState(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(2, 1, 0));
            if (this.checkCrystal(EntityUtil.getVarOffsets(2, 1, 1)) == null && Surround.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(2, 0, 1));
                this.perform(pos.add(2, 1, 1));
            }
        }
        else if (Surround.mc.world.getBlockState(pos.add(-2, 0, 0)).getBlock() != Blocks.AIR && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 1, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 1, 0)))) && Surround.mc.world.getBlockState(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-2, 1, 0));
            if (this.checkCrystal(EntityUtil.getVarOffsets(-2, 1, -1)) == null && Surround.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(-2, 0, -1));
                this.perform(pos.add(-2, 1, -1));
            }
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 0)))) && Surround.mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && (Surround.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR || (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0)))))) {
            this.perform(pos.add(-1, 1, 0));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 0)))) && Surround.mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && (Surround.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.AIR || (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0)))))) {
            this.perform(pos.add(1, 1, 0));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 1)))) && Surround.mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && (Surround.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.AIR || (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1)))))) {
            this.perform(pos.add(0, 1, 1));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -1)))) && Surround.mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && (Surround.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.AIR || (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1)))))) {
            this.perform(pos.add(0, 1, -1));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 2, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 2, 0)))) && BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 0))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 2, 0)))) && Surround.mc.world.getBlockState(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR) {
            this.perform(pos.add(-1, 2, 0));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 2, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 2, 0)))) && BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 0))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 2, 0)))) && Surround.mc.world.getBlockState(pos.add(1, 2, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR) {
            this.perform(pos.add(1, 2, 0));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, 1)))) && BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 1))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, 1)))) && Surround.mc.world.getBlockState(pos.add(0, 2, 1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR) {
            this.perform(pos.add(0, 2, 1));
        }
        else if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, -1)))) && BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -1))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, -1)))) && Surround.mc.world.getBlockState(pos.add(0, 2, -1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR) {
            this.perform(pos.add(0, 2, -1));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-3, 0, 0)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-3, -1, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-3, 0, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-3, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-3, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-3, 0, 0)))) && Surround.mc.world.getBlockState(pos.add(-3, 0, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-3, 0, 0));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(3, 0, 0)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(3, -1, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(3, 0, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(3, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(3, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(3, 0, 0)))) && Surround.mc.world.getBlockState(pos.add(3, 0, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(2, 0, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(3, 0, 0));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 3)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 3)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 3)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 3)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 3)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 3)))) && Surround.mc.world.getBlockState(pos.add(0, 0, 3)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 0, 2)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, 3));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -3)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -3)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -3)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -3)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -3)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -3)))) && Surround.mc.world.getBlockState(pos.add(0, 0, -3)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 0, -2)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, -3));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 1)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 1)))) && Surround.mc.world.getBlockState(pos.add(-2, 0, 1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-2, 0, 1));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 2)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 2)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 2)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 2)))) && Surround.mc.world.getBlockState(pos.add(-1, 0, 2)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-1, 0, 2));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 1)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 1)))) && Surround.mc.world.getBlockState(pos.add(2, 0, 1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(1, 0, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(2, 0, 1));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 2)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 2)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 2)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 2)))) && Surround.mc.world.getBlockState(pos.add(1, 0, 2)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(1, 0, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(1, 0, 2));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -2)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -2)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -2)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -2)))) && Surround.mc.world.getBlockState(pos.add(-1, 0, -2)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-1, 0, -2));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, -1)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, -1)))) && Surround.mc.world.getBlockState(pos.add(-2, 0, -1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-2, 0, -1));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, -1)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, -1)))) && Surround.mc.world.getBlockState(pos.add(2, 0, -1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(1, 0, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(2, 0, -1));
        }
        else if ((this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -2)))) && (this.CrystalPos == null || !new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -2)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -2)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -2)))) && Surround.mc.world.getBlockState(pos.add(1, 0, -2)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(1, 0, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(1, 0, -2));
        }
        else if (this.checkCrystal(EntityUtil.getVarOffsets(-3, 0, 0)) == null && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-3, 0, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-3, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-3, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-3, 0, 0)))) && Surround.mc.world.getBlockState(pos.add(-3, 0, 0)).getBlock() == Blocks.AIR && ((InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 0)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 0)))))) {
            this.perform(pos.add(-3, 0, 0));
        }
        else if (this.checkCrystal(EntityUtil.getVarOffsets(3, 0, 0)) == null && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(3, 0, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(3, -1, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(3, 0, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(3, 0, 0)))) && Surround.mc.world.getBlockState(pos.add(3, 0, 0)).getBlock() == Blocks.AIR && ((InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 0)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 0)))))) {
            this.perform(pos.add(3, 0, 0));
        }
        else if (this.checkCrystal(EntityUtil.getVarOffsets(0, 0, 3)) == null && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 3)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 3)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 3)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 3)))) && Surround.mc.world.getBlockState(pos.add(0, 0, 3)).getBlock() == Blocks.AIR && ((InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 2)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 2)))))) {
            this.perform(pos.add(0, 0, 3));
        }
        else if (this.checkCrystal(EntityUtil.getVarOffsets(0, 0, -3)) == null && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -3)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -3)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -3)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -3)))) && Surround.mc.world.getBlockState(pos.add(0, 0, -3)).getBlock() == Blocks.AIR && ((InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -2)))) || (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -2)))))) {
            this.perform(pos.add(0, 0, -3));
        }
        if (this.CrystalPos != null) {
            if (this.face.getValue() && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 1)))) {
                AttackCrystal(pos.add(1, 1, 1));
                if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 1)))) && Surround.mc.world.getBlockState(pos.add(1, 1, 1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(1, 1, 1));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 2, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 2, 1)))) && Surround.mc.world.getBlockState(pos.add(1, 1, 1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(1, 2, 1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(1, 2, 1));
                }
            }
            else if (this.face.getValue() && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, -1)))) {
                AttackCrystal(pos.add(1, 1, -1));
                if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, -1)))) && Surround.mc.world.getBlockState(pos.add(1, 1, -1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(1, 1, -1));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 2, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 2, -1)))) && Surround.mc.world.getBlockState(pos.add(1, 1, -1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(1, 2, -1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(1, 2, -1));
                }
            }
            else if (this.face.getValue() && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, -1)))) {
                AttackCrystal(pos.add(-1, 1, -1));
                if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, -1)))) && Surround.mc.world.getBlockState(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(-1, 1, -1));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 2, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 2, -1)))) && Surround.mc.world.getBlockState(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-1, 2, -1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(-1, 2, -1));
                }
            }
            else if (this.face.getValue() && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 1)))) {
                AttackCrystal(pos.add(-1, 1, 1));
                if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 1)))) && Surround.mc.world.getBlockState(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(-1, 1, 1));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 2, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 2, 1)))) && Surround.mc.world.getBlockState(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-1, 2, 1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(-1, 2, 1));
                }
            }
            else if (this.face2.getValue() && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 1)))) {
                AttackCrystal(pos.add(0, 1, 1));
                if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 1)))) && Surround.mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(0, 1, 1));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, 1)))) && Surround.mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 2, 1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(0, 2, 1));
                }
            }
            else if (this.face2.getValue() && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -1)))) {
                AttackCrystal(pos.add(0, 1, -1));
                if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -1)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -1)))) && Surround.mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(0, 1, -1));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(0, 2, -1)))) && Surround.mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(0, 2, -1)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(0, 2, -1));
                }
            }
            else if (this.face2.getValue() && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 0)))) {
                AttackCrystal(pos.add(1, 1, 0));
                if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 0)))) && Surround.mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(1, 1, 0));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 2, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(1, 2, 0)))) && Surround.mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(1, 2, 0));
                }
            }
            else if (this.face2.getValue() && new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 0)))) {
                AttackCrystal(pos.add(-1, 1, 0));
                if ((this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 0)))) && (this.FeetPos == null || !new BlockPos((Vec3i)this.FeetPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 0)))) && Surround.mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(-1, 1, 0));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 2, 0)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)pos.add(-1, 2, 0)))) && Surround.mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(-1, 2, 0));
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        Surround.isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
    }
    
    @Override
    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) {
            return null;
        }
        switch (this.isSafe) {
            case 0: {
                return ChatFormatting.RED + "Unsafe";
            }
            case 1: {
                return ChatFormatting.YELLOW + "Safe";
            }
            default: {
                return ChatFormatting.GREEN + "Safe";
            }
        }
    }
    
    public static void AttackCrystal(final BlockPos pos) {
        for (final Entity crystal : (List)Surround.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> Surround.mc.player.getDistance(e))).collect(Collectors.toList())) {
            if (crystal instanceof EntityEnderCrystal) {
                if (crystal.getDistanceSq(pos) > 1.0) {
                    continue;
                }
                Surround.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(crystal));
                Surround.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            }
        }
    }
    
    private Entity getEndCrystal() {
        Entity crystal = null;
        for (final Entity player : Surround.mc.world.loadedEntityList) {
            if (!(player instanceof EntityEnderCrystal)) {
                continue;
            }
            crystal = player;
        }
        return crystal;
    }
    
    private void perform(final BlockPos pos) {
        final int old = Surround.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            return;
        }
        Surround.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        Surround.mc.playerController.updateController();
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        Surround.mc.player.inventory.currentItem = old;
        Surround.mc.playerController.updateController();
    }
    
    Entity checkCrystal(final Vec3d[] list) {
        Entity crystal = null;
        for (final Vec3d vec3d : list) {
            final BlockPos position = new BlockPos(Surround.mc.player.getPositionVector()).add(vec3d.x, vec3d.y, vec3d.z);
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
    
    private EntityPlayer getTarget() {
        EntityPlayer target = null;
        double distance = 12.0;
        for (final EntityPlayer player : Surround.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, 12.0) && !Rebirth.friendManager.isFriend(player.getName())) {
                if (Surround.mc.player.posY - player.posY >= 5.0) {
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
    
    public static void breakcrystal() {
        for (final Entity crystal : (List)Surround.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> Surround.mc.player.getDistance(e))).collect(Collectors.toList())) {
            if (crystal instanceof EntityEnderCrystal) {
                if (Surround.mc.player.getDistance(crystal) > 4.0f) {
                    continue;
                }
                Surround.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(crystal));
                Surround.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.OFF_HAND));
            }
        }
    }
    
    private void doFeetPlace() {
        if (this.check()) {
            return;
        }
        if (!EntityUtil.isSafe((Entity)Surround.mc.player, 0, true)) {
            this.isSafe = 0;
            if (AntiCity.getInstance().isDisabled() || AntiCity.getInstance().Check == 0) {
                this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.player, 0, true), true, false, false);
            }
        }
        else if (!EntityUtil.isSafe((Entity)Surround.mc.player, -1, false)) {
            this.isSafe = 1;
            if (AntiCity.getInstance().isDisabled() || AntiCity.getInstance().Check == 0) {
                this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.player, -1, false), false, false, true);
            }
        }
        else {
            this.isSafe = 2;
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
    }
    
    private void processExtendingBlocks() {
        if (this.extendingBlocks.size() == 2 && this.extenders < 1) {
            final Vec3d[] array = new Vec3d[2];
            int i = 0;
            final Iterator<Vec3d> iterator = this.extendingBlocks.iterator();
            while (iterator.hasNext()) {
                final Vec3d vec3d = array[i] = iterator.next();
                ++i;
            }
            final int placementsBefore = this.placements;
            if (this.areClose(array) != null) {
                this.placeBlocks(this.areClose(array), EntityUtil.getUnsafeBlockArrayFromVec3d(this.areClose(array), 0, true), true, false, true);
            }
            if (placementsBefore < this.placements) {
                this.extendingBlocks.clear();
            }
        }
        else if (this.extendingBlocks.size() > 2 || this.extenders >= 1) {
            this.extendingBlocks.clear();
        }
    }
    
    private Vec3d areClose(final Vec3d[] vec3ds) {
        int matches = 0;
        for (final Vec3d vec3d : vec3ds) {
            for (final Vec3d pos : EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.player, 0, true)) {
                if (vec3d.equals((Object)pos)) {
                    ++matches;
                }
            }
        }
        if (matches == 2) {
            return Surround.mc.player.getPositionVector().add(vec3ds[0].add(vec3ds[1]));
        }
        return null;
    }
    
    private boolean placeBlocks(final Vec3d pos, final Vec3d[] vec3ds, final boolean hasHelpingBlocks, final boolean isHelping, final boolean isExtending) {
        boolean gotHelp = true;
        for (final Vec3d vec3d : vec3ds) {
            gotHelp = true;
            final BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            switch (BlockUtil.isPositionPlaceable(position, false)) {
                case 1: {
                    if (this.retries.get(position) == null || this.retries.get(position) < 4) {
                        this.placeBlock(position);
                        this.retries.put(position, (this.retries.get(position) == null) ? 1 : (this.retries.get(position) + 1));
                        this.retryTimer.reset();
                        break;
                    }
                    if (Rebirth.speedManager.getSpeedKpH() != 0.0 || isExtending) {
                        break;
                    }
                    if (this.extenders >= 1) {
                        break;
                    }
                    this.placeBlocks(Surround.mc.player.getPositionVector().add(vec3d), EntityUtil.getUnsafeBlockArrayFromVec3d(Surround.mc.player.getPositionVector().add(vec3d), 0, true), hasHelpingBlocks, false, true);
                    this.extendingBlocks.add(vec3d);
                    ++this.extenders;
                    break;
                }
                case 2: {
                    if (!hasHelpingBlocks) {
                        break;
                    }
                    gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
                }
                case 3: {
                    if (gotHelp) {
                        this.placeBlock(position);
                    }
                    if (!isHelping) {
                        break;
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean check() {
        if (nullCheck()) {
            return true;
        }
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        final int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
        }
        this.offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        Surround.isPlacing = false;
        this.didPlace = false;
        this.extenders = 1;
        this.placements = 0;
        obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        final int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (this.isOff()) {
            return true;
        }
        if (this.retryTimer.passedMs(2500L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (obbySlot == -1 && !this.offHand && echestSlot == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "No Obsidian in hotbar disabling...");
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (Surround.mc.player.inventory.currentItem != this.lastHotbarSlot && Surround.mc.player.inventory.currentItem != obbySlot && Surround.mc.player.inventory.currentItem != echestSlot) {
            this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        }
        if (!this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player))) {
            this.disable();
            return true;
        }
        return !this.timer.passedMs(this.delay.getValue());
    }
    
    private void placeBlock(final BlockPos pos) {
        if (this.placements < this.blocksPerTick.getValue()) {
            final int originalSlot = Surround.mc.player.inventory.currentItem;
            final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            final int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            Surround.isPlacing = true;
            Surround.mc.player.inventory.currentItem = ((obbySlot == -1) ? eChestSot : obbySlot);
            Surround.mc.playerController.updateController();
            this.isSneaking = BlockUtil.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), this.isSneaking);
            Surround.mc.player.inventory.currentItem = originalSlot;
            Surround.mc.playerController.updateController();
            this.didPlace = true;
            ++this.placements;
        }
    }
    
    static {
        Surround.isPlacing = false;
    }
}
