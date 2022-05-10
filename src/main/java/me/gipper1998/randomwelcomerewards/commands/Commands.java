package me.gipper1998.randomwelcomerewards.commands;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import me.gipper1998.randomwelcomerewards.playerdata.PlayerDataLeaderboard;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Commands implements TabExecutor {
    private final RandomWelcomeRewards main;
    public PlayerDataLeaderboard playerDataLeaderboard;
    public Commands (RandomWelcomeRewards main){
        this.main = main;
        playerDataLeaderboard = new PlayerDataLeaderboard(this.main);
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
                if (hasPermission(commandSender,"randomwelcomerewards.reload)")) {
                    main.config.reloadConfig();
                    main.messages.reloadConfig();
                    main.playerData.reloadConfig();
                    main.milestones.reloadConfig();
                    main.milestoneManager.reloadMilestones();
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
                    return true;
                }
                else {
                    Player player = (Player) commandSender;
                    int newWelcome = 0;
                    int returnWelcome = 0;
                    if (main.playerData.getConfig().contains("players." + player.getUniqueId().toString() + ".NewWelcomes") && main.playerData.getConfig().contains("players." + player.getUniqueId().toString() + ".ReturnWelcomes")) {
                        newWelcome = main.playerData.getConfig().getInt("players." + player.getUniqueId().toString() + ".NewWelcomes");
                        returnWelcome = main.playerData.getConfig().getInt("players." + player.getUniqueId().toString() + ".ReturnWelcomes");
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
            if (args[0].equalsIgnoreCase("leaderboards")){
                if (commandSender instanceof Player)
                    main.chatMessage(main.messages.getConfig().getString("messages.leaderboard-path"), (Player) commandSender);
                else
                    main.consoleMessage(main.messages.getConfig().getString("messages.leaderboard-path"));
                return true;
            }
        }
        else if (args.length == 2){
            if (args[0].equalsIgnoreCase("stats")) {
                if (hasPermission(commandSender,"randomwelcomerewards.stats.others)")) {
                    UUID uuid = main.playerDataManager.findPlayer(args[1]);
                    if (uuid == null){
                        main.chatMessage(main.messages.getConfig().getString("messages.no-player"), (Player) commandSender);
                        return false;
                    }
                    int newWelcome = 0;
                    int returnWelcome = 0;
                    if (main.playerData.getConfig().contains("players." + uuid + ".NewWelcomes") && main.playerData.getConfig().contains("players." + uuid + ".ReturnWelcomes")) {
                        newWelcome = main.playerData.getConfig().getInt("players." + uuid + ".NewWelcomes");
                        returnWelcome = main.playerData.getConfig().getInt("players." + uuid + ".ReturnWelcomes");
                    }
                    List<String> scoreSheet = main.messages.getConfig().getStringList("messages.scoreSheet");
                    for (int i = 0; i < scoreSheet.size(); i++) {
                        if (scoreSheet.get(i) == "")
                            commandSender.sendMessage("");
                        else {
                            String message = scoreSheet.get(i);
                            message = message.replaceAll("<player>", Bukkit.getOfflinePlayer(uuid).getName());
                            message = message.replaceAll("<newwelcomes>", Integer.toString(newWelcome));
                            message = message.replaceAll("<returnwelcomes>", Integer.toString(returnWelcome));
                            if (commandSender instanceof ConsoleCommandSender)
                                main.consoleMessage(message);
                            else
                                main.chatMessage(message, (Player) commandSender);
                        }
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("leaderboards")) {
                if (args[1].equalsIgnoreCase("newWelcomes")) {
                    if (hasPermission(commandSender,"randomwelcomerewards.leaderboards.newwelcomes)")) {
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
                            playerDataLeaderboard.sendLeaderBoardStats((Player) commandSender, true, main.config.getConfig().getInt("settings.leaderboardLength"), false);
                        else
                            playerDataLeaderboard.sendLeaderBoardStats((Player) commandSender, true, main.config.getConfig().getInt("settings.leaderboardLength"), true);
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
                        return true;
                    }
                    else {
                        main.chatMessage(main.messages.getConfig().getString("messages.no-perms"), (Player) commandSender);
                        return false;
                    }
                }
                else if (args[1].equalsIgnoreCase("returnWelcomes")){
                    if (hasPermission(commandSender,"randomwelcomerewards.leaderboards.returnwelcomes)")){
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
                            playerDataLeaderboard.sendLeaderBoardStats((Player) commandSender, false, main.config.getConfig().getInt("settings.leaderboardLength"), false);
                        else
                            playerDataLeaderboard.sendLeaderBoardStats((Player) commandSender, false, main.config.getConfig().getInt("settings.leaderboardLength"), true);
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
                        return true;
                    }
                    else {
                        main.chatMessage(main.messages.getConfig().getString("messages.no-perms"), (Player) commandSender);
                        return false;
                    }
                }
                if (commandSender instanceof Player)
                    main.chatMessage(main.messages.getConfig().getString("messages.leaderboard-path"), (Player) commandSender);
                else
                    main.consoleMessage(main.messages.getConfig().getString("messages.leaderboard-path"));
                return true;
            }
        }
        else if (args.length >= 3){
        }
        main.chatMessage(main.messages.getConfig().getString("messages.dne"), (Player) commandSender);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 1){
            List<String> firstArguments = new ArrayList<>();
            if (hasPermission(commandSender, "randomwelcomerewards.leaderboards.reload"))
                firstArguments.add("reload");
            if (hasPermission(commandSender, "randomwelcomerewards.leaderboards.returnwelcomes)") || hasPermission(commandSender, "randomwelcomerewards.leaderboards.newwelcomes)"))
                firstArguments.add("leaderboards");
            firstArguments.add("stats");
            return firstArguments;
        }
        else if (args.length == 2){
            if (args[0].equalsIgnoreCase("stats")){
                if (hasPermission(commandSender, "randomwelcomerewards.stats.others")) {
                    List<String> playerNames = new ArrayList<>();
                    Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    Bukkit.getServer().getOnlinePlayers().toArray(players);
                    for (int i = 0; i < players.length; i++)
                        playerNames.add(players[i].getName());
                }
            }
            else if (args[0].equalsIgnoreCase("leaderboards")){
                List<String> secondArguments = new ArrayList<>();
                if (hasPermission(commandSender, "randomwelcomerewards.leaderboards.newwelcomes)"))
                    secondArguments.add("newWelcomes");
                if (hasPermission(commandSender, "randomwelcomerewards.leaderboards.returnwelcomes)"))
                    secondArguments.add("returnWelcomes");
                return secondArguments;
            }
            else
                return null;
        }
        return null;
    }

    private boolean hasPermission(CommandSender commandSender, String permission){
        if ((commandSender.hasPermission(permission) || commandSender.isOp() || commandSender instanceof ConsoleCommandSender))
            return true;
        return false;
    }
}
