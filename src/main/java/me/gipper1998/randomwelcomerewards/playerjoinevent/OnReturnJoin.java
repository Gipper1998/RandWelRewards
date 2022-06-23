package me.gipper1998.randomwelcomerewards.playerjoinevent;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OnReturnJoin implements Listener {
    private RandomWelcomeRewards main;
    private WelcomeReturnPlayer wrp;
    private Random rand;
    private List<String> messageList;

    public OnReturnJoin(RandomWelcomeRewards main, WelcomeReturnPlayer wrp){
        this.main = main;
        this.wrp = wrp;
        this.rand = new Random();
    }

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent event){
        if (main.config.getConfig().getBoolean("settings.enableReturnWelcome")) {
            if (!event.isCancelled()) {
                if (wrp.messageContains(event.getMessage())) {
                    Player player = event.getPlayer();
                    String message = event.getMessage();
                    Iterator var3 = wrp.getReturnPlayers().entrySet().iterator();
                    while (true) {
                        while (var3.hasNext()) {
                            Map.Entry<Player, ReturnPlayer> entry = (Map.Entry) var3.next();
                            ReturnPlayer returnPlayer = (ReturnPlayer) entry.getValue();
                            Integer timeoutTime = main.config.getConfig().getInt("settings.returnTime") * 1000;
                            if (System.currentTimeMillis() - returnPlayer.getJoinTime() > (long) timeoutTime) {
                                wrp.removeNew(returnPlayer.getPlayer());
                            } else if (!returnPlayer.getPlayer().equals(player) && !returnPlayer.hasPlayer(player)) {
                                returnPlayer.addReturnPlayer(player);
                                messageList = main.messages.getConfig().getStringList("messages.returnWelcomeMessages");
                                int messageSelect = rand.nextInt(messageList.size());
                                String Text = messageList.get(messageSelect).replaceAll("<returnplayer>", returnPlayer.getPlayer().getName());
                                Text = Text.replaceAll("<player>", player.getDisplayName());
                                if (!Text.equals("")) {
                                    message = "";
                                    message = Text;
                                    vaultRewards(player);
                                    commandRewards(player, event);
                                    playSound(player);
                                    main.playerDataManager.addWelcomePoint(player, false);
                                    main.milestoneManager.checkReturnWelcomeMilestone(player);
                                }
                            }
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
        if (player.getPlayer().hasPlayedBefore() && checkPlayTime(player.getPlayer()))
            wrp.addNew(player.getPlayer());
    }

    public Boolean checkPlayTime(Player player) {
        long ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        long minutes = ticks / 20 / 60;
        if (minutes >= main.config.getConfig().getInt("settings.returnTimeNeed")) {
            return true;
        }
        return false;
    }

    public void vaultRewards(Player player) {
        if (main.vaultEnabled) {
            if (main.config.getConfig().getBoolean("returnWelcomeRewards.vault.enable") == true) {
                int money = main.config.getConfig().getInt("returnWelcomeRewards.vault.reward");
                main.vaultManager.deposit(player, money);
                main.vaultChat(main.messages.getConfig().getString("messages.vaultMoney"), player, money);
            }
        }
        else {
            main.consoleMessage(main.messages.getConfig().getString("messages.noVault"));
        }
    }

    public void commandRewards(Player player, AsyncPlayerChatEvent event) {
        if (main.config.getConfig().getBoolean("returnWelcomeRewards.commands.enable") == true) {
            List<String> rewardCommands = main.config.getConfig().getStringList("returnWelcomeRewards.commands.rewardCommands");
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

    public void playSound(Player player){
        if (main.config.getConfig().getBoolean("returnWelcomeRewards.sound.enable") == true){
            try {
                player.playSound(player.getLocation(), Sound.valueOf(main.config.getConfig().getString("returnWelcomeRewards.sound.playSound").toUpperCase()), 1, 1);
            } catch (Exception e){ main.consoleMessage(main.messages.getConfig().getString("messages.noSound")); }
        }
    }
}
