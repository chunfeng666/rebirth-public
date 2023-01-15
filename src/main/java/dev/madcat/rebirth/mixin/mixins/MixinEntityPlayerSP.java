

package dev.madcat.rebirth.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.*;
import net.minecraft.world.*;
import net.minecraft.client.network.*;
import net.minecraft.stats.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import dev.madcat.rebirth.features.modules.player.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.*;
import dev.madcat.rebirth.event.events.*;

@Mixin(value = { EntityPlayerSP.class }, priority = Integer.MAX_VALUE)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer
{
    public MixinEntityPlayerSP(final Minecraft p_i47378_1_, final World p_i47378_2_, final NetHandlerPlayClient p_i47378_3_, final StatisticsManager p_i47378_4_, final RecipeBook p_i47378_5_) {
        super(p_i47378_2_, p_i47378_3_.getGameProfile());
    }
    
    @Inject(method = { "sendChatMessage" }, at = { @At("HEAD") }, cancellable = true)
    public void sendChatMessage(final String message, final CallbackInfo ci) {
        final ChatEvent chatEvent = new ChatEvent(message);
        MinecraftForge.EVENT_BUS.post((Event)chatEvent);
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("HEAD") }, cancellable = true)
    private void preMotion(final CallbackInfo ci) {
        final UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            return;
        }
        ci.cancel();
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("RETURN") }, cancellable = true)
    private void postMotion(final CallbackInfo ci) {
        final UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(1);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            return;
        }
        ci.cancel();
    }
    
    @Inject(method = { "pushOutOfBlocks" }, at = { @At("HEAD") }, cancellable = true)
    private void pushOutOfBlocksHook(final double x, final double y, final double z, final CallbackInfoReturnable<Boolean> ci) {
        final PushEvent event = new PushEvent(1);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            return;
        }
        ci.setReturnValue((Object)false);
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"))
    public void closeScreenHook(final EntityPlayerSP entityPlayerSP) {
        if (!BetterPortal.getInstance().isOn() || !(boolean)BetterPortal.getInstance().portalChat.getValue()) {
            entityPlayerSP.closeScreen();
        }
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
    public void displayGuiScreenHook(final Minecraft mc, final GuiScreen screen) {
        if (!BetterPortal.getInstance().isOn() || !(boolean)BetterPortal.getInstance().portalChat.getValue()) {
            mc.displayGuiScreen(screen);
        }
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V", ordinal = 2))
    public void onLivingUpdate(final EntityPlayerSP entityPlayerSP, final boolean sprinting) {
        entityPlayerSP.setSprinting(sprinting);
    }
    
    @Inject(method = { "move" }, at = { @At("HEAD") }, cancellable = true)
    public void move(final MoverType moverType, final double n, final double n2, final double n3, final CallbackInfo ci) {
        final MoveEvent event = new MoveEvent(moverType, n, n2, n3);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            return;
        }
        super.move(moverType, event.getX(), event.getY(), event.getZ());
        ci.cancel();
    }
}
