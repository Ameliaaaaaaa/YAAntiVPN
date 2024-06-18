package me.amelia.yaantivpn.Shared.Providers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public abstract class ConfigProvider {
    protected final File dataFolder;

    protected final File configFile;

    protected String apiKey;

    protected HashMap<String, BlockProvider> blocks = new HashMap<>();

    protected BlacklistProvider countryBlacklist;

    protected BlacklistProvider ispBlacklist;

    protected List<String> whitelist;

    public ConfigProvider(File dataFolder, File configFile) {
        this.dataFolder = dataFolder;
        this.configFile = configFile;
    }

    public abstract void load() throws IOException;

    public abstract void unload();

    public void reload() throws IOException {
        unload();
        load();
    }

    public File getDataFolder() {
        return this.dataFolder;
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public HashMap<String, BlockProvider> getBlocks() {
        return this.blocks;
    }

    public BlacklistProvider getCountryBlacklist() {
        return this.countryBlacklist;
    }

    public BlacklistProvider getIspBlacklist() {
        return this.ispBlacklist;
    }

    public List<String> getWhitelist() {
        return this.whitelist;
    }
}