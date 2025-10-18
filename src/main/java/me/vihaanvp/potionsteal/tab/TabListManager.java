package me.vihaanvp.potionsteal.tab;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TabListManager {
    private final PotionSteal plugin;
    private final PlayerDataManager dataManager;

    public TabListManager(PotionSteal plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void updateTabList() {
        if (!plugin.getConfig().getBoolean("tablist.enabled", true)) return;
        String format = plugin.getConfig().getString("tablist.format", "[{balance}] {player}");
        for (Player p : Bukkit.getOnlinePlayers()) {
            int balance = dataManager.getBalance(p.getUniqueId());
            String name = format.replace("{balance}", String.valueOf(balance)).replace("{player}", p.getName());
            p.setPlayerListName(name);
        }
    }
}