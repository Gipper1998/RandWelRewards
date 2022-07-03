package me.gipper1998.randomwelcomerewards.playerjoinevent;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.entity.Player;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WelcomeReturnPlayer {
    private RandomWelcomeRewards main;
    private ConcurrentMap<Player, ReturnPlayer> returnPlayers = new ConcurrentHashMap();

    public WelcomeReturnPlayer(RandomWelcomeRewards main) {
        this.main = main;
    }

    public ConcurrentMap<Player, ReturnPlayer> getReturnPlayers() {
        return returnPlayers;
    }

    public void addNew(Player player) {
        returnPlayers.put(player, new ReturnPlayer(player));
    }

    public void removeNew(Player player) {
        returnPlayers.remove(player);
    }

    public Boolean messageContains(String message) {
        List<String> welcomeText = main.config.getConfig().getStringList("settings.triggeredReturnWelcomeTexts");
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