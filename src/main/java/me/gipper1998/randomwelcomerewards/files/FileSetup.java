package me.gipper1998.randomwelcomerewards.files;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileSetup {

    private final RandomWelcomeRewards main;
    private FileConfiguration dataConfig = null;
    private File dataConfigFile = null;
    private String name;

    public FileSetup(RandomWelcomeRewards main, String name) {
        this.main = main;
        this.name = name;
        saveDefaultConfig();
    }

    public void reloadConfig() {
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

    public FileConfiguration getConfig() {
        if (this.dataConfig == null)
            reloadConfig();
        return this.dataConfig;
    }

    public void saveConfig() {
        if ((this.dataConfig == null) || (this.dataConfigFile == null))
            return;
        try {
            getConfig().save(this.dataConfigFile);
        } catch (IOException e) {
            main.consoleMessage("<prefix> &cCould not load " + name + " folder.");
        }
    }

    public void saveDefaultConfig() {
        if (this.dataConfigFile == null)
            this.dataConfigFile = new File(this.main.getDataFolder(), name);
        if (!this.dataConfigFile.exists())
            this.main.saveResource(name, false);
    }

}