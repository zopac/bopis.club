package me.lord.bopis.features.command.commands;

import me.lord.bopis.Bopis;
import me.lord.bopis.features.command.Command;

public class ReloadCommand
        extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Bopis.reload();
    }
}

