package me.gipper1998.randomwelcomerewards.playerdata;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerData {

    private int newWelcomes;
    private int returnWelcomes;
    private String playerName;

    public PlayerData(UUID playerUUID, PlayerDataManager mainData){
        this.playerName = Bukkit.getOfflinePlayer(playerUUID).getName();
        this.newWelcomes = mainData.getNewWelcomes(playerUUID);
        this.returnWelcomes = mainData.getReturnWelcomes(playerUUID);
    }

    public int getNewWelcomes() {
        return newWelcomes;
    }

    public int getReturnWelcomes() {
        return returnWelcomes;
    }

    public String getPlayerName(){
        return playerName;
    }
}
