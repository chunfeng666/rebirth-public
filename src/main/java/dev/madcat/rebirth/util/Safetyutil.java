

package dev.madcat.rebirth.util;

import dev.madcat.rebirth.features.*;
import dev.madcat.rebirth.features.modules.combat.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import java.util.*;
import java.util.concurrent.*;

public class Safetyutil extends Feature implements Runnable
{
    private final Timer syncTimer;
    private static boolean SAFE;
    private ScheduledExecutorService service;
    
    public Safetyutil() {
        this.syncTimer = new Timer();
        Safetyutil.SAFE = false;
    }
    
    public void run() {
        if (AutoCrystal.getInstance().isOff() || AutoCrystal.getInstance().threadMode.getValue() == AutoCrystal.ThreadMode.NONE) {
            doSafetyCheck();
        }
    }
    
    public static void doSafetyCheck() {
        if (!Feature.fullNullCheck()) {
            boolean safe = true;
            final ArrayList<Entity> crystals = new ArrayList<Entity>(Safetyutil.mc.world.loadedEntityList);
            for (final Entity crystal : crystals) {
                if (crystal instanceof EntityEnderCrystal) {
                    if (DamageUtil.calculateDamage(crystal, (Entity)Safetyutil.mc.player) <= 4.0) {
                        continue;
                    }
                    safe = false;
                    break;
                }
            }
            Safetyutil.SAFE = safe;
        }
    }
    
    public void onUpdate() {
        this.run();
    }
    
    public String getSafetyString() {
        if (Safetyutil.SAFE) {
            return "\u6402aSecure";
        }
        return "\u6402cUnsafe";
    }
    
    public boolean isSafe() {
        return Safetyutil.SAFE;
    }
    
    public ScheduledExecutorService getService() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        return service;
    }
    
    static {
        Safetyutil.SAFE = false;
    }
}
