package me.vihaanvp.potionsteal.item;

import me.vihaanvp.potionsteal.PotionSteal;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class ElixirManager {
    private final PotionSteal plugin;

    public ElixirManager(PotionSteal plugin) {
        this.plugin = plugin;
    }

    public ItemStack createElixirItem() {
        ItemStack item = new ItemStack(Material.valueOf(plugin.getConfig().getString("elixir.material", "POTION")));
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.setBasePotionType(PotionType.MUNDANE);
        }
        String name = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("elixir.name", "Elixir of Renewal"));
        meta.setDisplayName(name);
        List<String> lore = plugin.getConfig().getStringList("elixir.lore").stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void registerElixirRecipe() {
        ItemStack elixir = createElixirItem();
        NamespacedKey key = new NamespacedKey(plugin, "elixir_of_renewal");

        ShapedRecipe recipe = new ShapedRecipe(key, elixir);
        recipe.shape(
                "RGR",
                "DPD",
                "RGR"
        );
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setIngredient('P', Material.GLASS_BOTTLE);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);

        // Remove existing recipe if already registered (to avoid duplicates on reload)
        Bukkit.removeRecipe(key);
        Bukkit.addRecipe(recipe);
    }
}