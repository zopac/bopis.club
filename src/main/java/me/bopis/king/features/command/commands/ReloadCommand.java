package me.bopis.king.features.command.commands;

import me.bopis.king.Bopis;
import me.bopis.king.features.command.Command;

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

