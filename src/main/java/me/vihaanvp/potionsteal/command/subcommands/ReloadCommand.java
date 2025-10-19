package me.vihaanvp.potionsteal.command.subcommands;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.command.SubCommand;
import org.bukkit.command.CommandSender;
import java.util.Collections;
import java.util.List;

public class ReloadCommand implements SubCommand {
    private final PotionSteal plugin;

    public ReloadCommand(PotionSteal plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "reload"; }
    @Override
    public String getDescription() { return "Reload PotionSteal config"; }
    @Override
    public String getUsage() { return "reload"; }
    @Override
    public String getPermission() { return "potionsteal.admin"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        plugin.reloadPluginConfig();
        sender.sendMessage("§aPotionSteal config reloaded!");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}