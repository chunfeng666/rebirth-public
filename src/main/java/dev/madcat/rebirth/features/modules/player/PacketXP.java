
package dev.madcat.rebirth.features.modules.player;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import org.lwjgl.input.*;
import net.minecraft.init.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.item.*;

public class PacketXP extends Module
{
    private final Setting<Bind> bind;
    private int delay_count;
    int prvSlot;
    public static Boolean inft;
    public static Bind binds;
    
    public PacketXP() {
        super("PacketXP", "Allows you to XP instantly", Module.Category.PLAYER, false, false, false);
        this.bind = (Setting<Bind>)this.register(new Setting("PacketBind", (T)new Bind(-1)));
    }
    
    public void onEnable() {
        this.delay_count = 0;
    }
    
    public void onUpdate() {
        PacketXP.binds = this.bind.getValue();
        if (this.findExpInHotbar() == -1) {
            return;
        }
        if (this.bind.getValue().getKey() > -1) {
            if (Keyboard.isKeyDown(this.bind.getValue().getKey()) && PacketXP.mc.currentScreen == null) {
                if (this.findExpInHotbar() == -1) {
                    return;
                }
                this.usedXp();
            }
        }
        else if (this.bind.getValue().getKey() < -1 && Mouse.isButtonDown(convertToMouse(this.bind.getValue().getKey())) && PacketXP.mc.currentScreen == null) {
            if (this.findExpInHotbar() == -1) {
                return;
            }
            this.usedXp();
        }
    }
    
    public static int convertToMouse(final int key) {
        switch (key) {
            case -2: {
                return 0;
            }
            case -3: {
                return 1;
            }
            case -4: {
                return 2;
            }
            case -5: {
                return 3;
            }
            case -6: {
                return 4;
            }
            default: {
                return -1;
            }
        }
    }
    
    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; ++i) {
            if (PacketXP.mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    private void usedXp() {
        final int oldPitch = (int)PacketXP.mc.player.rotationPitch;
        this.prvSlot = PacketXP.mc.player.inventory.currentItem;
        PacketXP.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.findExpInHotbar()));
        PacketXP.mc.player.rotationPitch = -90.0f;
        PacketXP.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        PacketXP.mc.player.rotationPitch = (float)oldPitch;
        PacketXP.mc.player.inventory.currentItem = this.prvSlot;
        PacketXP.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.prvSlot));
    }
    
    public Boolean notInInv(final Item itemOfChoice) {
        int n = 0;
        if (itemOfChoice == PacketXP.mc.player.getHeldItemOffhand().getItem()) {
            return true;
        }
        for (int i = 35; i >= 0; --i) {
            final Item item = PacketXP.mc.player.inventory.getStackInSlot(i).getItem();
            if (item == itemOfChoice) {
                return true;
            }
            if (item != itemOfChoice) {
                ++n;
            }
        }
        if (n >= 35) {
            return false;
        }
        return true;
    }
    
    static {
        PacketXP.inft = false;
    }
}
