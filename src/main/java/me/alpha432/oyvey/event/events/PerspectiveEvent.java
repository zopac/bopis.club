package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.EventStage;

/**
 * @author Madmegsox1
 * @since 29/04/2021
 */

public class PerspectiveEvent extends EventStage {
    private float aspect;

    public PerspectiveEvent(float aspect) {
        this.aspect = aspect;
    }

    public float getAspect() {
        return aspect;
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
    }
}