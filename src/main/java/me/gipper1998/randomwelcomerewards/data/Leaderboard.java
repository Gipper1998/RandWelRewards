package me.gipper1998.randomwelcomerewards.data;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class Leaderboard {
    RandomWelcomeRewards main;
    private ArrayList<PlayerData> newWelcomeBoard= new ArrayList<>();
    private ConfigurationSection newWelcomeBoardData;
    private ArrayList<PlayerData> returnWelcomeBoard= new ArrayList<>();
    private ConfigurationSection returnWelcomeBoardData;

    public Leaderboard(RandomWelcomeRewards main){
        this.main = main;
    }

    public void sendLeaderBoardStats(Player player, boolean newWelcome, int length, boolean fromConsole){
        if (newWelcome) {
            if (setNewWelcomeBoardData()) {
                setNewWelcomeOrder();
                String message = main.messages.getConfig().getString("messages.leaderboards.positionTile");
                int size = newWelcomeBoard.size();
                if (size > length)
                    size = length;
                for (int i = 0; i < size; i++) {
                    String temp = message.replaceAll("<position>", Integer.toString(i + 1));
                    PlayerData tempPlayer = newWelcomeBoard.get(0);
                    temp = temp.replaceAll("<name>", tempPlayer.getPlayerName());
                    temp = temp.replaceAll("<score>", Integer.toString(tempPlayer.getNewWelcomes()));
                    if (fromConsole)
                        main.consoleMessage(temp);
                    else
                        main.chatMessage(temp, player);
                    newWelcomeBoard.remove(0);
                }
            }
        }
        else {
            if (setReturnWelcomeBoardData()) {
                setReturnWelcomeOrder();
                String message = main.messages.getConfig().getString("messages.leaderboards.positionTile");
                int size = returnWelcomeBoard.size();
                if (size > length)
                    size = length;
                for (int i = 0; i < size; i++) {
                    String temp = message.replaceAll("<position>", Integer.toString(i + 1));
                    PlayerData tempPlayer = returnWelcomeBoard.get(0);
                    temp = temp.replaceAll("<name>", tempPlayer.getPlayerName());
                    temp = temp.replaceAll("<score>", Integer.toString(tempPlayer.getReturnWelcomes()));
                    if (fromConsole)
                        main.consoleMessage(temp);
                    else
                        main.chatMessage(temp, player);
                    returnWelcomeBoard.remove(0);
                }
            }
        }
    }

    private boolean setNewWelcomeBoardData(){
        newWelcomeBoardData = main.data.getConfig().getConfigurationSection("players");
        if (newWelcomeBoardData == null) {
            main.consoleMessage("<prefix>&c Getting the data for newWelcomes didn't work correctly, check to see if anythings in data.yml");
            return false;
        }
        Set<String> keys = newWelcomeBoardData.getKeys(false);
        for (String key : keys) {
            try {
                UUID uuid = UUID.fromString(key);
                PlayerData temp = new PlayerData(main, uuid,main.dataManager);
                newWelcomeBoard.add(temp);
            } catch (NumberFormatException e) {
                main.consoleMessage("<prefix> &cCan't resolve UUID: " + key + " into the system, check data.yml incase theres an error.");
                return false;
            }
        }
        return true;
    }

    private void setNewWelcomeOrder(){
        Collections.sort(newWelcomeBoard, (o1, o2) -> o1.getNewWelcomes() > o2.getNewWelcomes() ? -1 : 1);
    }

    private boolean setReturnWelcomeBoardData(){
        returnWelcomeBoardData = main.data.getConfig().getConfigurationSection("players");
        if (returnWelcomeBoardData == null) {
            main.consoleMessage("<prefix>&c Getting the data for returnWelcomes didn't work correctly, check to see if anythings in data.yml");
            return false;
        }
        Set<String> keys = returnWelcomeBoardData.getKeys(false);
        for (String key : keys) {
            try {
                UUID uuid = UUID.fromString(key);
                PlayerData temp = new PlayerData(main, uuid,main.dataManager);
                returnWelcomeBoard.add(temp);
            } catch (NumberFormatException e) {
                main.consoleMessage("<prefix> &cCan't resolve UUID: " + key + " into the system, check data.yml incase theres an error.");
                return false;
            }
        }
        return true;
    }

    private void setReturnWelcomeOrder(){
        Collections.sort(returnWelcomeBoard, (o1, o2) -> o1.getReturnWelcomes() > o2.getReturnWelcomes() ? -1 : 1);
    }

}
