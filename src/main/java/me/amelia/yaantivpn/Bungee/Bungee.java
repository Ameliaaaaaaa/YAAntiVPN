package me.amelia.yaantivpn.Bungee;

import co.aikar.commands.BungeeCommandManager;
import me.amelia.yaantivpn.Bungee.Commands.LookupCommand;
import me.amelia.yaantivpn.Bungee.Listeners.PostLoginListener;
import me.amelia.yaantivpn.Shared.API;
import me.amelia.yaantivpn.Shared.UnifiedLogger;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bungee extends Plugin {
    private static Bungee instance;

    private BungeeConfig bungeeConfig;

    private BungeeCommandManager commandManager;

    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private UnifiedLogger unifiedLogger;

    private API api;

    public void onEnable() {
        instance = this;

        this.bungeeConfig = new BungeeConfig();

        this.unifiedLogger = new UnifiedLogger(this);

        this.api = new API(this.getUnifiedLogger(), this.getBungeeConfig());

        this.commandManager = new BungeeCommandManager(this);

        this.commandManager.registerCommand(new LookupCommand());

        getProxy().getPluginManager().registerListener(this, new PostLoginListener());

        new Metrics(this, 20651);
    }

    public void onDisable() {
        this.commandManager.unregisterCommands();
        this.threadPool.shutdown();
    }

    public static Bungee getInstance() {
        return instance;
    }

    public BungeeConfig getBungeeConfig() {
        return this.bungeeConfig;
    }

    public BungeeCommandManager getCommandManager() {
        return this.commandManager;
    }

    public ExecutorService getThreadPool() {
        return this.threadPool;
    }

    public UnifiedLogger getUnifiedLogger() {
        return this.unifiedLogger;
    }

    public API getApi() {
        return this.api;
    }
}