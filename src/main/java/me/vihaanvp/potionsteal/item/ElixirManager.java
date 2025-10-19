package me.vihaanvp.potionsteal.item;

import me.vihaanvp.potionsteal.PotionSteal;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.ChatColor;

import java.util.List;

public class ElixirManager {
    private final PotionSteal plugin;

    public ElixirManager(PotionSteal plugin) {
        this.plugin = plugin;
    }

    /**
     * Create the custom Elixir item. This is used for both the command and as the result of the crafting recipe.
     */
    public ItemStack createElixirItem() {
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        // Set potion base and color
        meta.setBasePotionType(PotionType.MUNDANE);
        meta.setColor(Color.FUCHSIA); // Custom color for distinction
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "Elixir of Renewal"); // Non-italic name
        meta.setLore(List.of(
                ChatColor.AQUA + "Use to restore power!",
                ChatColor.YELLOW + "Special elixir for PotionSteal"
        ));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Register the elixir crafting recipe.
     * The result is always the custom elixir from createElixirItem().
     */
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