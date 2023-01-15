

package dev.madcat.rebirth.mixin.mixins;

import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import com.mojang.authlib.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.*;
import net.minecraftforge.common.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Mixin({ EntityPlayer.class })
public abstract class MixinEntityPlayer extends EntityLivingBase
{
    EntityPlayer player;
    
    public MixinEntityPlayer(final World worldIn, final GameProfile gameProfileIn) {
        super(worldIn);
    }
    
    @Inject(method = { "getCooldownPeriod" }, at = { @At("HEAD") }, cancellable = true)
    private void getCooldownPeriodHook(final CallbackInfoReturnable<Float> callbackInfoReturnable) {
    }
    
    @Inject(method = { "jump" }, at = { @At("HEAD") }, cancellable = true)
    public void onJump(final CallbackInfo ci) {
        if (Minecraft.getMinecraft().player.getName() == this.getName()) {
            MinecraftForge.EVENT_BUS.post((Event)new PlayerJumpEvent(this.motionX, this.motionY));
        }
    }
}
