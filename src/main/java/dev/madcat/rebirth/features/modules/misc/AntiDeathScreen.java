

package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.client.gui.*;

public class AntiDeathScreen extends Module
{
    public AntiDeathScreen() {
        super("AntiDeathScreen", "AntiDeathScreen", Category.MISC, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (AntiDeathScreen.mc.currentScreen instanceof GuiGameOver) {
            AntiDeathScreen.mc.player.respawnPlayer();
            AntiDeathScreen.mc.displayGuiScreen((GuiScreen)null);
        }
    }
}
