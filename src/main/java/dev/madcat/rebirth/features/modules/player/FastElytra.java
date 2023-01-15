
package dev.madcat.rebirth.features.modules.player;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import java.util.concurrent.atomic.*;
import net.minecraft.item.*;
import java.util.*;

public class FastElytra extends Module
{
    private int numOfTotems;
    private int preferredTotemSlot;
    private static FastElytra INSTANCE;
    
    public FastElytra() {
        super("FastElytra", "Tweaks for wear Elytra", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }
    
    private void setInstance() {
        FastElytra.INSTANCE = this;
    }
    
    public static FastElytra getInstance() {
        if (FastElytra.INSTANCE == null) {
            FastElytra.INSTANCE = new FastElytra();
        }
        return FastElytra.INSTANCE;
    }
    
    public void onUpdate() {
        if (!FastElytra.mc.player.onGround) {
            if (this.findTotems() && !FastElytra.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA)) {
                FastElytra.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)FastElytra.mc.player);
                FastElytra.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, (EntityPlayer)FastElytra.mc.player);
                FastElytra.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)FastElytra.mc.player);
            }
        }
        else if (this.findTotems2() && FastElytra.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA)) {
            FastElytra.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)FastElytra.mc.player);
            FastElytra.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, (EntityPlayer)FastElytra.mc.player);
            FastElytra.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)FastElytra.mc.player);
        }
    }
    
    private boolean findTotems() {
        this.numOfTotems = 0;
        final AtomicInteger preferredTotemSlotStackSize = new AtomicInteger();
        preferredTotemSlotStackSize.set(Integer.MIN_VALUE);
        int numOfTotemsInStack;
        final AtomicInteger atomicInteger;
        getInventorySlots(9).forEach((slotKey, slotValue) -> {
            numOfTotemsInStack = 0;
            if (slotValue.getItem().equals(Items.ELYTRA)) {
                numOfTotemsInStack = slotValue.getCount();
                if (atomicInteger.get() < numOfTotemsInStack) {
                    atomicInteger.set(numOfTotemsInStack);
                    this.preferredTotemSlot = slotKey;
                }
            }
            this.numOfTotems += numOfTotemsInStack;
            return;
        });
        if (FastElytra.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA)) {
            ++this.numOfTotems;
        }
        return this.numOfTotems != 0;
    }
    
    private boolean findTotems2() {
        this.numOfTotems = 0;
        final AtomicInteger preferredTotemSlotStackSize = new AtomicInteger();
        preferredTotemSlotStackSize.set(Integer.MIN_VALUE);
        int numOfTotemsInStack;
        final AtomicInteger atomicInteger;
        getInventorySlots(9).forEach((slotKey, slotValue) -> {
            numOfTotemsInStack = 0;
            if (slotValue.getItem().equals(Items.DIAMOND_CHESTPLATE)) {
                numOfTotemsInStack = slotValue.getCount();
                if (atomicInteger.get() < numOfTotemsInStack) {
                    atomicInteger.set(numOfTotemsInStack);
                    this.preferredTotemSlot = slotKey;
                }
            }
            this.numOfTotems += numOfTotemsInStack;
            return;
        });
        if (FastElytra.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.DIAMOND_CHESTPLATE)) {
            ++this.numOfTotems;
        }
        return this.numOfTotems != 0;
    }
    
    private static Map<Integer, ItemStack> getInventorySlots(int current) {
        final HashMap fullInventorySlots = new HashMap();
        while (current <= 44) {
            fullInventorySlots.put(current, FastElytra.mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return (Map<Integer, ItemStack>)fullInventorySlots;
    }
    
    static {
        FastElytra.INSTANCE = new FastElytra();
    }
}
