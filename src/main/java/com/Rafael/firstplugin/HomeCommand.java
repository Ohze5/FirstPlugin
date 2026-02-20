package com.Rafael.firstplugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.UUID;

public class HomeCommand implements CommandExecutor {

    private final HomeManager homeManager;

    public HomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        UUID playerID = player.getUniqueId();
        Map<String, Location> homes = homeManager.getOrCreatePlayerHomes(playerID);


        if (homes == null || homes.isEmpty()) {
            player.sendMessage("You don't have any homes!");
            return true;
        }

        Location loc;

        if (args.length == 0) {
            if (homes.size() == 1) {
                loc = homes.values().iterator().next();
            } else {
                player.sendMessage("You have multiple homes. Use /home <name> to choose which home to teleport to.");
                return true;
            }
        } else {
            String homeName = args[0];
            loc = homeManager.getHome(playerID, homeName);

            if (loc == null) {
                player.sendMessage("No home found with that name!");
                return true;
            }
        }

        player.teleport(loc);
        player.sendMessage("You have been teleported!");
        return true;
    }
}