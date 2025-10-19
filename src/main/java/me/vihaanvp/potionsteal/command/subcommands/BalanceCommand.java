package me.vihaanvp.potionsteal.command.subcommands;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;

public class BalanceCommand implements SubCommand {
    private final PotionSteal plugin;

    public BalanceCommand(PotionSteal plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "balance"; }
    @Override
    public String getDescription() { return "View your or another player's balance"; }
    @Override
    public String getUsage() { return "balance [player]"; }
    @Override
    public String getPermission() { return "potionsteal.balance"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player target = null;
        if (args.length > 0 && !args[0].isBlank()) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found!");
                return true;
            }
        } else if (sender instanceof Player) {
            target = (Player)sender;
        }
        if (target == null) {
            sender.sendMessage("§cSpecify a player!");
            return true;
        }
        int bal = plugin.getDataManager().getBalance(target.getUniqueId());
        sender.sendMessage("§eBalance for §a" + target.getName() + "§e: §b" + bal);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(s -> s.startsWith(args[0])).toList();
        return Collections.emptyList();
    }
}