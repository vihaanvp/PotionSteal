package me.vihaanvp.potionsteal.command;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.item.ElixirManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveElixirCommand implements CommandExecutor {
    private final PotionSteal plugin;
    private final ElixirManager elixirManager;

    public GiveElixirCommand(PotionSteal plugin, ElixirManager elixirManager) {
        this.plugin = plugin;
        this.elixirManager = elixirManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /potionsteal giveelixir <player> [amount]");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found!");
            return true;
        }
        int amt = 1;
        if (args.length > 1) {
            try { amt = Integer.parseInt(args[1]); } catch (NumberFormatException ignored) {}
        }
        ItemStack elixir = elixirManager.createElixirItem();
        elixir.setAmount(amt);
        target.getInventory().addItem(elixir);
        sender.sendMessage("Gave " + amt + " Elixir(s) to " + target.getName());
        return true;
    }
}