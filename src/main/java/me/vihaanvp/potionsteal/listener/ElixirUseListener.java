package me.vihaanvp.potionsteal.listener;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import me.vihaanvp.potionsteal.effect.PotionEffectManager;
import me.vihaanvp.potionsteal.item.ElixirManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

public class ElixirUseListener implements Listener {
    private final PotionSteal plugin;
    private final PlayerDataManager dataManager;
    private final PotionEffectManager effectManager;
    private final ElixirManager elixirManager;

    public ElixirUseListener(PotionSteal plugin, PlayerDataManager dataManager, PotionEffectManager effectManager, ElixirManager elixirManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.effectManager = effectManager;
        this.elixirManager = elixirManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        ItemStack elixir = elixirManager.createElixirItem();
        if (item.isSimilar(elixir)) {
            int balance = dataManager.getBalance(player.getUniqueId());
            dataManager.setBalance(player.getUniqueId(), balance + 1);
            item.setAmount(item.getAmount() - 1);
            effectManager.updatePlayer(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.usedElixir", "Elixir used! Your balance is now {balance}")
                            .replace("{balance}", String.valueOf(balance + 1))
            ));
            event.setCancelled(true);
        }
    }
}