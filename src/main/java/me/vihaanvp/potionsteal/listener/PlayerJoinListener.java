package me.vihaanvp.potionsteal.listener;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.effect.PotionEffectManager;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

public class PlayerJoinListener implements Listener {
    private final PotionSteal plugin;
    private final PotionEffectManager effectManager;
    private final PlayerDataManager dataManager;

    public PlayerJoinListener(PotionSteal plugin, PotionEffectManager effectManager, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.effectManager = effectManager;
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Ensure player is added to history
        dataManager.setBalance(player.getUniqueId(), dataManager.getBalance(player.getUniqueId()));
        effectManager.updatePlayer(player);
    }
}