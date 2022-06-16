package me.gipper1998.randomwelcomerewards;

import me.gipper1998.randomwelcomerewards.commands.Commands;
import me.gipper1998.randomwelcomerewards.depmanager.HologramManager;
import me.gipper1998.randomwelcomerewards.depmanager.PlaceholderManager;
import me.gipper1998.randomwelcomerewards.depmanager.VaultManager;
import me.gipper1998.randomwelcomerewards.playerdata.PlayerDataManager;
import me.gipper1998.randomwelcomerewards.filemanager.FileSetup;
import me.gipper1998.randomwelcomerewards.milestone.MilestoneManager;
import me.gipper1998.randomwelcomerewards.playerjoinevent.OnNewJoin;
import me.gipper1998.randomwelcomerewards.playerjoinevent.OnReturnJoin;
import me.gipper1998.randomwelcomerewards.playerjoinevent.WelcomePlayer;
import me.gipper1998.randomwelcomerewards.playerjoinevent.WelcomeReturnPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class RandomWelcomeRewards extends JavaPlugin {
    public HashMap<String, List<String>> players;
    public boolean vaultEnabled = false;
    public boolean hologramEnabled = false;
    public boolean placeholderEnabled = false;
    public FileSetup hologramData;
    public FileSetup playerData;
    public FileSetup messages;
    public FileSetup milestones;
    public FileSetup config;
    public PlayerDataManager playerDataManager;
    public MilestoneManager milestoneManager;
    public VaultManager vaultManager;
    public HologramManager hologramManager;

    @Override
    public void onEnable() {
        this.players = new HashMap();
        fileSetups();
        this.getCommand("randomwelcomerewards").setExecutor(new Commands(this));
        registerDependices();
        registerPlayerEvents();
        consoleMessage(messages.getConfig().getString("messages.startup"));
    }

    @Override
    public void onDisable() {
        consoleMessage(messages.getConfig().getString("messages.shutdown"));
        Bukkit.getPluginManager().disablePlugin(this);
    }

    private void fileSetups(){
        this.playerData = new FileSetup(this, "playerData.yml");
        this.messages = new FileSetup(this, "messages.yml");
        this.milestones = new FileSetup(this, "milestones.yml");
        this.config = new FileSetup(this, "config.yml");
        this.hologramData = new FileSetup(this, "hologramData.yml");
        this.playerDataManager = new PlayerDataManager(this);
        this.milestoneManager = new MilestoneManager(this);
        config.saveDefaultConfig();
        playerData.saveDefaultConfig();
        messages.saveDefaultConfig();
        milestones.saveDefaultConfig();
        hologramData.saveDefaultConfig();
    }

    private void registerPlayerEvents(){
        WelcomePlayer wp = new WelcomePlayer(this);
        WelcomeReturnPlayer wrp = new WelcomeReturnPlayer(this);
        this.getServer().getPluginManager().registerEvents(new OnNewJoin(this, wp), this);
        this.getServer().getPluginManager().registerEvents(new OnReturnJoin(this, wrp), this);
    }

    private void registerDependices(){
        if(getServer().getPluginManager().getPlugin("Vault") != null){
            this.vaultManager = new VaultManager(this);
            consoleMessage("<prefix> &aVault found and hooked!!");
            vaultEnabled = true;
        }
        if(getServer().getPluginManager().getPlugin("DecentHolograms") != null){
            this.hologramManager = new HologramManager(this);
            consoleMessage("<prefix> &aDecentHolograms found and hooked!!");
            hologramEnabled = true;
            hologramManager.updateHolograms(this);
        }
        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderManager(this).register();
            consoleMessage("<prefix> &aPlaceholderAPI found and hooked!!");
            placeholderEnabled = true;
        }
    }

    public void consoleMessage (String message){
        if (message != "") {
            String prefix = messages.getConfig().getString("messages.prefix");
            message = message.replaceAll("<prefix>", prefix);
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        else
            Bukkit.getConsoleSender().sendMessage("");
    }

    public void chatMessage(String message, Player player){
        if (message != "") {
            String prefix = messages.getConfig().getString("messages.prefix");
            message = message.replaceAll("<prefix>", prefix);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        else
            player.sendMessage("");
    }

    public void vaultChat(String message, Player player, int money){
        if (message != "") {
            String prefix = messages.getConfig().getString("messages.prefix");
            String cash = Integer.toString(money);
            message = message.replaceAll("<money>", cash);
            message = message.replaceAll("<prefix>", prefix);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

}


