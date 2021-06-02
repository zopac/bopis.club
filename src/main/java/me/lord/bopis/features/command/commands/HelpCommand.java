package me.lord.bopis.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.lord.bopis.Bopis;
import me.lord.bopis.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : Bopis.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + Bopis.commandManager.getPrefix() + command.getName());
        }
    }
}

