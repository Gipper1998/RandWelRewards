package me.gipper1998.randomwelcomerewards;

import me.gipper1998.randomwelcomerewards.commands.Commands;
import me.gipper1998.randomwelcomerewards.filemanager.DataManager;
import me.gipper1998.randomwelcomerewards.filemanager.FileSetup;
import me.gipper1998.randomwelcomerewards.filemanager.MilestoneManager;
import me.gipper1998.randomwelcomerewards.listeners.OnNewJoin;
import me.gipper1998.randomwelcomerewards.listeners.OnReturnJoin;
import me.gipper1998.randomwelcomerewards.utils.WelcomePlayer;
import me.gipper1998.randomwelcomerewards.utils.WelcomeReturnPlayer;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class RandomWelcomeRewards extends JavaPlugin {
    public HashMap<String, List<String>> players;
    public boolean vaultEnabled = false;
    public Economy economy;
    public Chat chat = null;
    public FileSetup data;
    public FileSetup messages;
    public FileSetup milestones;
    public DataManager dataManager;
    public MilestoneManager milestoneManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (!registerVault())
            consoleMessage("<prefix> &cVault was not found, make sure vaultRewards is disabled in the config.");
        this.players = new HashMap();
        this.data = new FileSetup(this, "data.yml");
        this.messages = new FileSetup(this, "messages.yml");
        this.milestones = new FileSetup(this, "milestones.yml");
        this.dataManager = new DataManager(this);
        this.milestoneManager = new MilestoneManager(this);
        WelcomePlayer wp = new WelcomePlayer(this);
        WelcomeReturnPlayer wrp = new WelcomeReturnPlayer(this);
        this.getCommand("randomwelcomerewards").setExecutor(new Commands(this));
        this.getServer().getPluginManager().registerEvents(new OnNewJoin(this, wp), this);
        this.getServer().getPluginManager().registerEvents(new OnReturnJoin(this, wrp), this);
        consoleMessage(messages.getConfig().getString("messages.startup"));
    }

    @Override
    public void onDisable() {
        consoleMessage(messages.getConfig().getString("messages.shutdown"));
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public boolean registerVault() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        economy = rsp.getProvider();
        setupChat();
        vaultEnabled = true;
        return economy != null;
    }

    public boolean setupChat(){
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null)
            chat = chatProvider.getProvider();
        return (chat != null);
    }

    public void consoleMessage (String message){
        if (message != "") {
            String prefix = messages.getConfig().getString("messages.prefix");
            message = message.replaceAll("<prefix>", prefix);
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public void chatMessage(String message, Player player){
        if (message != "") {
            String prefix = messages.getConfig().getString("messages.prefix");
            message = message.replaceAll("<prefix>", prefix);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
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

    public void deposit(Player player, int money){ economy.depositPlayer(player, money); }

    public void addWelcomePoint(Player player, boolean newPlayer) {
        dataManager.addWelcomePoint(player, newPlayer);
    }
}


