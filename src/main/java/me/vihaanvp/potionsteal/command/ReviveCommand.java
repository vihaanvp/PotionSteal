package me.vihaanvp.potionsteal.command;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReviveCommand implements CommandExecutor {
    private final PotionSteal plugin;
    private final PlayerDataManager dataManager;

    public ReviveCommand(PotionSteal plugin, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /potionsteal revive <player>");
            return true;
        }
        String targetName = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

        // Unban from server ban list
        Bukkit.getBanList(org.bukkit.BanList.Type.NAME).pardon(targetName);

        // Reset balance
        dataManager.setBalance(target.getUniqueId(), plugin.getConfig().getInt("balance.default", 0));

        // Announce
        Bukkit.broadcastMessage("§a" + targetName + " has been revived and unbanned!");

        sender.sendMessage("Player " + targetName + " has been unbanned and their balance reset.");
        return true;
    }
}