package me.vihaanvp.potionsteal.item;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.event.EventHandler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

public class ReviveBookManager implements Listener {
    private final PotionSteal plugin;
    private final PlayerDataManager dataManager;
    private final ElixirManager elixirManager;

    public ReviveBookManager(PotionSteal plugin, PlayerDataManager dataManager, ElixirManager elixirManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.elixirManager = elixirManager;
    }

    // Create the revive book item
    public ItemStack createReviveBook() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Revive Book");
        meta.setLore(List.of(
                ChatColor.GOLD + "Right-click to revive a deathbanned player!",
                ChatColor.GRAY + "Consumed after one use."
        ));
        book.setItemMeta(meta);
        return book;
    }

    // Register the revive book recipe, using the elixir as an ingredient (exact choice)
    public void registerReviveBookRecipe() {
        ItemStack reviveBook = createReviveBook();
        NamespacedKey key = new NamespacedKey(plugin, "revive_book");

        ShapedRecipe recipe = new ShapedRecipe(key, reviveBook);
        recipe.shape(
                "TDT",
                "EBE",
                "TDT"
        );
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('B', Material.BOOK);
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);

        // Use ExactChoice so only your elixir is valid
        ItemStack elixir = elixirManager.createElixirItem();
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(elixir));

        Bukkit.removeRecipe(key);
        Bukkit.addRecipe(recipe);
    }

    // When a player right-clicks with the revive book
    @EventHandler
    public void onBookInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName() ||
                !ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Revive Book")) return;

        event.setCancelled(true);
        Player player = event.getPlayer();
        showReviveGui(player, item);
    }

    public void showReviveGui(Player player, ItemStack bookUsed) {
        List<String> bannedNames = Bukkit.getBanList(org.bukkit.BanList.Type.NAME).getBanEntries()
                .stream().map(be -> be.getTarget()).collect(Collectors.toList());

        // Only show GUI if there's at least 1 banned player
        if (bannedNames.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No deathbanned players to revive!");
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE + "Revive Players");
        int slot = 0;
        for (String banned : bannedNames) {
            if (slot >= gui.getSize()) break;
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(banned));
            skullMeta.setDisplayName(ChatColor.RED + banned);
            skullMeta.setLore(List.of(ChatColor.GRAY + "Click to revive!"));
            head.setItemMeta(skullMeta);
            gui.setItem(slot++, head);
        }
        bookUsed.setAmount(1); // Only allow single-use per click
        player.openInventory(gui);
    }

    @EventHandler
    public void onReviveGuiClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.DARK_PURPLE + "Revive Players")) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked != null && clicked.getType() == Material.PLAYER_HEAD && clicked.hasItemMeta()) {
            String name = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
            Bukkit.getBanList(org.bukkit.BanList.Type.NAME).pardon(name);
            UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
            dataManager.setBalance(uuid, plugin.getConfig().getInt("balance.default", 0));
            player.sendMessage(ChatColor.GREEN + "Revived " + name + "!");
            Bukkit.broadcastMessage(ChatColor.AQUA + name + " has been revived by " + player.getName() + "!");
            player.closeInventory();

            // Remove one revive book from player's hand
            ItemStack hand = player.getInventory().getItemInMainHand();
            if (hand != null && hand.getType() == Material.ENCHANTED_BOOK) {
                ItemMeta meta = hand.getItemMeta();
                if (meta != null && meta.hasDisplayName() &&
                        ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Revive Book")) {
                    hand.setAmount(hand.getAmount() - 1);
                }
            }
        }
    }
}