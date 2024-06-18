package me.amelia.yaantivpn.Bukkit.Listeners;

import me.amelia.yaantivpn.Bukkit.Bukkit;
import me.amelia.yaantivpn.Shared.API;
import me.amelia.yaantivpn.Shared.Blocks.BusinessBlock;
import me.amelia.yaantivpn.Shared.Blocks.CloudGamingBlock;
import me.amelia.yaantivpn.Shared.Blocks.HostingBlock;
import me.amelia.yaantivpn.Shared.Blocks.VPNBlock;
import me.amelia.yaantivpn.Shared.Providers.BlacklistProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PlayerLoginListener implements Listener {
    private final Bukkit bukkit = Bukkit.getInstance();

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        String player = event.getName();

        UUID uuid = event.getUniqueId();

        if (this.bukkit.getBukkitConfig().getWhitelist().contains(String.valueOf(uuid))) return;

        JSONObject ipData;

        try {
            ipData = this.bukkit.getAPI().checkIp(event.getAddress().getHostAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BusinessBlock businessBlock = new BusinessBlock(ipData, this.bukkit.getBukkitConfig());

        if (businessBlock.blockAddress()) {
            businessBlock.kickPlayer(event);
            businessBlock.alertStaff(event, this.bukkit.getServer().getOnlinePlayers());

            return;
        }

        CloudGamingBlock cloudGamingBlock = new CloudGamingBlock(ipData, this.bukkit.getBukkitConfig());

        if (cloudGamingBlock.blockAddress()) {
            businessBlock.kickPlayer(event);
            businessBlock.alertStaff(event, this.bukkit.getServer().getOnlinePlayers());

            return;
        }

        HostingBlock hostingBlock = new HostingBlock(ipData, this.bukkit.getBukkitConfig());

        if (hostingBlock.blockAddress()) {
            businessBlock.kickPlayer(event);
            businessBlock.alertStaff(event, this.bukkit.getServer().getOnlinePlayers());

            return;
        }

        VPNBlock vpnBlock = new VPNBlock(ipData, this.bukkit.getBukkitConfig());

        if (vpnBlock.blockAddress()) {
            businessBlock.kickPlayer(event);
            businessBlock.alertStaff(event, this.bukkit.getServer().getOnlinePlayers());

            return;
        }

        BlacklistProvider countryBlacklist = this.bukkit.getBukkitConfig().getCountryBlacklist();

        if (countryBlacklist.getList().contains(ipData.getString("country"))) {
            List<String> list = countryBlacklist.getMessage();

            String msg = String.join("\n", list);

            msg = msg.replace("{COUNTRY}", ipData.getString("country"));

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', msg));

            for (Player online : this.bukkit.getServer().getOnlinePlayers()) {
                if (!online.hasPermission("yaantivpn.notify")) continue;

                for (String line : countryBlacklist.getAlert()) {
                    String notifString = ChatColor.translateAlternateColorCodes('&', line);

                    notifString = notifString.replace("{USERNAME}", player);
                    notifString = notifString.replace("{COUNTRY}", ipData.getString("country"));

                    online.sendMessage(notifString);
                }
            }
            return;
        }

        BlacklistProvider ispBlacklist = this.bukkit.getBukkitConfig().getIspBlacklist();

        if (ispBlacklist.getList().contains(ipData.getString("isp"))) {
            List<String> list = ispBlacklist.getMessage();

            String msg = String.join("\n", list);

            msg = msg.replace("{ISP}", ipData.getString("isp"));

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', msg));

            for (Player online : this.bukkit.getServer().getOnlinePlayers()) {
                if (!online.hasPermission("yaantivpn.notify")) continue;

                for (String line : ispBlacklist.getAlert()) {
                    String notifString = ChatColor.translateAlternateColorCodes('&', line);

                    notifString = notifString.replace("{USERNAME}", player);
                    notifString = notifString.replace("{ISP}", ipData.getString("isp"));

                    online.sendMessage(notifString);
                }
            }
        }
    }
}