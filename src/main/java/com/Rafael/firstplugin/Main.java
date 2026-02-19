package com.Rafael.firstplugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.Map;

public class Main extends JavaPlugin {

    private final Map<UUID, Map<String, Location>> playerHomes = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled !");
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
}