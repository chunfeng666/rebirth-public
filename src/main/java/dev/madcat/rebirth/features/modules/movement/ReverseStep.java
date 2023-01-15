
package dev.madcat.rebirth.features.modules.movement;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.*;
import net.minecraft.entity.*;

public class ReverseStep extends Module
{
    private static ReverseStep INSTANCE;
    private final Setting<Integer> speed;
    private final Setting<Boolean> inliquid;
    private final Setting<Cancel> canceller;
    
    public ReverseStep() {
        super("ReverseStep", "Rapid decline", Module.Category.MOVEMENT, true, false, false);
        this.speed = (Setting<Integer>)this.register(new Setting("Speed", 8, 1, 20));
        this.inliquid = (Setting<Boolean>)this.register(new Setting("Liquid", false));
        this.canceller = (Setting<Cancel>)this.register(new Setting("CancelType", Cancel.None));
        this.setInstance();
    }
    
    public static ReverseStep getInstance() {
        if (ReverseStep.INSTANCE == null) {
            ReverseStep.INSTANCE = new ReverseStep();
        }
        return ReverseStep.INSTANCE;
    }
    
    private void setInstance() {
        ReverseStep.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (ReverseStep.mc.player.isSneaking() || ReverseStep.mc.player.isDead || ReverseStep.mc.player.collidedHorizontally || !ReverseStep.mc.player.onGround || (ReverseStep.mc.player.isInWater() && !this.inliquid.getValue()) || (ReverseStep.mc.player.isInLava() && !this.inliquid.getValue()) || ReverseStep.mc.player.isOnLadder() || ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() || Rebirth.moduleManager.isModuleEnabled("Burrow") || ReverseStep.mc.player.noClip || Rebirth.moduleManager.isModuleEnabled("Packetfly") || Rebirth.moduleManager.isModuleEnabled("Phase") || (ReverseStep.mc.gameSettings.keyBindSneak.isKeyDown() && this.canceller.getValue() == Cancel.Shift) || (ReverseStep.mc.gameSettings.keyBindSneak.isKeyDown() && this.canceller.getValue() == Cancel.Both) || (ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() && this.canceller.getValue() == Cancel.Space) || (ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() && this.canceller.getValue() == Cancel.Both) || Rebirth.moduleManager.isModuleEnabled("Strafe")) {
            return;
        }
        for (double y = 0.0; y < 90.5; y += 0.01) {
            if (!ReverseStep.mc.world.getCollisionBoxes((Entity)ReverseStep.mc.player, ReverseStep.mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                ReverseStep.mc.player.motionY = -this.speed.getValue() / 10.0f;
                break;
            }
        }
    }
    
    static {
        ReverseStep.INSTANCE = new ReverseStep();
    }
    
    public enum Cancel
    {
        None, 
        Space, 
        Shift, 
        Both;
    }
}
