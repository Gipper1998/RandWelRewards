package me.gipper1998.randomwelcomerewards.listeners;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import me.gipper1998.randomwelcomerewards.managers.NewPlayer;
import me.gipper1998.randomwelcomerewards.managers.ReturnPlayer;
import me.gipper1998.randomwelcomerewards.managers.WelcomeReturnPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.*;

public class OnReturnJoin implements Listener {
    RandomWelcomeRewards main;
    WelcomeReturnPlayer wrp;
    ReturnPlayer rp;
    private Random rand;
    private List<String> messages;

    public OnReturnJoin(RandomWelcomeRewards main, WelcomeReturnPlayer rp){
        this.main = main;
        this.wrp = wrp;
    }

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent event){
        if (main.getConfig().getBoolean("settings.enableReturnWelcome")) {
            if (!event.isCancelled()) {
                if (wrp.messageContains(event.getMessage())) {
                    Player player = event.getPlayer();
                    String message = event.getMessage();
                    Iterator var3 = wrp.getReturnPlayers().entrySet().iterator();
                    while (true) {
                        while (var3.hasNext()) {
                            Map.Entry<Player, NewPlayer> entry = (Map.Entry) var3.next();
                            NewPlayer newPlayer = (NewPlayer) entry.getValue();
                            Integer timeoutTime = this.main.getConfig().getInt("settings.returnTime") * 1000;
                            if (System.currentTimeMillis() - newPlayer.getJoinTime() > (long) timeoutTime) {
                                wrp.removeNew(newPlayer.getPlayer());
                            } else if (!newPlayer.getPlayer().equals(player) && !newPlayer.hasPlayer(player)) {
                                newPlayer.addWelcomePlayer(player);
                                this.messages = main.getConfig().getStringList("messages.welcomeMessages");
                                int messageSelect = this.rand.nextInt(messages.size());
                                String Text = messages.get(messageSelect).replaceAll("<newplayer>", newPlayer.getPlayer().getName());
                                Text = Text.replaceAll("<player>", player.getDisplayName());
                                if (!Text.equals("")) {
                                    message = "";
                                    message = Text;
                                    main.chatMessage(main.getConfig().getString("messages.welcomedPlayer"), player);
                                    vaultRewards(player, event);
                                    commandRewards(player, event);
                                }
                            }
                            event.setMessage(ChatColor.translateAlternateColorCodes('&', message));
                        }
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent player){
        if (player.getPlayer().hasPlayedBefore()) {
            if (rp.checkPlayTime(player.getPlayer()))
                wrp.addNew(player.getPlayer());
        }
    }

    public void vaultRewards(Player player, AsyncPlayerChatEvent event) {
        if (main.vaultEnabled) {
            if (main.getConfig().getBoolean("newWelcomeRewards.vault.enable") == true) {
                int money = main.getConfig().getInt("newWelcomeRewards.vault.reward");
                main.deposit(player, money);
                main.vaultChat(main.getConfig().getString("messages.vaultMoney"), player, money);
            }
        }
        else
            main.consoleMessage("<prefix> &cVault was not found, please disable vault rewards in the config.");
    }

    public void commandRewards(Player player, AsyncPlayerChatEvent event) {
        if (main.getConfig().getBoolean("newWelcomeRewards.commands.enable") == true) {
            List<String> rewardCommands = this.main.getConfig().getStringList("newWelcomeRewards.commands.rewardCommands");
            ConsoleCommandSender console = this.main.getServer().getConsoleSender();
            Iterator var9 = rewardCommands.iterator();
            while (var9.hasNext()) {
                String command = (String) var9.next();
                command = command.replace("<player>", player.getName());
                ServerCommandEvent commandEvent = new ServerCommandEvent(console, command);
                this.main.getServer().getPluginManager().callEvent(commandEvent);
                if (!event.isCancelled()) {
                    this.main.getServer().getScheduler().callSyncMethod(this.main, () -> {
                        return this.main.getServer().dispatchCommand(commandEvent.getSender(), commandEvent.getCommand());
                    });
                }
            }
            main.chatMessage(main.getConfig().getString("messages.commandReward"), player);
        }
    }
}
