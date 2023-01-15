

package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;

public class Sprint extends Module
{
    public Setting<Boolean> shift;
    
    public Sprint() {
        super("Sprint", "Force sprint.", Module.Category.MOVEMENT, true, false, false);
        this.shift = (Setting<Boolean>)this.register(new Setting("Stair++", false));
    }
    
    public void onTick() {
        if ((Sprint.mc.player.moveForward != 0.0f || Sprint.mc.player.moveStrafing != 0.0f) && !Sprint.mc.player.isSprinting()) {
            Sprint.mc.player.setSprinting(true);
        }
        if (this.shift.getValue() && Sprint.mc.player.onGround && Sprint.mc.player.posY - Math.floor(Sprint.mc.player.posY) > 0.0 && Sprint.mc.player.moveForward != 0.0f) {
            Sprint.mc.player.jump();
        }
    }
}
