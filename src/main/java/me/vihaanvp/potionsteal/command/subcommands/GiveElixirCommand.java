package me.vihaanvp.potionsteal.command.subcommands;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;

public class GiveElixirCommand implements SubCommand {
    private final PotionSteal plugin;

    public GiveElixirCommand(PotionSteal plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "giveelixir"; }
    @Override
    public String getDescription() { return "Give elixir(s) to a player"; }
    @Override
    public String getUsage() { return "giveelixir <player> [amount]"; }
    @Override
    public String getPermission() { return "potionsteal.admin"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /potionsteal giveelixir <player> [amount]");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return true;
        }
        int amt = 1;
        if (args.length > 1) {
            try { amt = Integer.parseInt(args[1]); } catch (NumberFormatException ignored) {}
        }
        var elixir = plugin.getElixirManager().createElixirItem();
        elixir.setAmount(amt);
        target.getInventory().addItem(elixir);
        sender.sendMessage("§aGave " + amt + " elixir(s) to " + target.getName());
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(s -> s.startsWith(args[0])).toList();
        if (args.length == 2)
            return List.of("<amount>");
        return Collections.emptyList();
    }
}