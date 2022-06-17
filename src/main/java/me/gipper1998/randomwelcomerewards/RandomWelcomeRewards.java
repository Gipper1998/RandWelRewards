package me.gipper1998.randomwelcomerewards;

import me.gipper1998.randomwelcomerewards.commands.Commands;
import me.gipper1998.randomwelcomerewards.softdependmanagers.PlaceholderManager;
import me.gipper1998.randomwelcomerewards.softdependmanagers.VaultManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomWelcomeRewards extends JavaPlugin {
    public boolean vaultEnabled = false;
    public boolean placeholderEnabled = false;
    public FileSetup playerData;
    public FileSetup messages;
    public FileSetup milestones;
    public FileSetup config;
    public PlayerDataManager playerDataManager;
    public MilestoneManager milestoneManager;
    public VaultManager vaultManager;

    @Override
    public void onEnable() {
        fileSetups();
        this.getCommand("randomwelcomerewards").setExecutor(new Commands(this));
        registerSoftDependManagers();
        registerPlayerEvents();
        consoleMessage(messages.getConfig().getString("messages.startUp"));
    }

    @Override
    public void onDisable() {
        consoleMessage(messages.getConfig().getString("messages.shutDown"));
        Bukkit.getPluginManager().disablePlugin(this);
    }

    private void fileSetups(){
        this.playerData = new FileSetup(this, "playerData.yml");
        this.messages = new FileSetup(this, "messages.yml");
        this.milestones = new FileSetup(this, "milestones.yml");
        this.config = new FileSetup(this, "config.yml");
        this.playerDataManager = new PlayerDataManager(this);
        this.milestoneManager = new MilestoneManager(this);
        config.saveDefaultConfig();
        playerData.saveDefaultConfig();
        messages.saveDefaultConfig();
        milestones.saveDefaultConfig();
    }

    private void registerPlayerEvents(){
        WelcomePlayer wp = new WelcomePlayer(this);
        WelcomeReturnPlayer wrp = new WelcomeReturnPlayer(this);
        this.getServer().getPluginManager().registerEvents(new OnNewJoin(this, wp), this);
        this.getServer().getPluginManager().registerEvents(new OnReturnJoin(this, wrp), this);
    }

    private void registerSoftDependManagers(){
        if(getServer().getPluginManager().getPlugin("Vault") != null){
            this.vaultManager = new VaultManager(this);
            consoleMessage(messages.getConfig().getString("messages.vaultHooked"));
            vaultEnabled = true;
        }
        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderManager(this).register();
            consoleMessage(messages.getConfig().getString("messages.papiHooked"));
            placeholderEnabled = true;
        }
    }

    public void consoleMessage (String message){
        if (message != "") {
            String prefix = messages.getConfig().getString("messages.prefix");
            message = message.replaceAll("<prefix>", prefix);
            if (message.contains("#")){
                message = hexConverter(message);
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        else
            Bukkit.getConsoleSender().sendMessage("");
    }

    public void chatMessage(String message, Player player){
        if (message != "") {
            String prefix = messages.getConfig().getString("messages.prefix");
            message = message.replaceAll("<prefix>", prefix);
            if (message.contains("#")){
                message = hexConverter(message);
            }
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
            if (message.contains("#")){
                message = hexConverter(message);
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public String hexConverter(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
            char[] ch = replaceSharp.toCharArray();
            StringBuilder stringBuilder = new StringBuilder("");
            for (char c : ch) {
                stringBuilder.append("&" + c);
            }
            message = message.replace(hexCode, stringBuilder.toString());
            matcher = pattern.matcher(message);
        }
        return message;
    }

}


