package me.amelia.yaantivpn.Bukkit;

import co.aikar.commands.BukkitCommandManager;
import me.amelia.yaantivpn.Bukkit.Commands.LookupCommand;
import me.amelia.yaantivpn.Bukkit.Listeners.PlayerLoginListener;
import me.amelia.yaantivpn.Shared.API;
import me.amelia.yaantivpn.Shared.UnifiedLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bukkit extends JavaPlugin {
    private static Bukkit instance;

    private BukkitConfig bukkitConfig;

    private BukkitCommandManager commandManager;

    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private UnifiedLogger unifiedLogger;

    private API api;

    public void onEnable() {
        instance = this;

        this.bukkitConfig = new BukkitConfig();

        this.unifiedLogger = new UnifiedLogger(this);

        this.api = new API(this.getUnifiedLogger(), this.getBukkitConfig());

        this.commandManager = new BukkitCommandManager(this);

        this.commandManager.registerCommand(new LookupCommand());

        getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);

        new Metrics(this, 20651);
    }

    public void onDisable() {
        this.commandManager.unregisterCommands();
        this.threadPool.shutdown();
    }

    public static Bukkit getInstance() {
        return instance;
    }

    public BukkitConfig getBukkitConfig() {
        return this.bukkitConfig;
    }

    public BukkitCommandManager getCommandManager() {
        return this.commandManager;
    }

    public ExecutorService getThreadPool() {
        return this.threadPool;
    }

    public UnifiedLogger getUnifiedLogger() {
        return this.unifiedLogger;
    }

    public API getAPI() {
        return  this.api;
    }
}