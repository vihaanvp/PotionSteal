package me.vihaanvp.potionsteal.command.subcommands;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;

public class GiveReviveBookCommand implements SubCommand {
    private final PotionSteal plugin;

    public GiveReviveBookCommand(PotionSteal plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "giverevivebook"; }
    @Override
    public String getDescription() { return "Give Revive Book(s) to a player"; }
    @Override
    public String getUsage() { return "giverevivebook <player> [amount]"; }
    @Override
    public String getPermission() { return "potionsteal.admin"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /potionsteal giverevivebook <player> [amount]");
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
        var book = plugin.getReviveBookManager().createReviveBook();
        book.setAmount(amt);
        target.getInventory().addItem(book);
        sender.sendMessage("§dGave " + amt + " Revive Book(s) to " + target.getName());
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