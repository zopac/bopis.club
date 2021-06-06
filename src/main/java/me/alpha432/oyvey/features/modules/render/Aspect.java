package me.travis.wurstplusthree.hack.render;

import me.alpha432.oyvey.event.events.PerspectiveEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author SankuGG
 * @since 01/05/2021
 *  -> src from prism
 */

public class Aspect extends Module {
public Aspect() {
        super("Aspect", "Highlights the block u look at.", Module.Category.RENDER, false, false, false);
}
    private final Setting<Integer> aspect = register(new Setting<Integer>("AspectRatio", 255, 0, 255));

    @SubscribeEvent
    public void onPerspectiveEvent(PerspectiveEvent event){
        event.setAspect(aspect.getValue().floatValue());
    }
}
