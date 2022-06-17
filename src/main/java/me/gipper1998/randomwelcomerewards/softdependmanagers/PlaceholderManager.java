package me.gipper1998.randomwelcomerewards.softdependmanagers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import me.gipper1998.randomwelcomerewards.playerdata.PlayerDataLeaderboard;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        else if (identifier.contains("top")){
            String[] temp = identifier.split("_");
            String welcomeType = temp[1];
            String dataType = temp[2];
            String position = temp[3];
            PlayerDataLeaderboard pdl = new PlayerDataLeaderboard(main);
            if (welcomeType.equals("newwelcome")){
                List<String> data = pdl.sendLeaderBoardPAPIRanks(true, Integer.parseInt(position));
                if (data != null){
                    if (dataType.equalsIgnoreCase("player")){
                        return data.get(0);
                    }
                    else if (dataType.equalsIgnoreCase("amount")){
                        return data.get(1);
                    }
                    else {
                        return null;
                    }
                }
                else {
                    return "";
                }
            }
            else if (welcomeType.equals("returnwelcome")){
                List<String> data = pdl.sendLeaderBoardPAPIRanks(false, Integer.parseInt(position));
                if (data != null){
                    if (dataType.equalsIgnoreCase("player")){
                        return data.get(0);
                    }
                    else if (dataType.equalsIgnoreCase("amount")){
                        return data.get(1);
                    }
                    else {
                        return null;
                    }
                }
                else {
                    return "";
                }
            }
            else {
                return null;
            }
        }
        return null;
    }
}
