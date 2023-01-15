

package dev.madcat.rebirth.features.gui.components.items.buttons;

import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.*;
import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.features.modules.client.*;
import dev.madcat.rebirth.features.gui.*;
import dev.madcat.rebirth.manager.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;

public class BooleanButton extends Button
{
    private final Setting setting;
    
    public BooleanButton(final Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final float x = this.x;
        final float y = this.y;
        final float w = this.x + this.width + 7.4f;
        final float h = this.y + this.height - 0.5f;
        int color;
        if (this.getState()) {
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
        }
        else {
            color = (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077);
        }
        RenderUtil.drawRect(x, y, w, h, color);
        Label_0183: {
            if (!FontMod.getInstance().cfont.getValue()) {
                final ModuleManager moduleManager3 = Rebirth.moduleManager;
                if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                    break Label_0183;
                }
            }
            final ModuleManager moduleManager4 = Rebirth.moduleManager;
            if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                Rebirth.textManager.drawStringClickGui(this.getName(), this.x + 2.3f, this.y - 0.7f - RebirthGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
                return;
            }
        }
        Rebirth.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 1.7f - RebirthGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
    }
    
    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            BooleanButton.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }
    
    @Override
    public int getHeight() {
        return 14;
    }
    
    @Override
    public void toggle() {
        this.setting.setValue(!this.setting.getValue());
    }
    
    @Override
    public boolean getState() {
        return this.setting.getValue();
    }
}
