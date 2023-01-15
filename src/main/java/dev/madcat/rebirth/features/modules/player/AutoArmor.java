

package dev.madcat.rebirth.features.modules.player;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.manager.*;
import java.util.concurrent.atomic.*;
import net.minecraft.item.*;
import java.util.*;

public class AutoArmor extends Module
{
    private int numOfTotems;
    private int preferredTotemSlot;
    private static AutoArmor INSTANCE;
    
    public AutoArmor() {
        super("AutoArmor", "Tweaks for wear armors", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }
    
    private void setInstance() {
        AutoArmor.INSTANCE = this;
    }
    
    public static AutoArmor getInstance() {
        if (AutoArmor.INSTANCE == null) {
            AutoArmor.INSTANCE = new AutoArmor();
        }
        return AutoArmor.INSTANCE;
    }
    
    public void onUpdate() {
        if (AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_HELMET, 5)) {
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, 5, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
            return;
        }
        if (AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_CHESTPLATE, 6)) {
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
            return;
        }
        if (AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_LEGGINGS, 7)) {
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, 7, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
            return;
        }
        if (AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_BOOTS, 8)) {
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, 8, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
            return;
        }
        if (AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA) && AutoArmor.mc.player.onGround && !ModuleManager.getModuleByName("FastElytra").isEnabled() && this.findTotems(Items.DIAMOND_CHESTPLATE, 6)) {
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.player);
        }
    }
    
    private boolean findTotems(final ItemArmor item, final int slotId) {
        this.numOfTotems = 0;
        final AtomicInteger preferredTotemSlotStackSize = new AtomicInteger();
        preferredTotemSlotStackSize.set(Integer.MIN_VALUE);
        int numOfTotemsInStack;
        final AtomicInteger atomicInteger;
        getInventorySlots(9).forEach((slotKey, slotValue) -> {
            numOfTotemsInStack = 0;
            if (slotValue.getItem().equals(item)) {
                numOfTotemsInStack = slotValue.getCount();
                if (atomicInteger.get() < numOfTotemsInStack) {
                    atomicInteger.set(numOfTotemsInStack);
                    this.preferredTotemSlot = slotKey;
                }
            }
            this.numOfTotems += numOfTotemsInStack;
            return;
        });
        if (AutoArmor.mc.player.inventoryContainer.getSlot(slotId).getStack().getItem().equals(Items.DIAMOND_HELMET)) {
            ++this.numOfTotems;
        }
        return this.numOfTotems != 0;
    }
    
    private static Map<Integer, ItemStack> getInventorySlots(int current) {
        final HashMap fullInventorySlots = new HashMap();
        while (current <= 44) {
            fullInventorySlots.put(current, AutoArmor.mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return (Map<Integer, ItemStack>)fullInventorySlots;
    }
    
    static {
        AutoArmor.INSTANCE = new AutoArmor();
    }
}
