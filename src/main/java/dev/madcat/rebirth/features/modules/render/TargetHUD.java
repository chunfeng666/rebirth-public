

package dev.madcat.rebirth.features.modules.render;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import java.util.function.*;
import java.util.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.item.*;
import java.awt.*;
import net.minecraft.client.gui.*;

public class TargetHUD extends Module
{
    private final Setting<Modes> mode;
    private final Setting<Integer> x;
    private final Setting<Integer> y;
    private final Setting<Integer> backgroundAlpha;
    EntityLivingBase target;
    
    private static double applyAsDouble(final EntityLivingBase entityLivingBase) {
        return entityLivingBase.getDistance((Entity)TargetHUD.mc.player);
    }
    
    private static boolean checkIsNotPlayer(final Entity entity) {
        return !entity.equals((Object)TargetHUD.mc.player);
    }
    
    public TargetHUD() {
        super("TargetHUD", "description", Module.Category.RENDER, true, false, false);
        this.mode = (Setting<Modes>)this.register(new Setting("Mode", Modes.NOVOLINEOLD));
        this.x = (Setting<Integer>)this.register(new Setting("X", 50, 0, 2000));
        this.y = (Setting<Integer>)this.register(new Setting("Y", 50, 0, 2000));
        this.backgroundAlpha = (Setting<Integer>)this.register(new Setting("Alpha", 80, 0, 255));
        this.target = (EntityLivingBase)TargetHUD.mc.player;
    }
    
    public synchronized void onTick() {
        if (!fullNullCheck()) {
            final List<EntityLivingBase> entities = new LinkedList<EntityLivingBase>();
            TargetHUD.mc.world.loadedEntityList.stream().filter(EntityPlayer.class::isInstance).filter(TargetHUD::checkIsNotPlayer).map(EntityLivingBase.class::cast).sorted(Comparator.comparingDouble((ToDoubleFunction<? super T>)TargetHUD::applyAsDouble)).forEach(entities::add);
            if (!entities.isEmpty()) {
                this.target = entities.get(0);
            }
            else {
                this.target = (EntityLivingBase)TargetHUD.mc.player;
            }
            if (TargetHUD.mc.currentScreen instanceof GuiChat) {
                this.target = (EntityLivingBase)TargetHUD.mc.player;
            }
        }
    }
    
    public synchronized void onRender2D(final Render2DEvent event) {
        if (this.target != null && !this.target.isDead) {
            switch (this.mode.getValue()) {
                case NOVOLINEOLD: {
                    if (!fullNullCheck() && this.target != null) {
                        final FontRenderer fr = TargetHUD.mc.fontRenderer;
                        final int color = (this.target.getHealth() / this.target.getMaxHealth() > 0.66f) ? -16711936 : ((this.target.getHealth() / this.target.getMaxHealth() > 0.33f) ? -26368 : -65536);
                        GlStateManager.color(1.0f, 1.0f, 1.0f);
                        GuiInventory.drawEntityOnScreen(this.x.getValue() + 15, this.y.getValue() + 32, 15, 1.0f, 1.0f, this.target);
                        final List<ItemStack> armorList = new LinkedList<ItemStack>();
                        final List<ItemStack> _armorList = new LinkedList<ItemStack>();
                        final List<ItemStack> list;
                        this.target.getArmorInventoryList().forEach(itemStack -> {
                            if (!itemStack.isEmpty()) {
                                list.add(itemStack);
                            }
                            return;
                        });
                        for (int i = _armorList.size() - 1; i >= 0; --i) {
                            armorList.add(_armorList.get(i));
                        }
                        int armorSize = 0;
                        switch (armorList.size()) {
                            case 0: {
                                if (!this.target.getHeldItemMainhand().isEmpty() && !this.target.getHeldItemOffhand().isEmpty()) {
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand(), this.x.getValue() + 28, this.y.getValue() + 18);
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemOffhand(), this.x.getValue() + 43, this.y.getValue() + 18);
                                    armorSize += 45;
                                    break;
                                }
                                if (!this.target.getHeldItemMainhand().isEmpty() || !this.target.getHeldItemOffhand().isEmpty()) {
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand().isEmpty() ? this.target.getHeldItemOffhand() : this.target.getHeldItemMainhand(), this.x.getValue() + 28, this.y.getValue() + 18);
                                    armorSize += 30;
                                    break;
                                }
                                break;
                            }
                            case 1: {
                                armorSize = 15;
                                TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)armorList.get(0), this.x.getValue() + 28, this.y.getValue() + 18);
                                if (!this.target.getHeldItemMainhand().isEmpty() && !this.target.getHeldItemOffhand().isEmpty()) {
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand(), this.x.getValue() + 43, this.y.getValue() + 18);
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemOffhand(), this.x.getValue() + 58, this.y.getValue() + 18);
                                    armorSize += 45;
                                    break;
                                }
                                if (!this.target.getHeldItemMainhand().isEmpty() || !this.target.getHeldItemOffhand().isEmpty()) {
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand().isEmpty() ? this.target.getHeldItemOffhand() : this.target.getHeldItemMainhand(), this.x.getValue() + 43, this.y.getValue() + 18);
                                    armorSize += 30;
                                    break;
                                }
                                break;
                            }
                            case 2: {
                                armorSize = 30;
                                TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)armorList.get(0), this.x.getValue() + 28, this.y.getValue() + 18);
                                TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)armorList.get(1), this.x.getValue() + 43, this.y.getValue() + 18);
                                if (!this.target.getHeldItemMainhand().isEmpty() && !this.target.getHeldItemOffhand().isEmpty()) {
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand(), this.x.getValue() + 58, this.y.getValue() + 18);
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemOffhand(), this.x.getValue() + 73, this.y.getValue() + 18);
                                    armorSize += 45;
                                    break;
                                }
                                if (!this.target.getHeldItemMainhand().isEmpty() || !this.target.getHeldItemOffhand().isEmpty()) {
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand().isEmpty() ? this.target.getHeldItemOffhand() : this.target.getHeldItemMainhand(), this.x.getValue() + 58, this.y.getValue() + 18);
                                    armorSize += 30;
                                    break;
                                }
                                break;
                            }
                            case 3: {
                                armorSize = 45;
                                TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)armorList.get(0), this.x.getValue() + 28, this.y.getValue() + 18);
                                TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)armorList.get(1), this.x.getValue() + 43, this.y.getValue() + 18);
                                TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)armorList.get(2), this.x.getValue() + 58, this.y.getValue() + 18);
                                if (!this.target.getHeldItemMainhand().isEmpty() && !this.target.getHeldItemOffhand().isEmpty()) {
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand(), this.x.getValue() + 73, this.y.getValue() + 18);
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemOffhand(), this.x.getValue() + 98, this.y.getValue() + 18);
                                    armorSize += 45;
                                    break;
                                }
                                if (!this.target.getHeldItemMainhand().isEmpty() || !this.target.getHeldItemOffhand().isEmpty()) {
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand().isEmpty() ? this.target.getHeldItemOffhand() : this.target.getHeldItemMainhand(), this.x.getValue() + 73, this.y.getValue() + 18);
                                    armorSize += 30;
                                    break;
                                }
                                break;
                            }
                            case 4: {
                                armorSize = 60;
                                TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)armorList.get(0), this.x.getValue() + 28, this.y.getValue() + 18);
                                TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)armorList.get(1), this.x.getValue() + 43, this.y.getValue() + 18);
                                TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)armorList.get(2), this.x.getValue() + 58, this.y.getValue() + 18);
                                TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)armorList.get(3), this.x.getValue() + 73, this.y.getValue() + 18);
                                if (!this.target.getHeldItemMainhand().isEmpty() && !this.target.getHeldItemOffhand().isEmpty()) {
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand(), this.x.getValue() + 98, this.y.getValue() + 18);
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemOffhand(), this.x.getValue() + 113, this.y.getValue() + 18);
                                    armorSize += 45;
                                    break;
                                }
                                if (!this.target.getHeldItemMainhand().isEmpty() || !this.target.getHeldItemOffhand().isEmpty()) {
                                    TargetHUD.mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand().isEmpty() ? this.target.getHeldItemOffhand() : this.target.getHeldItemMainhand(), this.x.getValue() + 98, this.y.getValue() + 18);
                                    armorSize += 30;
                                    break;
                                }
                                break;
                            }
                        }
                        int backgroundStopY = this.y.getValue() + 35;
                        final int stringWidth = fr.getStringWidth(this.target.getName()) + 30;
                        int backgroundStopX;
                        if (fr.getStringWidth(this.target.getName()) > armorSize) {
                            backgroundStopX = this.x.getValue() + stringWidth;
                        }
                        else {
                            backgroundStopX = this.x.getValue() + armorSize + 30;
                        }
                        backgroundStopX += 5;
                        backgroundStopY += 5;
                        Gui.drawRect(this.x.getValue() - 2, (int)this.y.getValue(), backgroundStopX, backgroundStopY, new Color(0, 0, 0, this.backgroundAlpha.getValue()).getRGB());
                        final int healthBarLength = (int)(this.target.getHealth() / this.target.getMaxHealth() * (backgroundStopX - this.x.getValue()));
                        Gui.drawRect(this.x.getValue() - 2, backgroundStopY - 2, this.x.getValue() + healthBarLength, backgroundStopY, color);
                        fr.drawString(this.target.getName(), (float)(this.x.getValue() + 30), (float)(this.y.getValue() + 8), new Color(255, 255, 255).getRGB(), true);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public enum Modes
    {
        NOVOLINEOLD;
    }
}
