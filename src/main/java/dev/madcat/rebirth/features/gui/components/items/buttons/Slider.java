

package dev.madcat.rebirth.features.gui.components.items.buttons;

import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.*;
import dev.madcat.rebirth.features.modules.client.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.features.gui.*;
import dev.madcat.rebirth.manager.*;
import dev.madcat.rebirth.features.gui.components.*;
import java.util.*;
import org.lwjgl.input.*;

public class Slider extends Button
{
    private final Number min;
    private final Number max;
    private final int difference;
    public Setting setting;
    
    public Slider(final Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.min = setting.getMin();
        this.max = setting.getMax();
        this.difference = this.max.intValue() - this.min.intValue();
        this.width = 15;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.dragSetting(mouseX, mouseY);
        RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077);
        final float x = this.x;
        final float y = this.y;
        final float w = (this.setting.getValue().floatValue() <= this.min.floatValue()) ? this.x : (this.x + (this.width + 7.4f) * this.partialMultiplier());
        final float h = this.y + this.height - 0.5f;
        int color;
        if (!this.isHovering(mouseX, mouseY)) {
            final ColorManager colorManager = Rebirth.colorManager;
            final ModuleManager moduleManager = Rebirth.moduleManager;
            color = colorManager.getColorWithAlpha(ModuleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
        }
        else {
            final ColorManager colorManager2 = Rebirth.colorManager;
            final ModuleManager moduleManager2 = Rebirth.moduleManager;
            color = colorManager2.getColorWithAlpha(ModuleManager.getModuleByClass(ClickGui.class).alpha.getValue());
        }
        RenderUtil.drawRect(x, y, w, h, color);
        Label_0252: {
            if (!FontMod.getInstance().cfont.getValue()) {
                final ModuleManager moduleManager3 = Rebirth.moduleManager;
                if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                    break Label_0252;
                }
            }
            final ModuleManager moduleManager4 = Rebirth.moduleManager;
            if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                Rebirth.textManager.drawStringClickGui(this.getName() + " " + ChatFormatting.GRAY + ((this.setting.getValue() instanceof Float) ? this.setting.getValue() : this.setting.getValue().doubleValue()), this.x + 2.3f, this.y - 0.7f - RebirthGui.getClickGui().getTextOffset(), -1);
                return;
            }
        }
        Rebirth.textManager.drawStringWithShadow(this.getName() + " " + ChatFormatting.GRAY + ((this.setting.getValue() instanceof Float) ? this.setting.getValue() : this.setting.getValue().doubleValue()), this.x + 2.3f, this.y - 1.7f - RebirthGui.getClickGui().getTextOffset(), -1);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            this.setSettingFromX(mouseX);
        }
    }
    
    public boolean isHovering(final int mouseX, final int mouseY) {
        for (final Component component : RebirthGui.getClickGui().getComponents()) {
            if (!component.drag) {
                continue;
            }
            return false;
        }
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() + 8.0f && mouseY >= this.getY() && mouseY <= this.getY() + this.height;
    }
    
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    private void dragSetting(final int mouseX, final int mouseY) {
        if (this.isHovering(mouseX, mouseY) && Mouse.isButtonDown(0)) {
            this.setSettingFromX(mouseX);
        }
    }
    
    public int getHeight() {
        return 14;
    }
    
    private void setSettingFromX(final int mouseX) {
        final float percent = (mouseX - this.x) / (this.width + 7.4f);
        if (this.setting.getValue() instanceof Double) {
            final double result = this.setting.getMin() + this.difference * percent;
            this.setting.setValue(Math.round(10.0 * result) / 10.0);
        }
        else if (this.setting.getValue() instanceof Float) {
            final float result2 = this.setting.getMin() + this.difference * percent;
            this.setting.setValue(Math.round(10.0f * result2) / 10.0f);
        }
        else if (this.setting.getValue() instanceof Integer) {
            this.setting.setValue(this.setting.getMin() + (int)(this.difference * percent));
        }
    }
    
    private float middle() {
        return this.max.floatValue() - this.min.floatValue();
    }
    
    private float part() {
        return this.setting.getValue().floatValue() - this.min.floatValue();
    }
    
    private float partialMultiplier() {
        return this.part() / this.middle();
    }
}
