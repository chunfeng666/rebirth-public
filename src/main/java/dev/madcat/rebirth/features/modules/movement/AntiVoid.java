
package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.features.command.*;

public class AntiVoid extends Module
{
    public Setting<Mode> mode;
    public Setting<Boolean> display;
    
    public AntiVoid() {
        super("AntiVoid", "Glitches you up from void.", Module.Category.MOVEMENT, false, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.BOUNCE));
        this.display = (Setting<Boolean>)this.register(new Setting("Display", true));
    }
    
    public void onUpdate() {
        final double yLevel = AntiVoid.mc.player.posY;
        if (yLevel <= 0.5) {
            Command.sendMessage(ChatFormatting.RED + "Player " + ChatFormatting.GREEN + AntiVoid.mc.player.getName() + ChatFormatting.RED + " is in the void!");
            if (this.mode.getValue().equals(Mode.BOUNCE)) {
                AntiVoid.mc.player.moveVertical = 10.0f;
                AntiVoid.mc.player.jump();
            }
            if (this.mode.getValue().equals(Mode.LAUNCH)) {
                AntiVoid.mc.player.moveVertical = 100.0f;
                AntiVoid.mc.player.jump();
            }
        }
        else {
            AntiVoid.mc.player.moveVertical = 0.0f;
        }
    }
    
    public void onDisable() {
        AntiVoid.mc.player.moveVertical = 0.0f;
    }
    
    public String getDisplayInfo() {
        if (this.display.getValue()) {
            if (this.mode.getValue().equals(Mode.BOUNCE)) {
                return "Bounce";
            }
            if (this.mode.getValue().equals(Mode.LAUNCH)) {
                return "Launch";
            }
        }
        return null;
    }
    
    public enum Mode
    {
        BOUNCE, 
        LAUNCH;
    }
}
