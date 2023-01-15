
package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import java.util.function.*;
import net.minecraft.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.text.*;

public class AutoLog extends Module
{
    private final Setting<Float> health;
    public Setting<Integer> totems;
    private final Setting<Boolean> logout;
    
    public AutoLog() {
        super("AutoLog", "Logout when in danger.", Category.MISC, false, false, false);
        this.health = (Setting<Float>)this.register(new Setting("Health", 16.0f, 0.1f, 36.0f));
        this.totems = (Setting<Integer>)this.register(new Setting("Totems", 0, 0, 10));
        this.logout = (Setting<Boolean>)this.register(new Setting("LogoutOff", true));
    }
    
    @Override
    public void onTick() {
        int totems = AutoLog.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (AutoLog.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems += AutoLog.mc.player.getHeldItemOffhand().getCount();
        }
        if (AutoLog.mc.player.getHealth() <= this.health.getValue() && (totems <= (float)this.totems.getValue() || totems == (float)this.totems.getValue())) {
            Minecraft.getMinecraft().getConnection().handleDisconnect(new SPacketDisconnect((ITextComponent)new TextComponentString("Internal Exception: java.lang.NullPointerException")));
            if (this.logout.getValue()) {
                this.disable();
            }
        }
    }
}
