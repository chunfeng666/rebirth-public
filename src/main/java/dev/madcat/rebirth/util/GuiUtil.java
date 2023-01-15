

package dev.madcat.rebirth.util;

import dev.madcat.rebirth.*;
import dev.madcat.rebirth.manager.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;

public class GuiUtil
{
    public static void drawString(final String paramString, final float paramFloat1, final float paramFloat2, final int paramInt) {
        final ModuleManager moduleManager = Rebirth.moduleManager;
        if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
            Rebirth.fontRenderer.drawStringWithShadow(paramString, (double)paramFloat1, (double)paramFloat2, paramInt);
        }
        else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(paramString, paramFloat1, paramFloat2, paramInt);
        }
    }
    
    public static int getStringWidth(final String paramString) {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(paramString);
    }
    
    public static void drawHorizontalLine(int paramInt1, int paramInt2, final int paramInt3, final int paramInt4) {
        if (paramInt2 < paramInt1) {
            final int i = paramInt1;
            paramInt1 = paramInt2;
            paramInt2 = i;
        }
        drawRect(paramInt1, paramInt3, paramInt2 + 1, paramInt3 + 1, paramInt4);
    }
    
    public static void drawString(final String paramString, final int paramInt1, final int paramInt2, final int paramInt3) {
        final ModuleManager moduleManager = Rebirth.moduleManager;
        if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
            Rebirth.fontRenderer.drawStringWithShadow(paramString, (double)paramInt1, (double)paramInt2, paramInt3);
        }
        else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(paramString, (float)paramInt1, (float)paramInt2, paramInt3);
        }
    }
    
    public static String getCFont() {
        return Rebirth.fontRenderer.getFont().getFamily();
    }
    
    public static void drawRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, final int paramInt5) {
        if (paramInt1 < paramInt3) {
            final int i = paramInt1;
            paramInt1 = paramInt3;
            paramInt3 = i;
        }
        if (paramInt2 < paramInt4) {
            final int i = paramInt2;
            paramInt2 = paramInt4;
            paramInt4 = i;
        }
        final float f1 = (paramInt5 >> 24 & 0xFF) / 255.0f;
        final float f2 = (paramInt5 >> 16 & 0xFF) / 255.0f;
        final float f3 = (paramInt5 >> 8 & 0xFF) / 255.0f;
        final float f4 = (paramInt5 & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f2, f3, f4, f1);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos((double)paramInt1, (double)paramInt4, 0.0).endVertex();
        bufferBuilder.pos((double)paramInt3, (double)paramInt4, 0.0).endVertex();
        bufferBuilder.pos((double)paramInt3, (double)paramInt2, 0.0).endVertex();
        bufferBuilder.pos((double)paramInt1, (double)paramInt2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static int getHeight() {
        final ModuleManager moduleManager = Rebirth.moduleManager;
        return ModuleManager.getModuleByName("CustomFont").isEnabled() ? Rebirth.fontRenderer.getHeight() : Rebirth.fontRenderer.getHeight();
    }
    
    public static void drawVerticalLine(final int paramInt1, int paramInt2, int paramInt3, final int paramInt4) {
        if (paramInt3 < paramInt2) {
            final int i = paramInt2;
            paramInt2 = paramInt3;
            paramInt3 = i;
        }
        drawRect(paramInt1, paramInt2 + 1, paramInt1 + 1, paramInt3, paramInt4);
    }
    
    public static void drawCenteredString(final String paramString, final int paramInt1, final int paramInt2, final int paramInt3) {
        final ModuleManager moduleManager = Rebirth.moduleManager;
        if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
            Rebirth.fontRenderer.drawStringWithShadow(paramString, (double)(paramInt1 - Rebirth.fontRenderer.getStringWidth(paramString) / 2), (double)paramInt2, paramInt3);
        }
        else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(paramString, (float)(paramInt1 - Rebirth.fontRenderer.getStringWidth(paramString) / 2), (float)paramInt2, paramInt3);
        }
    }
}
