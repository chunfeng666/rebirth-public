
package dev.madcat.rebirth.features.gui.components.items.buttons;

import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.*;
import dev.madcat.rebirth.features.modules.client.*;
import dev.madcat.rebirth.util.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.features.gui.*;
import dev.madcat.rebirth.manager.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;

public class UnlimitedSlider extends Button
{
    public Setting setting;
    
    public UnlimitedSlider(final Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final float x = this.x;
        final float y = this.y;
        final float w = this.x + this.width + 7.4f;
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
        Rebirth.textManager.drawStringWithShadow(" - " + this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue() + ChatFormatting.WHITE + " +", this.x + 2.3f, this.y - 1.7f - RebirthGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            UnlimitedSlider.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            if (this.isRight(mouseX)) {
                if (this.setting.getValue() instanceof Double) {
                    this.setting.setValue(this.setting.getValue() + 1.0);
                }
                else if (this.setting.getValue() instanceof Float) {
                    this.setting.setValue(this.setting.getValue() + 1.0f);
                }
                else if (this.setting.getValue() instanceof Integer) {
                    this.setting.setValue(this.setting.getValue() + 1);
                }
            }
            else if (this.setting.getValue() instanceof Double) {
                this.setting.setValue(this.setting.getValue() - 1.0);
            }
            else if (this.setting.getValue() instanceof Float) {
                this.setting.setValue(this.setting.getValue() - 1.0f);
            }
            else if (this.setting.getValue() instanceof Integer) {
                this.setting.setValue(this.setting.getValue() - 1);
            }
        }
    }
    
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    public int getHeight() {
        return 14;
    }
    
    public void toggle() {
    }
    
    public boolean getState() {
        return true;
    }
    
    public boolean isRight(final int x) {
        return x > this.x + (this.width + 7.4f) / 2.0f;
    }
}
