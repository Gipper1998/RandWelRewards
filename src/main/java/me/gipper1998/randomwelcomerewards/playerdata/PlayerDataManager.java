package me.gipper1998.randomwelcomerewards.playerdata;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayerDataManager {
    private RandomWelcomeRewards main;

    public PlayerDataManager(RandomWelcomeRewards main){
        this.main = main;
    }

    public void addWelcomePoint(Player player, boolean newPlayer) {
        int newWelcome = 0;
        int returnWelcome = 0;
        if (main.playerData.getConfig().contains("players." + player.getUniqueId().toString() + ".NewWelcomes") && main.playerData.getConfig().contains("players." + player.getUniqueId().toString() + ".ReturnWelcomes")) {
            newWelcome = main.playerData.getConfig().getInt("players." + player.getUniqueId().toString() + ".NewWelcomes");
            returnWelcome = main.playerData.getConfig().getInt("players." + player.getUniqueId().toString() + ".ReturnWelcomes");
        }
        if (!newPlayer) {
            main.playerData.getConfig().set("players." + player.getUniqueId().toString() + ".NewWelcomes", (newWelcome));
            main.playerData.getConfig().set("players." + player.getUniqueId().toString() + ".ReturnWelcomes", (returnWelcome + 1));
        } else {
            main.playerData.getConfig().set("players." + player.getUniqueId().toString() + ".NewWelcomes", (newWelcome + 1));
            main.playerData.getConfig().set("players." + player.getUniqueId().toString() + ".ReturnWelcomes", (returnWelcome));
        }
        main.playerData.saveConfig();
    }

    public boolean setWelcomePoint(UUID uuid, boolean newPlayer, int point){
        int newWelcome = 0;
        int returnWelcome = 0;
        if (!main.playerData.getConfig().contains("players." + uuid.toString()))
            return false;
        if (main.playerData.getConfig().contains("players." + uuid.toString() + ".NewWelcomes") && main.playerData.getConfig().contains("players." + uuid.toString() + ".ReturnWelcomes")) {
            newWelcome = main.playerData.getConfig().getInt("players." + uuid.toString() + ".NewWelcomes");
            returnWelcome = main.playerData.getConfig().getInt("players." + uuid.toString() + ".ReturnWelcomes");
        }
        if (!newPlayer) {
            main.playerData.getConfig().set("players." + uuid.toString() + ".NewWelcomes", (newWelcome));
            main.playerData.getConfig().set("players." + uuid.toString() + ".ReturnWelcomes", point);
        } else {
            main.playerData.getConfig().set("players." + uuid.toString() + ".NewWelcomes", point);
            main.playerData.getConfig().set("players." + uuid.toString() + ".ReturnWelcomes", (returnWelcome));
        }
        main.playerData.saveConfig();
        return true;
    }

    public int getNewWelcomes(UUID uuid){
        int newWelcome = main.playerData.getConfig().getInt("players." + uuid.toString() + ".NewWelcomes");
        return newWelcome;
    }

    public int getReturnWelcomes(UUID uuid){
        int returnWelcome = main.playerData.getConfig().getInt("players." + uuid.toString() + ".ReturnWelcomes");
        return returnWelcome;
    }

    public UUID findPlayer(String playerName){
        ConfigurationSection playerDataBoard = main.playerData.getConfig().getConfigurationSection("players");
        List<UUID> uuids = new ArrayList<>();
        if (playerDataBoard == null) {
            main.consoleMessage("<preifx> &cNo milestones for the newWelcome section even though its enabled for some reason?");
                return null;
        }
        Set<String> keys = playerDataBoard.getKeys(false);
        for (String key : keys) {
            try {
                UUID uuid = UUID.fromString(key);
                String tempName = Bukkit.getOfflinePlayer(uuid).getName();
                if (tempName.equalsIgnoreCase(playerName))
                    return uuid;
            }
            catch (NumberFormatException e){
                return null;
            }
        }
        return null;
    }
}

