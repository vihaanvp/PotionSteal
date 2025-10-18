package me.vihaanvp.potionsteal.listener;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import me.vihaanvp.potionsteal.effect.PotionEffectManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.ChatColor;

public class PlayerDeathListener implements Listener {

    private final PotionSteal plugin;
    private final PlayerDataManager dataManager;
    private final PotionEffectManager effectManager;

    public PlayerDeathListener(PotionSteal plugin, PlayerDataManager dataManager, PotionEffectManager effectManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.effectManager = effectManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        int balance = dataManager.getBalance(victim.getUniqueId());
        boolean immune = dataManager.isImmune(victim.getUniqueId());
        int banThreshold = plugin.getConfig().getInt("ban.threshold", -10);

        // Victim loses 1 balance
        if (!immune) {
            balance -= 1;
            dataManager.setBalance(victim.getUniqueId(), balance);

            // Apply debuffs according to new balance
            effectManager.updatePlayer(victim);

            if (balance <= banThreshold) {
                // Ban logic (no sound)
                String banMsg = ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("ban.message", "{player} has been banned for reaching {balance} debuffs!")
                                .replace("{player}", victim.getName())
                                .replace("{balance}", String.valueOf(balance))
                );
                Bukkit.broadcastMessage(banMsg);

                // Kick and ban
                victim.kickPlayer("You have been banned for reaching too many debuffs!");
                Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(victim.getName(), "Too many debuffs!", null, null);
            } else {
                victim.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("messages.died", "You died! Your balance is now {balance}")
                                .replace("{balance}", String.valueOf(balance))
                ));
                if (balance <= plugin.getConfig().getInt("ban.warningThreshold", -8)) {
                    String warningMsg = ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfig().getString("ban.warningMessage", "{player} is close to being banned! ({balance} debuffs)")
                                    .replace("{player}", victim.getName())
                                    .replace("{balance}", String.valueOf(balance))
                    );
                    Bukkit.broadcastMessage(warningMsg);
                }
            }
        }

        // Killer gains 1 balance
        if (killer != null && killer != victim) {
            int killerBalance = dataManager.getBalance(killer.getUniqueId());
            killerBalance += 1;
            dataManager.setBalance(killer.getUniqueId(), killerBalance);

            // Apply buffs according to new balance
            effectManager.updatePlayer(killer);

            killer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.killedPlayer", "You killed {victim}! Your balance is now {balance}")
                            .replace("{victim}", victim.getName())
                            .replace("{balance}", String.valueOf(killerBalance))
            ));
        }
    }
}