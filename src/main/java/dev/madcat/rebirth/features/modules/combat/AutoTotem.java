

package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import java.util.concurrent.atomic.*;
import net.minecraft.item.*;
import java.util.*;

public class AutoTotem extends Module
{
    public Setting<Boolean> soft;
    public final Setting<Float> health;
    private int numOfTotems;
    private int preferredTotemSlot;
    
    public AutoTotem() {
        super("AutoTotem", "AutoTotem", Category.COMBAT, true, false, false);
        this.soft = (Setting<Boolean>)this.register(new Setting("MainHand", false));
        this.health = (Setting<Float>)this.register(new Setting("Health", 16.0f, 0.0f, 35.9f));
    }
    
    @Override
    public void onUpdate() {
        if (AutoTotem.mc.player != null && this.findTotems()) {
            if (!this.soft.getValue()) {
                if (AutoTotem.mc.player.getHealth() + AutoTotem.mc.player.getAbsorptionAmount() <= this.health.getValue() && !AutoTotem.mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {
                    final boolean offhandEmptyPreSwitch = AutoTotem.mc.player.getHeldItemOffhand().getItem().equals(Items.AIR);
                    AutoTotem.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                    AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                    if (offhandEmptyPreSwitch) {
                        AutoTotem.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                    }
                }
            }
            else if (AutoTotem.mc.player.getHealth() + AutoTotem.mc.player.getAbsorptionAmount() <= this.health.getValue() && !AutoTotem.mc.player.getHeldItemMainhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {
                AutoTotem.mc.player.inventory.currentItem = 0;
                final boolean mainhandEmptyPreSwitch = AutoTotem.mc.player.getHeldItemMainhand().getItem().equals(Items.AIR);
                AutoTotem.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                AutoTotem.mc.playerController.windowClick(0, 36, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                if (mainhandEmptyPreSwitch) {
                    AutoTotem.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                }
            }
        }
    }
    
    private boolean findTotems() {
        this.numOfTotems = 0;
        final AtomicInteger preferredTotemSlotStackSize = new AtomicInteger();
        preferredTotemSlotStackSize.set(Integer.MIN_VALUE);
        int numOfTotemsInStack;
        final AtomicInteger atomicInteger;
        getInventoryAndHotbarSlots().forEach((slotKey, slotValue) -> {
            numOfTotemsInStack = 0;
            if (slotValue.getItem().equals(Items.TOTEM_OF_UNDYING)) {
                numOfTotemsInStack = slotValue.getCount();
                if (atomicInteger.get() < numOfTotemsInStack) {
                    atomicInteger.set(numOfTotemsInStack);
                    this.preferredTotemSlot = slotKey;
                }
            }
            this.numOfTotems += numOfTotemsInStack;
            return;
        });
        if (AutoTotem.mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {
            this.numOfTotems += AutoTotem.mc.player.getHeldItemOffhand().getCount();
        }
        return this.numOfTotems != 0;
    }
    
    private static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        return getInventorySlots(9, 44);
    }
    
    private static Map<Integer, ItemStack> getInventorySlots(int current, final int last) {
        final HashMap fullInventorySlots = new HashMap();
        while (current <= last) {
            fullInventorySlots.put(current, AutoTotem.mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return (Map<Integer, ItemStack>)fullInventorySlots;
    }
}
