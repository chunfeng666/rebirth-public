

package dev.madcat.rebirth.util.shader;

import net.minecraft.client.*;
import net.minecraft.client.shader.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;

public abstract class FramebufferShader extends Shader
{
    public Minecraft mc;
    private static Framebuffer framebuffer;
    protected static int lastScale;
    protected static int lastScaleWidth;
    protected static int lastScaleHeight;
    protected float red;
    protected float green;
    protected float blue;
    protected float alpha;
    protected float radius;
    protected float quality;
    protected boolean animation;
    protected int animationSpeed;
    protected float divider;
    protected float maxSample;
    private boolean entityShadows;
    
    public FramebufferShader(final String fragmentShader) {
        super(fragmentShader);
        this.mc = Minecraft.getMinecraft();
        this.alpha = 1.0f;
        this.radius = 2.0f;
        this.quality = 1.0f;
        this.animation = true;
        this.animationSpeed = 1;
        this.divider = 1.0f;
        this.maxSample = 1.0f;
    }
    
    public void setShaderParams(final Boolean animation, final int animationSpeed, final OyColor color) {
        this.animation = animation;
        this.animationSpeed = animationSpeed;
        this.red = color.getRedNorm();
        this.green = color.getGreenNorm();
        this.blue = color.getBlueNorm();
        this.alpha = color.getBlueNorm();
    }
    
    public void setShaderParams(final Boolean animation, final int animationSpeed, final OyColor color, final float radius) {
        this.setShaderParams(animation, animationSpeed, color);
        this.radius = radius;
    }
    
    public void setShaderParams(final Boolean animation, final int animationSpeed, final OyColor color, final float radius, final float divider, final float maxSample) {
        this.setShaderParams(animation, animationSpeed, color, radius);
        this.divider = divider;
        this.maxSample = maxSample;
    }
    
    public void startDraw(final float partialTicks) {
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        (FramebufferShader.framebuffer = this.setupFrameBuffer(FramebufferShader.framebuffer)).bindFramebuffer(true);
        this.entityShadows = this.mc.gameSettings.entityShadows;
        this.mc.gameSettings.entityShadows = false;
    }
    
    public void stopDraw() {
        this.mc.gameSettings.entityShadows = this.entityShadows;
        GlStateManager.enableBlend();
        GL11.glBlendFunc(770, 771);
        this.mc.getFramebuffer().bindFramebuffer(true);
        this.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        this.startShader();
        this.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(FramebufferShader.framebuffer);
        this.stopShader();
        this.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
    }
    
    public void stopDraw(final OyColor color, final float radius, final float quality, final Runnable... shaderOps) {
        this.mc.gameSettings.entityShadows = this.entityShadows;
        GlStateManager.enableBlend();
        GL11.glBlendFunc(770, 771);
        this.mc.getFramebuffer().bindFramebuffer(true);
        this.red = color.getRed() / 255.0f;
        this.green = color.getGreen() / 255.0f;
        this.blue = color.getBlue() / 255.0f;
        this.alpha = color.getAlpha() / 255.0f;
        this.radius = radius;
        this.quality = quality;
        this.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        this.startShader();
        this.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(FramebufferShader.framebuffer);
        this.stopShader();
        this.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
    
    public Framebuffer setupFrameBuffer(Framebuffer frameBuffer) {
        if (Display.isActive() || Display.isVisible()) {
            if (frameBuffer != null) {
                frameBuffer.framebufferClear();
                final ScaledResolution scale = new ScaledResolution(this.mc);
                final int factor = scale.getScaleFactor();
                final int factor2 = scale.getScaledWidth();
                final int factor3 = scale.getScaledHeight();
                if (FramebufferShader.lastScale != factor || FramebufferShader.lastScaleWidth != factor2 || FramebufferShader.lastScaleHeight != factor3) {
                    frameBuffer.deleteFramebuffer();
                    frameBuffer = new Framebuffer(this.mc.displayWidth, this.mc.displayHeight, true);
                    frameBuffer.framebufferClear();
                }
                FramebufferShader.lastScale = factor;
                FramebufferShader.lastScaleWidth = factor2;
                FramebufferShader.lastScaleHeight = factor3;
            }
            else {
                frameBuffer = new Framebuffer(this.mc.displayWidth, this.mc.displayHeight, true);
            }
        }
        else if (frameBuffer == null) {
            frameBuffer = new Framebuffer(this.mc.displayWidth, this.mc.displayHeight, true);
        }
        return frameBuffer;
    }
    
    public void drawFramebuffer(final Framebuffer framebuffer) {
        final ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        GL11.glBindTexture(3553, framebuffer.framebufferTexture);
        GL11.glBegin(7);
        GL11.glTexCoord2d(0.0, 1.0);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glTexCoord2d(0.0, 0.0);
        GL11.glVertex2d(0.0, (double)scaledResolution.getScaledHeight());
        GL11.glTexCoord2d(1.0, 0.0);
        GL11.glVertex2d((double)scaledResolution.getScaledWidth(), (double)scaledResolution.getScaledHeight());
        GL11.glTexCoord2d(1.0, 1.0);
        GL11.glVertex2d((double)scaledResolution.getScaledWidth(), 0.0);
        GL11.glEnd();
        GL20.glUseProgram(0);
    }
}
