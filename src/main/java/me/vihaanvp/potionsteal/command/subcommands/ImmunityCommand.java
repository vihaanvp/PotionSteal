package me.vihaanvp.potionsteal.command.subcommands;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImmunityCommand implements SubCommand {
    private final PotionSteal plugin;

    public ImmunityCommand(PotionSteal plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "immunity"; }
    @Override
    public String getDescription() { return "Set ban immunity for a player"; }
    @Override
    public String getUsage() { return "immunity <player> <on|off>"; }
    @Override
    public String getPermission() { return "potionsteal.admin"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /potionsteal immunity <player> <on|off>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return true;
        }
        boolean immune = args[1].equalsIgnoreCase("on");
        plugin.getDataManager().setImmune(target.getUniqueId(), immune);
        sender.sendMessage(immune
                ? "§a" + target.getName() + " is now immune to ban."
                : "§c" + target.getName() + " is no longer immune to ban.");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(s -> s.startsWith(args[0])).toList();
        if (args.length == 2)
            return Arrays.asList("on", "off");
        return Collections.emptyList();
    }
}