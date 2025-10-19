package me.vihaanvp.potionsteal.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import me.vihaanvp.potionsteal.PotionSteal;
import me.vihaanvp.potionsteal.command.subcommands.*;

import java.util.*;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final Map<String, SubCommand> subCommands = new LinkedHashMap<>();
    private final PotionSteal plugin;

    public CommandManager(PotionSteal plugin) {
        this.plugin = plugin;
        registerSubCommands();
    }

    private void registerSubCommands() {
        addSubCommand(new BalanceCommand(plugin));
        addSubCommand(new SetBalanceCommand(plugin));
        addSubCommand(new GiveElixirCommand(plugin));
        addSubCommand(new ImmunityCommand(plugin));
        addSubCommand(new ReloadCommand(plugin));
        addSubCommand(new ReviveCommand(plugin));
        addSubCommand(new GiveReviveBookCommand(plugin));
    }

    private void addSubCommand(SubCommand cmd) {
        subCommands.put(cmd.getName().toLowerCase(), cmd);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§ePotionSteal Commands:");
            for (SubCommand cmd : subCommands.values()) {
                if (cmd.getPermission() == null || sender.hasPermission(cmd.getPermission()))
                    sender.sendMessage("§a/" + label + " " + cmd.getUsage() + " §7- " + cmd.getDescription());
            }
            return true;
        }
        String sub = args[0].toLowerCase();
        SubCommand cmd = subCommands.get(sub);
        if (cmd == null) {
            sender.sendMessage("§cUnknown subcommand. Type /" + label + " for help.");
            return true;
        }
        if (cmd.getPermission() != null && !sender.hasPermission(cmd.getPermission())) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }
        return cmd.execute(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> available = new ArrayList<>();
            for (SubCommand cmd : subCommands.values()) {
                if (cmd.getPermission() == null || sender.hasPermission(cmd.getPermission()))
                    available.add(cmd.getName());
            }
            return available.stream().filter(s -> s.startsWith(args[0].toLowerCase())).toList();
        }
        SubCommand cmd = subCommands.get(args[0].toLowerCase());
        if (cmd != null && (cmd.getPermission() == null || sender.hasPermission(cmd.getPermission()))) {
            return cmd.tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        return Collections.emptyList();
    }
}