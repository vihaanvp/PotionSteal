package me.vihaanvp.potionsteal.command;

import me.vihaanvp.potionsteal.PotionSteal;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final PotionSteal plugin;

    public ReloadCommand(PotionSteal plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadPluginConfig();
        sender.sendMessage(plugin.getConfig().getString("messages.reload", "PotionSteal config reloaded!"));
        return true;
    }
}