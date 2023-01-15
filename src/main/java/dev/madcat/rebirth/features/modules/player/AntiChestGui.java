
package dev.madcat.rebirth.features.modules.player;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.inventory.*;

public class AntiChestGui extends Module
{
    public AntiChestGui() {
        super("AntiChestGui", "AntiChestGui.", Module.Category.PLAYER, true, false, false);
    }
    
    public void onUpdate() {
        if (AntiChestGui.mc.player.openContainer instanceof ContainerChest) {
            AntiChestGui.mc.player.closeScreen();
        }
    }
}
