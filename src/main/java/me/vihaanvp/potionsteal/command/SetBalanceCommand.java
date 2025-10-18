package me.vihaanvp.potionsteal.command;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import me.vihaanvp.potionsteal.effect.PotionEffectManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetBalanceCommand implements CommandExecutor {
    private final PotionSteal plugin;
    private final PlayerDataManager dataManager;
    private final PotionEffectManager effectManager;

    public SetBalanceCommand(PotionSteal plugin, PlayerDataManager dataManager, PotionEffectManager effectManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.effectManager = effectManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /potionsteal setbalance <player> <value>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found!");
            return true;
        }
        try {
            int value = Integer.parseInt(args[1]);
            dataManager.setBalance(target.getUniqueId(), value);
            effectManager.updatePlayer(target);
            sender.sendMessage("Set balance for " + target.getName() + " to " + value);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid number!");
        }
        return true;
    }
}