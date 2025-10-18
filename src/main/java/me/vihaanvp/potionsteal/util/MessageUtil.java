package me.vihaanvp.potionsteal.util;

import org.bukkit.ChatColor;

public class MessageUtil {
    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}