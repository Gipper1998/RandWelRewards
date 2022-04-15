package me.gipper1998.randomwelcomerewards.data;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataManager {

    private final RandomWelcomeRewards main;
    private FileConfiguration dataConfig = null;
    private File dataConfigFile = null;
    private final String name = "data.yml";

    public DataManager(RandomWelcomeRewards main) {
        this.main = main;
        saveDefaultDataConfig();
    }

    public void reloadDataConfig() {
        if (this.dataConfigFile == null)
            this.dataConfigFile = new File(this.main.getDataFolder(), name);
        this.dataConfig = YamlConfiguration
                .loadConfiguration(this.dataConfigFile);
        InputStream defConfigStream = this.main.getResource(name);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration
                    .loadConfiguration(new InputStreamReader(defConfigStream));
            this.dataConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getDataConfig() {
        if (this.dataConfig == null)
            reloadDataConfig();
        return this.dataConfig;
    }

    public void saveDataConfig() {
        if ((this.dataConfig == null) || (this.dataConfigFile == null))
            return;
        try {
            getDataConfig().save(this.dataConfigFile);
        } catch (IOException e) {
            main.consoleMessage("<prefix> &cCould not load " + name + " folder.");
        }
    }

    public void saveDefaultDataConfig() {
        if (this.dataConfigFile == null)
            this.dataConfigFile = new File(this.main.getDataFolder(), name);
        if (!this.dataConfigFile.exists())
            this.main.saveResource(name, false);
    }

}