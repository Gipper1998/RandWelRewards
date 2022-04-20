package me.gipper1998.randomwelcomerewards.data;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {
    RandomWelcomeRewards main;
    DataManager mainData;
    private int newWelcomes;
    private int returnWelcomes;
    private UUID playerUUID;
    private String playerName;

    public PlayerData(RandomWelcomeRewards main, UUID playerUUID, DataManager mainData){
        this.playerUUID = playerUUID;
        this.main = main;
        this.mainData = mainData;
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

    public UUID getPlayerUUID(){
        return playerUUID;
    }
}
