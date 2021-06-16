package me.bopis.king.hwid;

import me.bopis.king.Bopis;

public class NoStackTraceThrowable extends RuntimeException {

    public NoStackTraceThrowable(final String msg) {
        super(msg);
        this.setStackTrace(new StackTraceElement[0]);
    }

    @Override
    public String toString() {
        return "" + Bopis.getVersion();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
