package me.gipper1998.randomwelcomerewards.utils;

import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class NewPlayer {
    private Long joinTime;
    private Player player;
    private List<Player> welcomePlayers = new ArrayList();

    public NewPlayer(Player player) {
        this.player = player;
        this.joinTime = System.currentTimeMillis();
    }
    public Player getPlayer(){
        return this.player;
    }
    public Long getJoinTime(){
        return this.joinTime;
    }
    public void addWelcomePlayer(Player player){
        this.welcomePlayers.add(player);
    }
    public boolean hasPlayer(Player player){
        return this.welcomePlayers.contains(player);
    }
}