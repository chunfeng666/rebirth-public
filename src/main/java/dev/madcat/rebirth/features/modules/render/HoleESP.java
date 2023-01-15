

package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import dev.madcat.rebirth.features.modules.client.*;
import java.awt.*;
import dev.madcat.rebirth.util.*;

public class HoleESP extends Module
{
    public Setting<Boolean> renderOwn;
    public Setting<Boolean> fov;
    public Setting<Boolean> rainbow;
    private final Setting<Integer> range;
    private final Setting<Integer> rangeY;
    public Setting<Boolean> box;
    public Setting<Boolean> boxs;
    public Setting<Boolean> gradientBox;
    public Setting<Boolean> invertGradientBox;
    public Setting<Boolean> outline;
    public Setting<Boolean> gradientOutline;
    public Setting<Boolean> invertGradientOutline;
    public Setting<Double> height;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    private Setting<Integer> boxAlpha;
    private Setting<Float> lineWidth;
    public Setting<Boolean> safeColor;
    private Setting<Integer> safeRed;
    private Setting<Integer> safeGreen;
    private Setting<Integer> safeBlue;
    private Setting<Integer> safeAlpha;
    public Setting<Boolean> customOutline;
    private Setting<Integer> cRed;
    private Setting<Integer> cGreen;
    private Setting<Integer> cBlue;
    private Setting<Integer> cAlpha;
    private Setting<Integer> safecRed;
    private Setting<Integer> safecGreen;
    private Setting<Integer> safecBlue;
    private Setting<Integer> safecAlpha;
    private static HoleESP INSTANCE;
    private int currentAlpha;
    
    public HoleESP() {
        super("HoleESP", "Shows safe spots.", Module.Category.RENDER, false, false, false);
        this.renderOwn = (Setting<Boolean>)this.register(new Setting("RenderOwn", true));
        this.fov = (Setting<Boolean>)this.register(new Setting("InFov", true));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", false));
        this.range = (Setting<Integer>)this.register(new Setting("RangeX", 0, 0, 10));
        this.rangeY = (Setting<Integer>)this.register(new Setting("RangeY", 0, 0, 10));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", true));
        this.boxs = (Setting<Boolean>)this.register(new Setting("Double Hole", true));
        this.gradientBox = (Setting<Boolean>)this.register(new Setting("Gradient", false, v -> this.box.getValue()));
        this.invertGradientBox = (Setting<Boolean>)this.register(new Setting("ReverseGradient", false, v -> this.gradientBox.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", true));
        this.gradientOutline = (Setting<Boolean>)this.register(new Setting("GradientOutline", false, v -> this.outline.getValue()));
        this.invertGradientOutline = (Setting<Boolean>)this.register(new Setting("ReverseOutline", false, v -> this.gradientOutline.getValue()));
        this.height = (Setting<Double>)this.register(new Setting("Height", 0.0, (-2.0), 2.0));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 0, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 0, 0, 255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 255, 0, 255));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 125, 0, 255, v -> this.box.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.0f, 0.1f, 5.0f, v -> this.outline.getValue()));
        this.safeColor = (Setting<Boolean>)this.register(new Setting("BedrockColor", false));
        this.safeRed = (Setting<Integer>)this.register(new Setting("BedrockRed", 0, 0, 255, v -> this.safeColor.getValue()));
        this.safeGreen = (Setting<Integer>)this.register(new Setting("BedrockGreen", 255, 0, 255, v -> this.safeColor.getValue()));
        this.safeBlue = (Setting<Integer>)this.register(new Setting("BedrockBlue", 0, 0, 255, v -> this.safeColor.getValue()));
        this.safeAlpha = (Setting<Integer>)this.register(new Setting("BedrockAlpha", 255, 0, 255, v -> this.safeColor.getValue()));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("CustomLine", false, v -> this.outline.getValue()));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", 0, 0, 255, v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", 0, 0, 255, v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", 255, 0, 255, v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", 255, 0, 255, v -> this.customOutline.getValue() && this.outline.getValue()));
        this.safecRed = (Setting<Integer>)this.register(new Setting("OL-SafeRed", 0, 0, 255, v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
        this.safecGreen = (Setting<Integer>)this.register(new Setting("OL-SafeGreen", 255, 0, 255, v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
        this.safecBlue = (Setting<Integer>)this.register(new Setting("OL-SafeBlue", 0, 0, 255, v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
        this.safecAlpha = (Setting<Integer>)this.register(new Setting("OL-SafeAlpha", 255, 0, 255, v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
        this.currentAlpha = 0;
        this.setInstance();
    }
    
    private void setInstance() {
        HoleESP.INSTANCE = this;
    }
    
    public static HoleESP getInstance() {
        if (HoleESP.INSTANCE == null) {
            HoleESP.INSTANCE = new HoleESP();
        }
        return HoleESP.INSTANCE;
    }
    
    public void onRender3D(final Render3DEvent event) {
        assert HoleESP.mc.renderViewEntity != null;
        final Vec3i playerPos = new Vec3i(HoleESP.mc.renderViewEntity.posX, HoleESP.mc.renderViewEntity.posY, HoleESP.mc.renderViewEntity.posZ);
        for (int x = playerPos.getX() - this.range.getValue(); x < playerPos.getX() + this.range.getValue(); ++x) {
            for (int z = playerPos.getZ() - this.range.getValue(); z < playerPos.getZ() + this.range.getValue(); ++z) {
                for (int y = playerPos.getY() + this.rangeY.getValue(); y > playerPos.getY() - this.rangeY.getValue(); --y) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (this.boxs.getValue()) {
                        if (HoleUtil.is2securityHole(pos) && (HoleESP.mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock() == Blocks.AIR || HoleESP.mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(), HoleUtil.is2Hole(pos).getY() + 1, HoleUtil.is2Hole(pos).getZ())).getBlock() == Blocks.AIR) && HoleESP.mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(), HoleUtil.is2Hole(pos).getY() - 1, HoleUtil.is2Hole(pos).getZ())).getBlock() == Blocks.BEDROCK) {
                            RenderUtil.drawBoxESP(pos, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.safeRed.getValue(), this.safeGreen.getValue(), this.safeBlue.getValue(), this.safeAlpha.getValue()), this.customOutline.getValue(), new Color(this.safecRed.getValue(), this.safecGreen.getValue(), this.safecBlue.getValue(), this.safecAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                            RenderUtil.drawBoxESP(HoleUtil.is2Hole(pos), ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.safeRed.getValue(), this.safeGreen.getValue(), this.safeBlue.getValue(), this.safeAlpha.getValue()), this.customOutline.getValue(), new Color(this.safecRed.getValue(), this.safecGreen.getValue(), this.safecBlue.getValue(), this.safecAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                            continue;
                        }
                        if (HoleUtil.is2Hole(pos) != null && (HoleESP.mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock() == Blocks.AIR || HoleESP.mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(), HoleUtil.is2Hole(pos).getY() + 1, HoleUtil.is2Hole(pos).getZ())).getBlock() == Blocks.AIR) && (HoleESP.mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock() == Blocks.OBSIDIAN) && (HoleESP.mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(), HoleUtil.is2Hole(pos).getY() - 1, HoleUtil.is2Hole(pos).getZ())).getBlock() == Blocks.BEDROCK || HoleESP.mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(), HoleUtil.is2Hole(pos).getY() - 1, HoleUtil.is2Hole(pos).getZ())).getBlock() == Blocks.OBSIDIAN)) {
                            RenderUtil.drawBoxESP(pos, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                            RenderUtil.drawBoxESP(HoleUtil.is2Hole(pos), ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                            continue;
                        }
                    }
                    if (HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && (!pos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) || this.renderOwn.getValue())) {
                        if (BlockUtil.isPosInFov(pos) || !this.fov.getValue()) {
                            if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                                RenderUtil.drawBoxESP(pos, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.safeRed.getValue(), this.safeGreen.getValue(), this.safeBlue.getValue(), this.safeAlpha.getValue()), this.customOutline.getValue(), new Color(this.safecRed.getValue(), this.safecGreen.getValue(), this.safecBlue.getValue(), this.safecAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                            }
                            else if (BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.down()).getBlock()) && BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.east()).getBlock()) && BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.west()).getBlock()) && BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.south()).getBlock())) {
                                if (BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.north()).getBlock())) {
                                    RenderUtil.drawBoxESP(pos, ((boolean)this.rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    static {
        HoleESP.INSTANCE = new HoleESP();
    }
}
