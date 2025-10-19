package me.vihaanvp.potionsteal.command.subcommands;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import java.util.Collections;
import java.util.List;

public class ReviveCommand implements SubCommand {
    private final PotionSteal plugin;

    public ReviveCommand(PotionSteal plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "revive"; }
    @Override
    public String getDescription() { return "Revive (unban and reset balance) a deathbanned player"; }
    @Override
    public String getUsage() { return "revive <player>"; }
    @Override
    public String getPermission() { return "potionsteal.admin"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /potionsteal revive <player>");
            return true;
        }
        String targetName = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        Bukkit.getBanList(org.bukkit.BanList.Type.NAME).pardon(targetName);
        plugin.getDataManager().setBalance(target.getUniqueId(), plugin.getConfig().getInt("balance.default", 0));
        Bukkit.broadcastMessage("§a" + targetName + " has been revived and unbanned!");
        sender.sendMessage("§aPlayer " + targetName + " has been unbanned and their balance reset.");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        // Optionally list deathbanned players
        return Collections.emptyList();
    }
}