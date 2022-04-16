package me.gipper1998.randomwelcomerewards.filemanager;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MilestoneManager {
    private RandomWelcomeRewards main;
    private ArrayList<Integer> newWelcomeMilestones = new ArrayList<>();
    private ConfigurationSection newWelcomeMilestonesSection;
    private ArrayList<Integer> returnWelcomeMilestones = new ArrayList<>();
    private ConfigurationSection returnWelcomeMilestonesSection;

    public MilestoneManager(RandomWelcomeRewards main){
        this.main = main;
        newWelcomeMilestonesLoader();
        returnWelcomeMilestonesLoader();
    }

    private void newWelcomeMilestonesLoader(){
        boolean enable = main.getConfig().getBoolean("settings.enableNewWelcomeMilestones");
        if (enable) {
            newWelcomeMilestonesSection = main.milestones.getConfig().getConfigurationSection("milestoneForNewWelcomes");
            if (newWelcomeMilestonesSection == null)
                main.consoleMessage("<preifx> &cNo milestones for the newWelcome section even though its enabled for some reason?");
            Set<String> keys = newWelcomeMilestonesSection.getKeys(false);
            for (String key : keys) {
                try {
                    int score = Integer.parseInt(key);
                    newWelcomeMilestones.add(score);
                } catch (NumberFormatException e) {
                    main.consoleMessage("<prefix> &cCan't resolve " + key + " as a valid score in the newWelcome Section.");
                }
            }
        }
    }

    private void returnWelcomeMilestonesLoader(){
        boolean enable = main.getConfig().getBoolean("settings.enableReturnWelcomeMilestones");
        if (enable) {
            returnWelcomeMilestonesSection = main.milestones.getConfig().getConfigurationSection("milestoneForReturnWelcomes");
            if (returnWelcomeMilestonesSection == null)
                main.consoleMessage("<preifx> &cNo milestones for the returnWelcome section even though its enabled for some reason?");
            Set<String> keys = returnWelcomeMilestonesSection.getKeys(false);
            for (String key : keys) {
                try {
                    int score = Integer.parseInt(key);
                    returnWelcomeMilestones.add(score);
                } catch (NumberFormatException e) {
                    main.consoleMessage("<prefix> &cCan't resolve " + key + " as a valid score in the newWelcome Section.");
                }
            }
        }
    }

    private boolean isNewWelcomeMilestone(int score){
        return newWelcomeMilestones.contains(score);
    }

    private boolean isReturnWelcomeMilestone(int score){
        return returnWelcomeMilestones.contains(score);
    }

    public void checkNewWelcomeMilestone(Player player){
        int score = main.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".NewWelcomes");
        boolean reward = isNewWelcomeMilestone(score);
        if (reward){
            if (main.milestones.getConfig().contains("milestoneForNewWelcomes." + score + ".message")){
                String message = main.milestones.getConfig().getString("milestoneForNewWelcomes." + score + ".message");
                main.chatMessage(message, player);
            }
            if (main.milestones.getConfig().contains("milestoneForNewWelcomes." + score + ".money")){
                int money = main.milestones.getConfig().getInt("milestoneForNewWelcomes." + score + ".money");
                main.vaultChat(main.messages.getConfig().getString("messages.vaultMoney"), player, money);
            }
            if (main.milestones.getConfig().contains("milestoneForNewWelcomes." + score + ".commands")) {
                List<String> rewardCommands = this.main.milestones.getConfig().getStringList("milestoneForNewWelcomes." + score + ".commands");
                ConsoleCommandSender console = this.main.getServer().getConsoleSender();
                Iterator var9 = rewardCommands.iterator();
                if (rewardCommands.size() != 0) {
                    while (var9.hasNext()) {
                        String command = (String) var9.next();
                        command = command.replace("<player>", player.getName());
                        ServerCommandEvent commandEvent = new ServerCommandEvent(console, command);
                        this.main.getServer().getPluginManager().callEvent(commandEvent);
                        this.main.getServer().getScheduler().callSyncMethod(this.main, () -> {
                            return this.main.getServer().dispatchCommand(commandEvent.getSender(), commandEvent.getCommand());
                        });
                    }
                }
            }
        }
    }
    public void checkReturnWelcomeMilestone(Player player){
        int score = main.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".ReturnWelcomes");
        boolean reward = isNewWelcomeMilestone(score);
        if (reward){
            if (main.milestones.getConfig().contains("milestoneForReturnWelcomes." + score + ".message")){
                String message = main.milestones.getConfig().getString("milestoneForReturnWelcomes." + score + ".message");
                main.chatMessage(message, player);
            }
            if (main.milestones.getConfig().contains("milestoneForReturnWelcomes." + score + ".money")){
                int money = main.milestones.getConfig().getInt("milestoneForReturnWelcomes." + score + ".money");
                main.vaultChat(main.messages.getConfig().getString("messages.vaultMoney"), player, money);
            }
            if (main.milestones.getConfig().contains("milestoneForReturnWelcomes." + score + ".commands")){
                List<String> rewardCommands = this.main.milestones.getConfig().getStringList("milestoneForReturnWelcomes." + score + ".commands");
                ConsoleCommandSender console = this.main.getServer().getConsoleSender();
                Iterator var9 = rewardCommands.iterator();
                while (var9.hasNext()) {
                    String command = (String) var9.next();
                    command = command.replace("<player>", player.getName());
                    ServerCommandEvent commandEvent = new ServerCommandEvent(console, command);
                    this.main.getServer().getPluginManager().callEvent(commandEvent);
                    this.main.getServer().getScheduler().callSyncMethod(this.main, () -> {
                        return this.main.getServer().dispatchCommand(commandEvent.getSender(), commandEvent.getCommand());
                    });
                }
            }
        }
    }
}

