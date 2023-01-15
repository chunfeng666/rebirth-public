

package dev.madcat.rebirth.features.gui.components.items.buttons;

import dev.madcat.rebirth.*;
import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.features.modules.client.*;
import dev.madcat.rebirth.features.gui.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.manager.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;
import dev.madcat.rebirth.features.setting.*;

public class BindButton extends Button
{
    private final Setting setting;
    public boolean isListening;
    
    public BindButton(final Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int color = ColorUtil.toARGB(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 255);
        final float x = this.x;
        final float y = this.y;
        final float w = this.x + this.width + 7.4f;
        final float h = this.y + this.height - 0.5f;
        int color2;
        if (this.getState()) {
            color2 = (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077);
        }
        else if (!this.isHovering(mouseX, mouseY)) {
            final ColorManager colorManager = Rebirth.colorManager;
            final ModuleManager moduleManager = Rebirth.moduleManager;
            color2 = colorManager.getColorWithAlpha(ModuleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
        }
        else {
            final ColorManager colorManager2 = Rebirth.colorManager;
            final ModuleManager moduleManager2 = Rebirth.moduleManager;
            color2 = colorManager2.getColorWithAlpha(ModuleManager.getModuleByClass(ClickGui.class).alpha.getValue());
        }
        RenderUtil.drawRect(x, y, w, h, color2);
        if (this.isListening) {
            Label_0243: {
                if (!FontMod.getInstance().cfont.getValue()) {
                    final ModuleManager moduleManager3 = Rebirth.moduleManager;
                    if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                        break Label_0243;
                    }
                }
                final ModuleManager moduleManager4 = Rebirth.moduleManager;
                if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                    Rebirth.textManager.drawStringClickGui("Press a Key...", this.x + 2.3f, this.y - 0.7f - RebirthGui.getClickGui().getTextOffset(), -1);
                    return;
                }
            }
            Rebirth.textManager.drawStringWithShadow("Press a Key...", this.x + 2.3f, this.y - 1.7f - RebirthGui.getClickGui().getTextOffset(), -1);
        }
        else {
            Label_0360: {
                if (!FontMod.getInstance().cfont.getValue()) {
                    final ModuleManager moduleManager5 = Rebirth.moduleManager;
                    if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                        break Label_0360;
                    }
                }
                final ModuleManager moduleManager6 = Rebirth.moduleManager;
                if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                    Rebirth.textManager.drawStringClickGui(this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue().toString().toUpperCase(), this.x + 2.3f, this.y - 0.7f - RebirthGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
                    return;
                }
            }
            Rebirth.textManager.drawStringWithShadow(this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue().toString().toUpperCase(), this.x + 2.3f, this.y - 1.7f - RebirthGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }
    
    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            BindButton.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }
    
    @Override
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (this.isListening) {
            Bind bind = new Bind(keyCode);
            if (bind.toString().equalsIgnoreCase("Escape")) {
                return;
            }
            if (bind.toString().equalsIgnoreCase("Delete")) {
                bind = new Bind(-1);
            }
            this.setting.setValue(bind);
            this.onMouseClick();
        }
    }
    
    @Override
    public int getHeight() {
        return 14;
    }
    
    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }
    
    @Override
    public boolean getState() {
        return !this.isListening;
    }
}
