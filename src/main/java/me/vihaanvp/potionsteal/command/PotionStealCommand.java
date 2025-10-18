package me.vihaanvp.potionsteal.command;

import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import me.vihaanvp.potionsteal.effect.PotionEffectManager;
import me.vihaanvp.potionsteal.item.ElixirManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class PotionStealCommand implements CommandExecutor {
    private final PotionSteal plugin;
    private final PlayerDataManager dataManager;
    private final PotionEffectManager effectManager;
    private final ElixirManager elixirManager;

    public PotionStealCommand(PotionSteal plugin, PlayerDataManager dataManager, PotionEffectManager effectManager, ElixirManager elixirManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.effectManager = effectManager;
        this.elixirManager = elixirManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Help message
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "PotionSteal Commands:");
            sender.sendMessage(ChatColor.YELLOW + "/potionsteal balance [player]");
            if (sender.hasPermission("potionsteal.admin")) {
                sender.sendMessage(ChatColor.YELLOW + "/potionsteal setbalance <player> <value>");
                sender.sendMessage(ChatColor.YELLOW + "/potionsteal giveelixir <player> [amount]");
                sender.sendMessage(ChatColor.YELLOW + "/potionsteal immunity <player> <on|off>");
                sender.sendMessage(ChatColor.YELLOW + "/potionsteal reload");
                sender.sendMessage(ChatColor.YELLOW + "/potionsteal revive <player>");
            }
            return true;
        }
        String sub = args[0].toLowerCase();
        // -- balance (everyone)
        if (sub.equals("balance")) {
            Player target = sender instanceof Player && args.length == 1
                    ? (Player)sender
                    : args.length > 1 ? Bukkit.getPlayer(args[1]) : null;
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            int bal = dataManager.getBalance(target.getUniqueId());
            sender.sendMessage(ChatColor.AQUA + "Balance for " + target.getName() + ": " + ChatColor.YELLOW + bal);
            return true;
        }
        // -- admin commands
        if (!sender.hasPermission("potionsteal.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
            return true;
        }
        switch (sub) {
            case "setbalance": {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /potionsteal setbalance <player> <value>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
                try {
                    int value = Integer.parseInt(args[2]);
                    dataManager.setBalance(target.getUniqueId(), value);
                    effectManager.updatePlayer(target);
                    sender.sendMessage(ChatColor.GREEN + "Set balance for " + target.getName() + " to " + value);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid number!");
                }
                return true;
            }
            case "giveelixir": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /potionsteal giveelixir <player> [amount]");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
                int amt = 1;
                if (args.length > 2) {
                    try { amt = Integer.parseInt(args[2]); } catch (NumberFormatException ignored) {}
                }
                var elixir = elixirManager.createElixirItem();
                elixir.setAmount(amt);
                target.getInventory().addItem(elixir);
                sender.sendMessage(ChatColor.GREEN + "Gave " + amt + " Elixir(s) to " + target.getName());
                return true;
            }
            case "immunity": {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /potionsteal immunity <player> <on|off>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
                boolean immune = args[2].equalsIgnoreCase("on");
                dataManager.setImmune(target.getUniqueId(), immune);
                sender.sendMessage(immune
                        ? ChatColor.GREEN + target.getName() + " is now immune to ban."
                        : ChatColor.RED + target.getName() + " is no longer immune to ban.");
                return true;
            }
            case "reload": {
                plugin.reloadPluginConfig();
                sender.sendMessage(ChatColor.GREEN + "PotionSteal config reloaded!");
                return true;
            }
            case "revive": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /potionsteal revive <player>");
                    return true;
                }
                String targetName = args[1];
                var target = Bukkit.getOfflinePlayer(targetName);
                Bukkit.getBanList(org.bukkit.BanList.Type.NAME).pardon(targetName);
                dataManager.setBalance(target.getUniqueId(), plugin.getConfig().getInt("balance.default", 0));
                Bukkit.broadcastMessage(ChatColor.GREEN + targetName + " has been revived and unbanned!");
                sender.sendMessage(ChatColor.GREEN + "Player " + targetName + " has been unbanned and their balance reset.");
                return true;
            }
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Type /potionsteal for help.");
                return true;
        }
    }
}