

package dev.madcat.rebirth.features.gui.components;

import dev.madcat.rebirth.features.*;
import net.minecraft.client.*;
import dev.madcat.rebirth.features.gui.components.items.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.features.modules.client.*;
import dev.madcat.rebirth.*;
import dev.madcat.rebirth.manager.*;
import dev.madcat.rebirth.features.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;
import dev.madcat.rebirth.features.gui.components.items.buttons.*;

public class Component extends Feature
{
    public static int[] counter1;
    private final Minecraft minecraft;
    private final ArrayList<Item> items;
    private final int barHeight;
    public boolean drag;
    private int old;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private int angle;
    private boolean hidden;
    private int startcolor;
    
    public Component(final String name, final int x, final int y, final boolean open) {
        super(name);
        this.minecraft = Minecraft.getMinecraft();
        this.items = new ArrayList<Item>();
        this.hidden = false;
        this.x = x;
        this.y = y;
        this.width = 88 + ClickGui.getInstance().moduleWidth.getValue();
        this.height = 18;
        this.barHeight = 15;
        this.angle = 180;
        this.open = open;
        this.setupItems();
    }
    
    public static void drawModalRect(final int var0, final int var1, final float var2, final float var3, final int var4, final int var5, final int var6, final int var7, final float var8, final float var9) {
        Gui.drawScaledCustomSizeModalRect(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
    }
    
    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static float calculateRotation(float var0) {
        final float f = 0.0f;
        var0 %= 360.0f;
        if (f >= 180.0f) {
            var0 -= 360.0f;
        }
        if (var0 < -180.0f) {
            var0 += 360.0f;
        }
        return var0;
    }
    
    public void setupItems() {
    }
    
    private void drag(final int mouseX, final int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.old = this.x;
        if (this.getName() == "Combat") {
            this.old += ClickGui.getInstance().moduleDistance.getValue() * 1;
        }
        if (this.getName() == "Misc") {
            this.old += ClickGui.getInstance().moduleDistance.getValue() * 2;
        }
        if (this.getName() == "Render") {
            this.old += ClickGui.getInstance().moduleDistance.getValue() * 3;
        }
        if (this.getName() == "Movement") {
            this.old += ClickGui.getInstance().moduleDistance.getValue() * 4;
        }
        if (this.getName() == "Player") {
            this.old += ClickGui.getInstance().moduleDistance.getValue() * 5;
        }
        if (this.getName() == "Client") {
            this.old += ClickGui.getInstance().moduleDistance.getValue() * 6;
        }
        this.width = 88 + ClickGui.getInstance().moduleWidth.getValue();
        this.drag(mouseX, mouseY);
        Component.counter1 = new int[] { 1 };
        final float f;
        final float totalItemHeight = f = (this.open ? (this.getTotalItemHeight() - 2.0f) : 0.0f);
        if (ClickGui.getInstance().rainbowg.getValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.startcolor = ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB();
                final int endcolor = ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB();
            }
        }
        else {
            this.startcolor = ColorUtil.toRGBA(ClickGui.getInstance().g_red.getValue(), ClickGui.getInstance().g_green.getValue(), ClickGui.getInstance().g_blue.getValue(), ClickGui.getInstance().g_alpha.getValue());
        }
        final int endcolor = ColorUtil.toRGBA(ClickGui.getInstance().g_red1.getValue(), ClickGui.getInstance().g_green1.getValue(), ClickGui.getInstance().g_blue1.getValue(), ClickGui.getInstance().g_alpha1.getValue());
        RenderUtil.drawRect((float)this.old, (float)this.y, (float)(this.old + this.width), (float)(this.y + this.height - 5), ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 255));
        RenderUtil.drawGradientSideways(this.old - 1, this.y, this.old + this.width + 1, this.y + this.barHeight - 2.0f, this.startcolor, endcolor);
        if (this.open) {
            RenderUtil.drawGradientSideways(this.old - 1, this.y + 13.2f, this.old + this.width + 1, this.y + totalItemHeight + 19.0f, this.startcolor, endcolor);
            RenderUtil.drawRect((float)this.old, this.y + 13.2f, (float)(this.old + this.width), this.y + this.height + totalItemHeight, ColorUtil.toRGBA(0, 0, 0, ClickGui.getInstance().alphaBox.getValue()));
        }
        Label_1024: {
            if (ClickGui.getInstance().moduleIcon.getValue()) {
                Label_0815: {
                    if (!FontMod.getInstance().cfont.getValue()) {
                        final ModuleManager moduleManager = Rebirth.moduleManager;
                        if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                            break Label_0815;
                        }
                    }
                    final ModuleManager moduleManager2 = Rebirth.moduleManager;
                    if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                        Rebirth.textManager.drawStringClickGui(this.getName(), this.old + 17.0f, this.y - 3.0f - RebirthGui.getClickGui().getTextOffset(), -1);
                        break Label_1024;
                    }
                }
                Rebirth.textManager.drawStringWithShadow(this.getName(), this.old + 17.0f, this.y - 4.0f - RebirthGui.getClickGui().getTextOffset(), -1);
            }
            else {
                Label_0946: {
                    if (!FontMod.getInstance().cfont.getValue()) {
                        final ModuleManager moduleManager3 = Rebirth.moduleManager;
                        if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                            break Label_0946;
                        }
                    }
                    final ModuleManager moduleManager4 = Rebirth.moduleManager;
                    if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                        Rebirth.textManager.drawStringClickGui(this.getName(), this.old + 3.0f, this.y - 3.0f - RebirthGui.getClickGui().getTextOffset(), -1);
                        break Label_1024;
                    }
                }
                Rebirth.textManager.drawStringWithShadow(this.getName(), this.old + 3.0f, this.y - 4.0f - RebirthGui.getClickGui().getTextOffset(), -1);
            }
        }
        if (ClickGui.getInstance().moduleIcon.getValue()) {
            if (this.getName() == "Combat") {
                Rebirth.textManager.drawStringlogo("b", this.old + 3.0f, this.y - 4.0f - RebirthGui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName() == "Misc") {
                Rebirth.textManager.drawStringlogo("[", this.old + 3.0f, this.y - 4.0f - RebirthGui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName() == "Render") {
                Rebirth.textManager.drawStringlogo("a", this.old + 3.0f, this.y - 4.0f - RebirthGui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName() == "Movement") {
                Rebirth.textManager.drawStringlogo("8", this.old + 3.0f, this.y - 4.0f - RebirthGui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName() == "Player") {
                Rebirth.textManager.drawStringlogo("5", this.old + 3.0f, this.y - 4.0f - RebirthGui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName() == "Client") {
                Rebirth.textManager.drawStringlogo("7", this.old + 3.0f, this.y - 4.0f - RebirthGui.getClickGui().getTextOffset(), -1);
            }
        }
        if (!this.open) {
            if (this.angle > 0) {
                this.angle -= 6;
            }
        }
        else if (this.angle < 180) {
            this.angle += 6;
        }
        if (ClickGui.getInstance().moduleIcon2.getValue()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            glColor(new Color(255, 255, 255, 255));
            this.minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/arrow.png"));
            GlStateManager.translate((float)(this.getX() + this.getWidth() - 7), this.getY() + 6 - 0.3f, 0.0f);
            GlStateManager.rotate(calculateRotation((float)this.angle), 0.0f, 0.0f, 1.0f);
            drawModalRect(-5, -5, 0.0f, 0.0f, 10, 10, 10, 10, 10.0f, 10.0f);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
        if (this.open) {
            RenderUtil.drawRect((float)this.old, this.y + 12.5f, (float)(this.old + this.width), this.y + this.height + totalItemHeight, 1996488704);
            if (ClickGui.getInstance().outline.getValue()) {
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.shadeModel(7425);
                GL11.glBegin(2);
                GL11.glColor4f(ClickGui.getInstance().red.getValue() / 255.0f, ClickGui.getInstance().green.getValue() / 255.0f, ClickGui.getInstance().blue.getValue() / 255.0f, 255.0f);
                GL11.glVertex3f((float)this.old, this.y - 0.5f, 0.0f);
                GL11.glVertex3f((float)(this.old + this.width), this.y - 0.5f, 0.0f);
                GL11.glVertex3f((float)(this.old + this.width), this.y + this.height + totalItemHeight, 0.0f);
                GL11.glVertex3f((float)this.old, this.y + this.height + totalItemHeight, 0.0f);
                GL11.glEnd();
                GlStateManager.shadeModel(7424);
                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
            }
        }
        if (this.open) {
            float y = this.getY() + this.getHeight() - 3.0f;
            for (final Item item : this.getItems()) {
                ++Component.counter1[0];
                if (item.isHidden()) {
                    continue;
                }
                item.setLocation(this.old + 2.0f, y);
                item.setWidth(this.getWidth() - 4);
                item.drawScreen(mouseX, mouseY, partialTicks);
                y += item.getHeight() + 1.5f;
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.old - mouseX;
            this.y2 = this.y - mouseY;
            if (this.getName() == "Combat") {
                this.x2 -= ClickGui.getInstance().moduleDistance.getValue() * 1;
            }
            if (this.getName() == "Misc") {
                this.x2 -= ClickGui.getInstance().moduleDistance.getValue() * 2;
            }
            if (this.getName() == "Render") {
                this.x2 -= ClickGui.getInstance().moduleDistance.getValue() * 3;
            }
            if (this.getName() == "Movement") {
                this.x2 -= ClickGui.getInstance().moduleDistance.getValue() * 4;
            }
            if (this.getName() == "Player") {
                this.x2 -= ClickGui.getInstance().moduleDistance.getValue() * 5;
            }
            if (this.getName() == "Client") {
                this.x2 -= ClickGui.getInstance().moduleDistance.getValue() * 6;
            }
            RebirthGui.getClickGui().getComponents().forEach(component -> {
                if (component.drag) {
                    component.drag = false;
                }
                return;
            });
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            Component.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }
    
    public void addButton(final Button button) {
        this.items.add(button);
    }
    
    public int getX() {
        return this.old;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public final ArrayList<Item> getItems() {
        return this.items;
    }
    
    private boolean isHovering(final int mouseX, final int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }
    
    private float getTotalItemHeight() {
        float height = 0.0f;
        for (final Item item : this.getItems()) {
            height += item.getHeight() + 1.5f;
        }
        return height;
    }
    
    static {
        Component.counter1 = new int[] { 1 };
    }
}
