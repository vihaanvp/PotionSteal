package me.vihaanvp.potionsteal.command;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class ImmunityCommand implements CommandExecutor {
    private final PotionSteal plugin;
    private final PlayerDataManager dataManager;

    public ImmunityCommand(PotionSteal plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /potionsteal immunity <player> <on|off>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found!");
            return true;
        }
        boolean immune = args[1].equalsIgnoreCase("on");
        dataManager.setImmune(target.getUniqueId(), immune);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                immune ? plugin.getConfig().getString("messages.immunityOn", "{player} is now immune to ban.").replace("{player}", target.getName())
                        : plugin.getConfig().getString("messages.immunityOff", "{player} is no longer immune to ban.").replace("{player}", target.getName())
        ));
        return true;
    }
}