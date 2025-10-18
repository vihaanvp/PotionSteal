package me.vihaanvp.potionsteal.effect;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PotionEffectManager {
    private final PotionSteal plugin;
    private final PlayerDataManager dataManager;

    public PotionEffectManager(PotionSteal plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void applyEffects(Player player) {
        // Remove all effects managed by this plugin
        List<String> buffs = plugin.getConfig().getStringList("effects.buffs");
        List<String> debuffs = plugin.getConfig().getStringList("effects.debuffs");
        Set<PotionEffectType> managedEffects = new HashSet<>();
        for (String s : buffs) managedEffects.add(PotionEffectType.getByName(s.split(":")[0]));
        for (String s : debuffs) managedEffects.add(PotionEffectType.getByName(s.split(":")[0]));
        for (PotionEffectType type : managedEffects) player.removePotionEffect(type);

        int balance = dataManager.getBalance(player.getUniqueId());
        if (balance == 0) return; // No effects

        List<String> effectList = balance > 0 ? buffs : debuffs;
        int count = Math.min(Math.abs(balance), effectList.size());

        for (int i = 0; i < count; i++) {
            String[] parts = effectList.get(i).split(":");
            PotionEffectType type = PotionEffectType.getByName(parts[0].toUpperCase());
            int level = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
            if (type != null) {
                player.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, level - 1, true, false));
            }
        }
    }

    public void updatePlayer(Player player) {
        applyEffects(player);
    }
}