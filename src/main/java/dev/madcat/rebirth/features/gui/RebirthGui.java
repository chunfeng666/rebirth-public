

package dev.madcat.rebirth.features.gui;

import dev.madcat.rebirth.features.gui.particle.*;
import dev.madcat.rebirth.features.gui.components.*;
import dev.madcat.rebirth.*;
import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.gui.components.items.buttons.*;
import dev.madcat.rebirth.features.*;
import java.util.function.*;
import java.util.*;
import dev.madcat.rebirth.features.gui.components.items.*;
import dev.madcat.rebirth.features.modules.client.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import java.io.*;

public class RebirthGui extends GuiScreen
{
    private static final DescriptionDisplay descriptionDisplay;
    private static RebirthGui RebirthGui;
    private static RebirthGui INSTANCE;
    private final ArrayList<Snow> _snowList;
    private final ArrayList<Component> components;
    
    public RebirthGui() {
        this._snowList = new ArrayList<Snow>();
        this.components = new ArrayList<Component>();
        this.setInstance();
        this.load();
    }
    
    public static RebirthGui getInstance() {
        if (dev.madcat.rebirth.features.gui.RebirthGui.INSTANCE == null) {
            dev.madcat.rebirth.features.gui.RebirthGui.INSTANCE = new RebirthGui();
        }
        return dev.madcat.rebirth.features.gui.RebirthGui.INSTANCE;
    }
    
    public static RebirthGui getClickGui() {
        final RebirthGui rebirthGui = dev.madcat.rebirth.features.gui.RebirthGui.RebirthGui;
        return getInstance();
    }
    
    private void setInstance() {
        dev.madcat.rebirth.features.gui.RebirthGui.INSTANCE = this;
    }
    
    private void load() {
        int x = -84;
        final Random random = new Random();
        for (int i = 0; i < 100; ++i) {
            for (int y = 0; y < 3; ++y) {
                final Snow snow = new Snow(25 * i, y * -50, random.nextInt(3) + 1, random.nextInt(2) + 1);
                this._snowList.add(snow);
            }
        }
        for (final Module.Category category : Rebirth.moduleManager.getCategories()) {
            final ArrayList<Component> components2 = this.components;
            final String name = category.getName();
            x += 90;
            components2.add(new Component(name, x, 34, true) {
                public void setupItems() {
                    RebirthGui$1.counter1 = new int[] { 1 };
                    Rebirth.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton((Button)new ModuleButton(module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing((Function<? super E, ? extends Comparable>)Feature::getName)));
    }
    
    public void updateModule(final Module module) {
        for (final Component component : this.components) {
            for (final Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) {
                    continue;
                }
                final ModuleButton button = (ModuleButton)item;
                final Module mod = button.getModule();
                if (module == null) {
                    continue;
                }
                if (!module.equals(mod)) {
                    continue;
                }
                button.initSettings();
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ClickGui clickGui = ClickGui.getInstance();
        dev.madcat.rebirth.features.gui.RebirthGui.descriptionDisplay.setDraw(false);
        this.checkMouseWheel();
        this.drawDefaultBackground();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
        if (dev.madcat.rebirth.features.gui.RebirthGui.descriptionDisplay.shouldDraw() && clickGui.moduleDescription.getValue()) {
            dev.madcat.rebirth.features.gui.RebirthGui.descriptionDisplay.drawScreen(mouseX, mouseY, partialTicks);
        }
        final ScaledResolution res = new ScaledResolution(this.mc);
        if (!this._snowList.isEmpty() && ClickGui.getInstance().snowing.getValue()) {
            this._snowList.forEach(snow -> snow.Update(res));
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public final ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public DescriptionDisplay getDescriptionDisplay() {
        return dev.madcat.rebirth.features.gui.RebirthGui.descriptionDisplay;
    }
    
    public void checkMouseWheel() {
        final int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        }
        else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }
    
    public int getTextOffset() {
        return -6;
    }
    
    public Component getComponentByName(final String name) {
        for (final Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return component;
        }
        return null;
    }
    
    public void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
    
    static {
        dev.madcat.rebirth.features.gui.RebirthGui.INSTANCE = new RebirthGui();
        descriptionDisplay = new DescriptionDisplay("", 0.0f, 0.0f);
    }
}
