

package dev.madcat.rebirth.util;

import java.awt.*;
import net.minecraft.client.renderer.*;

public class OyColor extends Color
{
    public OyColor(final int r, final int g, final int b) {
        super(r, g, b);
    }
    
    public OyColor(final int rgb) {
        super(rgb);
    }
    
    public OyColor(final int rgba, final boolean hasAlpha) {
        super(rgba, hasAlpha);
    }
    
    public OyColor(final int r, final int g, final int b, final int a) {
        super(r, g, b, a);
    }
    
    public OyColor(final Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public OyColor(final Color color, final int alpha) {
        super(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    public static OyColor fromHSB(final float hue, final float saturation, final float brightness) {
        return new OyColor(Color.getHSBColor(hue, saturation, brightness));
    }
    
    public float getHue() {
        return Color.RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), null)[0];
    }
    
    public float getSaturation() {
        return Color.RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), null)[1];
    }
    
    public float getBrightness() {
        return Color.RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), null)[2];
    }
    
    public float getRedNorm() {
        return this.getRed() / 255.0f;
    }
    
    public float getGreenNorm() {
        return this.getGreen() / 255.0f;
    }
    
    public float getBlueNorm() {
        return this.getBlue() / 255.0f;
    }
    
    public float getAlphaNorm() {
        return this.getAlpha() / 255.0f;
    }
    
    public void glColor() {
        GlStateManager.color(this.getRedNorm(), this.getGreenNorm(), this.getBlueNorm(), this.getAlphaNorm());
    }
}
