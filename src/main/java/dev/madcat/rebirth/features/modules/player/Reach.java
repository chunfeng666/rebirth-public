
package dev.madcat.rebirth.features.modules.player;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.entity.player.*;

public class Reach extends Module
{
    private final Setting<Integer> Reach;
    
    public Reach() {
        super("Reach", "reach", Module.Category.PLAYER, true, false, false);
        this.Reach = (Setting<Integer>)this.register(new Setting("Reach", (T)6, (T)5, (T)10));
    }
    
    public void onUpdate() {
        dev.madcat.rebirth.features.modules.player.Reach.mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue((double)this.Reach.getValue());
    }
    
    public void onDisable() {
        dev.madcat.rebirth.features.modules.player.Reach.mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(5.0);
    }
}
