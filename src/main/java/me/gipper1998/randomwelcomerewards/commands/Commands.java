package me.gipper1998.randomwelcomerewards.commands;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
        if (args.length == 0) {
            List<String> menus = main.messages.getConfig().getStringList("messages.menu");
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
        else if (args.length == 1){
            if (args[0].equalsIgnoreCase("reload")) {
                if (commandSender.hasPermission("randwelrewards.reload") || (commandSender instanceof ConsoleCommandSender) || commandSender.isOp()) {
                    main.reloadConfig();
                    main.messages.reloadConfig();
                    main.data.reloadConfig();
                    main.milestones.reloadConfig();
                    if (commandSender instanceof ConsoleCommandSender)
                        main.consoleMessage(main.messages.getConfig().getString("messages.has-reload"));
                    else
                        main.chatMessage(main.messages.getConfig().getString("messages.has-reload"), (Player) commandSender);
                    return true;
                } else {
                    main.chatMessage(main.messages.getConfig().getString("messages.no-reload"), (Player) commandSender);
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("stats")){
                if (commandSender instanceof ConsoleCommandSender) {
                    main.consoleMessage("<prefix> &cOnly players can perform this command.");
                    return false;
                }
                else {
                    Player player = (Player) commandSender;
                    int newWelcome = 0;
                    int returnWelcome = 0;
                    if (main.data.getConfig().contains("players." + player.getUniqueId().toString() + ".NewWelcomes") && main.data.getConfig().contains("players." + player.getUniqueId().toString() + ".ReturnWelcomes")) {
                        newWelcome = main.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".NewWelcomes");
                        returnWelcome = main.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".ReturnWelcomes");
                    }
                    List<String> scoreSheet = main.messages.getConfig().getStringList("messages.scoreSheet");
                    for (int i = 0; i < scoreSheet.size(); i++) {
                        if (scoreSheet.get(i) == "")
                            player.sendMessage("");
                        else {
                            String message = scoreSheet.get(i);
                            message = message.replaceAll("<player>", player.getName());
                            message = message.replaceAll("<newwelcomes>", Integer.toString(newWelcome));
                            message = message.replaceAll("<returnwelcomes>", Integer.toString(returnWelcome));
                            main.chatMessage(message, player);
                        }
                    }
                }
                return true;
            }
        }
        else if (args.length == 2){
            if (args[0].equalsIgnoreCase("stats") && commandSender.hasPermission("randwelrewards.stats.others")){
                Player player = Bukkit.getPlayerExact(args[1]);
                if (player == null) {
                    main.chatMessage(main.messages.getConfig().getString("messages.offline-player"), (Player) commandSender);
                    return false;
                }
                int newWelcome = 0;
                int returnWelcome = 0;
                if (main.data.getConfig().contains("players." + player.getUniqueId().toString() + ".NewWelcomes") && main.data.getConfig().contains("players." + player.getUniqueId().toString() + ".ReturnWelcomes")) {
                    newWelcome = main.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".NewWelcomes");
                    returnWelcome = main.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".ReturnWelcomes");
                }
                List<String> scoreSheet = main.messages.getConfig().getStringList("messages.scoreSheet");
                for (int i = 0; i < scoreSheet.size(); i++) {
                    if (scoreSheet.get(i) == "")
                        commandSender.sendMessage("");
                    else {
                        String message = scoreSheet.get(i);
                        message = message.replaceAll("<player>", player.getName());
                        message = message.replaceAll("<newwelcomes>", Integer.toString(newWelcome));
                        message = message.replaceAll("<returnwelcomes>", Integer.toString(returnWelcome));
                        main.chatMessage(message, (Player)commandSender);
                    }
                }
            }
        }
        else
            main.chatMessage(main.messages.getConfig().getString("messages.dne"), (Player) commandSender);
        return false;
    }

    List<String> arguments = new ArrayList<String>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("reload");
            arguments.add("stats");
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
