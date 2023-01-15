

package dev.madcat.rebirth.util;

import net.minecraftforge.fml.common.gameevent.*;
import dev.madcat.rebirth.*;
import org.lwjgl.opengl.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Title
{
    int ticks;
    int bruh;
    int breakTimer;
    String bruh1;
    boolean qwerty;
    
    public Title() {
        this.ticks = 0;
        this.bruh = 0;
        this.breakTimer = 0;
        this.qwerty = false;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.bruh1 != null && this.bruh1 != dev.madcat.rebirth.features.modules.client.Title.getInstance().titleName.getValue()) {
            this.ticks = 0;
            this.bruh = 0;
            this.breakTimer = 0;
            this.qwerty = false;
        }
        this.bruh1 = (String)dev.madcat.rebirth.features.modules.client.Title.getInstance().titleName.getValue();
        if (Rebirth.moduleManager.isModuleEnabled("ClientTitle")) {
            if (this.bruh1 != null) {
                ++this.ticks;
                if (this.ticks % 17 == 0) {
                    Display.setTitle(this.bruh1.substring(0, this.bruh1.length() - this.bruh));
                    if ((this.bruh == this.bruh1.length() && this.breakTimer != 2) || (this.bruh == 0 && this.breakTimer != 4)) {
                        ++this.breakTimer;
                        return;
                    }
                    this.breakTimer = 0;
                    if (this.bruh == this.bruh1.length()) {
                        this.qwerty = true;
                    }
                    if (this.qwerty) {
                        --this.bruh;
                    }
                    else {
                        ++this.bruh;
                    }
                    if (this.bruh == 0) {
                        this.qwerty = false;
                    }
                }
            }
        }
        else {
            Display.setTitle("Minecraft 1.12.2");
        }
    }
}
