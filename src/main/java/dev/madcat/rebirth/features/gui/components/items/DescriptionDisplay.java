

package dev.madcat.rebirth.features.gui.components.items;

import dev.madcat.rebirth.features.modules.client.*;
import dev.madcat.rebirth.*;
import dev.madcat.rebirth.manager.*;
import dev.madcat.rebirth.util.*;

public class DescriptionDisplay extends Item
{
    private String description;
    private boolean draw;
    
    public DescriptionDisplay(final String description, final float x, final float y) {
        super("DescriptionDisplay");
        this.description = description;
        this.setLocation(x, y);
        Label_0100: {
            Label_0065: {
                if (!FontMod.getInstance().cfont.getValue()) {
                    final ModuleManager moduleManager = Rebirth.moduleManager;
                    if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                        break Label_0065;
                    }
                }
                final ModuleManager moduleManager2 = Rebirth.moduleManager;
                if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                    this.width = Rebirth.textManager.getStringCWidth(this.description) + 4;
                    break Label_0100;
                }
            }
            this.width = Rebirth.textManager.getStringWidth(this.description) + 4;
        }
        this.height = Rebirth.textManager.getFontHeight() + 4;
        this.draw = false;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Label_0083: {
            Label_0048: {
                if (!FontMod.getInstance().cfont.getValue()) {
                    final ModuleManager moduleManager = Rebirth.moduleManager;
                    if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                        break Label_0048;
                    }
                }
                final ModuleManager moduleManager2 = Rebirth.moduleManager;
                if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                    this.width = Rebirth.textManager.getStringCWidth(this.description) + 5;
                    break Label_0083;
                }
            }
            this.width = Rebirth.textManager.getStringWidth(this.description) + 4;
        }
        this.height = Rebirth.textManager.getFontHeight() + 4;
        RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -704643072);
        Label_0176: {
            if (!FontMod.getInstance().cfont.getValue()) {
                final ModuleManager moduleManager3 = Rebirth.moduleManager;
                if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                    break Label_0176;
                }
            }
            final ModuleManager moduleManager4 = Rebirth.moduleManager;
            if (ModuleManager.getModuleByName("CustomFont").isEnabled()) {
                Rebirth.textManager.drawStringClickGui(this.description, this.x + 2.0f, this.y + 3.0f, 16777215);
                return;
            }
        }
        Rebirth.textManager.drawString(this.description, this.x + 2.0f, this.y + 2.0f, 16777215, true);
    }
    
    public boolean shouldDraw() {
        return this.draw;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setDraw(final boolean draw) {
        this.draw = draw;
    }
}
