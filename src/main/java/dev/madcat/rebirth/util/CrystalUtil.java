

package dev.madcat.rebirth.util;

import net.minecraft.client.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.enchantment.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.item.*;

public class CrystalUtil
{
    public static Minecraft mc;
    
    public static double getRange(final Vec3d a, final double x, final double y, final double z) {
        final double xl = a.x - x;
        final double yl = a.y - y;
        final double zl = a.z - z;
        return Math.sqrt(xl * xl + yl * yl + zl * zl);
    }
    
    public static boolean isReplaceable(final Block block) {
        return block == Blocks.FIRE || block == Blocks.DOUBLE_PLANT || block == Blocks.VINE;
    }
    
    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity, final Vec3d vec) {
        final float doubleExplosionSize = 12.0f;
        final double distanceSize = getRange(vec, posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distanceSize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finalValue = 1.0;
        if (entity instanceof EntityLivingBase) {
            finalValue = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)CrystalUtil.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finalValue;
    }
    
    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final Vec3d offset = new Vec3d(entity.posX, entity.posY, entity.posZ);
        return calculateDamage(posX, posY, posZ, entity, offset);
    }
    
    private static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        try {
            if (entity instanceof EntityPlayer) {
                final EntityPlayer ep = (EntityPlayer)entity;
                final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
                damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
                final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
                final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
                damage *= 1.0f - f / 25.0f;
                if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                    damage -= damage / 5.0f;
                }
                damage = Math.max(damage, 0.0f);
                return damage;
            }
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            return damage;
        }
        catch (Exception ignored) {
            return getBlastReduction(entity, damage, explosion);
        }
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = CrystalUtil.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static EnumFacing enumFacing(final BlockPos blockPos) {
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumFacing = values[i];
            final Vec3d vec3d = new Vec3d(CrystalUtil.mc.player.posX, CrystalUtil.mc.player.posY + CrystalUtil.mc.player.getEyeHeight(), CrystalUtil.mc.player.posZ);
            final Vec3d vec3d2 = new Vec3d((double)(blockPos.getX() + enumFacing.getDirectionVec().getX()), (double)(blockPos.getY() + enumFacing.getDirectionVec().getY()), (double)(blockPos.getZ() + enumFacing.getDirectionVec().getZ()));
            final RayTraceResult rayTraceBlocks;
            if ((rayTraceBlocks = CrystalUtil.mc.world.rayTraceBlocks(vec3d, vec3d2, false, true, false)) != null && rayTraceBlocks.typeOfHit.equals((Object)RayTraceResult.Type.BLOCK) && rayTraceBlocks.getBlockPos().equals((Object)blockPos)) {
                return enumFacing;
            }
        }
        if (blockPos.getY() > CrystalUtil.mc.player.posY + CrystalUtil.mc.player.getEyeHeight()) {
            return EnumFacing.DOWN;
        }
        return EnumFacing.UP;
    }
    
    public static boolean isEating() {
        return CrystalUtil.mc.player != null && (CrystalUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemFood || CrystalUtil.mc.player.getHeldItemOffhand().getItem() instanceof ItemFood) && CrystalUtil.mc.player.isHandActive();
    }
    
    public static boolean canSeeBlock(final BlockPos p_Pos) {
        return CrystalUtil.mc.player == null || CrystalUtil.mc.world.rayTraceBlocks(new Vec3d(CrystalUtil.mc.player.posX, CrystalUtil.mc.player.posY + CrystalUtil.mc.player.getEyeHeight(), CrystalUtil.mc.player.posZ), new Vec3d((double)p_Pos.getX(), (double)p_Pos.getY(), (double)p_Pos.getZ()), false, true, false) != null;
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(CrystalUtil.mc.player.posX), Math.floor(CrystalUtil.mc.player.posY), Math.floor(CrystalUtil.mc.player.posZ));
    }
    
    public static double getVecDistance(final BlockPos a, final double posX, final double posY, final double posZ) {
        final double x1 = a.getX() - posX;
        final double y1 = a.getY() - posY;
        final double z1 = a.getZ() - posZ;
        return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
    }
    
    static {
        CrystalUtil.mc = Minecraft.getMinecraft();
    }
}
