
package dev.madcat.rebirth.mixin;

import net.minecraftforge.fml.relauncher.*;
import org.spongepowered.asm.launch.*;
import org.spongepowered.asm.mixin.*;
import java.util.*;

@IFMLLoadingPlugin.Name("Rebirth")
public class RebirthMixinLoader implements IFMLLoadingPlugin
{
    private static boolean isObfuscatedEnvironment;
    
    public RebirthMixinLoader() {
        System.out.println("Loading mixins by MadCat");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.rebirth.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        System.out.println(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        RebirthMixinLoader.isObfuscatedEnvironment = data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    static {
        RebirthMixinLoader.isObfuscatedEnvironment = false;
    }
}
