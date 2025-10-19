package me.vihaanvp.potionsteal;

import me.vihaanvp.potionsteal.command.CommandManager;
import me.vihaanvp.potionsteal.data.PlayerDataManager;
import me.vihaanvp.potionsteal.effect.PotionEffectManager;
import me.vihaanvp.potionsteal.item.ElixirManager;
import me.vihaanvp.potionsteal.item.ReviveBookManager;
import me.vihaanvp.potionsteal.listener.PlayerDeathListener;
import me.vihaanvp.potionsteal.listener.PlayerJoinListener;
import me.vihaanvp.potionsteal.listener.PlayerRespawnListener;
import me.vihaanvp.potionsteal.listener.ElixirUseListener;
import me.vihaanvp.potionsteal.tab.TabListManager;
import me.vihaanvp.potionsteal.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PotionSteal extends JavaPlugin {
    private static PotionSteal instance;
    private String pluginDisplayName;
    private PlayerDataManager dataManager;
    private PotionEffectManager effectManager;
    private ElixirManager elixirManager;
    private TabListManager tabListManager;
    private ScoreboardManager scoreboardManager;
    private ReviveBookManager reviveBookManager;

    public static PotionSteal getInstance() { return instance; }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (!getConfig().getBoolean("enabled", true)) {
            getLogger().warning("PotionSteal is disabled in config.yml. Plugin will not load.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        instance = this;
        pluginDisplayName = getConfig().getString("pluginDisplayName", "PotionSteal");

        // Initialize managers
        dataManager = new PlayerDataManager(this);
        effectManager = new PotionEffectManager(this, dataManager);
        elixirManager = new ElixirManager(this);
        tabListManager = new TabListManager(this, dataManager);
        scoreboardManager = new ScoreboardManager(this, dataManager);
        reviveBookManager = new ReviveBookManager(this, dataManager, elixirManager);

        // Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this, dataManager, effectManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, effectManager, dataManager), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this, effectManager, dataManager), this);
        getServer().getPluginManager().registerEvents(new ElixirUseListener(this, dataManager, effectManager, elixirManager), this);
        getServer().getPluginManager().registerEvents(reviveBookManager, this);

        // Register commands
        CommandManager cmdManager = new CommandManager(this);
        getCommand("potionsteal").setExecutor(cmdManager);
        getCommand("potionsteal").setTabCompleter(cmdManager);

        // Register custom recipes
        elixirManager.registerElixirRecipe();
        reviveBookManager.registerReviveBookRecipe();

        // Tab list and scoreboard updater (every 10 seconds)
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            tabListManager.updateTabList();
            for (Player p : Bukkit.getOnlinePlayers()) {
                scoreboardManager.updateScoreboard(p);
            }
        }, 40L, 200L);

        // Potion effect updater (every 10 seconds)
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                effectManager.updatePlayer(p);
            }
        }, 0L, 200L); // 200 ticks = 10 seconds
    }

    @Override
    public void onDisable() {
        getLogger().info(pluginDisplayName + " disabled!");
    }

    public String getPluginDisplayName() { return pluginDisplayName; }
    public void reloadPluginConfig() {
        reloadConfig();
        pluginDisplayName = getConfig().getString("pluginDisplayName", "PotionSteal");
        dataManager.reload();
    }
    public PlayerDataManager getDataManager() { return dataManager; }
    public PotionEffectManager getEffectManager() { return effectManager; }
    public ElixirManager getElixirManager() { return elixirManager; }
    public TabListManager getTabListManager() { return tabListManager; }
    public ScoreboardManager getScoreboardManager() { return scoreboardManager; }
    public ReviveBookManager getReviveBookManager() { return reviveBookManager; }
}