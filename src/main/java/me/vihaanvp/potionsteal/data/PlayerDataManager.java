package me.vihaanvp.potionsteal.data;

import me.vihaanvp.potionsteal.PotionSteal;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class PlayerDataManager {
    private final PotionSteal plugin;
    private final File dataFile;
    private YamlConfiguration dataConfig;

    public PlayerDataManager(PotionSteal plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "players.yml");
        reload();
    }

    public void reload() {
        if (!dataFile.exists()) {
            try { dataFile.createNewFile(); } catch (Exception ignored) {}
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void save() {
        try { dataConfig.save(dataFile); } catch (Exception ignored) {}
    }

    public int getBalance(UUID uuid) {
        return dataConfig.getInt(uuid + ".balance", plugin.getConfig().getInt("balance.default", 0));
    }

    public void setBalance(UUID uuid, int balance) {
        dataConfig.set(uuid + ".balance", balance);
        save();
    }

    public boolean isImmune(UUID uuid) {
        return dataConfig.getBoolean(uuid + ".immune", false);
    }

    public void setImmune(UUID uuid, boolean immune) {
        dataConfig.set(uuid + ".immune", immune);
        save();
    }

    public Set<UUID> getAllPlayers() {
        Set<UUID> uuids = new HashSet<>();
        for (String key : dataConfig.getKeys(false)) {
            try { uuids.add(UUID.fromString(key)); } catch (Exception ignored) {}
        }
        return uuids;
    }
}