
package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.potion.*;

public class Animations extends Module
{
    int slowmode;
    private final Setting<Mode> mode;
    private final Setting<Swing> swing;
    private final Setting<Boolean> slow;
    
    public Animations() {
        super("Animations", "Change animations.", Module.Category.RENDER, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("OldAnimations", Mode.OneDotEight));
        this.swing = (Setting<Swing>)this.register(new Setting("Swing", Swing.Mainhand));
        this.slow = (Setting<Boolean>)this.register(new Setting("Slow", false));
    }
    
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (this.swing.getValue() == Swing.Offhand) {
            Animations.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (this.mode.getValue() == Mode.OneDotEight) {
            Animations.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            Animations.mc.entityRenderer.itemRenderer.itemStackMainHand = Animations.mc.player.getHeldItemMainhand();
        }
        if (this.slow.getValue() && this.slowmode == 0) {
            Animations.mc.player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 255000, 3));
            this.slowmode = 1;
        }
        if (!this.slow.getValue() && this.slowmode == 1) {
            Animations.mc.player.removePotionEffect(MobEffects.MINING_FATIGUE);
            this.slowmode = 0;
        }
    }
    
    public void onEnable() {
        if (this.slow.getValue()) {
            this.slowmode = 1;
            Animations.mc.player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 255000, 3));
        }
        else {
            this.slowmode = 0;
            Animations.mc.player.removePotionEffect(MobEffects.MINING_FATIGUE);
        }
    }
    
    public void onDisable() {
        if (this.slowmode == 1) {
            Animations.mc.player.removePotionEffect(MobEffects.MINING_FATIGUE);
            this.slowmode = 0;
        }
    }
    
    private enum Swing
    {
        Mainhand, 
        Offhand;
    }
    
    private enum Mode
    {
        Normal, 
        OneDotEight;
    }
}
