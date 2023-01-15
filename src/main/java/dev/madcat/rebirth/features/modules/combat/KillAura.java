

package dev.madcat.rebirth.features.modules.combat;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.player.*;
import dev.madcat.rebirth.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.util.*;
import java.util.*;

public class KillAura extends Module
{
    private static KillAura INSTANCE;
    public static Entity target;
    private final Timer timer;
    public Setting<Float> range;
    public Setting<Boolean> delay;
    public Setting<Boolean> rotate;
    public Setting<Boolean> onlySharp;
    public Setting<Float> raytrace;
    public Setting<Boolean> players;
    public Setting<Boolean> mobs;
    public Setting<Boolean> animals;
    public Setting<Boolean> vehicles;
    public Setting<Boolean> projectiles;
    public Setting<Boolean> tps;
    public Setting<Boolean> packet;
    
    public KillAura() {
        super("KillAura", "Kills aura.", Category.COMBAT, true, false, false);
        this.timer = new Timer();
        this.range = (Setting<Float>)this.register(new Setting("Range", 6.0f, 0.1f, 7.0f));
        this.delay = (Setting<Boolean>)this.register(new Setting("HitDelay", true));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", true));
        this.onlySharp = (Setting<Boolean>)this.register(new Setting("SwordOnly", true));
        this.raytrace = (Setting<Float>)this.register(new Setting("Raytrace", 6.0f, 0.1f, 7.0f, "Wall Range."));
        this.players = (Setting<Boolean>)this.register(new Setting("Players", true));
        this.mobs = (Setting<Boolean>)this.register(new Setting("Mobs", false));
        this.animals = (Setting<Boolean>)this.register(new Setting("Animals", false));
        this.vehicles = (Setting<Boolean>)this.register(new Setting("Entities", false));
        this.projectiles = (Setting<Boolean>)this.register(new Setting("Projectiles", false));
        this.tps = (Setting<Boolean>)this.register(new Setting("TpsSync", true));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", false));
    }
    
    @Override
    public void onTick() {
        if (!this.rotate.getValue()) {
            this.doKillAura();
        }
    }
    
    public static KillAura getInstance() {
        if (KillAura.INSTANCE == null) {
            KillAura.INSTANCE = new KillAura();
        }
        return KillAura.INSTANCE;
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.rotate.getValue()) {
            this.doKillAura();
        }
    }
    
    private void doKillAura() {
        if (this.onlySharp.getValue() && !EntityUtil.holdingWeapon((EntityPlayer)KillAura.mc.player)) {
            KillAura.target = null;
            return;
        }
        final int wait = this.delay.getValue() ? ((int)(DamageUtil.getCooldownByWeapon((EntityPlayer)KillAura.mc.player) * (this.tps.getValue() ? Rebirth.serverManager.getTpsFactor() : 1.0f))) : 0;
        if (!this.timer.passedMs(wait)) {
            return;
        }
        KillAura.target = this.getTarget();
        if (KillAura.target == null) {
            return;
        }
        if (this.rotate.getValue()) {
            Rebirth.rotationManager.lookAtEntity(KillAura.target);
        }
        EntityUtil.attackEntity(KillAura.target, this.packet.getValue(), true);
        this.timer.reset();
    }
    
    private Entity getTarget() {
        Entity target = null;
        double distance = this.range.getValue();
        double maxHealth = 36.0;
        for (final Entity entity : KillAura.mc.world.playerEntities) {
            if ((this.players.getValue() && entity instanceof EntityPlayer) || (this.animals.getValue() && EntityUtil.isPassive(entity)) || (this.mobs.getValue() && EntityUtil.isMobAggressive(entity)) || (this.vehicles.getValue() && EntityUtil.isVehicle(entity)) || (this.projectiles.getValue() && EntityUtil.isProjectile(entity))) {
                if (entity instanceof EntityLivingBase && EntityUtil.isntValid(entity, distance)) {
                    continue;
                }
                if (!KillAura.mc.player.canEntityBeSeen(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && KillAura.mc.player.getDistanceSq(entity) > MathUtil.square(this.raytrace.getValue())) {
                    continue;
                }
                if (target == null) {
                    target = entity;
                    distance = KillAura.mc.player.getDistanceSq(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
                else {
                    if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                        target = entity;
                        break;
                    }
                    if (KillAura.mc.player.getDistanceSq(entity) < distance) {
                        target = entity;
                        distance = KillAura.mc.player.getDistanceSq(entity);
                        maxHealth = EntityUtil.getHealth(entity);
                    }
                    if (EntityUtil.getHealth(entity) >= maxHealth) {
                        continue;
                    }
                    target = entity;
                    distance = KillAura.mc.player.getDistanceSq(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
            }
        }
        return target;
    }
    
    @Override
    public String getDisplayInfo() {
        if (KillAura.target instanceof EntityPlayer) {
            return KillAura.target.getName();
        }
        return null;
    }
    
    static {
        KillAura.INSTANCE = new KillAura();
    }
}
