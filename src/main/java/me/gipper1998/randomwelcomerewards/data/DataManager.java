package me.gipper1998.randomwelcomerewards.data;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataManager {
    private RandomWelcomeRewards main;
    private FileConfiguration dataConfig = null;
    private File dataFile = null;

    public DataManager(RandomWelcomeRewards main){
        this.main = main;
        saveDefaultDataConfig();
    }

    public void reloadDataConfig(){
        if (this.dataFile == null)
            this.dataFile = new File(this.main.getDataFolder(), "data.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);
        InputStream defaultStream = this.main.getResource("data.yml");
        if (defaultStream != null){
            YamlConfiguration dataConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(dataConfig);
        }
    }

    public FileConfiguration getDataConfig(){
        if (this.dataConfig == null)
            reloadDataConfig();
        return this.dataConfig;
    }

    public void saveDataConfig(){
        if (this.dataConfig == null || this.dataConfig == null)
            return;
        try {
            this.getDataConfig().save(this.dataFile);
        } catch (IOException e){
            main.consoleMessage("<prefix> &cCould not save config " + this.dataFile + ": " + e);
        }
    }

    public void saveDefaultDataConfig(){
        if (this.dataConfig == null)
            this.dataFile = new File(this.main.getDataFolder(), "data.yml");
        if (!this.dataFile.exists()){
            this.main.saveResource("data.yml", false);
        }
    }
}
