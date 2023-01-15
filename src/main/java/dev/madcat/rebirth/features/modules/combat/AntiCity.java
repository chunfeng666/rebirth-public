
package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.init.*;
import dev.madcat.rebirth.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.item.*;
import java.util.*;
import java.util.stream.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.block.state.*;

public class AntiCity extends Module
{
    private static AntiCity INSTANCE;
    public EntityPlayer target;
    public Entity crystal;
    int Check;
    BlockPos CrystalPos;
    BlockPos feet;
    BlockPos pos;
    int obsidian;
    BlockPos startPos;
    int checke;
    public Setting<Boolean> rotate;
    public Setting<Boolean> packet;
    public Setting<Boolean> old;
    public Setting<Boolean> ac2;
    public Setting<Boolean> ac;
    public Setting<Boolean> sm;
    boolean isSneaking;
    
    public AntiCity() {
        super("AntiCity", "Very mart surround extend.", Category.COMBAT, true, false, false);
        this.Check = 0;
        this.obsidian = -1;
        this.checke = 0;
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", true));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", true));
        this.old = (Setting<Boolean>)this.register(new Setting("OldMode", false));
        this.ac2 = (Setting<Boolean>)this.register(new Setting("SurroundExtend", true, v -> this.old.getValue()));
        this.ac = (Setting<Boolean>)this.register(new Setting("SurroundExtend+", true, v -> this.ac2.getValue() && this.old.getValue()));
        this.sm = (Setting<Boolean>)this.register(new Setting("Smart", true, v -> this.old.getValue()));
        this.setInstance();
    }
    
    public static AntiCity getInstance() {
        if (AntiCity.INSTANCE != null) {
            return AntiCity.INSTANCE;
        }
        return AntiCity.INSTANCE = new AntiCity();
    }
    
    @Override
    public void onTick() {
        if (!this.old.getValue()) {
            return;
        }
        if (this.startPos == null && this.checke == 1) {
            this.checke = 0;
            return;
        }
        if (this.checke == 1 && !this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)AntiCity.mc.player))) {
            this.checke = 0;
        }
        final BlockPos pos = new BlockPos(AntiCity.mc.player.posX, AntiCity.mc.player.posY, AntiCity.mc.player.posZ);
        if (!this.sm.getValue()) {
            return;
        }
        if ((this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN | this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) && (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN | this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK) && (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN | this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK) && (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN | this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK)) {
            if (Rebirth.moduleManager.isModuleEnabled("Surround")) {
                Rebirth.moduleManager.disableModule("Surround");
            }
            if (this.checke == 1) {
                return;
            }
            this.checke = 1;
            this.startPos = new BlockPos(AntiCity.mc.player.posX, AntiCity.mc.player.posY, AntiCity.mc.player.posZ);
        }
    }
    
    private void setInstance() {
        AntiCity.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        this.checke = 0;
    }
    
    @Override
    public void onDisable() {
        this.checke = 0;
        this.Check = 0;
    }
    
    @Override
    public void onUpdate() {
        if (!this.old.getValue()) {
            this.pos = new BlockPos(AntiCity.mc.player.posX, AntiCity.mc.player.posY, AntiCity.mc.player.posZ);
            if (!Rebirth.moduleManager.isModuleEnabled("Surround")) {
                this.Check = 0;
                return;
            }
            if ((this.getBlock(this.pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN | this.getBlock(this.pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) && (this.getBlock(this.pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN | this.getBlock(this.pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK) && (this.getBlock(this.pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN | this.getBlock(this.pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK) && (this.getBlock(this.pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN | this.getBlock(this.pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK)) {
                this.Check = 1;
            }
            if (this.Check == 0) {
                return;
            }
            this.target = this.getTarget();
            if (this.target == null) {
                return;
            }
            this.feet = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
            if (InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN) == -1) {
                return;
            }
            this.perform(this.pos.add(0, 0, 1));
            this.perform(this.pos.add(0, 0, -1));
            this.perform(this.pos.add(1, 0, 0));
            this.perform(this.pos.add(-1, 0, 0));
            this.perform(this.pos.add(1, -1, 0));
            this.perform(this.pos.add(-1, -1, 0));
            this.perform(this.pos.add(0, -1, 1));
            this.perform(this.pos.add(0, -1, -1));
            this.perform(this.pos.add(0, 0, 2));
            this.perform(this.pos.add(0, 0, -2));
            this.perform(this.pos.add(2, 0, 0));
            this.perform(this.pos.add(-2, 0, 0));
            this.perform(this.pos.add(1, 0, 1));
            this.perform(this.pos.add(-1, 0, 1));
            this.perform(this.pos.add(1, 0, -1));
            this.perform(this.pos.add(-1, 0, -1));
            if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 0, 0)))) {
                this.perform(this.pos.add(1, 1, 0));
            }
            if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 0, 0)))) {
                this.perform(this.pos.add(-1, 1, 0));
            }
            if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(0, 0, 1)))) {
                this.perform(this.pos.add(0, 1, 1));
            }
            if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(0, 0, -1)))) {
                this.perform(this.pos.add(0, 1, -1));
            }
            if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 0, 1)))) {
                this.perform(this.pos.add(1, 1, 1));
            }
            if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 0, -1)))) {
                this.perform(this.pos.add(1, 1, -1));
            }
            if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 0, 1)))) {
                this.perform(this.pos.add(-1, 1, 1));
            }
            if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 0, -1)))) {
                this.perform(this.pos.add(-1, 1, -1));
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 0, 0)))) {
                this.perform(this.pos.add(1, 1, 0));
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 0, 0)))) {
                this.perform(this.pos.add(-1, 1, 0));
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(0, 0, 1)))) {
                this.perform(this.pos.add(0, 1, 1));
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(0, 0, -1)))) {
                this.perform(this.pos.add(0, 1, -1));
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 0, 1)))) {
                this.perform(this.pos.add(1, 1, 1));
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 0, -1)))) {
                this.perform(this.pos.add(1, 1, -1));
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 0, 1)))) {
                this.perform(this.pos.add(-1, 1, 1));
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 0, -1)))) {
                this.perform(this.pos.add(-1, 1, -1));
            }
            if (new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(1, -1, 0)))) {
                this.perform(this.pos.add(2, 0, 0));
            }
            this.perform(this.pos.add(2, 1, 0));
            this.perform(this.pos.add(3, 0, 0));
            if (new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, -1, 0)))) {
                this.perform(this.pos.add(-2, 0, 0));
            }
            this.perform(this.pos.add(-2, 1, 0));
            this.perform(this.pos.add(-3, 0, 0));
            if (new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(0, -1, 1)))) {
                this.perform(this.pos.add(0, 0, 2));
            }
            this.perform(this.pos.add(0, 1, 2));
            this.perform(this.pos.add(0, 0, 3));
            if (new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(0, -1, -1)))) {
                this.perform(this.pos.add(0, 0, -2));
            }
            this.perform(this.pos.add(0, 1, -2));
            this.perform(this.pos.add(0, 0, -3));
            if (new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(2, -1, 0)))) {
                this.perform(this.pos.add(1, 0, 0));
            }
            this.perform(this.pos.add(2, 1, 0));
            this.perform(this.pos.add(3, 0, 0));
            if (new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(-2, -1, 0)))) {
                this.perform(this.pos.add(-1, 0, 0));
            }
            this.perform(this.pos.add(-2, 1, 0));
            this.perform(this.pos.add(-3, 0, 0));
            if (new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(0, -1, 2)))) {
                this.perform(this.pos.add(0, 0, 1));
            }
            this.perform(this.pos.add(0, 1, 2));
            this.perform(this.pos.add(0, 0, 3));
            if (new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(0, -1, -2)))) {
                this.perform(this.pos.add(0, 0, -1));
            }
            this.perform(this.pos.add(0, 1, -2));
            this.perform(this.pos.add(0, 0, -3));
            this.crystal = this.getEndCrystal();
            if (this.crystal == null) {
                return;
            }
            this.CrystalPos = new BlockPos(this.crystal.posX, this.crystal.posY, this.crystal.posZ);
            if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 1, 1)))) {
                AttackCrystal(this.pos.add(1, 1, 1));
                if ((this.feet == null || !new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 1, 1)))) && (this.feet == null || !new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 1, 1)))) && AntiCity.mc.world.getBlockState(this.pos.add(1, 1, 1)).getBlock() == Blocks.AIR) {
                    this.perform(this.pos.add(1, 1, 1));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 2, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 2, 1)))) && AntiCity.mc.world.getBlockState(this.pos.add(1, 1, 1)).getBlock() == Blocks.AIR && AntiCity.mc.world.getBlockState(this.pos.add(1, 2, 1)).getBlock() == Blocks.AIR) {
                    this.perform(this.pos.add(1, 2, 1));
                }
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 1, -1)))) {
                AttackCrystal(this.pos.add(1, 1, -1));
                if ((this.feet == null || !new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 1, -1)))) && (this.feet == null || !new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 1, -1)))) && AntiCity.mc.world.getBlockState(this.pos.add(1, 1, -1)).getBlock() == Blocks.AIR) {
                    this.perform(this.pos.add(1, 1, -1));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 2, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 2, -1)))) && AntiCity.mc.world.getBlockState(this.pos.add(1, 1, -1)).getBlock() == Blocks.AIR && AntiCity.mc.world.getBlockState(this.pos.add(1, 2, -1)).getBlock() == Blocks.AIR) {
                    this.perform(this.pos.add(1, 2, -1));
                }
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 1, -1)))) {
                AttackCrystal(this.pos.add(-1, 1, -1));
                if ((this.feet == null || !new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 1, -1)))) && (this.feet == null || !new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 0, -1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 1, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 1, -1)))) && AntiCity.mc.world.getBlockState(this.pos.add(-1, 1, -1)).getBlock() == Blocks.AIR) {
                    this.perform(this.pos.add(-1, 1, -1));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 2, -1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 2, -1)))) && AntiCity.mc.world.getBlockState(this.pos.add(-1, 1, -1)).getBlock() == Blocks.AIR && AntiCity.mc.world.getBlockState(this.pos.add(-1, 2, -1)).getBlock() == Blocks.AIR) {
                    this.perform(this.pos.add(-1, 2, -1));
                }
            }
            else if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 1, 1)))) {
                AttackCrystal(this.pos.add(-1, 1, 1));
                if ((this.feet == null || !new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 1, 1)))) && (this.feet == null || !new BlockPos((Vec3i)this.feet).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 0, 1)))) && (BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 1, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 1, 1)))) && AntiCity.mc.world.getBlockState(this.pos.add(-1, 1, 1)).getBlock() == Blocks.AIR) {
                    this.perform(this.pos.add(-1, 1, 1));
                }
                else if ((BreakCheck.Instance().BrokenPos == null || !new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 2, 1)))) && (InstantMine.breakPos == null || !new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 2, 1)))) && AntiCity.mc.world.getBlockState(this.pos.add(-1, 1, 1)).getBlock() == Blocks.AIR && AntiCity.mc.world.getBlockState(this.pos.add(-1, 2, 1)).getBlock() == Blocks.AIR) {
                    this.perform(this.pos.add(-1, 2, 1));
                }
            }
            if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)this.pos.add(0, 1, 1)))) {
                AttackCrystal(this.pos.add(0, 1, 1));
                this.perform(this.pos.add(0, 1, 1));
                if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(this.pos.add(1, 1, 1)).getBlock() != Blocks.AIR) {
                        this.perform(this.pos.add(1, 2, 1));
                    }
                    else if (this.getBlock(this.pos.add(1, 0, 1)).getBlock() != Blocks.AIR) {
                        this.perform(this.pos.add(1, 1, 1));
                    }
                }
                if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(0, 1, 1)))) {
                    this.perform(this.pos.add(0, 2, 1));
                }
                if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(0, 1, 1)))) {
                    this.perform(this.pos.add(0, 2, 1));
                }
            }
            if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)this.pos.add(0, 1, -1)))) {
                AttackCrystal(this.pos.add(0, 1, -1));
                this.perform(this.pos.add(0, 1, -1));
                if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(this.pos.add(-1, 1, -1)).getBlock() != Blocks.AIR) {
                        this.perform(this.pos.add(-1, 2, -1));
                    }
                    else if (this.getBlock(this.pos.add(-1, 0, -1)).getBlock() != Blocks.AIR) {
                        this.perform(this.pos.add(-1, 1, -1));
                    }
                }
                if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(0, 1, -1)))) {
                    this.perform(this.pos.add(0, 2, -1));
                }
                if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(0, 1, -1)))) {
                    this.perform(this.pos.add(0, 2, -1));
                }
            }
            if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 1, 0)))) {
                AttackCrystal(this.pos.add(1, 1, 0));
                this.perform(this.pos.add(1, 1, 0));
                if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(this.pos.add(1, 1, 1)).getBlock() != Blocks.AIR) {
                        this.perform(this.pos.add(1, 2, 1));
                    }
                    else if (this.getBlock(this.pos.add(1, 0, 1)).getBlock() != Blocks.AIR) {
                        this.perform(this.pos.add(1, 1, 1));
                    }
                }
                if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 1, 0)))) {
                    this.perform(this.pos.add(1, 2, 0));
                }
                if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(1, 1, 0)))) {
                    this.perform(this.pos.add(1, 2, 0));
                }
            }
            if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 1, 0)))) {
                AttackCrystal(this.pos.add(-1, 1, 0));
                this.perform(this.pos.add(-1, 1, 0));
                if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
                    if (this.getBlock(this.pos.add(-1, 1, -1)).getBlock() != Blocks.AIR) {
                        this.perform(this.pos.add(-1, 2, -1));
                    }
                    else if (this.getBlock(this.pos.add(-1, 0, -1)).getBlock() != Blocks.AIR) {
                        this.perform(this.pos.add(-1, 1, -1));
                    }
                }
                if (InstantMine.breakPos != null && new BlockPos((Vec3i)InstantMine.breakPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 1, 0)))) {
                    this.perform(this.pos.add(-1, 2, 0));
                }
                if (BreakCheck.Instance().BrokenPos != null && new BlockPos((Vec3i)BreakCheck.Instance().BrokenPos).equals((Object)new BlockPos((Vec3i)this.pos.add(-1, 1, 0)))) {
                    this.perform(this.pos.add(-1, 2, 0));
                }
            }
        }
        else {
            final Vec3d a = AntiCity.mc.player.getPositionVector();
            this.obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
            this.target = this.getTarget();
            if (this.target == null) {
                return;
            }
            final BlockPos feet = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
            final BlockPos pos = new BlockPos(AntiCity.mc.player.posX, AntiCity.mc.player.posY, AntiCity.mc.player.posZ);
            if (this.obsidian == -1) {
                return;
            }
            if (this.ac2.getValue() && Rebirth.moduleManager.isModuleEnabled("Surround")) {
                if (this.getBlock(pos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                    this.perform(pos.add(0, 0, -1));
                    this.perform(pos.add(0, 0, -2));
                    this.perform(pos.add(1, 0, -1));
                    this.perform(pos.add(-1, 0, -1));
                    if (this.ac.getValue()) {
                        this.perform(pos.add(0, 1, -2));
                        this.perform(pos.add(0, 1, -1));
                    }
                }
                if (this.getBlock(pos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                    this.perform(pos.add(0, 0, 1));
                    this.perform(pos.add(0, 0, 2));
                    this.perform(pos.add(1, 0, 1));
                    this.perform(pos.add(-1, 0, 1));
                    if (this.ac.getValue()) {
                        this.perform(pos.add(0, 1, 2));
                        this.perform(pos.add(0, 1, 1));
                    }
                }
                if (this.getBlock(pos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.perform(pos.add(1, 0, 0));
                    this.perform(pos.add(2, 0, 0));
                    this.perform(pos.add(1, 0, 1));
                    this.perform(pos.add(1, 0, -1));
                    if (this.ac.getValue()) {
                        this.perform(pos.add(2, 1, 0));
                        this.perform(pos.add(1, 1, 0));
                    }
                }
                if (this.getBlock(pos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.perform(pos.add(-1, 0, 0));
                    this.perform(pos.add(-2, 0, 0));
                    this.perform(pos.add(-1, 0, 1));
                    this.perform(pos.add(-1, 0, -1));
                    if (this.ac.getValue()) {
                        this.perform(pos.add(-2, 1, 0));
                        this.perform(pos.add(-1, 1, 0));
                    }
                }
            }
            if (this.checke == 0) {
                return;
            }
            if (!this.sm.getValue()) {
                return;
            }
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 1)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 2)) != null) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 1)), true);
                this.place(a, EntityUtil.getVarOffsets(0, 1, 1));
                this.place(a, EntityUtil.getVarOffsets(0, 1, 2));
            }
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -1)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -2)) != null) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -1)), true);
                this.place(a, EntityUtil.getVarOffsets(0, 1, -1));
                this.place(a, EntityUtil.getVarOffsets(0, 1, -2));
            }
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 1, 0)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(2, 1, 0)) != null) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 1, 0)), true);
                this.place(a, EntityUtil.getVarOffsets(1, 1, 0));
                this.place(a, EntityUtil.getVarOffsets(2, 1, 0));
            }
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 1, 0)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 1, 0)) != null) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 1, 0)), true);
                this.place(a, EntityUtil.getVarOffsets(-1, 1, 0));
                this.place(a, EntityUtil.getVarOffsets(-2, 1, 0));
            }
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)) != null) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)), true);
                this.place(a, EntityUtil.getVarOffsets(0, 0, 1));
            }
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)) != null) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)), true);
                this.place(a, EntityUtil.getVarOffsets(0, 0, -1));
            }
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)) != null) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)), true);
                this.place(a, EntityUtil.getVarOffsets(1, 0, 0));
            }
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)) != null) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)), true);
                this.place(a, EntityUtil.getVarOffsets(-1, 0, 0));
            }
            if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(2, 1, 0)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(2, 1, 0)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(2, 1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 0)))) {
                    this.perform(pos.add(2, 1, 0));
                }
            }
            if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 1, 0)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 1, 0)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-2, 1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 0)))) {
                    this.perform(pos.add(-2, 1, 0));
                }
            }
            if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 2)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 2)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 2))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 2)))) {
                    this.perform(pos.add(0, 1, 2));
                }
            }
            if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -2)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -2)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -2))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -2)))) {
                    this.perform(pos.add(0, 1, -2));
                }
            }
            if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, -1, 1)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -2, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1)))) {
                    this.perform(pos.add(0, -1, 1));
                    this.perform(pos.add(0, 0, 1));
                }
            }
            if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, -1, -1)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -2, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1)))) {
                    this.perform(pos.add(0, -1, -1));
                    this.perform(pos.add(0, 0, -1));
                }
            }
            if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, -1, 0)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -2, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0)))) {
                    this.perform(pos.add(1, -1, 0));
                    this.perform(pos.add(1, 0, 0));
                }
            }
            if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, -1, 0)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -2, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0)))) {
                    this.perform(pos.add(-1, -1, 0));
                    this.perform(pos.add(-1, 0, 0));
                }
            }
            if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, 2)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 2))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 2)))) {
                    if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)) == null) {
                        this.perform(pos.add(0, 0, 1));
                        this.perform(pos.add(0, 0, 2));
                        this.perform(pos.add(0, -1, 2));
                        this.perform(pos.add(0, 0, 3));
                    }
                    else if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)) != null) {
                        EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)), true);
                        this.perform(pos.add(0, 0, 1));
                        this.perform(pos.add(0, 0, 2));
                        this.perform(pos.add(0, -1, 2));
                        this.perform(pos.add(0, 0, 3));
                    }
                }
            }
            if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, -2)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -2))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -2)))) {
                    if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)) == null) {
                        this.perform(pos.add(0, 0, -1));
                        this.perform(pos.add(0, 0, -2));
                        this.perform(pos.add(0, -1, -2));
                        this.perform(pos.add(0, 0, -3));
                    }
                    else if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)) != null) {
                        EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)), true);
                        this.perform(pos.add(0, 0, -1));
                        this.perform(pos.add(0, 0, -2));
                        this.perform(pos.add(0, -1, -2));
                        this.perform(pos.add(0, 0, -3));
                    }
                }
            }
            if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(2, 0, 0)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(2, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(2, -1, 0)))) {
                    if (this.checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)) == null) {
                        this.perform(pos.add(1, 0, 0));
                        this.perform(pos.add(2, 0, 0));
                        this.perform(pos.add(2, -1, 0));
                        this.perform(pos.add(3, 0, 0));
                    }
                    else if (this.checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)) != null) {
                        EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)), true);
                        this.perform(pos.add(1, 0, 0));
                        this.perform(pos.add(2, 0, 0));
                        this.perform(pos.add(2, -1, 0));
                        this.perform(pos.add(3, 0, 0));
                    }
                }
            }
            if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-2, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-2, -1, 0)))) {
                    if (this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)) == null) {
                        this.perform(pos.add(-1, 0, 0));
                        this.perform(pos.add(-2, 0, 0));
                        this.perform(pos.add(-2, -1, 0));
                        this.perform(pos.add(-3, 0, 0));
                    }
                    else if (this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)) != null && this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)) != null) {
                        EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)), true);
                        this.perform(pos.add(-1, 0, 0));
                        this.perform(pos.add(-2, 0, 0));
                        this.perform(pos.add(-2, -1, 0));
                        this.perform(pos.add(-3, 0, 0));
                    }
                }
            }
            if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1)))) {
                    if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1)))) {
                        this.perform(pos.add(0, 0, 1));
                        this.perform(pos.add(0, 1, 1));
                        this.perform(pos.add(1, 1, 1));
                        this.perform(pos.add(0, 1, 2));
                    }
                }
                else if (this.getBlock(pos.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(0, 0, 2));
                    this.perform(pos.add(0, 1, 2));
                    this.perform(pos.add(1, 0, 2));
                    this.perform(pos.add(1, 1, 2));
                }
            }
            if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR) {
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1)))) {
                    if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1)))) {
                        this.perform(pos.add(0, 0, -1));
                        this.perform(pos.add(0, 1, -1));
                        this.perform(pos.add(-1, 1, -1));
                        this.perform(pos.add(0, 1, -2));
                    }
                }
                else if (this.getBlock(pos.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(0, 0, -2));
                    this.perform(pos.add(0, 1, -2));
                    this.perform(pos.add(1, 0, -2));
                    this.perform(pos.add(1, 1, -2));
                }
            }
            if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR) {
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0)))) {
                    if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0)))) {
                        this.perform(pos.add(1, 0, 0));
                        this.perform(pos.add(1, 1, 0));
                        this.perform(pos.add(1, 1, 1));
                        this.perform(pos.add(2, 1, 0));
                    }
                }
                else if (this.getBlock(pos.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(2, 0, 0));
                    this.perform(pos.add(2, 1, 0));
                    this.perform(pos.add(2, 0, 1));
                    this.perform(pos.add(2, 1, 1));
                }
            }
            if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0)))) {
                    if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0)))) {
                        this.perform(pos.add(-1, 0, 0));
                        this.perform(pos.add(-1, 1, 0));
                        this.perform(pos.add(-1, 1, -1));
                        this.perform(pos.add(-2, 1, 0));
                    }
                }
                else if (this.getBlock(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                    this.perform(pos.add(-2, 0, 0));
                    this.perform(pos.add(-2, 1, 0));
                    this.perform(pos.add(-2, 0, 1));
                    this.perform(pos.add(-2, 1, 1));
                }
            }
            if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 1)))) {
                    this.perform(pos.add(1, 0, 0));
                    this.perform(pos.add(1, 0, 1));
                }
            }
            if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 1)))) {
                    this.perform(pos.add(0, 0, 1));
                    this.perform(pos.add(1, 0, 1));
                }
            }
            if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -1)))) {
                    this.perform(pos.add(-1, 0, 0));
                    this.perform(pos.add(-1, 0, -1));
                }
            }
            if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -1)))) {
                    this.perform(pos.add(0, 0, -1));
                    this.perform(pos.add(-1, 0, -1));
                }
            }
            if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 1)))) {
                    this.perform(pos.add(-1, 0, 0));
                    this.perform(pos.add(-1, 0, 1));
                }
            }
            if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 1)))) {
                    this.perform(pos.add(0, 0, 1));
                    this.perform(pos.add(-1, 0, 1));
                }
            }
            if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -1)))) {
                    this.perform(pos.add(1, 0, 0));
                    this.perform(pos.add(1, 0, -1));
                }
            }
            if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR) {
                if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)) != null) {
                    EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)), true);
                }
                if (!new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -1)))) {
                    this.perform(pos.add(0, 0, -1));
                    this.perform(pos.add(1, 0, -1));
                }
            }
            if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.AIR && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 1)))) {
                this.perform(pos.add(1, 0, 0));
                this.perform(pos.add(1, 0, 1));
                this.perform(pos.add(1, 1, 1));
            }
            if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.AIR && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 1)))) {
                this.perform(pos.add(0, 0, 1));
                this.perform(pos.add(1, 0, 1));
                this.perform(pos.add(1, 1, 1));
            }
            if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -1)))) {
                this.perform(pos.add(-1, 0, 0));
                this.perform(pos.add(-1, 0, -1));
                this.perform(pos.add(-1, 1, -1));
            }
            if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, -1)))) {
                this.perform(pos.add(0, 0, -1));
                this.perform(pos.add(-1, 0, -1));
                this.perform(pos.add(-1, 1, -1));
            }
            if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 1)))) {
                this.perform(pos.add(-1, 0, 0));
                this.perform(pos.add(-1, 0, 1));
                this.perform(pos.add(-1, 1, 1));
            }
            if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, -1, 1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(-1, 0, 1)))) {
                this.perform(pos.add(0, 0, 1));
                this.perform(pos.add(-1, 0, 1));
                this.perform(pos.add(-1, 1, 1));
            }
            if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.AIR && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, 0))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -1)))) {
                this.perform(pos.add(1, 0, 0));
                this.perform(pos.add(1, 0, -1));
                this.perform(pos.add(1, 1, -1));
            }
            if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.AIR && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, 0, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(0, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, -1, -1))) && !new BlockPos((Vec3i)feet).equals((Object)new BlockPos((Vec3i)pos.add(1, 0, -1)))) {
                this.perform(pos.add(0, 0, -1));
                this.perform(pos.add(1, 0, -1));
                this.perform(pos.add(1, 1, -1));
            }
        }
    }
    
    private void perform(final BlockPos pos2) {
        final int old = AntiCity.mc.player.inventory.currentItem;
        if (AntiCity.mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
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
            AntiCity.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            AntiCity.mc.playerController.updateController();
            BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            AntiCity.mc.player.inventory.currentItem = old;
            AntiCity.mc.playerController.updateController();
        }
        this.crystal = this.getEndCrystal();
        if (this.crystal == null) {
            return;
        }
        this.CrystalPos = new BlockPos(this.crystal.posX, this.crystal.posY, this.crystal.posZ);
        if (new BlockPos((Vec3i)this.CrystalPos).equals((Object)new BlockPos((Vec3i)pos2))) {
            AttackCrystal(pos2);
        }
    }
    
    Entity checkCrystal(final Vec3d pos, final Vec3d[] list) {
        Entity crystal = null;
        final Vec3d[] var4 = list;
        for (int var5 = list.length, var6 = 0; var6 < var5; ++var6) {
            final Vec3d vec3d = var4[var6];
            final BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            for (final Entity entity : AntiCity.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(position))) {
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
    
    private void place(final Vec3d pos, final Vec3d[] list) {
        final Vec3d[] var3 = list;
        for (int var4 = list.length, var5 = 0; var5 < var4; ++var5) {
            final Vec3d vec3d = var3[var5];
            final BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            final int a = AntiCity.mc.player.inventory.currentItem;
            AntiCity.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            AntiCity.mc.playerController.updateController();
            this.isSneaking = BlockUtil.placeBlock(position, EnumHand.MAIN_HAND, false, this.packet.getValue(), true);
            AntiCity.mc.player.inventory.currentItem = a;
            AntiCity.mc.playerController.updateController();
        }
    }
    
    public static void AttackCrystal(final BlockPos pos) {
        for (final Entity crystal : (List)AntiCity.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> AntiCity.mc.player.getDistance(e))).collect(Collectors.toList())) {
            if (crystal instanceof EntityEnderCrystal) {
                if (crystal.getDistanceSq(pos) > 1.0) {
                    continue;
                }
                AntiCity.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(crystal));
                AntiCity.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            }
        }
    }
    
    private Entity getEndCrystal() {
        Entity crystal = null;
        for (final Entity player : AntiCity.mc.world.loadedEntityList) {
            if (!(player instanceof EntityEnderCrystal)) {
                continue;
            }
            crystal = player;
        }
        return crystal;
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return AntiCity.mc.world.getBlockState(block);
    }
    
    private EntityPlayer getTarget() {
        EntityPlayer target = null;
        double distance = 12.0;
        for (final EntityPlayer player : AntiCity.mc.world.playerEntities) {
            if (!EntityUtil.isntValid((Entity)player, 12.0) && !Rebirth.friendManager.isFriend(player.getName())) {
                if (AntiCity.mc.player.posY - player.posY >= 5.0) {
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
    
    static {
        AntiCity.INSTANCE = new AntiCity();
    }
}
