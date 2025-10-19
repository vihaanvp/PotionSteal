package me.vihaanvp.potionsteal.command.subcommands;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;

public class SetBalanceCommand implements SubCommand {
    private final PotionSteal plugin;

    public SetBalanceCommand(PotionSteal plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "setbalance"; }
    @Override
    public String getDescription() { return "Set a player's balance"; }
    @Override
    public String getUsage() { return "setbalance <player> <value>"; }
    @Override
    public String getPermission() { return "potionsteal.admin"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /potionsteal setbalance <player> <value>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return true;
        }
        try {
            int value = Integer.parseInt(args[1]);
            plugin.getDataManager().setBalance(target.getUniqueId(), value);
            plugin.getEffectManager().updatePlayer(target);
            sender.sendMessage("§aSet balance for " + target.getName() + " to " + value);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid number!");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(s -> s.startsWith(args[0])).toList();
        if (args.length == 2)
            return List.of("<value>");
        return Collections.emptyList();
    }
}