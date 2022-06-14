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

    public HologramManager(RandomWelcomeRewards main){
        this.main = main;
        this.pdl = new PlayerDataLeaderboard(main);
    }

    public void updateHolograms(RandomWelcomeRewards main){
        int minutes = main.config.getConfig().getInt("settings.hologramMinuteInterval");
        if (this.task != null)
            this.task.cancel();
        this.task = (new BukkitRunnable(){
            public void run(){
                loadHolograms();
            }
        }).runTaskTimerAsynchronously(main, 0L, 20L * (long)60 * (long)minutes);
    }

    public void createHologram(String name, Boolean newWelcome, Location location){
       try {
           if (newWelcome) {
               main.getConfig().set(path + name + ".type", "newWelcome");
           }
           else {
               main.getConfig().set(name + ".type", "returnWelcome");
           }
           main.hologramData.getConfig().set(path + name + ".world", location.getWorld().getName());
           main.hologramData.getConfig().set(path + name + ".x", location.getX());
           main.hologramData.getConfig().set(path + name + ".y", location.getY());
           main.hologramData.getConfig().set(path + name + ".z", location.getZ());
           main.hologramData.getConfig().set(path + name + ".pitch", location.getPitch());
           main.hologramData.getConfig().set(path + name + ".yaw", location.getYaw());
       } catch (Exception e){}
       loadHolograms();
       main.hologramData.saveConfig();
    }

    public void loadHolograms(){
        boolean isNewWelcome = false;
        hologramList.clear();
        List<String> leaderBoard = pdl.sendLeaderBoardHologram(isNewWelcome, main.config.getConfig().getInt("settings.hologramLength"));
        ConfigurationSection hologramDataFile = main.hologramData.getConfig().getConfigurationSection("Holograms");
        if (hologramDataFile == null){
            main.consoleMessage("<prefix>&c Nothing in hologram folder");
            return;
        }
        Set<String> keys = hologramDataFile.getKeys(false);
        for (String key : keys){
            try {
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
                List<String> top = new ArrayList<>();
                if (isNewWelcome){
                    top = main.hologramData.getConfig().getStringList("messages.holograms.newWelcomeBoard");
                    for (int i = 0; i < top.size(); i++){
                        leaderBoard.add(i, top.get(i));
                    }
                }
                else {
                    top = main.hologramData.getConfig().getStringList("messages.holograms.returnWelcomeBoard");
                    for (int i = 0; i < top.size(); i++){
                        leaderBoard.add(i, top.get(i));
                    }
                }
                Hologram hologram = DHAPI.createHologram(key, location, leaderBoard);
                hologramList.add(key);
            } catch (Exception e){}
        }
    }

    public boolean moveHologram(String name, Location location){
        if (main.hologramData.getConfig().contains(path + name)){
            DHAPI.moveHologram(name, location);
            main.hologramData.getConfig().set(path + name + ".world", location.getWorld().getName());
            main.hologramData.getConfig().set(path + name + ".x", location.getX());
            main.hologramData.getConfig().set(path + name + ".y", location.getY());
            main.hologramData.getConfig().set(path + name + ".z", location.getZ());
            main.hologramData.getConfig().set(path + name + ".pitch", location.getPitch());
            main.hologramData.getConfig().set(path + name + ".yaw", location.getYaw());
            main.hologramData.saveConfig();
            loadHolograms();
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
            main.hologramData.saveConfig();
            loadHolograms();
            return true;
        }
        return false;
    }
}
