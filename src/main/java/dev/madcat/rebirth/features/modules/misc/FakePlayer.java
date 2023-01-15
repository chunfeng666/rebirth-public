

package dev.madcat.rebirth.features.modules.misc;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.client.entity.*;
import java.util.*;
import com.mojang.authlib.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;

public class FakePlayer extends Module
{
    private final Setting<Integer> setHealth;
    private EntityOtherPlayerMP clonedPlayer;
    
    public FakePlayer() {
        super("FakePlayer", "Spawns a FakePlayer for testing", Category.MISC, true, false, false);
        this.setHealth = (Setting<Integer>)this.register(new Setting("SetHealth", 20, 1, 20));
    }
    
    @Override
    public void onEnable() {
        if (FakePlayer.mc.player != null && !FakePlayer.mc.player.isDead) {
            (this.clonedPlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString("a3ca166d-c5f1-3d5a-baac-b18a5b38d4cd"), "MadCat"))).copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
            this.clonedPlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
            this.clonedPlayer.rotationYaw = FakePlayer.mc.player.rotationYaw;
            this.clonedPlayer.rotationPitch = FakePlayer.mc.player.rotationPitch;
            this.clonedPlayer.inventory.copyInventory(FakePlayer.mc.player.inventory);
            this.clonedPlayer.setGameType(GameType.SURVIVAL);
            this.clonedPlayer.setHealth((float)this.setHealth.getValue());
            FakePlayer.mc.world.addEntityToWorld(-404, (Entity)this.clonedPlayer);
            this.clonedPlayer.onLivingUpdate();
            return;
        }
        this.disable();
    }
    
    @Override
    public void onDisable() {
        if (FakePlayer.mc.world == null) {
            return;
        }
        FakePlayer.mc.world.removeEntityFromWorld(-404);
    }
}
