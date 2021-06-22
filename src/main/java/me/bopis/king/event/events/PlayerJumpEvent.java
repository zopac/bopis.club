package me.bopis.king.event.events;

import me.bopis.king.event.EventStage;

public class PlayerJumpEvent extends EventStage {
    public double motionX;
    public double motionY;

    public PlayerJumpEvent(double motionX, double motionY) {
        super();
        this.motionX = motionX;
        this.motionY = motionY;
    }
}