package com.Rafael.firstplugin;

import org.bukkit.command.TabCompleter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class Main extends JavaPlugin implements TabCompleter {

    private HomeManager homeManager;

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled !");

        homeManager = new HomeManager(this);

        this.getCommand("sethome").setExecutor(new SetHomeCommand(homeManager));
        this.getCommand("home").setExecutor(new HomeCommand(homeManager));

        Objects.requireNonNull(this.getCommand("home")).setTabCompleter(this);

    }

    @Override
    public void onDisable() {
        homeManager.saveHomes();
        getLogger().info("Plugin disabled !");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String [] args) {
        if (!(sender instanceof Player player)) return List.of();

        if (command.getName().equalsIgnoreCase("home")) {
            UUID playerID = player.getUniqueId();
            Map<String, Location> homes = homeManager.getOrCreatePlayerHomes(player.getUniqueId());

            if (homes == null) return List.of();

            if (args.length == 1) {
                String typed = args[0].toLowerCase();

                return homes.keySet().stream()
                        .filter(name -> name.toLowerCase().startsWith(typed))
                        .collect(Collectors.toList());
            }
        }
        return List.of();
    }
}