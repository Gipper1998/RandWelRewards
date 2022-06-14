package me.gipper1998.randomwelcomerewards.depmanager;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gipper1998.randomwelcomerewards.RandomWelcomeRewards;
import org.jetbrains.annotations.NotNull;

public class PlaceholderManager extends PlaceholderExpansion {

    private RandomWelcomeRewards main;

    public PlaceholderManager(RandomWelcomeRewards main){
        this.main = main;
    }

    @Override
    public @NotNull String getIdentifier() {
        return null;
    }

    @Override
    public @NotNull String getAuthor() {
        return null;
    }

    @Override
    public @NotNull String getVersion() {
        return null;
    }
}
