package me.gipper1998.randomwelcomerewards.depmanager;

import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultManager {
    private Economy economy;
    private Chat chat;

    private RandomWelcomeRewards main;

    public VaultManager(RandomWelcomeRewards main){
        this.main = main;
        registerVault();
    }

    public boolean registerVault() {
        RegisteredServiceProvider<Economy> rsp = main.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        economy = rsp.getProvider();
        setupChat();
        return economy != null;
    }

    public boolean setupChat(){
        RegisteredServiceProvider<Chat> chatProvider = main.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null)
            chat = chatProvider.getProvider();
        return (chat != null);
    }

    public void deposit(Player player, int money){ economy.depositPlayer(player, money); }


}
