
package dev.madcat.rebirth.features.modules.client;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import java.util.function.*;
import net.minecraft.client.renderer.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import dev.madcat.rebirth.util.*;
import java.util.*;

public class ItemStats extends Module
{
    public Setting<Boolean> combatCount;
    public Setting<Integer> combatCountX;
    public Setting<Integer> combatCountY;
    public Setting<Boolean> armor;
    private static final ItemStack totem;
    private static final ItemStack Crystal;
    private static final ItemStack xp;
    private static final ItemStack ap;
    private static final ItemStack obs;
    
    public ItemStats() {
        super("CombatStats", "Show your items count.", Category.CLIENT, true, false, false);
        this.combatCount = (Setting<Boolean>)this.register(new Setting("ItemsCount", true));
        this.combatCountX = (Setting<Integer>)this.register(new Setting("X", 125, 0, 300, v -> this.combatCount.getValue()));
        this.combatCountY = (Setting<Integer>)this.register(new Setting("Y", 18, 0, 500, v -> this.combatCount.getValue()));
        this.armor = (Setting<Boolean>)this.register(new Setting("Armor", true));
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (this.combatCount.getValue()) {
            final int width = this.renderer.scaledWidth;
            final int height = this.renderer.scaledHeight;
            int totems = ItemStats.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
            if (ItemStats.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                totems += ItemStats.mc.player.getHeldItemOffhand().getCount();
            }
            GlStateManager.enableTexture2D();
            final int i = width / 2;
            final int y = height - this.combatCountY.getValue();
            int x = i + this.combatCountX.getValue();
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(ItemStats.totem, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(ItemStats.mc.fontRenderer, ItemStats.totem, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(totems + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (float)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            int Crystals = ItemStats.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
            if (ItemStats.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                Crystals += ItemStats.mc.player.getHeldItemOffhand().getCount();
            }
            x += 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(ItemStats.Crystal, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(ItemStats.mc.fontRenderer, ItemStats.Crystal, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(Crystals + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(Crystals + "")), (float)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            int EXP = ItemStats.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.EXPERIENCE_BOTTLE).mapToInt(ItemStack::getCount).sum();
            if (ItemStats.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE) {
                EXP += ItemStats.mc.player.getHeldItemOffhand().getCount();
            }
            x += 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(ItemStats.xp, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(ItemStats.mc.fontRenderer, ItemStats.xp, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(EXP + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(EXP + "")), (float)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            int GP = ItemStats.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
            if (ItemStats.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
                GP += ItemStats.mc.player.getHeldItemOffhand().getCount();
            }
            x += 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(ItemStats.ap, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(ItemStats.mc.fontRenderer, ItemStats.ap, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(GP + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(GP + "")), (float)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            final int OBS = ItemStats.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)).mapToInt(ItemStack::getCount).sum();
            x += 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(ItemStats.obs, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(ItemStats.mc.fontRenderer, ItemStats.obs, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(OBS + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(OBS + "")), (float)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        if (this.armor.getValue()) {
            final int width = this.renderer.scaledWidth;
            final int height = this.renderer.scaledHeight;
            GlStateManager.enableTexture2D();
            final int j = width / 2;
            int iteration = 0;
            final int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            for (final ItemStack is : HUD.mc.player.inventory.armorInventory) {
                ++iteration;
                if (is.isEmpty()) {
                    continue;
                }
                final int x2 = j - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.enableDepth();
                RenderUtil.itemRender.zLevel = 200.0f;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x2, y);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, is, x2, y, "");
                RenderUtil.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
                this.renderer.drawStringWithShadow(s, (float)(x2 + 19 - 2 - this.renderer.getStringWidth(s)), (float)(y + 9), 16777215);
                final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                final float red = 1.0f - green;
                int dmg = 100 - (int)(red * 100.0f);
                if (dmg == -2147483547) {
                    dmg = 100;
                }
                this.renderer.drawStringWithShadow(dmg + "", (float)(x2 + 8 - this.renderer.getStringWidth(dmg + "") / 2), (float)(y - 11), ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    
    static {
        totem = new ItemStack(Items.TOTEM_OF_UNDYING);
        Crystal = new ItemStack(Items.END_CRYSTAL);
        xp = new ItemStack(Items.EXPERIENCE_BOTTLE);
        ap = new ItemStack(Items.GOLDEN_APPLE);
        obs = new ItemStack(Blocks.OBSIDIAN);
    }
}
