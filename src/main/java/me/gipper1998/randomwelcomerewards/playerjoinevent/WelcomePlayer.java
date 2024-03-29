package me.gipper1998.randomwelcomerewards.playerjoinevent;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.entity.Player;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WelcomePlayer {
    private RandomWelcomeRewards main;
    private ConcurrentMap<Player, NewPlayer> newPlayers = new ConcurrentHashMap();

    public WelcomePlayer(RandomWelcomeRewards main) {
        this.main = main;
    }

    public ConcurrentMap<Player, NewPlayer> getNewPlayers() {
        return newPlayers;
    }

    public void addNew(Player player) {
        newPlayers.put(player, new NewPlayer(player));
    }

    public void removeNew(Player player) {
        newPlayers.remove(player);
    }

    public Boolean messageContains(String message) {
        List<String> welcomeText = main.config.getConfig().getStringList("settings.triggeredNewWelcomeTexts");
        Iterator var3 = welcomeText.iterator();
        String string;
        do {
            if (!var3.hasNext())
                return false;
            string = (String)var3.next();
        } while(!message.toLowerCase().contains(string.toLowerCase()));
        return true;
    }
}