package me.lord.bopis.event.events;

import me.lord.bopis.event.EventStage;

public class PlayerJumpEvent extends EventStage {
    public double motionX;
    public double motionY;

    public PlayerJumpEvent(double motionX, double motionY)
    {
        super();
        this.motionX = motionX;
        this.motionY = motionY;
    }
}