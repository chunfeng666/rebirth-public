

package dev.madcat.rebirth.features.modules.player;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;

public class PacketEat extends Module
{
    private static PacketEat INSTANCE;
    private Setting<Boolean> autoEat;
    public final Setting<Float> health;
    public final Setting<Float> hunger;
    
    public PacketEat() {
        super("PacketEat", "PacketEat", Module.Category.PLAYER, true, false, false);
        this.autoEat = (Setting<Boolean>)this.register(new Setting("OnlyGappleAutoEAT", (T)true));
        this.health = (Setting<Float>)this.register(new Setting("Health", (T)32.0f, (T)0.0f, (T)35.9f, v -> this.autoEat.getValue()));
        this.hunger = (Setting<Float>)this.register(new Setting("Hunger", (T)19.0f, (T)0.0f, (T)19.9f, v -> this.autoEat.getValue()));
        this.setInstance();
    }
    
    public static PacketEat getInstance() {
        if (PacketEat.INSTANCE != null) {
            return PacketEat.INSTANCE;
        }
        return PacketEat.INSTANCE = new PacketEat();
    }
    
    public void onUpdate() {
        if (this.autoEat.getValue()) {
            if (PacketEat.mc.player.isCreative()) {
                return;
            }
            if (PacketEat.mc.player.getHealth() + PacketEat.mc.player.getAbsorptionAmount() <= this.health.getValue() || PacketEat.mc.player.getFoodStats().getFoodLevel() <= this.hunger.getValue()) {
                if (PacketEat.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.GOLDEN_APPLE) {
                    PacketEat.mc.playerController.processRightClick((EntityPlayer)PacketEat.mc.player, (World)PacketEat.mc.world, EnumHand.MAIN_HAND);
                    return;
                }
                if (PacketEat.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.GOLDEN_APPLE) {
                    PacketEat.mc.playerController.processRightClick((EntityPlayer)PacketEat.mc.player, (World)PacketEat.mc.world, EnumHand.OFF_HAND);
                }
            }
        }
    }
    
    private void setInstance() {
        PacketEat.INSTANCE = this;
    }
    
    static {
        PacketEat.INSTANCE = new PacketEat();
    }
}
