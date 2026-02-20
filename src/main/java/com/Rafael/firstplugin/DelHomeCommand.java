package com.Rafael.firstplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DelHomeCommand implements CommandExecutor {

    private final HomeManager homeManager;

    public DelHomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("You must provide the name of the home to delete");
            return true;
        }

        String homeName = args[0];
        UUID playerId = player.getUniqueId();

        boolean deleted = homeManager.deleteHome(playerId, homeName);

        if (!deleted) {
            player.sendMessage("No home found with that name.");
            return true;
        }

        homeManager.saveHomes();
        player.sendMessage("Home '" + homeName + "' has been deleted.");
        return true;
    }
}
