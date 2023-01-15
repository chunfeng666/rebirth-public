

package dev.madcat.rebirth;

import net.minecraftforge.fml.common.*;
import dev.madcat.rebirth.features.gui.font.*;
import dev.madcat.rebirth.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import dev.madcat.rebirth.manager.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.common.*;
import dev.madcat.rebirth.util.*;
import org.apache.logging.log4j.*;

@Mod(modid = "rebirth", name = "rebirth", version = "1.12.2")
public class Rebirth
{
    public static final String MOD_ID = "rebirth";
    public static final String MOD_NAME = "rebirth";
    public static final String VERSION = "1.12.2";
    public static final String ID = "1.0.0.0";
    public static final Logger LOGGER;
    public static TimerManager timerManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    public static CustomFont fontRenderer;
    public static Render3DEvent render3DEvent;
    public static Enemy enemy;
    public static final EventBus EVENT_BUS;
    public static MenuFont MENU_FONT_MANAGER;
    public static GuiFont GUI_FONT_MANAGER;
    public static DonatorFont DONATOR_FONT_MANAGER;
    public static SongManager SONG_MANAGER;
    @Mod.Instance
    public static Rebirth INSTANCE;
    private static boolean unloaded;
    
    @Mod.EventHandler
    public void preinit(final FMLPreInitializationEvent event) {
        System.out.println("Mod preinit");
    }
    
    @Mod.EventHandler
    public void postinit(final FMLPostInitializationEvent event) {
        System.out.println("Mod postinit");
    }
    
    public static void load() {
        Rebirth.LOGGER.info("loading rebirth");
        Rebirth.unloaded = false;
        if (Rebirth.reloadManager != null) {
            Rebirth.reloadManager.unload();
            Rebirth.reloadManager = null;
        }
        Rebirth.textManager = new TextManager();
        Rebirth.commandManager = new CommandManager();
        Rebirth.friendManager = new FriendManager();
        Rebirth.moduleManager = new ModuleManager();
        Rebirth.rotationManager = new RotationManager();
        Rebirth.packetManager = new PacketManager();
        Rebirth.eventManager = new EventManager();
        Rebirth.speedManager = new SpeedManager();
        Rebirth.potionManager = new PotionManager();
        Rebirth.inventoryManager = new InventoryManager();
        Rebirth.serverManager = new ServerManager();
        Rebirth.fileManager = new FileManager();
        Rebirth.colorManager = new ColorManager();
        Rebirth.positionManager = new PositionManager();
        Rebirth.configManager = new ConfigManager();
        Rebirth.holeManager = new HoleManager();
        Rebirth.LOGGER.info("Managers loaded.");
        Rebirth.moduleManager.init();
        Rebirth.LOGGER.info("Modules loaded.");
        Rebirth.configManager.init();
        Rebirth.eventManager.init();
        Rebirth.LOGGER.info("EventManager loaded.");
        Rebirth.textManager.init(true);
        Rebirth.moduleManager.onLoad();
        Rebirth.LOGGER.info("rebirth successfully loaded!\n");
    }
    
    public static void unload(final boolean unload) {
        Rebirth.LOGGER.info("unloading rebirth");
        if (unload) {
            (Rebirth.reloadManager = new ReloadManager()).init((Rebirth.commandManager != null) ? Rebirth.commandManager.getPrefix() : ".");
        }
        onUnload();
        Rebirth.eventManager = null;
        Rebirth.friendManager = null;
        Rebirth.speedManager = null;
        Rebirth.holeManager = null;
        Rebirth.positionManager = null;
        Rebirth.rotationManager = null;
        Rebirth.configManager = null;
        Rebirth.commandManager = null;
        Rebirth.colorManager = null;
        Rebirth.serverManager = null;
        Rebirth.fileManager = null;
        Rebirth.potionManager = null;
        Rebirth.inventoryManager = null;
        Rebirth.moduleManager = null;
        Rebirth.textManager = null;
        Rebirth.LOGGER.info("rebirth unloaded!\n");
    }
    
    public static void reload() {
        unload(false);
        load();
    }
    
    public static void onUnload() {
        if (!Rebirth.unloaded) {
            Rebirth.eventManager.onUnload();
            Rebirth.moduleManager.onUnload();
            Rebirth.configManager.saveConfig(Rebirth.configManager.config.replaceFirst("Rebirth/", ""));
            Rebirth.moduleManager.onUnloadPost();
            Rebirth.unloaded = true;
        }
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)new Title());
        load();
    }
    
    static {
        LOGGER = LogManager.getLogger("rebirth");
        EVENT_BUS = new EventBus();
        Rebirth.MENU_FONT_MANAGER = new MenuFont();
        Rebirth.GUI_FONT_MANAGER = new GuiFont();
        Rebirth.DONATOR_FONT_MANAGER = new DonatorFont();
        Rebirth.SONG_MANAGER = new SongManager();
        Rebirth.unloaded = false;
    }
}
