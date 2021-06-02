package me.lord.bopis.event.events;

import me.lord.bopis.event.EventStage;

public class KeyPressedEvent
        extends EventStage {
    public boolean info;
    public boolean pressed;

    public KeyPressedEvent(boolean info, boolean pressed) {
        this.info = info;
        this.pressed = pressed;
    }
}

