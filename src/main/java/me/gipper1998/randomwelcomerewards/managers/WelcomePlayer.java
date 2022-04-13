package me.gipper1998.randomwelcomerewards.managers;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.entity.Player;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WelcomePlayer {
    private RandomWelcomeRewards main;
    private ConcurrentMap<Player, NewPlayer> newPlayers = new ConcurrentHashMap();
    private String word;

    public WelcomePlayer(RandomWelcomeRewards main) {
        this.main = main;
    }

    public ConcurrentMap<Player, NewPlayer> getNewPlayers() {
        return this.newPlayers;
    }

    public void addNew(Player player) {
        this.newPlayers.put(player, new NewPlayer(player));
    }

    public void removeNew(Player player) {
        this.newPlayers.remove(player);
    }

    public Boolean messageContains(String message) {
        List<String> welcomeText = this.main.getConfig().getStringList("settings.triggeredNewWelcomeTexts");
        Iterator var3 = welcomeText.iterator();
        String string;
        do {
            if (!var3.hasNext())
                return false;
            string = (String)var3.next();
        } while(!message.toLowerCase().contains(string.toLowerCase()));
        word = string;
        return true;
    }

    public String getMessageContains(){
        return word;
    }
}