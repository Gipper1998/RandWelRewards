package me.gipper1998.randomwelcomerewards.playerjoinevent;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class OnNewJoin implements Listener {
    private RandomWelcomeRewards main;
    private WelcomePlayer wp;
    private Random rand;
    private List<String> messageList;

    public OnNewJoin(RandomWelcomeRewards main, WelcomePlayer wp){
        this.main = main;
        this.wp = wp;
        this.rand = new Random();
    }

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent event){
        if (main.config.getConfig().getBoolean("settings.enableNewWelcome")) {
            if (!event.isCancelled()) {
                if (wp.messageContains(event.getMessage())) {
                    Player player = event.getPlayer();
                    String message = event.getMessage();
                    Iterator var3 = wp.getNewPlayers().entrySet().iterator();
                    while (true) {
                        while (var3.hasNext()) {
                            Entry<Player, NewPlayer> entry = (Entry) var3.next();
                            NewPlayer newPlayer = (NewPlayer) entry.getValue();
                            Integer timeoutTime = main.config.getConfig().getInt("settings.newTime") * 1000;
                            if (System.currentTimeMillis() - newPlayer.getJoinTime() > (long) timeoutTime) {
                                wp.removeNew(newPlayer.getPlayer());
                            } else if (!newPlayer.getPlayer().equals(player) && !newPlayer.hasPlayer(player)) {
                                newPlayer.addWelcomePlayer(player);
                                messageList = main.messages.getConfig().getStringList("messages.newWelcomeMessages");
                                int messageSelect = rand.nextInt(messageList.size());
                                String Text = messageList.get(messageSelect).replaceAll("<newplayer>", newPlayer.getPlayer().getName());
                                Text = Text.replaceAll("<player>", player.getDisplayName());
                                if (!Text.equals("")) {
                                    message = "";
                                    message = Text;
                                    vaultRewards(player, event);
                                    commandRewards(player, event);
                                    main.playerDataManager.addWelcomePoint(player, true);
                                }
                            }
                            main.milestoneManager.checkNewWelcomeMilestone(player);
                            if (message.contains("#")){
                                message = main.hexConverter(message);
                            }
                            event.setMessage(main.returnChatEventFormat(message));
                        }
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent player){
        if (!player.getPlayer().hasPlayedBefore()) {
            wp.addNew(player.getPlayer());
        }
    }

    public void vaultRewards(Player player, AsyncPlayerChatEvent event) {
        if (main.vaultEnabled) {
            if (main.config.getConfig().getBoolean("newWelcomeRewards.vault.enable") == true) {
                int money = main.config.getConfig().getInt("newWelcomeRewards.vault.reward");
                main.vaultManager.deposit(player, money);
                main.vaultChat(main.messages.getConfig().getString("messages.vaultMoney"), player, money);
            }
        }
        else {
            main.consoleMessage(main.messages.getConfig().getString("messages.noVault"));
        }
    }

    public void commandRewards(Player player, AsyncPlayerChatEvent event) {
        if (main.config.getConfig().getBoolean("newWelcomeRewards.commands.enable") == true) {
            List<String> rewardCommands = main.config.getConfig().getStringList("newWelcomeRewards.commands.rewardCommands");
            ConsoleCommandSender console = main.getServer().getConsoleSender();
            Iterator var9 = rewardCommands.iterator();
            if (rewardCommands.size() != 0) {
                while (var9.hasNext()) {
                    String command = (String) var9.next();
                    command = command.replace("<player>", player.getName());
                    ServerCommandEvent commandEvent = new ServerCommandEvent(console, command);
                    main.getServer().getPluginManager().callEvent(commandEvent);
                    if (!event.isCancelled()) {
                        main.getServer().getScheduler().callSyncMethod(main, () -> {
                            return main.getServer().dispatchCommand(commandEvent.getSender(), commandEvent.getCommand());
                        });
                    }
                }
                main.chatMessage(main.messages.getConfig().getString("messages.commandReward"), player);
            }
        }
    }
}
