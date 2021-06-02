package me.lord.bopis.features.command.commands;

import me.lord.bopis.Bopis;
import me.lord.bopis.features.command.Command;

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

