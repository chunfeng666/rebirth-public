

package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import dev.madcat.rebirth.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class WebTime extends Module
{
    private final Setting<Float> speed;
    
    public WebTime() {
        super("WebTime", "Stops you being slowed down by webs", Module.Category.MOVEMENT, true, false, false);
        this.speed = (Setting<Float>)this.register(new Setting("Factor", 2.0f, 1.0f, 10.0f));
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (WebTime.mc.player.isInWeb) {
            WebTime.mc.timer.tickLength = 50.0f / this.speed.getValue();
        }
        else if (!Rebirth.moduleManager.isModuleEnabled("TickShift") && !Rebirth.moduleManager.isModuleEnabled("TickShift+")) {
            WebTime.mc.timer.tickLength = 50.0f;
        }
    }
    
    public void onDisable() {
        WebTime.mc.timer.tickLength = 50.0f;
    }
}
