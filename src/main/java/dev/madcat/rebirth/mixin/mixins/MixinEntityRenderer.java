
package dev.madcat.rebirth.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.*;
import net.minecraft.item.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import com.google.common.base.*;
import dev.madcat.rebirth.features.modules.misc.*;
import java.util.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import dev.madcat.rebirth.*;
import dev.madcat.rebirth.manager.*;
import org.spongepowered.asm.mixin.injection.*;
import dev.madcat.rebirth.features.modules.render.*;
import net.minecraft.init.*;

@Mixin({ EntityRenderer.class })
public class MixinEntityRenderer
{
    public ItemStack itemActivationItem;
    
    @Redirect(method = { "getMouseOver" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcluding(final WorldClient worldClient, final Entity entityIn, final AxisAlignedBB boundingBox, final Predicate predicate) {
        if (NoEntityTrace.getINSTANCE().isOn()) {
            return new ArrayList<Entity>();
        }
        return (List<Entity>)worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }
    
    @Inject(method = { "hurtCameraEffect" }, at = { @At("HEAD") }, cancellable = true)
    public void hurtCameraEffect(final float ticks, final CallbackInfo info) {
        final ModuleManager moduleManager = Rebirth.moduleManager;
        if (ModuleManager.getModuleByName("NoRender").isEnabled()) {
            info.cancel();
        }
    }
    
    @ModifyVariable(method = { "orientCamera" }, ordinal = 3, at = @At(value = "STORE", ordinal = 0), require = 1)
    public double changeCameraDistanceHook(final double range) {
        final CameraClip CameraClip = (CameraClip)Rebirth.moduleManager.getModuleByDisplayName("CameraClip");
        if (ModuleManager.getModuleByName("CameraClip").isEnabled()) {
            return (double)CameraClip.distance.getValue();
        }
        return range;
    }
    
    @Inject(method = { "renderItemActivation" }, at = { @At("HEAD") }, cancellable = true)
    public void renderItemActivationHook(final CallbackInfo info) {
        if (this.itemActivationItem != null && NoRender.getInstance().isOn() && (boolean)NoRender.getInstance().totemPops.getValue() && this.itemActivationItem.getItem() == Items.TOTEM_OF_UNDYING) {
            info.cancel();
        }
    }
    
    @ModifyVariable(method = { "orientCamera" }, ordinal = 7, at = @At(value = "STORE", ordinal = 0), require = 1)
    public double orientCameraHook(final double range) {
        final CameraClip CameraClip = (CameraClip)Rebirth.moduleManager.getModuleByDisplayName("CameraClip");
        if (ModuleManager.getModuleByName("CameraClip").isEnabled()) {
            return (double)CameraClip.distance.getValue();
        }
        return range;
    }
}
