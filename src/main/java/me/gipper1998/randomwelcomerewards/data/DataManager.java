package me.gipper1998.randomwelcomerewards.data;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DataManager {
    RandomWelcomeRewards main;

    public DataManager(RandomWelcomeRewards main){
        this.main = main;
    }

    public void addWelcomePoint(Player player, boolean newPlayer) {
        int newWelcome = 0;
        int returnWelcome = 0;
        if (main.data.getConfig().contains("players." + player.getUniqueId().toString() + ".NewWelcomes") && main.data.getConfig().contains("players." + player.getUniqueId().toString() + ".ReturnWelcomes")) {
            newWelcome = main.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".NewWelcomes");
            returnWelcome = main.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".ReturnWelcomes");
        }
        if (!newPlayer) {
            main.data.getConfig().set("players." + player.getUniqueId().toString() + ".NewWelcomes", (newWelcome));
            main.data.getConfig().set("players." + player.getUniqueId().toString() + ".ReturnWelcomes", (returnWelcome + 1));
            main.milestoneManager.checkReturnWelcomeMilestone(player);
        } else {
            main.data.getConfig().set("players." + player.getUniqueId().toString() + ".NewWelcomes", (newWelcome + 1));
            main.data.getConfig().set("players." + player.getUniqueId().toString() + ".ReturnWelcomes", (returnWelcome));
            main.milestoneManager.checkNewWelcomeMilestone(player);
        }
        main.data.saveConfig();
    }

    public int getNewWelcomes(UUID uuid){
        int newWelcome = main.data.getConfig().getInt("players." + uuid.toString() + ".NewWelcomes");
        return newWelcome;
    }

    public int getReturnWelcomes(UUID uuid){
        int returnWelcome = main.data.getConfig().getInt("players." + uuid.toString() + ".ReturnWelcomes");
        return returnWelcome;
    }
}

