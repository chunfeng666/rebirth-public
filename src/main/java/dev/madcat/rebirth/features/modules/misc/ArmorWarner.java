

package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.event.events.*;
import dev.madcat.rebirth.*;
import net.minecraft.item.*;
import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.features.command.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.init.*;

public class ArmorWarner extends Module
{
    private final Setting<Integer> armorThreshhold;
    private final Setting<Boolean> notifySelf;
    private final Map<EntityPlayer, Integer> entityArmorArraylist;
    private final Timer timer;
    
    public ArmorWarner() {
        super("ArmorWarner", "Message friends when their armor is low durable.", Category.MISC, true, false, false);
        this.armorThreshhold = (Setting<Integer>)this.register(new Setting("Armor%", 20, 1, 100));
        this.notifySelf = (Setting<Boolean>)this.register(new Setting("NotifySelf", true));
        this.entityArmorArraylist = new HashMap<EntityPlayer, Integer>();
        this.timer = new Timer();
    }
    
    @SubscribeEvent
    public void onUpdate(final UpdateWalkingPlayerEvent event) {
        for (final EntityPlayer player : ArmorWarner.mc.world.playerEntities) {
            if (!player.isDead) {
                if (!Rebirth.friendManager.isFriend(player.getName())) {
                    continue;
                }
                for (final ItemStack stack : player.inventory.armorInventory) {
                    if (stack == ItemStack.EMPTY) {
                        continue;
                    }
                    final int percent = DamageUtil.getRoundedDamage(stack);
                    if (percent <= this.armorThreshhold.getValue() && !this.entityArmorArraylist.containsKey(player)) {
                        if (player == ArmorWarner.mc.player && this.notifySelf.getValue()) {
                            Command.sendMessage(player.getName() + " watchout your " + this.getArmorPieceName(stack) + " low durable!");
                        }
                        else {
                            ArmorWarner.mc.player.sendChatMessage("/msg " + player.getName() + " " + player.getName() + " watchout your " + this.getArmorPieceName(stack) + " low durable!");
                        }
                        this.entityArmorArraylist.put(player, player.inventory.armorInventory.indexOf((Object)stack));
                    }
                    if (!this.entityArmorArraylist.containsKey(player) || this.entityArmorArraylist.get(player) != player.inventory.armorInventory.indexOf((Object)stack)) {
                        continue;
                    }
                    if (percent <= this.armorThreshhold.getValue()) {
                        continue;
                    }
                    this.entityArmorArraylist.remove(player);
                }
                if (!this.entityArmorArraylist.containsKey(player)) {
                    continue;
                }
                if (player.inventory.armorInventory.get((int)this.entityArmorArraylist.get(player)) != ItemStack.EMPTY) {
                    continue;
                }
                this.entityArmorArraylist.remove(player);
            }
        }
    }
    
    private String getArmorPieceName(final ItemStack stack) {
        if (stack.getItem() == Items.DIAMOND_HELMET || stack.getItem() == Items.GOLDEN_HELMET || stack.getItem() == Items.IRON_HELMET || stack.getItem() == Items.CHAINMAIL_HELMET || stack.getItem() == Items.LEATHER_HELMET) {
            return "helmet is";
        }
        if (stack.getItem() == Items.DIAMOND_CHESTPLATE || stack.getItem() == Items.GOLDEN_CHESTPLATE || stack.getItem() == Items.IRON_CHESTPLATE || stack.getItem() == Items.CHAINMAIL_CHESTPLATE || stack.getItem() == Items.LEATHER_CHESTPLATE) {
            return "chestplate is";
        }
        if (stack.getItem() == Items.DIAMOND_LEGGINGS || stack.getItem() == Items.GOLDEN_LEGGINGS || stack.getItem() == Items.IRON_LEGGINGS || stack.getItem() == Items.CHAINMAIL_LEGGINGS || stack.getItem() == Items.LEATHER_LEGGINGS) {
            return "leggings are";
        }
        return "boots are";
    }
}
