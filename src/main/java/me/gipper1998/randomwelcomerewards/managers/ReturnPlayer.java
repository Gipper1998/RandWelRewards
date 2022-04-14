package me.gipper1998.randomwelcomerewards.managers;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReturnPlayer {
    RandomWelcomeRewards main;
    public ReturnPlayer(Player player) {
        this.player = player;
        this.joinTime = System.currentTimeMillis();
    }

    public Boolean checkPlayTime(Player player) {
        long ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        long minutes = ticks / 20 / 60;
        long hours = minutes / 60;
        if (minutes > main.getConfig().getInt("settings.returnTimeNeed"))
            return true;
        return false;
    }
    private Long joinTime;
    private Player player;
    private List<Player> welcomePlayers = new ArrayList();
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