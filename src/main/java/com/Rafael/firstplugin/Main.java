package com.Rafael.firstplugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {

    private HomeManager homeManager;

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled !");

        homeManager = new HomeManager(this);
        HomeTabCompleter completer = new HomeTabCompleter(homeManager);

        this.getCommand("sethome").setExecutor(new SetHomeCommand(homeManager));
        this.getCommand("home").setExecutor(new HomeCommand(homeManager));
        this.getCommand("delhome").setExecutor(new DelHomeCommand(homeManager));

        Objects.requireNonNull(this.getCommand("delhome")).setTabCompleter(completer);
        Objects.requireNonNull(this.getCommand("home")).setTabCompleter(completer);

    }

    @Override
    public void onDisable() {
        homeManager.saveHomes();
        getLogger().info("Plugin disabled !");
    }


}