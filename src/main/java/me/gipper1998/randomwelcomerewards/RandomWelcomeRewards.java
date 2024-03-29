package me.gipper1998.randomwelcomerewards;

import me.gipper1998.randomwelcomerewards.commands.Commands;
import me.gipper1998.randomwelcomerewards.softdependmanager.PlaceholderManager;
import me.gipper1998.randomwelcomerewards.softdependmanager.VaultManager;
import me.gipper1998.randomwelcomerewards.playerdata.PlayerDataManager;
import me.gipper1998.randomwelcomerewards.filemanager.FileSetup;
import me.gipper1998.randomwelcomerewards.milestone.MilestoneManager;
import me.gipper1998.randomwelcomerewards.playerjoinevent.listener.OnNewJoin;
import me.gipper1998.randomwelcomerewards.playerjoinevent.listener.OnReturnJoin;
import me.gipper1998.randomwelcomerewards.playerjoinevent.WelcomePlayer;
import me.gipper1998.randomwelcomerewards.playerjoinevent.WelcomeReturnPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomWelcomeRewards extends JavaPlugin {
    public boolean vaultEnabled = false;
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
        registerCommands();
        registerSoftDependManagers();
        registerPlayerEvents();
        consoleMessage(messages.getConfig().getString("messages.startUp"));
    }

    @Override
    public void onDisable() {
        consoleMessage(messages.getConfig().getString("messages.shutDown"));
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public void registerCommands() {
        getCommand("randomwelcomerewards").setExecutor(new Commands(this));
    }

    private void fileSetups(){
        this.playerData = new FileSetup(this, "playerData.yml");
        this.messages = new FileSetup(this, "messages.yml");
        this.milestones = new FileSetup(this, "milestones.yml");
        this.config = new FileSetup(this, "config.yml");
        this.playerDataManager = new PlayerDataManager(this);
        this.milestoneManager = new MilestoneManager(this);
    }

    private void registerPlayerEvents(){
        WelcomePlayer wp = new WelcomePlayer(this);
        WelcomeReturnPlayer wrp = new WelcomeReturnPlayer(this);
        getServer().getPluginManager().registerEvents(new OnNewJoin(this, wp), this);
        getServer().getPluginManager().registerEvents(new OnReturnJoin(this, wrp), this);
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
        else {
            Bukkit.getConsoleSender().sendMessage("");
        }
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
        else {
            player.sendMessage("");
        }
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
        else {
            player.sendMessage("");
        }
    }
    public String returnChatEventFormat(String string){
        if (string.contains("#")){
            hexConverter(string);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
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


