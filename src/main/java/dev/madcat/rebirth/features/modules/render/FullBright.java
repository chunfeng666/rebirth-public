

package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.init.*;
import net.minecraft.potion.*;

public class FullBright extends Module
{
    public Setting<SwingMode> mode;
    float oldBright;
    
    public FullBright() {
        super("FullBright", "FullBright", Module.Category.RENDER, false, false, false);
        this.mode = (Setting<SwingMode>)this.register(new Setting("Swing", SwingMode.Gamma));
    }
    
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (this.mode.getValue() == SwingMode.Potion) {
            FullBright.mc.player.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 80950, 1, false, false)));
        }
    }
    
    public void onEnable() {
        if (nullCheck()) {
            return;
        }
        this.oldBright = FullBright.mc.gameSettings.gammaSetting;
        if (this.mode.getValue() == SwingMode.Gamma) {
            FullBright.mc.gameSettings.gammaSetting = 100.0f;
        }
    }
    
    public void onDisable() {
        FullBright.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        if (this.mode.getValue() == SwingMode.Gamma) {
            FullBright.mc.gameSettings.gammaSetting = this.oldBright;
        }
    }
    
    public enum SwingMode
    {
        Gamma, 
        Potion;
    }
}
