package me.gipper1998.randomwelcomerewards.listeners;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import me.gipper1998.randomwelcomerewards.managers.NewPlayer;
import me.gipper1998.randomwelcomerewards.managers.WelcomePlayer;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class OnNewJoin implements Listener {
    private RandomWelcomeRewards main;
    private WelcomePlayer wp;
    private Random rand;
    private List<String> messageList;

    public OnNewJoin(RandomWelcomeRewards main, WelcomePlayer wp){
        this.main = main;
        this.wp = wp;
        rand = new Random();
    }

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent event){
        if (main.getConfig().getBoolean("settings.enableNewWelcome")) {
            if (!event.isCancelled()) {
                if (wp.messageContains(event.getMessage())) {
                    Player player = event.getPlayer();
                    String message = event.getMessage();
                    Iterator var3 = wp.getNewPlayers().entrySet().iterator();
                    while (true) {
                        while (var3.hasNext()) {
                            Entry<Player, NewPlayer> entry = (Entry) var3.next();
                            NewPlayer newPlayer = (NewPlayer) entry.getValue();
                            Integer timeoutTime = this.main.getConfig().getInt("settings.newTime") * 1000;
                            if (System.currentTimeMillis() - newPlayer.getJoinTime() > (long) timeoutTime) {
                                wp.removeNew(newPlayer.getPlayer());
                            } else if (!newPlayer.getPlayer().equals(player) && !newPlayer.hasPlayer(player)) {
                                newPlayer.addWelcomePlayer(player);
                                this.messageList = main.messages.getConfig().getStringList("messages.newWelcomeMessages");
                                int messageSelect = this.rand.nextInt(messageList.size());
                                String Text = messageList.get(messageSelect).replaceAll("<newplayer>", newPlayer.getPlayer().getName());
                                Text = Text.replaceAll("<player>", player.getDisplayName());
                                if (!Text.equals("")) {
                                    message = "";
                                    message = Text;
                                    vaultRewards(player, event);
                                    commandRewards(player, event);
                                    main.addWelcomePoint(player, true);
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
        if (!player.getPlayer().hasPlayedBefore())
            wp.addNew(player.getPlayer());
    }

    public void vaultRewards(Player player, AsyncPlayerChatEvent event) {
        if (main.vaultEnabled) {
            if (main.getConfig().getBoolean("newWelcomeRewards.vault.enable") == true) {
                int money = main.getConfig().getInt("newWelcomeRewards.vault.reward");
                main.deposit(player, money);
                main.vaultChat(main.messages.getConfig().getString("messages.vaultMoney"), player, money);
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
            main.chatMessage(main.messages.getConfig().getString("messages.commandReward"), player);
        }
    }
}
