package me.vihaanvp.potionsteal.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.*;

public class PotionStealTabCompleter implements TabCompleter {
    private static final List<String> BASIC_COMMANDS = Collections.singletonList("balance");
    private static final List<String> ADMIN_COMMANDS = Arrays.asList("setbalance", "giveelixir", "immunity", "reload", "revive");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        boolean isAdmin = sender.hasPermission("potionsteal.admin");

        if (args.length == 1) {
            completions.addAll(BASIC_COMMANDS);
            if (isAdmin) completions.addAll(ADMIN_COMMANDS);
            completions.removeIf(s -> !s.startsWith(args[0].toLowerCase()));
            return completions;
        }

        String sub = args[0].toLowerCase();
        if (sub.equals("balance")) {
            if (args.length == 2) {
                for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) completions.add(p.getName());
                completions.removeIf(s -> !s.toLowerCase().startsWith(args[1].toLowerCase()));
                return completions;
            }
        }
        if (isAdmin) {
            if (sub.equals("setbalance") || sub.equals("giveelixir") || sub.equals("immunity") || sub.equals("revive")) {
                if (args.length == 2) {
                    for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) completions.add(p.getName());
                    completions.removeIf(s -> !s.toLowerCase().startsWith(args[1].toLowerCase()));
                    return completions;
                }
            }
            if (sub.equals("setbalance") && args.length == 3) {
                completions.add("<value>");
                return completions;
            }
            if (sub.equals("giveelixir") && args.length == 3) {
                completions.add("<amount>");
                return completions;
            }
            if (sub.equals("immunity") && args.length == 3) {
                completions.add("on");
                completions.add("off");
                completions.removeIf(s -> !s.startsWith(args[2].toLowerCase()));
                return completions;
            }
        }
        return Collections.emptyList();
    }
}