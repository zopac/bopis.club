package me.bopis.king.features.command.commands;

import me.bopis.king.Bopis;
import me.bopis.king.features.command.Command;

public class UnloadCommand
        extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Bopis.unload(true);
    }
}

