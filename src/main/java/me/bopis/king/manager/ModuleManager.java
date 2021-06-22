package me.bopis.king.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.bopis.king.Bopis;
import me.bopis.king.event.events.Render2DEvent;
import me.bopis.king.event.events.Render3DEvent;
import me.bopis.king.features.Feature;
import me.bopis.king.features.gui.ClickGuiScreen;
import me.bopis.king.features.modules.Module;
import me.bopis.king.features.modules.client.*;
import me.bopis.king.features.modules.combat.*;
import me.bopis.king.features.modules.misc.*;
import me.bopis.king.features.modules.movement.*;
import me.bopis.king.features.modules.player.*;
import me.bopis.king.features.modules.render.Swing;
import me.bopis.king.features.modules.render.*;
import me.bopis.king.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModuleManager
        extends Feature {
    public ArrayList<Module> modules = new ArrayList<>();
    public List<Module> sortedModules = new ArrayList<>();
    public List<String> sortedModulesABC = new ArrayList<>();
    public Animation animationThread;

    public void init() {
        //CLIENT
        modules.add(new ClickGui());
        modules.add(new FontMod());
        modules.add(new GUIBlur());
        modules.add(new HUD());
        modules.add(new HudComponents());
        modules.add(new NickHider());
        //RENDER
        modules.add(new Shaders());
        modules.add(new BlockHighlight());
        modules.add(new HoleESP());
        modules.add(new Skeleton());
        modules.add(new Chams());
        modules.add(new SmallShield());
        modules.add(new HandChams());
        modules.add(new Trajectories());
        modules.add(new ArrowESP());
        modules.add(new GlintModify());
        modules.add(new SkyColor());
        modules.add(new ESP());
        modules.add(new TexturedChams());
        modules.add(new HitMarkers());
        modules.add(new CrystalModifier());
        modules.add(new StorageESP());
        modules.add(new NoFog());
        modules.add(new LogoutSpots());
        modules.add(new VoidESP());
        modules.add(new PortalESP());
        modules.add(new Nametags());
        //COMBAT
        modules.add(new AutoTotem());
        modules.add(new Surround());
        modules.add(new AutoTrap());
        modules.add(new GodModule());
        modules.add(new AutoWeb());
        modules.add(new AutoCrystal());
        modules.add(new Killaura());
        modules.add(new Criticals());
        modules.add(new HoleFill());
        modules.add(new AutoArmor());
        modules.add(new Selftrap());
        modules.add(new Quiver());
        modules.add(new Anti32k());
        modules.add(new Auto32k());
        modules.add(new Burrow());
        modules.add(new BedBomb());
        modules.add(new AutoLog());
        //PLAYER
        modules.add(new Announcer());
        modules.add(new BlockTweaks());
        modules.add(new FakeKick());
        modules.add(new FakePlayer());
        modules.add(new FastPlace());
        modules.add(new Freecam());
        modules.add(new LiquidInteract());
        modules.add(new MCP());
        modules.add(new MultiTask());
        modules.add(new Replenish());
        modules.add(new Speedmine());
        modules.add(new Swing());
        modules.add(new TpsSync());
        modules.add(new XCarry());
        //MISC
        modules.add(new ExtraTab());
        modules.add(new NoHitBox());
        modules.add(new Timestamps());
        modules.add(new NoSoundLag());
        modules.add(new NoHandShake());
        modules.add(new BuildHeight());
        modules.add(new ChatModifier());
        modules.add(new MCF());
        modules.add(new PearlNotify());
        modules.add(new ToolTips());
        modules.add(new Tracker());
        modules.add(new PopCounter());
        modules.add(new NarratorTweaks());
        modules.add(new GhastNotifier());
        modules.add(new BopisHack());
        modules.add(new AutoRespawn());
        modules.add(new ChatMods());
        modules.add(new RPC());
        //MOVEMENT
        modules.add(new BoatFly());
        modules.add(new ReverseStep());
        modules.add(new Static());
        modules.add(new NoSlowDown());
        modules.add(new Sprint());
        modules.add(new Velocity());
        modules.add(new IceSpeed());
        modules.add(new ElytraFlight());
        modules.add(new Flight());

        modules.sort(Comparator.comparing(object -> object.getName())); //sort the modules alphabetically
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public boolean isModuleEnabledthe(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabledthe(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        return module != null && ((Module) module).isOn();
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<String>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public <T extends Module> T getModuleT(Class<T> clazz) {
        return modules.stream().filter(module -> module.getClass() == clazz).map(module -> (T) module).findFirst().orElse(null);
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof ClickGuiScreen) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    private class Animation extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;

        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (Module module : ModuleManager.this.sortedModules) {
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            } else {
                for (String e : ModuleManager.this.sortedModulesABC) {
                    Module module = Bopis.moduleManager.getModuleByName(e);
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            }
        }

        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }

    public static ArrayList<Module> nigger;

    public static ArrayList<Module> getModules() {
        return nigger;
    }

    public static boolean isModuleEnablednigger(String name) {
        return getModules().stream().filter(mm -> mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null).isEnabled();
    }

    public static boolean isModuleEnablednigger(Module modulenigger) {
        return modulenigger.isEnabled();
    }
}

