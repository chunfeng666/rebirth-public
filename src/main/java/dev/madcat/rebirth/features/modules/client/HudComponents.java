
package dev.madcat.rebirth.features.modules.client;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.inventory.*;

public class HudComponents extends Module
{
    private static final ResourceLocation box;
    private static final double HALF_PI = 1.5707963267948966;
    public Setting<Boolean> inventory;
    public Setting<Integer> invX;
    public Setting<Integer> invY;
    public Setting<Integer> fineinvX;
    public Setting<Integer> fineinvY;
    public Setting<Boolean> renderXCarry;
    public Setting<Integer> invH;
    public Setting<Boolean> holeHud;
    public Setting<Integer> holeX;
    public Setting<Integer> holeY;
    public Setting<Compass> compass;
    public Setting<Integer> compassX;
    public Setting<Integer> compassY;
    public Setting<Integer> scale;
    public Setting<Boolean> playerViewer;
    public Setting<Integer> playerViewerX;
    public Setting<Integer> playerViewerY;
    public Setting<Float> playerScale;
    
    public HudComponents() {
        super("Components", "Components.", Category.CLIENT, false, false, true);
        this.inventory = (Setting<Boolean>)this.register(new Setting("Inventory", false));
        this.invX = (Setting<Integer>)this.register(new Setting("InvX", 564, 0, 1000, v -> this.inventory.getValue()));
        this.invY = (Setting<Integer>)this.register(new Setting("InvY", 467, 0, 1000, v -> this.inventory.getValue()));
        this.fineinvX = (Setting<Integer>)this.register(new Setting("InvFineX", 0, v -> this.inventory.getValue()));
        this.fineinvY = (Setting<Integer>)this.register(new Setting("InvFineY", 0, v -> this.inventory.getValue()));
        this.renderXCarry = (Setting<Boolean>)this.register(new Setting("RenderXCarry", false, v -> this.inventory.getValue()));
        this.invH = (Setting<Integer>)this.register(new Setting("InvH", 3, v -> this.inventory.getValue()));
        this.holeHud = (Setting<Boolean>)this.register(new Setting("HoleHUD", false));
        this.holeX = (Setting<Integer>)this.register(new Setting("HoleX", 279, 0, 1000, v -> this.holeHud.getValue()));
        this.holeY = (Setting<Integer>)this.register(new Setting("HoleY", 485, 0, 1000, v -> this.holeHud.getValue()));
        this.compass = (Setting<Compass>)this.register(new Setting("Compass", Compass.NONE));
        this.compassX = (Setting<Integer>)this.register(new Setting("CompX", 472, 0, 1000, v -> this.compass.getValue() != Compass.NONE));
        this.compassY = (Setting<Integer>)this.register(new Setting("CompY", 424, 0, 1000, v -> this.compass.getValue() != Compass.NONE));
        this.scale = (Setting<Integer>)this.register(new Setting("Scale", 3, 0, 10, v -> this.compass.getValue() != Compass.NONE));
        this.playerViewer = (Setting<Boolean>)this.register(new Setting("PlayerViewer", false));
        this.playerViewerX = (Setting<Integer>)this.register(new Setting("PlayerX", 752, 0, 1000, v -> this.playerViewer.getValue()));
        this.playerViewerY = (Setting<Integer>)this.register(new Setting("PlayerY", 497, 0, 1000, v -> this.playerViewer.getValue()));
        this.playerScale = (Setting<Float>)this.register(new Setting("PlayerScale", 1.0f, 0.1f, 2.0f, v -> this.playerViewer.getValue()));
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (this.playerViewer.getValue()) {
            this.drawPlayer();
        }
        if (this.compass.getValue() != Compass.NONE) {
            this.drawCompass();
        }
        if (this.holeHud.getValue()) {
            this.drawOverlay(event.partialTicks);
        }
        if (this.inventory.getValue()) {
            this.renderInventory();
        }
    }
    
    public void drawCompass() {
        final ScaledResolution sr = new ScaledResolution(HudComponents.mc);
        if (this.compass.getValue() == Compass.LINE) {
            final float playerYaw = HudComponents.mc.player.rotationYaw;
            final float rotationYaw = MathUtil.wrap(playerYaw);
            RenderUtil.drawRect(this.compassX.getValue(), this.compassY.getValue(), (float)(this.compassX.getValue() + 100), (float)(this.compassY.getValue() + this.renderer.getFontHeight()), 1963986960);
            RenderUtil.glScissor(this.compassX.getValue(), this.compassY.getValue(), (float)(this.compassX.getValue() + 100), (float)(this.compassY.getValue() + this.renderer.getFontHeight()), sr);
            GL11.glEnable(3089);
            final float zeroZeroYaw = MathUtil.wrap((float)(Math.atan2(0.0 - HudComponents.mc.player.posZ, 0.0 - HudComponents.mc.player.posX) * 180.0 / 3.141592653589793) - 90.0f);
            RenderUtil.drawLine(this.compassX.getValue() - rotationYaw + 50.0f + zeroZeroYaw, (float)(this.compassY.getValue() + 2), this.compassX.getValue() - rotationYaw + 50.0f + zeroZeroYaw, (float)(this.compassY.getValue() + this.renderer.getFontHeight() - 2), 2.0f, -61424);
            RenderUtil.drawLine(this.compassX.getValue() - rotationYaw + 50.0f + 45.0f, (float)(this.compassY.getValue() + 2), this.compassX.getValue() - rotationYaw + 50.0f + 45.0f, (float)(this.compassY.getValue() + this.renderer.getFontHeight() - 2), 2.0f, -1);
            RenderUtil.drawLine(this.compassX.getValue() - rotationYaw + 50.0f - 45.0f, (float)(this.compassY.getValue() + 2), this.compassX.getValue() - rotationYaw + 50.0f - 45.0f, (float)(this.compassY.getValue() + this.renderer.getFontHeight() - 2), 2.0f, -1);
            RenderUtil.drawLine(this.compassX.getValue() - rotationYaw + 50.0f + 135.0f, (float)(this.compassY.getValue() + 2), this.compassX.getValue() - rotationYaw + 50.0f + 135.0f, (float)(this.compassY.getValue() + this.renderer.getFontHeight() - 2), 2.0f, -1);
            RenderUtil.drawLine(this.compassX.getValue() - rotationYaw + 50.0f - 135.0f, (float)(this.compassY.getValue() + 2), this.compassX.getValue() - rotationYaw + 50.0f - 135.0f, (float)(this.compassY.getValue() + this.renderer.getFontHeight() - 2), 2.0f, -1);
            this.renderer.drawStringWithShadow("n", this.compassX.getValue() - rotationYaw + 50.0f + 180.0f - this.renderer.getStringWidth("n") / 2.0f, this.compassY.getValue(), -1);
            this.renderer.drawStringWithShadow("n", this.compassX.getValue() - rotationYaw + 50.0f - 180.0f - this.renderer.getStringWidth("n") / 2.0f, this.compassY.getValue(), -1);
            this.renderer.drawStringWithShadow("e", this.compassX.getValue() - rotationYaw + 50.0f - 90.0f - this.renderer.getStringWidth("e") / 2.0f, this.compassY.getValue(), -1);
            this.renderer.drawStringWithShadow("s", this.compassX.getValue() - rotationYaw + 50.0f - this.renderer.getStringWidth("s") / 2.0f, this.compassY.getValue(), -1);
            this.renderer.drawStringWithShadow("w", this.compassX.getValue() - rotationYaw + 50.0f + 90.0f - this.renderer.getStringWidth("w") / 2.0f, this.compassY.getValue(), -1);
            RenderUtil.drawLine((float)(this.compassX.getValue() + 50), (float)(this.compassY.getValue() + 1), (float)(this.compassX.getValue() + 50), (float)(this.compassY.getValue() + this.renderer.getFontHeight() - 1), 2.0f, -7303024);
            GL11.glDisable(3089);
        }
        else {
            final double centerX = this.compassX.getValue();
            final double centerY = this.compassY.getValue();
            for (final Direction dir : Direction.values()) {
                final double rad = getPosOnCompass(dir);
                this.renderer.drawStringWithShadow(dir.name(), (float)(centerX + this.getX(rad)), (float)(centerY + this.getY(rad)), (dir == Direction.N) ? -65536 : -1);
            }
        }
    }
    
    public void drawPlayer() {
        final EntityPlayer ent = (EntityPlayer)HudComponents.mc.player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.playerViewerX.getValue() + 25), (float)(this.playerViewerY.getValue() + 25), 50.0f);
        GlStateManager.scale(-50.0f * this.playerScale.getValue(), 50.0f * this.playerScale.getValue(), 50.0f * this.playerScale.getValue());
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(this.playerViewerY.getValue() / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = HudComponents.mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception ex) {}
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }
    
    private double getX(final double rad) {
        return Math.sin(rad) * (this.scale.getValue() * 10);
    }
    
    private double getY(final double rad) {
        final double epicPitch = MathHelper.clamp(HudComponents.mc.player.rotationPitch + 30.0f, -90.0f, 90.0f);
        final double pitchRadians = Math.toRadians(epicPitch);
        return Math.cos(rad) * Math.sin(pitchRadians) * (this.scale.getValue() * 10);
    }
    
    private static double getPosOnCompass(final Direction dir) {
        final double yaw = Math.toRadians(MathHelper.wrapDegrees(HudComponents.mc.player.rotationYaw));
        final int index = dir.ordinal();
        return yaw + index * 1.5707963267948966;
    }
    
    public void drawOverlay(final float partialTicks) {
        float yaw = 0.0f;
        final int dir = MathHelper.floor(HudComponents.mc.player.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
        switch (dir) {
            case 1: {
                yaw = 90.0f;
                break;
            }
            case 2: {
                yaw = -180.0f;
                break;
            }
            case 3: {
                yaw = -90.0f;
                break;
            }
        }
        final BlockPos northPos = this.traceToBlock(partialTicks, yaw);
        final Block north = this.getBlock(northPos);
        if (north != null && north != Blocks.AIR) {
            final int damage = this.getBlockDamage(northPos);
            if (damage != 0) {
                RenderUtil.drawRect((float)(this.holeX.getValue() + 16), this.holeY.getValue(), (float)(this.holeX.getValue() + 32), (float)(this.holeY.getValue() + 16), 1627324416);
            }
            this.drawBlock(north, (float)(this.holeX.getValue() + 16), this.holeY.getValue());
        }
        final BlockPos southPos = this.traceToBlock(partialTicks, yaw - 180.0f);
        final Block south = this.getBlock(southPos);
        if (south != null && south != Blocks.AIR) {
            final int damage2 = this.getBlockDamage(southPos);
            if (damage2 != 0) {
                RenderUtil.drawRect((float)(this.holeX.getValue() + 16), (float)(this.holeY.getValue() + 32), (float)(this.holeX.getValue() + 32), (float)(this.holeY.getValue() + 48), 1627324416);
            }
            this.drawBlock(south, (float)(this.holeX.getValue() + 16), (float)(this.holeY.getValue() + 32));
        }
        final BlockPos eastPos = this.traceToBlock(partialTicks, yaw + 90.0f);
        final Block east = this.getBlock(eastPos);
        if (east != null && east != Blocks.AIR) {
            final int damage3 = this.getBlockDamage(eastPos);
            if (damage3 != 0) {
                RenderUtil.drawRect((float)(this.holeX.getValue() + 32), (float)(this.holeY.getValue() + 16), (float)(this.holeX.getValue() + 48), (float)(this.holeY.getValue() + 32), 1627324416);
            }
            this.drawBlock(east, (float)(this.holeX.getValue() + 32), (float)(this.holeY.getValue() + 16));
        }
        final BlockPos westPos = this.traceToBlock(partialTicks, yaw - 90.0f);
        final Block west = this.getBlock(westPos);
        if (west != null && west != Blocks.AIR) {
            final int damage4 = this.getBlockDamage(westPos);
            if (damage4 != 0) {
                RenderUtil.drawRect(this.holeX.getValue(), (float)(this.holeY.getValue() + 16), (float)(this.holeX.getValue() + 16), (float)(this.holeY.getValue() + 32), 1627324416);
            }
            this.drawBlock(west, this.holeX.getValue(), (float)(this.holeY.getValue() + 16));
        }
    }
    
    private int getBlockDamage(final BlockPos pos) {
        for (final DestroyBlockProgress destBlockProgress : HudComponents.mc.renderGlobal.damagedBlocks.values()) {
            if (destBlockProgress.getPosition().getX() == pos.getX() && destBlockProgress.getPosition().getY() == pos.getY() && destBlockProgress.getPosition().getZ() == pos.getZ()) {
                return destBlockProgress.getPartialBlockDamage();
            }
        }
        return 0;
    }
    
    private BlockPos traceToBlock(final float partialTicks, final float yaw) {
        final Vec3d pos = EntityUtil.interpolateEntity((Entity)HudComponents.mc.player, partialTicks);
        final Vec3d dir = MathUtil.direction(yaw);
        return new BlockPos(pos.x + dir.x, pos.y, pos.z + dir.z);
    }
    
    private Block getBlock(final BlockPos pos) {
        final Block block = HudComponents.mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN) {
            return block;
        }
        return Blocks.AIR;
    }
    
    private void drawBlock(final Block block, final float x, final float y) {
        final ItemStack stack = new ItemStack(block);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.translate(x, y, 0.0f);
        HudComponents.mc.getRenderItem().zLevel = 501.0f;
        HudComponents.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        HudComponents.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    public void renderInventory() {
        this.boxrender(this.invX.getValue() + this.fineinvX.getValue(), this.invY.getValue() + this.fineinvY.getValue());
        this.itemrender((NonNullList<ItemStack>)HudComponents.mc.player.inventory.mainInventory, this.invX.getValue() + this.fineinvX.getValue(), this.invY.getValue() + this.fineinvY.getValue());
    }
    
    private static void preboxrender() {
        GL11.glPushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();
        GlStateManager.color(255.0f, 255.0f, 255.0f, 255.0f);
    }
    
    private static void postboxrender() {
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
    }
    
    private static void preitemrender() {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
    }
    
    private static void postitemrender() {
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }
    
    private void boxrender(final int x, final int y) {
        preboxrender();
        HudComponents.mc.renderEngine.bindTexture(HudComponents.box);
        RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
        RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + this.invH.getValue(), 500);
        RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
        postboxrender();
    }
    
    private void itemrender(final NonNullList<ItemStack> items, final int x, final int y) {
        for (int i = 0; i < items.size() - 9; ++i) {
            final int iX = x + i % 9 * 18 + 8;
            final int iY = y + i / 9 * 18 + 18;
            final ItemStack itemStack = (ItemStack)items.get(i + 9);
            preitemrender();
            HudComponents.mc.getRenderItem().zLevel = 501.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HudComponents.mc.fontRenderer, itemStack, iX, iY, (String)null);
            HudComponents.mc.getRenderItem().zLevel = 0.0f;
            postitemrender();
        }
        if (this.renderXCarry.getValue()) {
            for (int i = 1; i < 5; ++i) {
                final int iX = x + (i + 4) % 9 * 18 + 8;
                final ItemStack itemStack2 = HudComponents.mc.player.inventoryContainer.inventorySlots.get(i).getStack();
                if (!itemStack2.isEmpty) {
                    preitemrender();
                    HudComponents.mc.getRenderItem().zLevel = 501.0f;
                    RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack2, iX, y + 1);
                    RenderUtil.itemRender.renderItemOverlayIntoGUI(HudComponents.mc.fontRenderer, itemStack2, iX, y + 1, (String)null);
                    HudComponents.mc.getRenderItem().zLevel = 0.0f;
                    postitemrender();
                }
            }
        }
    }
    
    static {
        box = new ResourceLocation("textures/gui/container/shulker_box.png");
    }
    
    private enum Direction
    {
        N, 
        W, 
        S, 
        E;
    }
    
    public enum Compass
    {
        NONE, 
        CIRCLE, 
        LINE;
    }
}
