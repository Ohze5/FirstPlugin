package com.Rafael.firstplugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetHomeCommand implements CommandExecutor {

    private final Map<UUID, Map<String, Location>> playerHomes;

    public SetHomeCommand(Map<UUID, Map<String, Location>> playerHomes) {
        this.playerHomes = playerHomes;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("You must provide a name for your home!");
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
}