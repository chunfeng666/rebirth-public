
package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AntiWeb extends Module
{
    private final Setting<Float> speed;
    
    public AntiWeb() {
        super("AntiWeb", "Stops you being slowed down by webs", Module.Category.MOVEMENT, true, false, false);
        this.speed = (Setting<Float>)this.register(new Setting("Factor", 10.0f, 1.0f, 10.0f));
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            return;
        }
        if (AntiWeb.mc.player.isInWeb) {
            final double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
            AntiWeb.mc.player.motionX = calc[0];
            AntiWeb.mc.player.motionZ = calc[1];
            if (AntiWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP player = AntiWeb.mc.player;
                player.motionY -= this.speed.getValue() / 10.0f;
            }
        }
    }
}
