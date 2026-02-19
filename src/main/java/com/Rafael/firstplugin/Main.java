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

            player.sendMessage("You have set your home " + homeName + " at your current location");

            return true;

        }

        return false;
    }
}