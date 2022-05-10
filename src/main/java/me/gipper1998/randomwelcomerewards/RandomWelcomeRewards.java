package me.gipper1998.randomwelcomerewards;

import me.gipper1998.randomwelcomerewards.commands.Commands;
import me.gipper1998.randomwelcomerewards.playerdata.PlayerDataManager;
import me.gipper1998.randomwelcomerewards.filemanager.FileSetup;
import me.gipper1998.randomwelcomerewards.milestone.MilestoneManager;
import me.gipper1998.randomwelcomerewards.playerjoinevent.OnNewJoin;
import me.gipper1998.randomwelcomerewards.playerjoinevent.OnReturnJoin;
import me.gipper1998.randomwelcomerewards.playerjoinevent.WelcomePlayer;
import me.gipper1998.randomwelcomerewards.playerjoinevent.WelcomeReturnPlayer;
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
    private boolean holoEnabled = false;
    public Economy economy;
    public Chat chat = null;
    public FileSetup playerData;
    public FileSetup messages;
    public FileSetup milestones;
    public FileSetup config;
    public PlayerDataManager dataManager;
    public MilestoneManager milestoneManager;

    @Override
    public void onEnable() {
        this.players = new HashMap();
        fileSetups();
        this.getCommand("randomwelcomerewards").setExecutor(new Commands(this));
        if (!registerVault())
            consoleMessage("<prefix> &cVault was not found, make sure rewards that uses vault is removed or disabled.");
        else
            consoleMessage("<prefix> &aVault found and hooked :D.");
        consoleMessage(messages.getConfig().getString("messages.startup"));
    }

    @Override
    public void onDisable() {
        consoleMessage(messages.getConfig().getString("messages.shutdown"));
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public void fileSetups(){
        this.playerData = new FileSetup(this, "playerData.yml");
        this.messages = new FileSetup(this, "messages.yml");
        this.milestones = new FileSetup(this, "milestones.yml");
        this.config = new FileSetup(this, "config.yml");
        this.dataManager = new PlayerDataManager(this);
        this.milestoneManager = new MilestoneManager(this);
        config.saveDefaultConfig();
        playerData.saveDefaultConfig();
        messages.saveDefaultConfig();
        milestones.saveDefaultConfig();
    }

    public void registerPlayerEvents(){
        WelcomePlayer wp = new WelcomePlayer(this);
        WelcomeReturnPlayer wrp = new WelcomeReturnPlayer(this);
        this.getServer().getPluginManager().registerEvents(new OnNewJoin(this, wp), this);
        this.getServer().getPluginManager().registerEvents(new OnReturnJoin(this, wrp), this);
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


