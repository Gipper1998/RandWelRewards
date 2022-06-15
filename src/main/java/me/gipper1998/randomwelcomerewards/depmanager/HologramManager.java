package me.gipper1998.randomwelcomerewards.depmanager;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import me.gipper1998.randomwelcomerewards.playerdata.PlayerDataLeaderboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HologramManager {

    private RandomWelcomeRewards main;

    private PlayerDataLeaderboard pdl;

    private List<String> hologramList = new ArrayList<>();

    private String path = "Holograms.";

    private BukkitTask task;

    private Hologram hologram;

    public HologramManager(RandomWelcomeRewards main){
        this.main = main;
        this.pdl = new PlayerDataLeaderboard(main);
    }

    public void updateHolograms(RandomWelcomeRewards main){
        createFirstLoadHolograms();
        int minutes = main.config.getConfig().getInt("settings.hologramMinuteInterval");
        if (this.task != null)
            this.task.cancel();
        this.task = (new BukkitRunnable(){
            public void run(){
                loadHolograms();
            }
        }).runTaskTimerAsynchronously(main, 0L, 20L * (long)60 * (long)minutes);
    }

    public void createFirstLoadHolograms(){
        hologramList.clear();
        ConfigurationSection hologramDataFile = main.hologramData.getConfig().getConfigurationSection("Holograms");
        if (hologramDataFile == null){
            main.consoleMessage("<prefix>&c Nothing in hologram folder");
            return;
        }
        Set<String> keys = hologramDataFile.getKeys(false);
        for (String key : keys){
            boolean isNewWelcome = false;
            if (main.hologramData.getConfig().getString(path + key + ".type").equals("newWelcome")){
                isNewWelcome = true;
            }
            World world = Bukkit.getWorld(main.hologramData.getConfig().getString(path + key + ".world"));
            double x = main.hologramData.getConfig().getDouble(path + key + ".x");
            double y = main.hologramData.getConfig().getDouble(path + key + ".y");
            double z = main.hologramData.getConfig().getDouble(path + key + ".z");
            float pitch = (float) main.hologramData.getConfig().getDouble(path + key + ".pitch");
            float yaw = (float) main.hologramData.getConfig().getDouble(path + key + ".yaw");
            Location location = new Location(world, x, y, z, pitch, yaw);
            List<String> leaderBoard;
            List<String> top;
            if (isNewWelcome == true){
                leaderBoard = pdl.sendLeaderBoardHologram(true, main.config.getConfig().getInt("settings.hologramLength"));
                top = main.messages.getConfig().getStringList("messages.holograms.newWelcomeBoard");
                for (int i = 0; i < top.size(); i++){
                    leaderBoard.add(i, top.get(i));
                }
            }
            else {
                leaderBoard = pdl.sendLeaderBoardHologram(false, main.config.getConfig().getInt("settings.hologramLength"));
                top = main.messages.getConfig().getStringList("messages.holograms.returnWelcomeBoard");
                for (int i = 0; i < top.size(); i++){
                    leaderBoard.add(i, top.get(i));
                }
            }
            hologram = DHAPI.createHologram(key, location, leaderBoard);
            hologramList.add(key);
        }
    }


    public void createHologram(String name, Boolean newWelcome, Location location){
       try {
           if (newWelcome) {
               main.hologramData.getConfig().set(path + name + ".type", "newWelcome");
           }
           else {
               main.hologramData.getConfig().set(path + name + ".type", "returnWelcome");
           }
           main.hologramData.getConfig().set(path + name + ".world", location.getWorld().getName());
           main.hologramData.getConfig().set(path + name + ".x", location.getX());
           main.hologramData.getConfig().set(path + name + ".y", location.getY() + 2);
           main.hologramData.getConfig().set(path + name + ".z", location.getZ());
           main.hologramData.getConfig().set(path + name + ".pitch", location.getPitch());
           main.hologramData.getConfig().set(path + name + ".yaw", location.getYaw());
       } catch (Exception e){}
        main.hologramData.saveConfig();
        double y = location.getY();
        location.setY(y + 2);
        hologram = DHAPI.createHologram(name, location);
        hologramList.add(name);
        loadHolograms();
    }

    public void loadHolograms(){
        if (hologramList.size() == 0) {
            return;
        }
        for (String name : hologramList){
            boolean isNewWelcome = false;
            if (main.hologramData.getConfig().getString(path + name + ".type").equals("newWelcome")){
                isNewWelcome = true;
            }
            World world = Bukkit.getWorld(main.hologramData.getConfig().getString(path + name + ".world"));
            double x = main.hologramData.getConfig().getDouble(path + name + ".x");
            double y = main.hologramData.getConfig().getDouble(path + name + ".y");
            double z = main.hologramData.getConfig().getDouble(path + name + ".z");
            float pitch = (float) main.hologramData.getConfig().getDouble(path + name + ".pitch");
            float yaw = (float) main.hologramData.getConfig().getDouble(path + name + ".yaw");
            Location location = new Location(world, x, y, z, pitch, yaw);
            List<String> leaderBoard;
            List<String> top;
            if (isNewWelcome == true){
                leaderBoard = pdl.sendLeaderBoardHologram(true, main.config.getConfig().getInt("settings.hologramLength"));
                top = main.messages.getConfig().getStringList("messages.holograms.newWelcomeBoard");
                for (int i = 0; i < top.size(); i++){
                    leaderBoard.add(i, top.get(i));
                }
            }
            else {
                leaderBoard = pdl.sendLeaderBoardHologram(false, main.config.getConfig().getInt("settings.hologramLength"));
                top = main.messages.getConfig().getStringList("messages.holograms.returnWelcomeBoard");
                for (int i = 0; i < top.size(); i++){
                    leaderBoard.add(i, top.get(i));
                }
            }
            DHAPI.removeHologram(name);
            hologram = DHAPI.createHologram(name, location, leaderBoard);
        }
    }

    public boolean moveHologram(String name, Location location){
        if (main.hologramData.getConfig().contains(path + name)){
            main.hologramData.getConfig().set(path + name + ".world", location.getWorld().getName());
            main.hologramData.getConfig().set(path + name + ".x", location.getX());
            main.hologramData.getConfig().set(path + name + ".y", location.getY() + 2);
            main.hologramData.getConfig().set(path + name + ".z", location.getZ());
            main.hologramData.getConfig().set(path + name + ".pitch", location.getPitch());
            main.hologramData.getConfig().set(path + name + ".yaw", location.getYaw());
            double y = location.getY();
            location.setY(y + 2);
            DHAPI.moveHologram(name, location);
            main.hologramData.saveConfig();
            return true;
        }
        return false;
    }

    public List<String> listHolograms(){
        return hologramList;
    }

    public boolean deleteHologram(String name){
        if (main.hologramData.getConfig().contains(path + name)){
            main.hologramData.getConfig().set(path + name, null);
            DHAPI.removeHologram(name);
            hologramList.remove(name);
            main.hologramData.saveConfig();
            return true;
        }
        return false;
    }
}
