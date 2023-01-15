
package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraftforge.client.event.sound.*;
import dev.madcat.rebirth.features.modules.combat.*;
import net.minecraft.init.*;
import net.minecraftforge.fml.common.eventhandler.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import java.awt.*;
import dev.madcat.rebirth.util.*;

public class BreakESP extends Module
{
    private static BreakESP INSTANCE;
    BlockPos pos;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha2;
    private Setting<Integer> alpha;
    public final Timer imerS;
    double manxi;
    
    public BreakESP() {
        super("BreakESP", "Show enemy's break packet.", Module.Category.RENDER, true, false, false);
        this.red = (Setting<Integer>)this.register(new Setting("Red", 255, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 255, 0, 255));
        this.alpha2 = (Setting<Integer>)this.register(new Setting("Alpha", 100, 20, 255));
        this.alpha = (Setting<Integer>)this.register(new Setting("OutlineAlpha", 100, 20, 255));
        this.imerS = new Timer();
        this.manxi = 0.0;
        this.setInstance();
    }
    
    public static BreakESP getInstance() {
        if (BreakESP.INSTANCE == null) {
            BreakESP.INSTANCE = new BreakESP();
        }
        return BreakESP.INSTANCE;
    }
    
    private void setInstance() {
        BreakESP.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void BrokenBlock(final PlaySoundEvent event) {
        if (InstantMine.breakPos != null && InstantMine.breakPos.equals((Object)new BlockPos((double)event.getSound().getXPosF(), (double)event.getSound().getYPosF(), (double)event.getSound().getZPosF()))) {
            return;
        }
        if (!event.getName().endsWith("hit")) {
            return;
        }
        if (event.getName().endsWith("arrow.hit")) {
            return;
        }
        if (event.getName().endsWith("stand.hit")) {
            return;
        }
        if (this.pos != null && this.pos.equals((Object)new BlockPos((double)event.getSound().getXPosF(), (double)event.getSound().getYPosF(), (double)event.getSound().getZPosF()))) {
            return;
        }
        if (BreakESP.mc.world.getBlockState(new BlockPos((double)event.getSound().getXPosF(), (double)event.getSound().getYPosF(), (double)event.getSound().getZPosF())).getBlock() == Blocks.BEDROCK) {
            return;
        }
        this.pos = new BlockPos((double)event.getSound().getXPosF(), (double)event.getSound().getYPosF(), (double)event.getSound().getZPosF());
        this.manxi = 0.0;
    }
    
    @SubscribeEvent
    public void BrokenBlock2(final PlaySoundEvent event) {
        if (InstantMine.breakPos != null && InstantMine.breakPos.equals((Object)new BlockPos((double)event.getSound().getXPosF(), (double)event.getSound().getYPosF(), (double)event.getSound().getZPosF()))) {
            return;
        }
        if (!event.getName().endsWith("break")) {
            return;
        }
        if (event.getName().endsWith("potion.break")) {
            return;
        }
        if (this.pos != null && this.pos.equals((Object)new BlockPos((double)event.getSound().getXPosF(), (double)event.getSound().getYPosF(), (double)event.getSound().getZPosF()))) {
            return;
        }
        if (BreakESP.mc.world.getBlockState(new BlockPos((double)event.getSound().getXPosF(), (double)event.getSound().getYPosF(), (double)event.getSound().getZPosF())).getBlock() == Blocks.BEDROCK) {
            return;
        }
        this.pos = new BlockPos((double)event.getSound().getXPosF(), (double)event.getSound().getYPosF(), (double)event.getSound().getZPosF());
        this.manxi = 0.0;
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (this.pos != null) {
            if (this.imerS.passedMs(10L)) {
                if (this.manxi <= 10.0) {
                    this.manxi += 0.11;
                }
                this.imerS.reset();
            }
            final AxisAlignedBB axisAlignedBB = BreakESP.mc.world.getBlockState(this.pos).getSelectedBoundingBox((World)BreakESP.mc.world, this.pos);
            final double centerX = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0;
            final double centerY = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0;
            final double centerZ = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0;
            final double progressValX = this.manxi * ((axisAlignedBB.maxX - centerX) / 10.0);
            final double progressValY = this.manxi * ((axisAlignedBB.maxY - centerY) / 10.0);
            final double progressValZ = this.manxi * ((axisAlignedBB.maxZ - centerZ) / 10.0);
            final AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
            RenderUtil.drawBBBox(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.alpha.getValue());
            RenderUtil.drawBBFill(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha2.getValue()), this.alpha2.getValue());
        }
    }
}
