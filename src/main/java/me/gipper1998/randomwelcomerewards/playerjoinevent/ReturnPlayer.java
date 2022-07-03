package me.gipper1998.randomwelcomerewards.playerjoinevent;

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
        return player;
    }
    public Long getJoinTime(){
        return joinTime;
    }
    public void addReturnPlayer(Player player){
        returnPlayers.add(player);
    }
    public boolean hasPlayer(Player player){
        return returnPlayers.contains(player);
    }
}