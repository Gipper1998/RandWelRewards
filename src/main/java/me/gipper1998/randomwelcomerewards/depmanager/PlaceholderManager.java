package me.gipper1998.randomwelcomerewards.depmanager;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderManager extends PlaceholderExpansion {

    private RandomWelcomeRewards main;

    public PlaceholderManager(RandomWelcomeRewards main){
        this.main = main;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "randwelrewards";
    }

    @Override
    public @NotNull String getAuthor() {
        return "gipper1998";
    }

    @Override
    public @NotNull String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer p, String identifier){
        if (p == null) {
            return "";
        }
        else if (identifier.equals("newweclome_score")){
            return Integer.toString(main.playerData.getConfig().getInt("players." + p.getUniqueId() + ".NewWelcomes"));
        }
        else if (identifier.equals("returnweclome_score")){
            return Integer.toString(main.playerData.getConfig().getInt("players." + p.getUniqueId() + ".ReturnWelcomes"));
        }
        return null;
    }
}
