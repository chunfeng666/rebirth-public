

package dev.madcat.rebirth.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraft.util.math.*;
import java.util.*;
import com.google.common.base.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Mixin({ World.class })
public class MixinWorld
{
    @Redirect(method = { "getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getEntitiesOfTypeWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lcom/google/common/base/Predicate;)V"))
    public <T extends Entity> void getEntitiesOfTypeWithinAABBHook(final Chunk chunk, final Class<? extends T> entityClass, final AxisAlignedBB aabb, final List<T> listToFill, final Predicate<? super T> filter) {
        try {
            chunk.getEntitiesOfTypeWithinAABB((Class)entityClass, aabb, (List)listToFill, (Predicate)filter);
        }
        catch (Exception ex) {}
    }
    
    @Inject(method = { "onEntityAdded" }, at = { @At("HEAD") })
    private void onEntityAdded(final Entity entityIn, final CallbackInfo ci) {
    }
    
    @Redirect(method = { "handleMaterialAcceleration" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPushedByWater()Z"))
    public boolean isPushedbyWaterHook(final Entity entity) {
        final PushEvent event = new PushEvent(2, entity);
        MinecraftForge.EVENT_BUS.post((Event)event);
        return entity.isPushedByWater() && !event.isCanceled();
    }
}
