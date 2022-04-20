package me.gipper1998.randomwelcomerewards.commands;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import me.gipper1998.randomwelcomerewards.data.Leaderboard;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;


public class Commands implements TabExecutor {
    private final RandomWelcomeRewards main;
    public Leaderboard leaderboard;
    public Commands (RandomWelcomeRewards main){
        this.main = main;
        leaderboard = new Leaderboard(this.main);
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
                if (commandSender.hasPermission("randomwelcomerewards.reload") || (commandSender instanceof ConsoleCommandSender) || commandSender.isOp()) {
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
                    main.chatMessage(main.messages.getConfig().getString("messages.no-perms"), (Player) commandSender);
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
            if (args[0].equalsIgnoreCase("stats")) {
                if (commandSender.hasPermission("randomwelcomerewards.stats.others") || commandSender.isOp() || commandSender instanceof ConsoleCommandSender) {
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
                            if (commandSender instanceof ConsoleCommandSender)
                                main.consoleMessage(message);
                            else
                                main.chatMessage(message, (Player) commandSender);
                        }
                    }
                }
            }
            if (args[0].equalsIgnoreCase("leaderboards")) {
                if (args[1].equalsIgnoreCase("newWelcomes")) {
                    if (commandSender.hasPermission("randomwelcomerewards.leaderboards.newwelcomes") || commandSender.isOp() || commandSender instanceof ConsoleCommandSender) {
                        List<String> topMessageList = main.messages.getConfig().getStringList("messages.leaderboards.topNewWelcomeBoard");
                        List<String> bottomMessageList = main.messages.getConfig().getStringList("messages.leaderboards.bottomNewWelcomeBoard");
                        for (int i = 0; i < topMessageList.size(); i++) {
                            if (topMessageList.get(i) == "")
                                commandSender.sendMessage("");
                            else {
                                String message = topMessageList.get(i);
                                if (commandSender instanceof ConsoleCommandSender)
                                    main.consoleMessage(message);
                                else
                                    main.chatMessage(message, (Player) commandSender);
                            }
                        }
                        if (commandSender instanceof Player)
                            leaderboard.sendLeaderBoardStats((Player) commandSender, true, main.getConfig().getInt("settings.leaderboardLength"), false);
                        else
                            leaderboard.sendLeaderBoardStats((Player) commandSender, true, main.getConfig().getInt("settings.leaderboardLength"), true);
                        for (int i = 0; i < bottomMessageList.size(); i++) {
                            if (bottomMessageList.get(i) == "")
                                commandSender.sendMessage("");
                            else {
                                String message = bottomMessageList.get(i);
                                if (commandSender instanceof ConsoleCommandSender)
                                    main.consoleMessage(message);
                                else
                                    main.chatMessage(message, (Player) commandSender);
                            }
                        }
                    }
                }
                if (args[1].equalsIgnoreCase("returnWelcomes")){
                    if (commandSender.hasPermission("randomwelcomerewards.leaderboards.returnwelcomes") || commandSender.isOp() || commandSender instanceof ConsoleCommandSender) {
                        List<String> topMessageList = main.messages.getConfig().getStringList("messages.leaderboards.topReturnWelcomeBoard");
                        List<String> bottomMessageList = main.messages.getConfig().getStringList("messages.leaderboards.bottomReturnWelcomeBoard");
                        for (int i = 0; i < topMessageList.size(); i++) {
                            if (topMessageList.get(i) == "")
                                commandSender.sendMessage("");
                            else {
                                String message = topMessageList.get(i);
                                if (commandSender instanceof ConsoleCommandSender)
                                    main.consoleMessage(message);
                                else
                                    main.chatMessage(message, (Player) commandSender);
                            }
                        }
                        if (commandSender instanceof Player)
                            leaderboard.sendLeaderBoardStats((Player) commandSender, false, main.getConfig().getInt("settings.leaderboardLength"), false);
                        else
                            leaderboard.sendLeaderBoardStats((Player) commandSender, false, main.getConfig().getInt("settings.leaderboardLength"), true);
                        for (int i = 0; i < bottomMessageList.size(); i++) {
                            if (bottomMessageList.get(i) == "")
                                commandSender.sendMessage("");
                            else {
                                String message = bottomMessageList.get(i);
                                if (commandSender instanceof ConsoleCommandSender)
                                    main.consoleMessage(message);
                                else
                                    main.chatMessage(message, (Player) commandSender);
                            }
                        }
                    }
                }
            }
        }
        else
            main.chatMessage(main.messages.getConfig().getString("messages.dne"), (Player) commandSender);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1){
            List<String> firstArguments = new ArrayList<>();
            firstArguments.add("reload");
            firstArguments.add("stats");
            firstArguments.add("leaderboards");
            return firstArguments;
        }
        else if (args.length == 2){
            if (args[0].equalsIgnoreCase("stats")){
                List<String> playerNames = new ArrayList<>();
                Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                Bukkit.getServer().getOnlinePlayers().toArray(players);
                for (int i = 0; i < players.length; i++)
                    playerNames.add(players[i].getName());
            }
            else if (args[0].equalsIgnoreCase("leaderboards")){
                List<String> secondArguments = new ArrayList<>();
                secondArguments.add("newwelcomes");
                secondArguments.add("returnwelcomes");
                return secondArguments;
            }
            else
                return null;
        }
        return null;
    }
}
