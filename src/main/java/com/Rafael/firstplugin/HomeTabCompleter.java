package com.Rafael.firstplugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeTabCompleter implements TabCompleter {

    private final HomeManager homeManager;

    private static final Set<String> HOME_COMMANDS =
            Set.of("home", "delhome");

    public HomeTabCompleter(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!(sender instanceof Player player)) return List.of();

        if (!(HOME_COMMANDS.contains(command.getName().toLowerCase())))
            return List.of();

        if (args.length == 1) {

            Map<String, Location> homes = homeManager.getPlayerHomes(player.getUniqueId());

            if (homes == null)
                return List.of();

            if (homes.isEmpty())
                return List.of();

            String typed = args[0].toLowerCase();

            return homes.keySet().stream()
                    .filter(name -> name.toLowerCase().startsWith(typed))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
