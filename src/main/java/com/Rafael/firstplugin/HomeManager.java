package com.Rafael.firstplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeManager {

    private final JavaPlugin plugin;
    private final Map<UUID, Map<String, Location>> playerHomes = new HashMap<>();
    private File homesFile;
    private FileConfiguration homesConfig;

    public HomeManager(JavaPlugin plugin) {
        this.plugin = plugin;

        // Initialize homes.yml file
        homesFile = new File(plugin.getDataFolder(), "homes.yml");

        // Ensure plugin folder exists
        File parent = homesFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        // Create the file if it doesn't exist
        if (!homesFile.exists()) {
            try {
                homesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load configuration
        homesConfig = YamlConfiguration.loadConfiguration(homesFile);

        // Load homes into memory
        loadHomes();
    }

    public Map<String, Location> getOrCreatePlayerHomes(UUID playerId) {
        return playerHomes.computeIfAbsent(playerId, k -> new HashMap<>());

    }

    public Map<String, Location> getPlayerHomes(UUID playerId) {
        return playerHomes.get(playerId);
    }

    private void loadHomes() {
        for (String playerId : homesConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(playerId);
            Map<String, Location> homesMap = new HashMap<>();

            if (homesConfig.getConfigurationSection(playerId) == null) continue;

            for (String homeName : homesConfig.getConfigurationSection(playerId).getKeys(false)) {
                double x = homesConfig.getDouble(playerId + "." + homeName + ".x");
                double y = homesConfig.getDouble(playerId + "." + homeName + ".y");
                double z = homesConfig.getDouble(playerId + "." + homeName + ".z");
                float yaw = (float) homesConfig.getDouble(playerId + "." + homeName + ".yaw");
                float pitch = (float) homesConfig.getDouble(playerId + "." + homeName + ".pitch");
                String world = homesConfig.getString(playerId + "." + homeName + ".world");

                if (Bukkit.getWorld(world) == null) continue;
                Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                homesMap.put(homeName, loc);
            }

            playerHomes.put(uuid, homesMap);
        }
    }

    // Save all homes to file
    public void saveHomes() {
        try {
            for (UUID uuid : playerHomes.keySet()) {
                Map<String, Location> homesMap = playerHomes.get(uuid);
                for (String homeName : homesMap.keySet()) {
                    Location loc = homesMap.get(homeName);
                    homesConfig.set(uuid.toString() + "." + homeName + ".x", loc.getX());
                    homesConfig.set(uuid.toString() + "." + homeName + ".y", loc.getY());
                    homesConfig.set(uuid.toString() + "." + homeName + ".z", loc.getZ());
                    homesConfig.set(uuid.toString() + "." + homeName + ".yaw", loc.getYaw());
                    homesConfig.set(uuid.toString() + "." + homeName + ".pitch", loc.getPitch());
                    homesConfig.set(uuid.toString() + "." + homeName + ".world", loc.getWorld().getName());
                }
            }

            homesConfig.save(homesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getHome(UUID playerId, String homeName) {
        Map<String, Location> homes = playerHomes.get(playerId);
        if (homes == null) return null;
        return homes.get(homeName);
    }

    public boolean deleteHome(UUID playerId, String homeName) {
        Map<String, Location> homes = playerHomes.get(playerId);
        if (homes == null) return false;
        if (!(homes.containsKey(homeName))) return false;
        homes.remove(homeName);
        if (homes.isEmpty()) playerHomes.remove(playerId);
        return true;
    }
}