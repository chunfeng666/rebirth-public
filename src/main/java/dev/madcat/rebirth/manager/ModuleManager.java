
package dev.madcat.rebirth.manager;

import dev.madcat.rebirth.features.*;
import dev.madcat.rebirth.features.modules.*;
import dev.madcat.rebirth.features.modules.client.*;
import dev.madcat.rebirth.features.modules.combat.*;
import dev.madcat.rebirth.features.modules.render.*;
import dev.madcat.rebirth.features.modules.player.*;
import dev.madcat.rebirth.features.modules.movement.*;
import dev.madcat.rebirth.features.modules.misc.*;
import net.minecraftforge.common.*;
import java.util.function.*;
import dev.madcat.rebirth.event.events.*;
import java.util.stream.*;
import java.util.*;
import org.lwjgl.input.*;
import dev.madcat.rebirth.features.gui.*;
import com.mojang.realmsclient.gui.*;
import dev.madcat.rebirth.util.*;
import java.util.concurrent.*;

public class ModuleManager extends Feature
{
    public static ArrayList<Module> modules;
    public List<Module> sortedModules;
    public List<String> sortedModulesABC;
    public static ArrayList<Module> nigger;
    
    public ModuleManager() {
        this.sortedModules = new ArrayList<Module>();
        this.sortedModulesABC = new ArrayList<String>();
    }
    
    public void init() {
        ModuleManager.modules.add((Module)new ClickGui());
        ModuleManager.modules.add((Module)new FontMod());
        ModuleManager.modules.add((Module)new PopCounter());
        ModuleManager.modules.add((Module)new AntiWeb());
        ModuleManager.modules.add((Module)new GUIBlur());
        ModuleManager.modules.add((Module)new HUD());
        ModuleManager.modules.add((Module)new Criticals());
        ModuleManager.modules.add((Module)new HoleESP());
        ModuleManager.modules.add((Module)new WorldModify());
        ModuleManager.modules.add((Module)new AutoReconnect());
        ModuleManager.modules.add((Module)new Message());
        ModuleManager.modules.add((Module)new KillAura());
        ModuleManager.modules.add((Module)new StorageESP());
        ModuleManager.modules.add((Module)new Crasher());
        ModuleManager.modules.add((Module)new Peek());
        ModuleManager.modules.add((Module)new ItemStats());
        ModuleManager.modules.add((Module)new HudComponents());
        ModuleManager.modules.add((Module)new Title());
        ModuleManager.modules.add((Module)new BreakCheck());
        ModuleManager.modules.add((Module)new NoEntityTrace());
        ModuleManager.modules.add((Module)new Notify());
        ModuleManager.modules.add((Module)new SmartTrap());
        ModuleManager.modules.add((Module)new CameraClip());
        ModuleManager.modules.add((Module)new BlockFly());
        ModuleManager.modules.add((Module)new NoRotate());
        ModuleManager.modules.add((Module)new Trajectories());
        ModuleManager.modules.add((Module)new TargetHUD());
        ModuleManager.modules.add((Module)new FullBright());
        ModuleManager.modules.add((Module)new ArrowESP());
        ModuleManager.modules.add((Module)new NickHider());
        ModuleManager.modules.add((Module)new AntiDeathScreen());
        ModuleManager.modules.add((Module)new HoleDefense());
        ModuleManager.modules.add((Module)new CityESP());
        ModuleManager.modules.add((Module)new Shaders());
        ModuleManager.modules.add((Module)new Nuker());
        ModuleManager.modules.add((Module)new AntiCity());
        ModuleManager.modules.add((Module)new NameTags());
        ModuleManager.modules.add((Module)new AntiVoid());
        ModuleManager.modules.add((Module)new AntiHunger());
        ModuleManager.modules.add((Module)new Animations());
        ModuleManager.modules.add((Module)new ReverseStep());
        ModuleManager.modules.add((Module)new LogESP());
        ModuleManager.modules.add((Module)new SkyColor());
        ModuleManager.modules.add((Module)new AutoDupe());
        ModuleManager.modules.add((Module)new ViewModel());
        ModuleManager.modules.add((Module)new WebTime());
        ModuleManager.modules.add((Module)new Tracer());
        ModuleManager.modules.add((Module)new FastPlace());
        ModuleManager.modules.add((Module)new InventoryMove());
        ModuleManager.modules.add((Module)new NoSlow());
        ModuleManager.modules.add((Module)new Sprint());
        ModuleManager.modules.add((Module)new Velocity());
        ModuleManager.modules.add((Module)new Flight());
        ModuleManager.modules.add((Module)new AutoTotem());
        ModuleManager.modules.add((Module)new Surround());
        ModuleManager.modules.add((Module)new NoFall());
        ModuleManager.modules.add((Module)new Strafe());
        ModuleManager.modules.add((Module)new Chat());
        ModuleManager.modules.add((Module)new Burrow());
        ModuleManager.modules.add((Module)new AutoCrystal());
        ModuleManager.modules.add((Module)new AutoCity());
        ModuleManager.modules.add((Module)new Chams());
        ModuleManager.modules.add((Module)new FeetPad());
        ModuleManager.modules.add((Module)new Particles());
        ModuleManager.modules.add((Module)new PopChams());
        ModuleManager.modules.add((Module)new NoRender());
        ModuleManager.modules.add((Module)new InstantMine());
        ModuleManager.modules.add((Module)new AntiBurrow());
        ModuleManager.modules.add((Module)new ArmorWarner());
        ModuleManager.modules.add((Module)new AntiPackets());
        ModuleManager.modules.add((Module)new AutoLog());
        ModuleManager.modules.add((Module)new AntiCev());
        ModuleManager.modules.add((Module)new CevSelect());
        ModuleManager.modules.add((Module)new TrapSelf());
        ModuleManager.modules.add((Module)new AntiHoleKick());
        ModuleManager.modules.add((Module)new Anchor());
        ModuleManager.modules.add((Module)new HoleFiller());
        ModuleManager.modules.add((Module)new Flatten());
        ModuleManager.modules.add((Module)new BreakESP());
        ModuleManager.modules.add((Module)new Timers());
        ModuleManager.modules.add((Module)new Blink());
        ModuleManager.modules.add((Module)new FastElytra());
        ModuleManager.modules.add((Module)new AutoArmor());
        ModuleManager.modules.add((Module)new AntiChestGui());
        ModuleManager.modules.add((Module)new Interact());
        ModuleManager.modules.add((Module)new BetterPortal());
        ModuleManager.modules.add((Module)new Replenish());
        ModuleManager.modules.add((Module)new FakePlayer());
        ModuleManager.modules.add((Module)new Reach());
        ModuleManager.modules.add((Module)new MCP());
        ModuleManager.modules.add((Module)new PacketEat());
        ModuleManager.modules.add((Module)new MCF());
        ModuleManager.modules.add((Module)new AutoQueue());
        ModuleManager.modules.add((Module)new PacketXP());
        ModuleManager.modules.add((Module)new XCarry());
        ModuleManager.modules.add((Module)new Speed());
        ModuleManager.modules.add((Module)new Step());
        ModuleManager.modules.add((Module)new ChatSuffix());
    }
    
    public static Module getModuleByName(final String name) {
        for (final Module module : ModuleManager.modules) {
            if (!module.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public static <T extends Module> T getModuleByClass(final Class<T> clazz) {
        for (final Module module : ModuleManager.modules) {
            if (!clazz.isInstance(module)) {
                continue;
            }
            return (T)module;
        }
        return null;
    }
    
    public void enableModule(final Class<Module> clazz) {
        final Module module = getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final Class<Module> clazz) {
        final Module module = getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }
    
    public void enableModule(final String name) {
        final Module module = getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final String name) {
        final Module module = getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }
    
    public boolean isModuleEnabled(final String name) {
        final Module module = getModuleByName(name);
        return module != null && module.isOn();
    }
    
    public boolean isModuleEnabled(final Class<Module> clazz) {
        final Module module = getModuleByClass(clazz);
        return module != null && module.isOn();
    }
    
    public Module getModuleByDisplayName(final String displayName) {
        for (final Module module : ModuleManager.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public ArrayList<Module> getEnabledModules() {
        final ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (final Module module : ModuleManager.modules) {
            if (!module.isEnabled()) {
                continue;
            }
            enabledModules.add(module);
        }
        return enabledModules;
    }
    
    public ArrayList<String> getEnabledModulesName() {
        final ArrayList<String> enabledModules = new ArrayList<String>();
        for (final Module module : ModuleManager.modules) {
            if (module.isEnabled()) {
                if (!module.isDrawn()) {
                    continue;
                }
                enabledModules.add(module.getFullArrayString());
            }
        }
        return enabledModules;
    }
    
    public ArrayList<Module> getModulesByCategory(final Module.Category category) {
        final ArrayList<Module> modulesCategory = new ArrayList<Module>();
        final ArrayList<Module> list;
        ModuleManager.modules.forEach(module -> {
            if (module.getCategory() == category) {
                list.add(module);
            }
            return;
        });
        return modulesCategory;
    }
    
    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }
    
    public void onLoad() {
        ModuleManager.modules.stream().filter(Module::listening).forEach(MinecraftForge.EVENT_BUS::register);
        ModuleManager.modules.forEach(Module::onLoad);
    }
    
    public void onUpdate() {
        ModuleManager.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }
    
    public void onTick() {
        ModuleManager.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }
    
    public void onRender2D(final Render2DEvent event) {
        ModuleManager.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }
    
    public void onRender3D(final Render3DEvent event) {
        ModuleManager.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }
    
    public <T extends Module> T getModuleT(final Class<T> clazz) {
        return ModuleManager.modules.stream().filter(module -> module.getClass() == clazz).map(module -> module).findFirst().orElse(null);
    }
    
    public void sortModules(final boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
    }
    
    public void sortModulesABC() {
        (this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName())).sort(String.CASE_INSENSITIVE_ORDER);
    }
    
    public void onLogout() {
        ModuleManager.modules.forEach(Module::onLogout);
    }
    
    public void onLogin() {
        ModuleManager.modules.forEach(Module::onLogin);
    }
    
    public void onUnload() {
        ModuleManager.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        ModuleManager.modules.forEach(Module::onUnload);
    }
    
    public void onUnloadPost() {
        for (final Module module : ModuleManager.modules) {
            module.enabled.setValue((Object)false);
        }
    }
    
    public void onKeyPressed(final int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof RebirthGui) {
            return;
        }
        ModuleManager.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }
    
    public static ArrayList<Module> getModules() {
        return ModuleManager.nigger;
    }
    
    public static boolean isModuleEnablednigger(final String name) {
        final Module modulenigger = getModules().stream().filter(mm -> mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        assert modulenigger != null;
        return modulenigger.isEnabled();
    }
    
    public static boolean isModuleEnablednigger(final Module modulenigger) {
        return modulenigger.isEnabled();
    }
    
    static {
        ModuleManager.modules = new ArrayList<Module>();
    }
    
    private class Animation extends Thread
    {
        public Module module;
        public float offset;
        ScheduledExecutorService service;
        
        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }
        
        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (final Module module : ModuleManager.this.sortedModules) {
                    final String text = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    module.offset = (float)ModuleManager.this.renderer.getStringWidth(text);
                    module.vOffset = (float)ModuleManager.this.renderer.getFontHeight();
                    if (module.isEnabled()) {
                        if (module.arrayListOffset <= module.offset) {
                            continue;
                        }
                        if (Util.mc.world == null) {
                            continue;
                        }
                        final Module module3 = module;
                        module3.arrayListOffset -= module.offset;
                        module.sliding = true;
                    }
                    else {
                        if (!module.isDisabled()) {
                            continue;
                        }
                        if (module.arrayListOffset < ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                            final Module module4 = module;
                            module4.arrayListOffset += module.offset;
                            module.sliding = true;
                        }
                        else {
                            module.sliding = false;
                        }
                    }
                }
            }
            else {
                for (final String e : ModuleManager.this.sortedModulesABC) {
                    final Module module2 = ModuleManager.getModuleByName(e);
                    assert module2 != null;
                    final String text2 = module2.getDisplayName() + ChatFormatting.GRAY + ((module2.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module2.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    module2.offset = (float)ModuleManager.this.renderer.getStringWidth(text2);
                    module2.vOffset = (float)ModuleManager.this.renderer.getFontHeight();
                    if (module2.isEnabled()) {
                        if (module2.arrayListOffset <= module2.offset) {
                            continue;
                        }
                        if (Util.mc.world == null) {
                            continue;
                        }
                        final Module module5 = module2;
                        module5.arrayListOffset -= module2.offset;
                        module2.sliding = true;
                    }
                    else {
                        if (!module2.isDisabled()) {
                            continue;
                        }
                        if (module2.arrayListOffset < ModuleManager.this.renderer.getStringWidth(text2) && Util.mc.world != null) {
                            final Module module6 = module2;
                            module6.arrayListOffset += module2.offset;
                            module2.sliding = true;
                        }
                        else {
                            module2.sliding = false;
                        }
                    }
                }
            }
        }
        
        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}
