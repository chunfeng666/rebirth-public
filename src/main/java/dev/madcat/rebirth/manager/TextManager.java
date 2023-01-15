

package dev.madcat.rebirth.manager;

import dev.madcat.rebirth.features.*;
import dev.madcat.rebirth.features.gui.font.*;
import dev.madcat.rebirth.*;
import dev.madcat.rebirth.features.modules.client.*;
import java.awt.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.util.math.*;

public class TextManager extends Feature
{
    private final Timer idleTimer;
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    private CustomFont customFont;
    public CFontRenderer iconFont;
    private CustomFont ClickGuiFont;
    private boolean idling;
    
    public TextManager() {
        this.idleTimer = new Timer();
        this.customFont = new CustomFont(new Font("Verdana", 0, 17), true, false);
        this.iconFont = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/textures/fonts/IconFont.ttf", 20.0f, 0), true, false);
        this.ClickGuiFont = new CustomFont(new Font("Arial", 1, 17), true, false);
        this.updateResolution();
    }
    
    public void init(final boolean startup) {
        final ModuleManager moduleManager = Rebirth.moduleManager;
        final FontMod cFont = (FontMod)ModuleManager.getModuleByClass((Class)FontMod.class);
        try {
            this.setFontRenderer(new Font((String)cFont.fontName.getValue(), (int)cFont.fontStyle.getValue(), (int)cFont.fontSize.getValue()), (boolean)cFont.antiAlias.getValue(), (boolean)cFont.fractionalMetrics.getValue());
        }
        catch (Exception ex) {}
    }
    
    public void drawStringWithShadow(final String text, final float x, final float y, final int color) {
        this.drawString(text, x, y, color, true);
    }
    
    public float drawString2(final String text, final float x, final float y, final int color) {
        TextManager.mc.fontRenderer.drawString(text, x, y, color, true);
        return x;
    }
    
    public float drawString(final String text, final float x, final float y, final int color, final boolean shadow) {
        final ModuleManager moduleManager = Rebirth.moduleManager;
        if (ModuleManager.getModuleByName("CustomFont").isEnabled() && (boolean)FontMod.getInstance().clientFont.getValue()) {
            if (shadow) {
                this.customFont.drawStringWithShadow(text, (double)x, (double)y, color);
            }
            else {
                this.customFont.drawString(text, x, y, color);
            }
            return x;
        }
        TextManager.mc.fontRenderer.drawString(text, x, y, color, shadow);
        return x;
    }
    
    public float drawStringlogo(final String text, final float x, final float y, final int color) {
        this.iconFont.drawStringWithShadow(text, (double)x, (double)y, color);
        return x;
    }
    
    public float drawStringClickGui(final String text, final float x, final float y, final int color) {
        this.ClickGuiFont.drawStringWithShadow(text, (double)x, (double)y, color);
        return x;
    }
    
    public void drawRainbowString(final String text, final float x, final float y, final int startColor, final float factor, final boolean shadow) {
        Color currentColor = new Color(startColor);
        final float hueIncrement = 1.0f / factor;
        final String[] rainbowStrings = text.split("��.");
        float currentHue = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[0];
        final float saturation = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[1];
        final float brightness = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[2];
        int currentWidth = 0;
        boolean shouldRainbow = true;
        boolean shouldContinue = false;
        for (int i = 0; i < text.length(); ++i) {
            final char currentChar = text.charAt(i);
            final char nextChar = text.charAt(MathUtil.clamp(i + 1, 0, text.length() - 1));
            if ((String.valueOf(currentChar) + nextChar).equals("��r")) {
                shouldRainbow = false;
            }
            else if ((String.valueOf(currentChar) + nextChar).equals("��+")) {
                shouldRainbow = true;
            }
            if (shouldContinue) {
                shouldContinue = false;
            }
            else {
                if ((String.valueOf(currentChar) + nextChar).equals("��r")) {
                    final String escapeString = text.substring(i);
                    this.drawString(escapeString, x + currentWidth, y, Color.WHITE.getRGB(), shadow);
                    break;
                }
                this.drawString(String.valueOf(currentChar).equals("��") ? "" : String.valueOf(currentChar), x + currentWidth, y, shouldRainbow ? currentColor.getRGB() : Color.WHITE.getRGB(), shadow);
                if (String.valueOf(currentChar).equals("��")) {
                    shouldContinue = true;
                }
                currentWidth += this.getStringWidth(String.valueOf(currentChar));
                if (!String.valueOf(currentChar).equals(" ")) {
                    currentColor = new Color(Color.HSBtoRGB(currentHue, saturation, brightness));
                    currentHue += hueIncrement;
                }
            }
        }
    }
    
    public int getStringWidth(final String text) {
        final ModuleManager moduleManager = Rebirth.moduleManager;
        if (ModuleManager.getModuleByName("CustomFont").isEnabled() && (boolean)FontMod.getInstance().clientFont.getValue()) {
            return this.customFont.getStringWidth(text);
        }
        return TextManager.mc.fontRenderer.getStringWidth(text);
    }
    
    public int getStringCWidth(final String text) {
        return this.ClickGuiFont.getStringWidth(text);
    }
    
    public int getFontHeight() {
        return TextManager.mc.fontRenderer.FONT_HEIGHT;
    }
    
    public void setFontRenderer(final Font font, final boolean antiAlias, final boolean fractionalMetrics) {
        this.customFont = new CustomFont(font, antiAlias, fractionalMetrics);
    }
    
    public Font getCurrentFont() {
        return this.customFont.getFont();
    }
    
    public void updateResolution() {
        this.scaledWidth = TextManager.mc.displayWidth;
        this.scaledHeight = TextManager.mc.displayHeight;
        this.scaleFactor = 1;
        final boolean flag = Util.mc.isUnicode();
        int i = TextManager.mc.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        final double scaledWidthD = this.scaledWidth / this.scaleFactor;
        final double scaledHeightD = this.scaledHeight / this.scaleFactor;
        this.scaledWidth = MathHelper.ceil(scaledWidthD);
        this.scaledHeight = MathHelper.ceil(scaledHeightD);
    }
    
    public String getIdleSign() {
        if (this.idleTimer.passedMs(500L)) {
            this.idling = !this.idling;
            this.idleTimer.reset();
        }
        if (this.idling) {
            return "_";
        }
        return "";
    }
}
