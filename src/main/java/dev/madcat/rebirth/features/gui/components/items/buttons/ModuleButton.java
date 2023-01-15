
package dev.madcat.rebirth.features.gui.components.items.buttons;

import dev.madcat.rebirth.features.modules.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;
import dev.madcat.rebirth.features.setting.*;
import java.util.*;
import dev.madcat.rebirth.features.gui.*;
import dev.madcat.rebirth.features.modules.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import dev.madcat.rebirth.features.gui.components.*;
import dev.madcat.rebirth.*;
import dev.madcat.rebirth.features.gui.components.items.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;

public class ModuleButton extends Button
{
    private final Module module;
    private int logs;
    private List<Item> items;
    private boolean subOpen;
    
    public ModuleButton(final Module module) {
        super(module.getName());
        this.items = new ArrayList<Item>();
        this.module = module;
        this.initSettings();
    }
    
    public static void drawCompleteImage(final float posX, final float posY, final int width, final int height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, (float)height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f((float)width, (float)height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f((float)width, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
    
    public static float fa(float var0) {
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
    
    public static void drawModalRect(final int var0, final int var1, final float var2, final float var3, final int var4, final int var5, final int var6, final int var7, final float var8, final float var9) {
        Gui.drawScaledCustomSizeModalRect(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
    }
    
    public void initSettings() {
        final ArrayList<Item> newItems = new ArrayList<Item>();
        if (!this.module.getSettings().isEmpty()) {
            for (final Setting setting : this.module.getSettings()) {
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    newItems.add((Item)new BooleanButton(setting));
                }
                if (setting.getValue() instanceof Bind && !setting.getName().equalsIgnoreCase("Keybind") && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add((Item)new BindButton(setting));
                }
                if ((setting.getValue() instanceof String || setting.getValue() instanceof Character) && !setting.getName().equalsIgnoreCase("displayName")) {
                    newItems.add((Item)new StringButton(setting));
                }
                if (setting.isNumberSetting() && setting.hasRestriction()) {
                    newItems.add((Item)new Slider(setting));
                }
                else {
                    if (!setting.isEnumSetting()) {
                        continue;
                    }
                    newItems.add((Item)new EnumButton(setting));
                }
            }
        }
        newItems.add((Item)new BindButton(this.module.getSettingByName("Keybind")));
        this.items = newItems;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.isHovering(mouseX, mouseY)) {
            final DescriptionDisplay descriptionDisplay = RebirthGui.getInstance().getDescriptionDisplay();
            descriptionDisplay.setDescription(this.module.getDescription());
            descriptionDisplay.setLocation((float)(mouseX + 2), (float)(mouseY + 1));
            descriptionDisplay.setDraw(true);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!this.items.isEmpty()) {
            if (ClickGui.getInstance().iconmode.getValue() == 2 || ClickGui.getInstance().iconmode.getValue() == 3) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                if (ClickGui.getInstance().iconmode.getValue() == 2) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/setting.png"));
                }
                if (ClickGui.getInstance().iconmode.getValue() == 3) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/setting2.png"));
                }
                GlStateManager.translate(this.getX() + this.getWidth() - 6.7f, this.getY() + 7.7f - 0.3f, 0.0f);
                GlStateManager.rotate(fa((float)this.logs), 0.0f, 0.0f, 1.0f);
                drawModalRect(-5, -5, 0.0f, 0.0f, 10, 10, 10, 10, 10.0f, 10.0f);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                if (this.subOpen) {
                    float height = 1.0f;
                    this.logs += 5;
                    for (final Item item : this.items) {
                        ++Component.counter1[0];
                        if (!item.isHidden()) {
                            item.setLocation(this.x + 1.0f, this.y + (height += 15.0f));
                            item.setHeight(15);
                            item.setWidth(this.width - 9);
                            item.drawScreen(mouseX, mouseY, partialTicks);
                        }
                        item.update();
                    }
                }
                else {
                    this.logs = 0;
                }
            }
            else if (ClickGui.getInstance().iconmode.getValue() == 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                if (this.subOpen) {
                    Rebirth.textManager.drawStringlogo("_", this.x - 4.5f + this.width - 7.4f, this.y - 2.2f - RebirthGui.getClickGui().getTextOffset(), -1);
                }
                else {
                    Rebirth.textManager.drawStringlogo("A", this.x - 4.5f + this.width - 7.4f, this.y - 2.2f - RebirthGui.getClickGui().getTextOffset(), -1);
                }
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                if (this.subOpen) {
                    float height = 1.0f;
                    for (final Item item : this.items) {
                        ++Component.counter1[0];
                        if (!item.isHidden()) {
                            item.setLocation(this.x + 1.0f, this.y + (height += 15.0f));
                            item.setHeight(15);
                            item.setWidth(this.width - 9);
                            item.drawScreen(mouseX, mouseY, partialTicks);
                        }
                        item.update();
                    }
                }
            }
            else if (ClickGui.getInstance().iconmode.getValue() == 1) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/setting3.png"));
                drawCompleteImage(this.x - 1.5f + this.width - 7.4f, this.y - 2.2f - RebirthGui.getClickGui().getTextOffset(), 8, 8);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                if (this.subOpen) {
                    float height = 1.0f;
                    for (final Item item : this.items) {
                        ++Component.counter1[0];
                        if (!item.isHidden()) {
                            item.setLocation(this.x + 1.0f, this.y + (height += 15.0f));
                            item.setHeight(15);
                            item.setWidth(this.width - 9);
                            item.drawScreen(mouseX, mouseY, partialTicks);
                        }
                        item.update();
                    }
                }
            }
            else if (ClickGui.getInstance().iconmode.getValue() == 4) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                if (this.subOpen) {
                    Rebirth.textManager.drawString("-", this.x + this.width - 7.4f, this.y - 2.2f - RebirthGui.getClickGui().getTextOffset(), -1, true);
                }
                else {
                    Rebirth.textManager.drawString("+", this.x + this.width - 7.4f, this.y - 2.2f - RebirthGui.getClickGui().getTextOffset(), -1, true);
                }
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                if (this.subOpen) {
                    float height = 1.0f;
                    for (final Item item : this.items) {
                        ++Component.counter1[0];
                        if (!item.isHidden()) {
                            item.setLocation(this.x + 1.0f, this.y + (height += 15.0f));
                            item.setHeight(15);
                            item.setWidth(this.width - 9);
                            item.drawScreen(mouseX, mouseY, partialTicks);
                        }
                        item.update();
                    }
                }
            }
            else if (ClickGui.getInstance().iconmode.getValue() == 5) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                if (this.subOpen) {
                    float height = 1.0f;
                    for (final Item item : this.items) {
                        ++Component.counter1[0];
                        if (!item.isHidden()) {
                            item.setLocation(this.x + 1.0f, this.y + (height += 15.0f));
                            item.setHeight(15);
                            item.setWidth(this.width - 9);
                            item.drawScreen(mouseX, mouseY, partialTicks);
                        }
                        item.update();
                    }
                }
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                ModuleButton.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
            if (this.subOpen) {
                for (final Item item : this.items) {
                    if (item.isHidden()) {
                        continue;
                    }
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
    
    public void onKeyTyped(final char typedChar, final int keyCode) {
        super.onKeyTyped(typedChar, keyCode);
        if (!this.items.isEmpty() && this.subOpen) {
            for (final Item item : this.items) {
                if (item.isHidden()) {
                    continue;
                }
                item.onKeyTyped(typedChar, keyCode);
            }
        }
    }
    
    public int getHeight() {
        if (this.subOpen) {
            int height = 14;
            for (final Item item : this.items) {
                if (item.isHidden()) {
                    continue;
                }
                height += item.getHeight() + 1;
            }
            return height + 2;
        }
        return 14;
    }
    
    public Module getModule() {
        return this.module;
    }
    
    public void toggle() {
        this.module.toggle();
    }
    
    public boolean getState() {
        return this.module.isEnabled();
    }
}
