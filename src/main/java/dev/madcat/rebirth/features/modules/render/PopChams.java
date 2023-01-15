

package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.world.*;
import com.mojang.authlib.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import dev.madcat.rebirth.features.modules.client.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.util.math.*;

public class PopChams extends Module
{
    public static Setting<Boolean> self;
    public static Setting<Boolean> elevator;
    public static Setting<Integer> rL;
    public static Setting<Integer> gL;
    public static Setting<Integer> bL;
    public static Setting<Integer> aL;
    public static Setting<Integer> rF;
    public static Setting<Integer> gF;
    public static Setting<Integer> bF;
    public static Setting<Integer> aF;
    public static Setting<Integer> fadestart;
    public static Setting<Float> fadetime;
    public static Setting<Boolean> fillrainbow;
    public static Setting<Boolean> outlinerainbow;
    public static Setting<Boolean> onlyOneEsp;
    public static Setting<ElevatorMode> elevatorMode;
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;
    
    public PopChams() {
        super("PopChams", "Pop rendering", Module.Category.RENDER, true, false, false);
        PopChams.self = (Setting<Boolean>)this.register(new Setting("Render Own Pops", true));
        PopChams.elevator = (Setting<Boolean>)this.register(new Setting("Travel", true));
        PopChams.elevatorMode = (Setting<ElevatorMode>)this.register(new Setting("Elevator", ElevatorMode.UP, v -> PopChams.elevator.getValue()));
        PopChams.rL = (Setting<Integer>)this.register(new Setting("Outline Red", 30, 0, 255));
        PopChams.bL = (Setting<Integer>)this.register(new Setting("Outline Green", 167, 0, 255));
        PopChams.gL = (Setting<Integer>)this.register(new Setting("Outline Blue", 255, 0, 255));
        PopChams.aL = (Setting<Integer>)this.register(new Setting("Outline Alpha", 255, 0, 255));
        PopChams.outlinerainbow = (Setting<Boolean>)this.register(new Setting("Outline Rainbow", true));
        PopChams.rF = (Setting<Integer>)this.register(new Setting("Fill Red", 30, 0, 255));
        PopChams.bF = (Setting<Integer>)this.register(new Setting("Fill Green", 167, 0, 255));
        PopChams.gF = (Setting<Integer>)this.register(new Setting("Fill Blue", 255, 0, 255));
        PopChams.aF = (Setting<Integer>)this.register(new Setting("Fill Alpha", 140, 0, 255));
        PopChams.fillrainbow = (Setting<Boolean>)this.register(new Setting("Fill Rainbow", true));
        PopChams.fadestart = (Setting<Integer>)this.register(new Setting("Fade Start", 0, 0, 255));
        PopChams.fadetime = (Setting<Float>)this.register(new Setting("Fade Time", 0.5f, 0.0f, 2.0f));
        PopChams.onlyOneEsp = (Setting<Boolean>)this.register(new Setting("Only Render One", true));
    }
    
    public static void renderEntity(final EntityLivingBase entity, final ModelBase modelBase, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final int scale) {
        if (PopChams.mc.getRenderManager() == null) {
            return;
        }
        final float partialTicks = PopChams.mc.getRenderPartialTicks();
        final double x = entity.posX - PopChams.mc.getRenderManager().viewerPosX;
        double y = entity.posY - PopChams.mc.getRenderManager().viewerPosY;
        final double z = entity.posZ - PopChams.mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entity.isSneaking()) {
            y -= 0.125;
        }
        final float interpolateRotation = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        final float interpolateRotation2 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        final float rotationInterp = interpolateRotation2 - interpolateRotation;
        final float renderPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        renderLivingAt(x, y, z);
        final float f8 = handleRotationFloat(entity, partialTicks);
        prepareRotations(entity);
        final float f9 = prepareScale(entity, (float)scale);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9, (Entity)entity);
        modelBase.render((Entity)entity, limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9);
        GlStateManager.popMatrix();
    }
    
    public static void prepareTranslate(final EntityLivingBase entityIn, final double x, final double y, final double z) {
        renderLivingAt(x - PopChams.mc.getRenderManager().viewerPosX, y - PopChams.mc.getRenderManager().viewerPosY, z - PopChams.mc.getRenderManager().viewerPosZ);
    }
    
    public static void renderLivingAt(final double x, final double y, final double z) {
        GlStateManager.translate((float)x, (float)y, (float)z);
    }
    
    public static float prepareScale(final EntityLivingBase entity, final float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        final double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        final double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;
        GlStateManager.scale(scale + widthX, (double)(scale * entity.height), scale + widthZ);
        final float f = 0.0625f;
        GlStateManager.translate(0.0f, -1.501f, 0.0f);
        return 0.0625f;
    }
    
    public static void prepareRotations(final EntityLivingBase entityLivingBase) {
        GlStateManager.rotate(180.0f - entityLivingBase.rotationYaw, 0.0f, 1.0f, 0.0f);
    }
    
    public static float interpolateRotation(final float prevYawOffset, final float yawOffset, final float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevYawOffset + partialTicks * f;
    }
    
    public static Color newAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static float handleRotationFloat(final EntityLivingBase livingBase, final float partialTicks) {
        return 0.0f;
    }
    
    public static void prepareGL() {
        GL11.glBlendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(1.5f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }
    
    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }
    
    @SubscribeEvent
    public void onUpdate(final PacketEvent.Receive event) {
        final SPacketEntityStatus packet;
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 35 && packet.getEntity((World)PopChams.mc.world) != null && (PopChams.self.getValue() || packet.getEntity((World)PopChams.mc.world).getEntityId() != PopChams.mc.player.getEntityId())) {
            final GameProfile profile = new GameProfile(PopChams.mc.player.getUniqueID(), "");
            (this.player = new EntityOtherPlayerMP((World)PopChams.mc.world, profile)).copyLocationAndAnglesFrom(packet.getEntity((World)PopChams.mc.world));
            this.playerModel = new ModelPlayer(0.0f, false);
            this.startTime = System.currentTimeMillis();
            this.playerModel.bipedHead.showModel = false;
            this.playerModel.bipedBody.showModel = false;
            this.playerModel.bipedLeftArmwear.showModel = false;
            this.playerModel.bipedLeftLegwear.showModel = false;
            this.playerModel.bipedRightArmwear.showModel = false;
            this.playerModel.bipedRightLegwear.showModel = false;
            this.alphaFill = PopChams.aF.getValue();
            this.alphaLine = PopChams.aL.getValue();
            if (!PopChams.onlyOneEsp.getValue()) {
                final TotemPopChams totemPopChams = new TotemPopChams(this.player, this.playerModel, this.startTime, this.alphaFill, this.alphaLine);
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        if (PopChams.onlyOneEsp.getValue()) {
            if (this.player == null || PopChams.mc.world == null || PopChams.mc.player == null) {
                return;
            }
            if (PopChams.elevator.getValue()) {
                if (PopChams.elevatorMode.getValue() == ElevatorMode.UP) {
                    final EntityOtherPlayerMP player = this.player;
                    player.posY += 0.05f * event.getPartialTicks();
                }
                else if (PopChams.elevatorMode.getValue() == ElevatorMode.DOWN) {
                    final EntityOtherPlayerMP player2 = this.player;
                    player2.posY -= 0.05f * event.getPartialTicks();
                }
            }
            GL11.glLineWidth(1.0f);
            final Color lineColorS = PopChams.outlinerainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(PopChams.rL.getValue(), PopChams.bL.getValue(), PopChams.gL.getValue(), PopChams.aL.getValue());
            final Color fillColorS = PopChams.fillrainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(PopChams.rF.getValue(), PopChams.bF.getValue(), PopChams.gF.getValue(), PopChams.aF.getValue());
            int lineA = lineColorS.getAlpha();
            int fillA = fillColorS.getAlpha();
            final long time = System.currentTimeMillis() - this.startTime - PopChams.fadestart.getValue().longValue();
            if (System.currentTimeMillis() - this.startTime > PopChams.fadestart.getValue().longValue()) {
                double normal = this.normalize((double)time, 0.0, PopChams.fadetime.getValue().doubleValue());
                normal = MathHelper.clamp(normal, 0.0, 1.0);
                normal = -normal + 1.0;
                lineA *= (int)normal;
                fillA *= (int)normal;
            }
            final Color lineColor = newAlpha(lineColorS, lineA);
            final Color fillColor = newAlpha(fillColorS, fillA);
            if (this.player != null && this.playerModel != null) {
                prepareGL();
                GL11.glPushAttrib(1048575);
                GL11.glEnable(2881);
                GL11.glEnable(2848);
                if (this.alphaFill > 1.0) {
                    this.alphaFill -= PopChams.fadetime.getValue();
                }
                final Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int)this.alphaFill);
                if (this.alphaLine > 1.0) {
                    this.alphaLine -= PopChams.fadetime.getValue();
                }
                final Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)this.alphaLine);
                glColor(fillFinal);
                GL11.glPolygonMode(1032, 6914);
                renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float)this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                glColor(outlineFinal);
                GL11.glPolygonMode(1032, 6913);
                renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float)this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                GL11.glPolygonMode(1032, 6914);
                GL11.glPopAttrib();
                releaseGL();
            }
        }
    }
    
    double normalize(final double value, final double min, final double max) {
        return (value - min) / (max - min);
    }
    
    public enum ElevatorMode
    {
        UP, 
        DOWN;
    }
}
