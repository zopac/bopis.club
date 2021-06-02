package me.lord.bopis.event.events;

import me.lord.bopis.event.EventStage;
import net.minecraft.util.math.BlockPos;

public class BlockDestructionEvent extends EventStage {
    BlockPos nigger;
    public BlockDestructionEvent(BlockPos nigger){
        super();
        nigger = nigger;
    }

    public BlockPos getBlockPos(){
        return nigger;
    }
}
