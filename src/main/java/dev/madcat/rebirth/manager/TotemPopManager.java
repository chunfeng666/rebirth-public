

package dev.madcat.rebirth.manager;

import dev.madcat.rebirth.features.*;
import net.minecraft.entity.player.*;
import java.util.concurrent.*;
import java.util.*;
import java.util.function.*;

public class TotemPopManager extends Feature
{
    private Map<EntityPlayer, Integer> poplist;
    private Set<EntityPlayer> toAnnounce;
    
    public TotemPopManager() {
        this.poplist = new ConcurrentHashMap<EntityPlayer, Integer>();
        this.toAnnounce = new HashSet<EntityPlayer>();
    }
    
    public void onUpdate() {
        for (final EntityPlayer player : this.toAnnounce) {
            if (player == null) {
                continue;
            }
            int playerNumber = 0;
            for (final char character : player.getName().toCharArray()) {
                playerNumber += character;
                playerNumber *= 10;
            }
            this.toAnnounce.remove(player);
            break;
        }
    }
    
    public void onLogout() {
    }
    
    public void init() {
    }
    
    public void onTotemPop(final EntityPlayer player) {
        this.popTotem(player);
        if (!player.equals((Object)TotemPopManager.mc.player)) {
            this.toAnnounce.add(player);
        }
    }
    
    public void onDeath(final EntityPlayer player) {
        int playerNumber = 0;
        for (final char character : player.getName().toCharArray()) {
            playerNumber += character;
            playerNumber *= 10;
        }
        this.toAnnounce.remove(player);
    }
    
    public void onLogout(final EntityPlayer player, final boolean clearOnLogout) {
        if (clearOnLogout) {
            this.resetPops(player);
        }
    }
    
    public void onOwnLogout(final boolean clearOnLogout) {
        if (clearOnLogout) {
            this.clearList();
        }
    }
    
    public void clearList() {
        this.poplist = new ConcurrentHashMap<EntityPlayer, Integer>();
    }
    
    public void resetPops(final EntityPlayer player) {
        this.setTotemPops(player, 0);
    }
    
    public void popTotem(final EntityPlayer player) {
        this.poplist.merge(player, 1, Integer::sum);
    }
    
    public void setTotemPops(final EntityPlayer player, final int amount) {
        this.poplist.put(player, amount);
    }
    
    public int getTotemPops(final EntityPlayer player) {
        final Integer pops = this.poplist.get(player);
        if (pops == null) {
            return 0;
        }
        return pops;
    }
    
    public String getTotemPopString(final EntityPlayer player) {
        return "��f" + ((this.getTotemPops(player) <= 0) ? "" : ("-" + this.getTotemPops(player) + " "));
    }
}
