package me.gipper1998.randomwelcomerewards.listeners;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import me.gipper1998.randomwelcomerewards.managers.ReturnPlayer;
import me.gipper1998.randomwelcomerewards.managers.WelcomeReturnPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
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
    private Random rand;
    private List<String> messageList;

    public OnReturnJoin(RandomWelcomeRewards main, WelcomeReturnPlayer wrp){
        this.main = main;
        this.wrp = wrp;
        this.rand = new Random();
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
                            Map.Entry<Player, ReturnPlayer> entry = (Map.Entry) var3.next();
                            ReturnPlayer returnPlayer = (ReturnPlayer) entry.getValue();
                            Integer timeoutTime = this.main.getConfig().getInt("settings.returnTime") * 1000;
                            if (System.currentTimeMillis() - returnPlayer.getJoinTime() > (long) timeoutTime) {
                                wrp.removeNew(returnPlayer.getPlayer());
                            } else if (!returnPlayer.getPlayer().equals(player) && !returnPlayer.hasPlayer(player)) {
                                returnPlayer.addWelcomePlayer(player);
                                this.messageList = main.messages.getConfig().getStringList("messages.returnWelcomeMessages");
                                int messageSelect = this.rand.nextInt(messageList.size());
                                String Text = messageList.get(messageSelect).replaceAll("<returnplayer>", returnPlayer.getPlayer().getName());
                                Text = Text.replaceAll("<player>", player.getDisplayName());
                                if (!Text.equals("")) {
                                    message = "";
                                    message = Text;
                                    vaultRewards(player, event);
                                    commandRewards(player, event);
                                    main.addWelcomePoint(player, false);
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
        if (player.getPlayer().hasPlayedBefore() && checkPlayTime(player.getPlayer()))
            wrp.addNew(player.getPlayer());
    }

    public Boolean checkPlayTime(Player player) {
        long ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        long minutes = ticks / 20 / 60;
        if (minutes >= main.getConfig().getInt("settings.returnTimeNeed"))
            return true;
        return false;
    }

    public void vaultRewards(Player player, AsyncPlayerChatEvent event) {
        if (main.vaultEnabled) {
            if (main.getConfig().getBoolean("returnWelcomeRewards.vault.enable") == true) {
                int money = main.getConfig().getInt("returnWelcomeRewards.vault.reward");
                main.deposit(player, money);
                main.vaultChat(main.messages.getConfig().getString("messages.vaultMoney"), player, money);
            }
        }
        else
            main.consoleMessage("<prefix> &cVault was not found, please disable vault rewards in the config.");
    }

    public void commandRewards(Player player, AsyncPlayerChatEvent event) {
        if (main.getConfig().getBoolean("returnWelcomeRewards.commands.enable") == true) {
            List<String> rewardCommands = this.main.getConfig().getStringList("returnWelcomeRewards.commands.rewardCommands");
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
