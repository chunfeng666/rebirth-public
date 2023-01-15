

package dev.madcat.rebirth.features.modules.client;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import java.awt.*;
import dev.madcat.rebirth.features.command.*;
import dev.madcat.rebirth.event.events.*;
import com.mojang.realmsclient.gui.*;
import net.minecraftforge.fml.common.eventhandler.*;
import dev.madcat.rebirth.*;

public class FontMod extends Module
{
    private static FontMod INSTANCE;
    public Setting<Boolean> cfont;
    public Setting<Boolean> clientFont;
    public Setting<String> fontName;
    public Setting<Boolean> antiAlias;
    public Setting<Boolean> fractionalMetrics;
    public Setting<Integer> fontSize;
    public Setting<Integer> fontStyle;
    private boolean reloadFont;
    
    public FontMod() {
        super("CustomFont", "Modify the font of client text.", Category.CLIENT, true, false, false);
        this.cfont = (Setting<Boolean>)this.register(new Setting("ClickGuiFont", Boolean.TRUE));
        this.clientFont = (Setting<Boolean>)this.register(new Setting("ClientFont", Boolean.TRUE, "test."));
        this.fontName = (Setting<String>)this.register(new Setting("FontName", "Arial", v -> this.clientFont.getValue()));
        this.antiAlias = (Setting<Boolean>)this.register(new Setting("AntiAlias", Boolean.TRUE, v -> this.clientFont.getValue()));
        this.fractionalMetrics = (Setting<Boolean>)this.register(new Setting("Metrics", Boolean.TRUE, v -> this.clientFont.getValue()));
        this.fontSize = (Setting<Integer>)this.register(new Setting("Size", 18, 12, 30, v -> this.clientFont.getValue()));
        this.fontStyle = (Setting<Integer>)this.register(new Setting("Style", 0, 0, 3, v -> this.clientFont.getValue()));
        this.reloadFont = false;
        this.setInstance();
    }
    
    public static FontMod getInstance() {
        if (FontMod.INSTANCE == null) {
            FontMod.INSTANCE = new FontMod();
        }
        return FontMod.INSTANCE;
    }
    
    public static boolean checkFont(final String font, final boolean message) {
        for (final String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            if (!message && s.equals(font)) {
                return true;
            }
            if (message) {
                Command.sendMessage(s);
            }
        }
        return false;
    }
    
    private void setInstance() {
        FontMod.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (!this.clientFont.getValue()) {
            return;
        }
        final Setting setting;
        if (event.getStage() == 2 && (setting = event.getSetting()) != null && setting.getFeature().equals(this)) {
            if (setting.getName().equals("FontName") && !checkFont(setting.getPlannedValue().toString(), false)) {
                Command.sendMessage(ChatFormatting.RED + "That font doesnt exist.");
                event.setCanceled(true);
                return;
            }
            this.reloadFont = true;
        }
    }
    
    @Override
    public void onTick() {
        if (!this.clientFont.getValue()) {
            return;
        }
        if (this.reloadFont) {
            Rebirth.textManager.init(false);
            this.reloadFont = false;
        }
    }
    
    static {
        FontMod.INSTANCE = new FontMod();
    }
}
