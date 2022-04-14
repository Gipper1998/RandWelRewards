package me.gipper1998.randomwelcomerewards.commands;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;


public class Commands implements TabExecutor {
    private final RandomWelcomeRewards main;

    public Commands (RandomWelcomeRewards main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String s, String[] args) {
        if (args.length != 0 && (args[0].equalsIgnoreCase("reload"))) {
            if (commandSender.hasPermission("randwelrewards.reload") || (commandSender instanceof ConsoleCommandSender) || commandSender.isOp()) {
                main.reloadConfig();
                if (commandSender instanceof ConsoleCommandSender)
                    main.consoleMessage(main.getConfig().getString("messages.has-reload"));
                else
                    main.chatMessage(main.getConfig().getString("messages.has-reload"), (Player) commandSender);
                return true;
            } else {
                main.chatMessage(main.getConfig().getString("messages.no-reload"), (Player) commandSender);
                return true;
            }
        }
        else {
            if (args.length == 0) {
                List<String> menus = main.getConfig().getStringList("messages.menu");
                for (int i = 0; i < menus.size(); i++) {
                    if (commandSender instanceof ConsoleCommandSender) {
                        if (menus.get(i) == "")
                            commandSender.sendMessage("");
                        else
                            main.consoleMessage(menus.get(i));
                    }
                    else {
                        if (menus.get(i) == "")
                            commandSender.sendMessage("");
                        else
                            main.chatMessage(menus.get(i), (Player) commandSender);
                    }
                }
                return true;
            }
            else
                main.chatMessage(main.getConfig().getString("messages.dne"), (Player) commandSender);
        }
        return false;
    }

    List<String> arguments = new ArrayList<String>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("reload");
        }
        List<String> result = new ArrayList<String>();
        if (args.length == 1){
            for (String a : arguments){
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        return null;
    }
}
