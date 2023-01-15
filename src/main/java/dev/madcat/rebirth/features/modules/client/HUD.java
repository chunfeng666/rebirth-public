

package dev.madcat.rebirth.features.modules.client;

import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.setting.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import dev.madcat.rebirth.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.*;
import dev.madcat.rebirth.util.*;
import net.minecraft.entity.player.*;
import java.util.*;

public class HUD extends Module
{
    private static HUD INSTANCE;
    private final Setting<Boolean> grayNess;
    private final Setting<Boolean> renderingUp;
    private final Setting<Boolean> arrayList;
    private final Setting<Boolean> coords;
    private final Setting<Boolean> speed;
    private final Setting<Boolean> server;
    private final Setting<Boolean> burrow;
    private final Setting<Boolean> ping;
    private final Setting<Boolean> tps;
    private final Setting<Boolean> fps;
    private final Setting<Boolean> lag;
    public Setting<Boolean> FriendList;
    public Setting<Integer> arraylisty;
    public Setting<Integer> FriendListY;
    public Setting<TextUtil.Color> bracketColor;
    public Setting<TextUtil.Color> commandColor;
    public Setting<Boolean> notifyToggles;
    public Setting<RenderingMode> renderingMode;
    public Setting<Integer> lagTime;
    private int color;
    public final Setting<Boolean> moduleInfo;
    final Setting<Boolean> waterMark;
    public final Setting<Boolean> watermark2;
    public final Setting<Integer> compactX;
    public final Setting<Integer> compactY;
    public final Setting<Integer> alpha;
    
    public HUD() {
        super("HUD", "HUD Elements rendered on your screen.", Category.CLIENT, true, false, false);
        this.moduleInfo = (Setting<Boolean>)this.register(new Setting("ModuleInfo", true));
        this.waterMark = (Setting<Boolean>)this.register(new Setting("Watermark", false, "displays watermark"));
        this.watermark2 = (Setting<Boolean>)this.register(new Setting("SkeetWatermark", false, v -> this.waterMark.getValue()));
        this.compactX = (Setting<Integer>)this.register(new Setting("SkeetMarkX", 20, 0, 1080, v -> this.waterMark.getValue() && this.watermark2.getValue()));
        this.compactY = (Setting<Integer>)this.register(new Setting("SkeetMarkY", 20, 0, 530, v -> this.waterMark.getValue() && this.watermark2.getValue()));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 50, 0, 255, v -> this.waterMark.getValue() && this.watermark2.getValue()));
        this.grayNess = (Setting<Boolean>)this.register(new Setting("Gray", true));
        this.arrayList = (Setting<Boolean>)this.register(new Setting("ActiveModules", true, "Lists the active modules."));
        this.renderingUp = (Setting<Boolean>)this.register(new Setting("RenderingUp", Boolean.TRUE, v -> this.arrayList.getValue()));
        this.arraylisty = (Setting<Integer>)this.register(new Setting("OffsetY", 40, 0, 200, v -> this.arrayList.getValue() && this.renderingUp.getValue()));
        this.coords = (Setting<Boolean>)this.register(new Setting("Coords", true, "Your current coordinates"));
        this.speed = (Setting<Boolean>)this.register(new Setting("Speed", false, "Your Speed"));
        this.server = (Setting<Boolean>)this.register(new Setting("IP", false, "Shows the server"));
        this.ping = (Setting<Boolean>)this.register(new Setting("Ping", false, "Your response time to the server."));
        this.tps = (Setting<Boolean>)this.register(new Setting("TPS", false, "Ticks per second of the server."));
        this.fps = (Setting<Boolean>)this.register(new Setting("FPS", false, "Your frames per second."));
        this.lag = (Setting<Boolean>)this.register(new Setting("LagNotifier", true, "The time"));
        this.FriendList = (Setting<Boolean>)this.register(new Setting("FriendList", Boolean.FALSE));
        this.FriendListY = (Setting<Integer>)this.register(new Setting("FriendListY", 173, 0, 530, v -> this.FriendList.getValue()));
        this.bracketColor = (Setting<TextUtil.Color>)this.register(new Setting("BracketColor", TextUtil.Color.WHITE));
        this.commandColor = (Setting<TextUtil.Color>)this.register(new Setting("NameColor", TextUtil.Color.WHITE));
        this.notifyToggles = (Setting<Boolean>)this.register(new Setting("ChatNotify", true, "notifys in chat"));
        this.renderingMode = (Setting<RenderingMode>)this.register(new Setting("Ordering", RenderingMode.Length));
        this.lagTime = (Setting<Integer>)this.register(new Setting("LagTime", 1000, 0, 2000));
        this.burrow = (Setting<Boolean>)this.register(new Setting("BurrowWarner", false, "Your Speed"));
        this.setInstance();
    }
    
    public static HUD getInstance() {
        if (HUD.INSTANCE == null) {
            HUD.INSTANCE = new HUD();
        }
        return HUD.INSTANCE;
    }
    
    private void setInstance() {
        HUD.INSTANCE = this;
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (this.burrow.getValue()) {
            final BlockPos burrowpos = new BlockPos(HUD.mc.player.posX, HUD.mc.player.posY, HUD.mc.player.posZ);
            if (HUD.mc.world.getBlockState(burrowpos).getBlock() == Blocks.OBSIDIAN | HUD.mc.world.getBlockState(burrowpos.add(0.0, 0.4, 0.0)).getBlock() == Blocks.ENDER_CHEST) {
                Rebirth.textManager.drawString(ChatFormatting.DARK_RED + "You are in Burrow!", (float)(this.renderer.scaledWidth / 2 - 45), (float)(this.renderer.scaledHeight / 2 - 20), 200, true);
            }
        }
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        if (this.waterMark.getValue() && !this.watermark2.getValue()) {
            final String string = ClickGui.getInstance().clientName.getValueAsString() + " " + "1.0.0.0";
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, 2.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = string.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 2.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        final int[] array2 = arrayOfInt;
                        final int n = 0;
                        ++array2[n];
                    }
                }
            }
            else {
                this.renderer.drawString(string, 2.0f, 2.0f, this.color, true);
            }
        }
        if (this.waterMark.getValue() && this.watermark2.getValue()) {
            if (this.alpha.getValue() >= 0) {
                RenderUtil.drawRectangleCorrectly(this.compactX.getValue(), this.compactY.getValue(), 10 + this.renderer.getStringWidth(ClickGui.getInstance().clientName.getValueAsString() + " | FPS:" + Minecraft.debugFPS + " | TPS:" + Rebirth.serverManager.getTPS() + " | Ping:" + Rebirth.serverManager.getPing()), 15, ColorUtil.toRGBA(20, 20, 20, this.alpha.getValue()));
            }
            final String string = ClickGui.getInstance().clientName.getValueAsString() + " | FPS:" + Minecraft.debugFPS + " | TPS:" + Rebirth.serverManager.getTPS() + " | Ping:" + Rebirth.serverManager.getPing();
            this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
            if (ClickGui.getInstance().rainbow.getValue()) {
                RenderUtil.drawRectangleCorrectly(this.compactX.getValue(), this.compactY.getValue(), 10 + this.renderer.getStringWidth(ClickGui.getInstance().clientName.getValueAsString() + " | FPS:" + Minecraft.debugFPS + " | TPS:" + Rebirth.serverManager.getTPS() + " | Ping:" + Rebirth.serverManager.getPing()), 1, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB());
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, (float)(this.compactX.getValue() + 5), (float)(this.compactY.getValue() + 4), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = string.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), this.compactX.getValue() + 5 + f, (float)(this.compactY.getValue() + 4), ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        final int[] array4 = arrayOfInt;
                        final int n3 = 0;
                        ++array4[n3];
                    }
                }
            }
            else {
                this.renderer.drawString(string, (float)(this.compactX.getValue() + 5), (float)(this.compactY.getValue() + 4), this.color, true);
                RenderUtil.drawRectangleCorrectly(this.compactX.getValue(), this.compactY.getValue(), 10 + this.renderer.getStringWidth(ClickGui.getInstance().clientName.getValueAsString() + " | FPS:" + Minecraft.debugFPS + " | TPS:" + Rebirth.serverManager.getTPS() + " | Ping:" + Rebirth.serverManager.getPing()), 1, this.color);
            }
        }
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        if (this.FriendList.getValue()) {
            this.renderFriends();
        }
        final int[] counter1 = { 1 };
        int j = (HUD.mc.currentScreen instanceof GuiChat && !this.renderingUp.getValue()) ? 14 : 0;
        if (this.arrayList.getValue()) {
            if (this.renderingUp.getValue()) {
                if (this.renderingMode.getValue() == RenderingMode.ABC) {
                    for (int k = 0; k < Rebirth.moduleManager.sortedModulesABC.size(); ++k) {
                        final String str = Rebirth.moduleManager.sortedModulesABC.get(k);
                        this.renderer.drawString(str, (float)(width - 2 - this.renderer.getStringWidth(str)), (float)(2 + j * 4 + this.arraylisty.getValue()), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        j += 3;
                        final int[] array5 = counter1;
                        final int n4 = 0;
                        ++array5[n4];
                    }
                }
                else {
                    for (int k = 0; k < Rebirth.moduleManager.sortedModules.size(); ++k) {
                        final Module module = Rebirth.moduleManager.sortedModules.get(k);
                        final String str2 = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                        this.renderer.drawString(str2, (float)(width - 2 - this.renderer.getStringWidth(str2)), (float)(2 + j * 4 + this.arraylisty.getValue()), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        j += 3;
                        final int[] array6 = counter1;
                        final int n5 = 0;
                        ++array6[n5];
                    }
                }
            }
            else if (this.renderingMode.getValue() == RenderingMode.ABC) {
                for (int k = 0; k < Rebirth.moduleManager.sortedModulesABC.size(); ++k) {
                    final String str = Rebirth.moduleManager.sortedModulesABC.get(k);
                    j += 12;
                    this.renderer.drawString(str, (float)(width - 2 - this.renderer.getStringWidth(str)), (float)(height - j), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    final int[] array7 = counter1;
                    final int n6 = 0;
                    ++array7[n6];
                }
            }
            else {
                for (int k = 0; k < Rebirth.moduleManager.sortedModules.size(); ++k) {
                    final Module module = Rebirth.moduleManager.sortedModules.get(k);
                    final String str2 = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    j += 12;
                    this.renderer.drawString(str2, (float)(width - 2 - this.renderer.getStringWidth(str2)), (float)(height - j), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    final int[] array8 = counter1;
                    final int n7 = 0;
                    ++array8[n7];
                }
            }
        }
        final String grayString = this.grayNess.getValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        int i = (HUD.mc.currentScreen instanceof GuiChat && this.renderingUp.getValue()) ? 13 : (this.renderingUp.getValue() ? -2 : 0);
        if (this.renderingUp.getValue()) {
            if (this.server.getValue()) {
                final String sText = grayString + "IP " + ChatFormatting.WHITE + (HUD.mc.isSingleplayer() ? "SinglePlayer" : Objects.requireNonNull(HUD.mc.getCurrentServerData()).serverIP);
                i += 10;
                this.renderer.drawString(sText, (float)(width - this.renderer.getStringWidth(sText) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                final int[] array9 = counter1;
                final int n8 = 0;
                ++array9[n8];
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + Rebirth.speedManager.getSpeedKpH() + " km/h";
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                final int[] array10 = counter1;
                final int n9 = 0;
                ++array10[n9];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + Rebirth.serverManager.getTPS();
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                final int[] array11 = counter1;
                final int n10 = 0;
                ++array11[n10];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            final String str3 = grayString + "Ping " + ChatFormatting.WHITE + Rebirth.serverManager.getPing();
            if (this.renderer.getStringWidth(str3) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    final int[] array12 = counter1;
                    final int n11 = 0;
                    ++array12[n11];
                }
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    final int[] array13 = counter1;
                    final int n12 = 0;
                    ++array13[n12];
                }
            }
            else {
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    final int[] array14 = counter1;
                    final int n13 = 0;
                    ++array14[n13];
                }
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    final int[] array15 = counter1;
                    final int n14 = 0;
                    ++array15[n14];
                }
            }
        }
        else {
            if (this.server.getValue()) {
                final String sText = grayString + "IP " + ChatFormatting.WHITE + (HUD.mc.isSingleplayer() ? "SinglePlayer" : Objects.requireNonNull(HUD.mc.getCurrentServerData()).serverIP);
                this.renderer.drawString(sText, (float)(width - this.renderer.getStringWidth(sText) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                final int[] array16 = counter1;
                final int n15 = 0;
                ++array16[n15];
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + Rebirth.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                final int[] array17 = counter1;
                final int n16 = 0;
                ++array17[n16];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + Rebirth.serverManager.getTPS();
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                final int[] array18 = counter1;
                final int n17 = 0;
                ++array18[n17];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            final String str4 = grayString + "Ping " + ChatFormatting.WHITE + Rebirth.serverManager.getPing();
            if (this.renderer.getStringWidth(str4) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    final int[] array19 = counter1;
                    final int n18 = 0;
                    ++array19[n18];
                }
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    final int[] array20 = counter1;
                    final int n19 = 0;
                    ++array20[n19];
                }
            }
            else {
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    final int[] array21 = counter1;
                    final int n20 = 0;
                    ++array21[n20];
                }
                if (this.ping.getValue()) {
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    final int[] array22 = counter1;
                    final int n21 = 0;
                    ++array22[n21];
                }
            }
        }
        final boolean inHell = HUD.mc.world.getBiome(HUD.mc.player.getPosition()).getBiomeName().equals("Hell");
        final int posX = (int)HUD.mc.player.posX;
        final int posY = (int)HUD.mc.player.posY;
        final int posZ = (int)HUD.mc.player.posZ;
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int)(HUD.mc.player.posX * nether);
        final int hposZ = (int)(HUD.mc.player.posZ * nether);
        i = ((HUD.mc.currentScreen instanceof GuiChat) ? 14 : 0);
        final String coordinates = ChatFormatting.WHITE + "XYZ " + ChatFormatting.RESET + (inHell ? (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]" + ChatFormatting.RESET) : (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]"));
        final String direction = this.coords.getValue() ? Rebirth.rotationManager.getDirection4D(false) : "";
        final String coords = this.coords.getValue() ? coordinates : "";
        i += 10;
        if (ClickGui.getInstance().rainbow.getValue()) {
            final String rainbowCoords = this.coords.getValue() ? ("XYZ " + posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]") : "";
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(direction, 2.0f, (float)(height - i - 11), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                this.renderer.drawString(rainbowCoords, 2.0f, (float)(height - i), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            else {
                final int[] counter2 = { 1 };
                final char[] stringToCharArray2 = direction.toCharArray();
                float s = 0.0f;
                for (final char c2 : stringToCharArray2) {
                    this.renderer.drawString(String.valueOf(c2), 2.0f + s, (float)(height - i - 11), ColorUtil.rainbow(counter2[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    s += this.renderer.getStringWidth(String.valueOf(c2));
                    final int[] array24 = counter2;
                    final int n23 = 0;
                    ++array24[n23];
                }
                final int[] counter3 = { 1 };
                final char[] stringToCharArray3 = rainbowCoords.toCharArray();
                float u = 0.0f;
                for (final char c3 : stringToCharArray3) {
                    this.renderer.drawString(String.valueOf(c3), 2.0f + u, (float)(height - i), ColorUtil.rainbow(counter3[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    u += this.renderer.getStringWidth(String.valueOf(c3));
                    final int[] array26 = counter3;
                    final int n25 = 0;
                    ++array26[n25];
                }
            }
        }
        else {
            this.renderer.drawString(direction, 2.0f, (float)(height - i - 11), this.color, true);
            this.renderer.drawString(coords, 2.0f, (float)(height - i), this.color, true);
        }
        if (this.lag.getValue()) {
            this.renderLag();
        }
    }
    
    public void renderLag() {
        final int width = this.renderer.scaledWidth;
        if (Rebirth.serverManager.isServerNotResponding()) {
            final String text = ChatFormatting.RED + "Server not responding " + MathUtil.round(Rebirth.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 20.0f, this.color, true);
        }
    }
    
    private void renderFriends() {
        final List<String> friends = new ArrayList<String>();
        for (final EntityPlayer player : HUD.mc.world.playerEntities) {
            if (Rebirth.friendManager.isFriend(player.getName())) {
                friends.add(player.getName());
            }
        }
        if (ClickGui.getInstance().rainbow.getValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                int y = this.FriendListY.getValue();
                if (friends.isEmpty()) {
                    this.renderer.drawString("No friends", 0.0f, (float)y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    this.renderer.drawString("Friends:", 0.0f, (float)y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    y += 12;
                    for (final String friend : friends) {
                        this.renderer.drawString(friend, 0.0f, (float)y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        y += 12;
                    }
                }
            }
        }
        else {
            int y = this.FriendListY.getValue();
            if (friends.isEmpty()) {
                this.renderer.drawString("No Webstas online", 0.0f, (float)y, this.color, true);
            }
            else {
                this.renderer.drawString("Webstas near you:", 0.0f, (float)y, this.color, true);
                y += 12;
                for (final String friend : friends) {
                    this.renderer.drawString(friend, 0.0f, (float)y, this.color, true);
                    y += 12;
                }
            }
        }
    }
    
    static {
        HUD.INSTANCE = new HUD();
    }
    
    public enum RenderingMode
    {
        Length, 
        ABC;
    }
}
