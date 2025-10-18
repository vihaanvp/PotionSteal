package me.vihaanvp.potionsteal.listener;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.effect.PotionEffectManager;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.entity.Player;

public class PlayerRespawnListener implements Listener {
    private final PotionSteal plugin;
    private final PotionEffectManager effectManager;
    private final PlayerDataManager dataManager;

    public PlayerRespawnListener(PotionSteal plugin, PotionEffectManager effectManager, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.effectManager = effectManager;
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        effectManager.updatePlayer(event.getPlayer());
    }
}