package me.gipper1998.randomwelcomerewards.playerjoinevent;

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
        return player;
    }
    public Long getJoinTime(){
        return joinTime;
    }
    public void addWelcomePlayer(Player player){
        welcomePlayers.add(player);
    }
    public boolean hasPlayer(Player player){
        return welcomePlayers.contains(player);
    }
}