package me.amelia.yaantivpn.Bukkit;

import me.amelia.yaantivpn.Shared.Providers.BlacklistProvider;
import me.amelia.yaantivpn.Shared.Providers.BlockProvider;
import me.amelia.yaantivpn.Shared.Providers.ConfigProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BukkitConfig extends ConfigProvider {
    private static final Bukkit bukkit = Bukkit.getInstance();

    private static final File dataFolder = bukkit.getDataFolder();

    private static final File configFile = new File(dataFolder, "config.yml");

    private FileConfiguration loadedConfig;

    public BukkitConfig() {
        super(dataFolder, configFile);

        try {
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() throws IOException {
        if (!dataFolder.exists()) dataFolder.mkdirs();

        if (!configFile.exists()) {
            try {
                InputStream inputStream = bukkit.getResource("config.yml");

                Files.copy(inputStream, configFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.loadedConfig = YamlConfiguration.loadConfiguration(configFile);

        this.apiKey = this.loadedConfig.getString("APIKey");

        this.blocks.put("BUSINESS", new BlockProvider("BUSINESS", this.loadedConfig.getBoolean("Block.BUSINESS.Enabled"), this.loadedConfig.getStringList("Block.BUSINESS.Message"), this.loadedConfig.getStringList("Block.BUSINESS.Alert")));
        this.blocks.put("HOSTING", new BlockProvider("HOSTING", this.loadedConfig.getBoolean("Block.HOSTING.Enabled"), this.loadedConfig.getStringList("Block.HOSTING.Message"), this.loadedConfig.getStringList("Block.HOSTING.Alert")));
        this.blocks.put("VPN", new BlockProvider("VPN", this.loadedConfig.getBoolean("Block.VPN.Enabled"), this.loadedConfig.getStringList("Block.VPN.Message"), this.loadedConfig.getStringList("Block.VPN.Alert")));
        this.blocks.put("CLOUD_GAMING", new BlockProvider("CLOUD_GAMING", this.loadedConfig.getBoolean("Block.CLOUD_GAMING.Enabled"), this.loadedConfig.getStringList("Block.CLOUD_GAMING.Message"), this.loadedConfig.getStringList("Block.CLOUD_GAMING.Alert")));

        this.countryBlacklist = new BlacklistProvider("CountryBlacklist", this.loadedConfig.getStringList("CountryBlacklist.Countries"), this.loadedConfig.getStringList("CountryBlacklist.Message"), this.loadedConfig.getStringList("CountryBlacklist.Alert"));
        this.ispBlacklist = new BlacklistProvider("ISPBlacklist", this.loadedConfig.getStringList("ISPBlacklist.ISPs"), this.loadedConfig.getStringList("ISPBlacklist.Message"), this.loadedConfig.getStringList("ISPBlacklist.Alert"));

        this.whitelist = this.loadedConfig.getStringList("Whitelist");
    }

    public void unload() {
        this.loadedConfig = null;
    }
}