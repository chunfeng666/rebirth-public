

package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import java.util.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.features.command.*;

public class PopCounter extends Module
{
    public static HashMap<String, Integer> TotemPopContainer;
    private static PopCounter INSTANCE;
    
    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Category.MISC, true, false, false);
        this.setInstance();
    }
    
    public static PopCounter getInstance() {
        if (PopCounter.INSTANCE == null) {
            PopCounter.INSTANCE = new PopCounter();
        }
        return PopCounter.INSTANCE;
    }
    
    private void setInstance() {
        PopCounter.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        PopCounter.TotemPopContainer.clear();
    }
    
    public void onDeath(final EntityPlayer player) {
        if (PopCounter.TotemPopContainer.containsKey(player.getName())) {
            final int l_Count = PopCounter.TotemPopContainer.get(player.getName());
            PopCounter.TotemPopContainer.remove(player.getName());
            if (l_Count == 1) {
                if (PopCounter.mc.player.equals((Object)player)) {
                    if (Rebirth.moduleManager.isModuleEnabled("PopCounter")) {
                        Command.sendMessage(ChatFormatting.BLUE + "You died after popping " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totem!");
                    }
                }
                else if (Rebirth.moduleManager.isModuleEnabled("PopCounter")) {
                    Command.sendMessage(ChatFormatting.RED + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totem!");
                }
            }
            else if (PopCounter.mc.player.equals((Object)player)) {
                if (Rebirth.moduleManager.isModuleEnabled("PopCounter")) {
                    Command.sendMessage(ChatFormatting.BLUE + "You died after popping " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totems!");
                }
            }
            else if (Rebirth.moduleManager.isModuleEnabled("PopCounter")) {
                Command.sendMessage(ChatFormatting.RED + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totems!");
            }
        }
    }
    
    public void onTotemPop(final EntityPlayer player) {
        if (fullNullCheck()) {
            return;
        }
        int l_Count = 1;
        if (PopCounter.TotemPopContainer.containsKey(player.getName())) {
            l_Count = PopCounter.TotemPopContainer.get(player.getName());
            PopCounter.TotemPopContainer.put(player.getName(), ++l_Count);
        }
        else {
            PopCounter.TotemPopContainer.put(player.getName(), l_Count);
        }
        if (l_Count == 1) {
            if (PopCounter.mc.player.equals((Object)player)) {
                if (Rebirth.moduleManager.isModuleEnabled("PopCounter")) {
                    Command.sendMessage(ChatFormatting.BLUE + "You popped " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totem.");
                }
            }
            else if (Rebirth.moduleManager.isModuleEnabled("PopCounter")) {
                Command.sendMessage(ChatFormatting.RED + player.getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totem.");
            }
        }
        else if (PopCounter.mc.player.equals((Object)player)) {
            if (Rebirth.moduleManager.isModuleEnabled("PopCounter")) {
                Command.sendMessage(ChatFormatting.BLUE + "You popped " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totems.");
            }
        }
        else if (Rebirth.moduleManager.isModuleEnabled("PopCounter")) {
            Command.sendMessage(ChatFormatting.RED + player.getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totems.");
        }
    }
    
    static {
        PopCounter.TotemPopContainer = new HashMap<String, Integer>();
        PopCounter.INSTANCE = new PopCounter();
    }
}
