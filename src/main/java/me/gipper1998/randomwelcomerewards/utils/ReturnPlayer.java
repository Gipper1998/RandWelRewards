package me.gipper1998.randomwelcomerewards.utils;

import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class ReturnPlayer {
    private Long joinTime;
    private Player player;
    private List<Player> returnPlayers = new ArrayList();

    public ReturnPlayer(Player player) {
        this.player = player;
        this.joinTime = System.currentTimeMillis();
    }

    public Player getPlayer(){
        return this.player;
    }
    public Long getJoinTime(){
        return this.joinTime;
    }
    public void addReturnPlayer(Player player){
        this.returnPlayers.add(player);
    }
    public boolean hasPlayer(Player player){
        return this.returnPlayers.contains(player);
    }
}