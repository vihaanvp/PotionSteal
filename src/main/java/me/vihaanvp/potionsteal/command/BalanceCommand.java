package me.vihaanvp.potionsteal.command;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    private final PotionSteal plugin;
    private final PlayerDataManager dataManager;

    public BalanceCommand(PotionSteal plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("Player not found!");
                return true;
            }
        } else if (sender instanceof Player player) {
            target = player;
        } else {
            sender.sendMessage("Console must specify a player.");
            return true;
        }
        int balance = dataManager.getBalance(target.getUniqueId());
        sender.sendMessage("Balance for " + target.getName() + ": " + balance);
        return true;
    }
}