package me.lord.bopis.event.events;

import me.lord.bopis.event.EventStage;

public class Render3DEvent extends EventStage
{
    private float partialTicks;

    public Render3DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

