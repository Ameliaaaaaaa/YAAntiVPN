package me.amelia.yaantivpn.Shared;

import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class UnifiedLogger {
    private final Logger logger;

    public UnifiedLogger(JavaPlugin spigotPlugin) {
        this.logger = spigotPlugin.getLogger();
    }

    public UnifiedLogger(Plugin bungeePlugin) {
        this.logger = bungeePlugin.getLogger();
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warning(String message) {
        logger.warning(message);
    }

    public void severe(String message) {
        logger.severe(message);
    }
}