

package dev.madcat.rebirth.features.modules.player;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;

public class FastPlace extends Module
{
    private final Setting<Integer> timeLimit;
    
    public FastPlace() {
        super("FastPlace", "Fast everything.", Module.Category.PLAYER, true, false, false);
        this.timeLimit = (Setting<Integer>)this.register(new Setting("Time", 0, 0, 5));
    }
    
    public void onUpdate() {
        FastPlace.mc.rightClickDelayTimer = this.timeLimit.getValue();
    }
}
