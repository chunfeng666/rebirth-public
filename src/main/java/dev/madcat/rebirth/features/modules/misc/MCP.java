
package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import org.lwjgl.input.*;
import net.minecraft.item.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;

public class MCP extends Module
{
    private boolean clicked;
    
    public MCP() {
        super("MCP", "Throws a pearl", Category.MISC, false, false, false);
        this.clicked = false;
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }
    }
    
    @Override
    public void onTick() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked) {
                this.throwPearl();
            }
            this.clicked = true;
        }
        else {
            this.clicked = false;
        }
    }
    
    private void throwPearl() {
        final int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        final boolean bl;
        final boolean offhand = bl = (MCP.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL);
        if (pearlSlot != -1 || offhand) {
            final int oldslot = MCP.mc.player.inventory.currentItem;
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
            }
            MCP.mc.playerController.processRightClick((EntityPlayer)MCP.mc.player, (World)MCP.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }
}
