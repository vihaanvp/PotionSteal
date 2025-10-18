package me.vihaanvp.potionsteal.scoreboard;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class ScoreboardManager {
    private final PotionSteal plugin;
    private final PlayerDataManager dataManager;

    public ScoreboardManager(PotionSteal plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    private String stripColorCodes(String input) {
        return input.replaceAll("(&[0-9a-fk-orA-FK-OR])", "");
    }

    public void updateScoreboard(Player viewer) {
        if (!plugin.getConfig().getBoolean("scoreboard.enabled", true)) return;

        String title = stripColorCodes(plugin.getConfig().getString("scoreboard.title", "PotionSteal Leaderboard"));
        String lineFormat = stripColorCodes(plugin.getConfig().getString("scoreboard.lineFormat", "{rank}. {player} - {balance}"));

        // Get all known players (history), not just online
        List<UUID> allPlayers = new ArrayList<>(dataManager.getAllPlayers());
        allPlayers.sort(Comparator.comparingInt(dataManager::getBalance).reversed());

        org.bukkit.scoreboard.Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = sb.registerNewObjective("PotionSteal", "dummy", title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int rank = 1;
        for (UUID uuid : allPlayers) {
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            int balance = dataManager.getBalance(uuid);
            String line = lineFormat.replace("{rank}", String.valueOf(rank))
                    .replace("{player}", name == null ? "Unknown" : name)
                    .replace("{balance}", String.valueOf(balance));
            obj.getScore(line).setScore(allPlayers.size() - rank);
            rank++;
        }

        viewer.setScoreboard(sb);
    }
}