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

    private final Map<UUID, Map<String, Location>> playerHomes = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled !");
        Objects.requireNonNull(this.getCommand("home")).setTabCompleter(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled !");
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command !");
            return true;
        }

        if (command.getName().equalsIgnoreCase("sethome")) {
            if (args.length == 0) {
                player.sendMessage("You must provide a name for your home !");
                return true;
            }

            String homeName = args[0];
            Location loc = player.getLocation();

            UUID playerID = player.getUniqueId();
            Map<String, Location> homes = playerHomes.computeIfAbsent(playerID, k -> new HashMap<>());
            homes.put(homeName, loc);
            player.sendMessage("Home '" + homeName + "' has been set");

            return true;

        }

        if (command.getName().equalsIgnoreCase("home")) {
            UUID playerID = player.getUniqueId();
            Map<String, Location> homes = playerHomes.get(playerID);

            if (homes == null) {
                player.sendMessage("You don't have any homes");
                return true;
            }

            if (homes.isEmpty()) {
                player.sendMessage("You don't have any homes");
                return true;
            }

            Location loc;

            if (args.length == 0) {
                if (homes.size() == 1) {
                    loc = homes.values().iterator().next();
                } else {
                    player.sendMessage("You have multiple homes. Use /home <name> to teleport.");
                    return true;
                }
            } else {
                String homeName = args[0];
                loc = homes.get(homeName);

                if (loc == null) {
                    player.sendMessage("No home found with that name");
                    return true;
                }
            }

            player.teleport(loc);
            player.sendMessage("You have been teleported");
            return true;

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String [] args) {
        if (!(sender instanceof Player player)) return List.of();

        if (command.getName().equalsIgnoreCase("home")) {
            UUID playerID = player.getUniqueId();
            Map<String, Location> homes = playerHomes.get(playerID);

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