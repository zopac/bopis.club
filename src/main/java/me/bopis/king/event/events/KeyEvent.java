package me.bopis.king.event.events;

import me.bopis.king.event.EventStage;

public class KeyEvent
        extends EventStage {
    private final int key;
    public boolean info;
    public boolean pressed;

    public KeyEvent(int key, boolean info, boolean pressed) {
        this.key = key;
        this.info = info;
        this.pressed = pressed;
    }

    public int getKey() {
        return this.key;
    }
}

