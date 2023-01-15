
package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import net.minecraft.entity.player.*;
import java.util.concurrent.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import dev.madcat.rebirth.features.modules.client.*;
import java.awt.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import net.minecraft.nbt.*;

public class Peek extends Module
{
    private static final ResourceLocation SHULKER_GUI_TEXTURE;
    private static Peek INSTANCE;
    public Map<EntityPlayer, ItemStack> spiedPlayers;
    public Map<EntityPlayer, Timer> playerTimers;
    private int textRadarY;
    
    public Peek() {
        super("Peek", "Several tweaks for Peek.", Module.Category.RENDER, true, false, false);
        this.spiedPlayers = new ConcurrentHashMap<EntityPlayer, ItemStack>();
        this.playerTimers = new ConcurrentHashMap<EntityPlayer, Timer>();
        this.textRadarY = 0;
        this.setInstance();
    }
    
    public static Peek getInstance() {
        if (Peek.INSTANCE == null) {
            Peek.INSTANCE = new Peek();
        }
        return Peek.INSTANCE;
    }
    
    public static void displayInv(final ItemStack stack, final String name) {
        try {
            final Item item = stack.getItem();
            final TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            final ItemShulkerBox shulker = (ItemShulkerBox)item;
            entityBox.blockType = shulker.getBlock();
            entityBox.setWorld((World)Peek.mc.world);
            ItemStackHelper.loadAllItems(stack.getTagCompound().getCompoundTag("BlockEntityTag"), entityBox.items);
            entityBox.readFromNBT(stack.getTagCompound().getCompoundTag("BlockEntityTag"));
            entityBox.setCustomName((name == null) ? stack.getDisplayName() : name);
            final IInventory inventory;
            new Thread(() -> {
                try {
                    Thread.sleep(200L);
                }
                catch (InterruptedException ex) {}
                Peek.mc.player.displayGUIChest(inventory);
            }).start();
        }
        catch (Exception ex2) {}
    }
    
    private void setInstance() {
        Peek.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        for (final EntityPlayer player : Peek.mc.world.playerEntities) {
            if (player != null && player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox) {
                if (Peek.mc.player == player) {
                    continue;
                }
                final ItemStack stack = player.getHeldItemMainhand();
                this.spiedPlayers.put(player, stack);
            }
        }
    }
    
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        final int x = -3;
        int y = 124;
        this.textRadarY = 0;
        for (final EntityPlayer player : Peek.mc.world.playerEntities) {
            if (this.spiedPlayers.get(player) == null) {
                continue;
            }
            if (player.getHeldItemMainhand() == null || !(player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox)) {
                final Timer playerTimer = this.playerTimers.get(player);
                if (playerTimer == null) {
                    final Timer timer = new Timer();
                    timer.reset();
                    this.playerTimers.put(player, timer);
                }
                else if (playerTimer.passedS(3.0)) {
                    continue;
                }
            }
            else {
                final Timer playerTimer;
                if (player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox && (playerTimer = this.playerTimers.get(player)) != null) {
                    playerTimer.reset();
                    this.playerTimers.put(player, playerTimer);
                }
            }
            final ItemStack stack = this.spiedPlayers.get(player);
            this.renderShulkerToolTip(stack, x, y, player.getName());
            y += 78;
            this.textRadarY = y - 10 - 114 + 2;
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void makeTooltip(final ItemTooltipEvent event) {
    }
    
    public void renderShulkerToolTip(final ItemStack stack, final int x, final int y, final String name) {
        final NBTTagCompound tagCompound = stack.getTagCompound();
        final NBTTagCompound blockEntityTag;
        if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Peek.mc.getTextureManager().bindTexture(Peek.SHULKER_GUI_TEXTURE);
            RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
            RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 57, 500);
            RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
            GlStateManager.disableDepth();
            final Color color = new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 200);
            this.renderer.drawStringWithShadow((name == null) ? stack.getDisplayName() : name, (float)(x + 8), (float)(y + 6), ColorUtil.toRGBA(color));
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            final NonNullList nonnulllist = NonNullList.withSize(27, (Object)ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(blockEntityTag, nonnulllist);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final int iX = x + i % 9 * 18 + 8;
                final int iY = y + i / 9 * 18 + 18;
                final ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                Peek.mc.getItemRenderer().itemRenderer.zLevel = 501.0f;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(Peek.mc.fontRenderer, itemStack, iX, iY, (String)null);
                Peek.mc.getItemRenderer().itemRenderer.zLevel = 0.0f;
            }
            GlStateManager.disableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    static {
        SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
        Peek.INSTANCE = new Peek();
    }
}
