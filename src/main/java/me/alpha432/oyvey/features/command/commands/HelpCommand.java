package me.alpha432.oyvey.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.bopis;
import me.alpha432.oyvey.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : bopis.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + bopis.commandManager.getPrefix() + command.getName());
        }
    }
}

