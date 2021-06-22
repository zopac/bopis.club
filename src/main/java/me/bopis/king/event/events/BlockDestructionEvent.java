package me.bopis.king.event.events;

import me.bopis.king.event.EventStage;
import net.minecraft.util.math.BlockPos;

public class BlockDestructionEvent extends EventStage {
    BlockPos nigger;

    public BlockDestructionEvent(BlockPos nigger) {
        super();
        this.nigger = nigger;
    }

    public BlockPos getBlockPos() {
        return nigger;
    }
}
