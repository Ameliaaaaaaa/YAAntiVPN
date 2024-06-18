package me.amelia.yaantivpn.Bungee.Listeners;

import me.amelia.yaantivpn.Bungee.Bungee;
import me.amelia.yaantivpn.Shared.API;
import me.amelia.yaantivpn.Shared.Blocks.BusinessBlock;
import me.amelia.yaantivpn.Shared.Blocks.CloudGamingBlock;
import me.amelia.yaantivpn.Shared.Blocks.HostingBlock;
import me.amelia.yaantivpn.Shared.Blocks.VPNBlock;
import me.amelia.yaantivpn.Shared.Providers.BlacklistProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PostLoginListener implements Listener {
    private final Bungee bungee = Bungee.getInstance();

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        this.bungee.getThreadPool().execute(() -> {
            ProxiedPlayer player = event.getPlayer();

            UUID uuid = player.getUniqueId();

            if (this.bungee.getBungeeConfig().getWhitelist().contains(String.valueOf(uuid))) return;

            JSONObject ipData;

            try {
                ipData = this.bungee.getApi().checkIp(event.getPlayer().getAddress().getAddress().getHostAddress());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            BusinessBlock businessBlock = new BusinessBlock(ipData, this.bungee.getBungeeConfig());

            if (businessBlock.blockAddress()) {
                businessBlock.kickPlayer(event);
                businessBlock.alertStaff(event, this.bungee.getProxy().getPlayers());

                return;
            }

            CloudGamingBlock cloudGamingBlock = new CloudGamingBlock(ipData, this.bungee.getBungeeConfig());

            if (cloudGamingBlock.blockAddress()) {
                businessBlock.kickPlayer(event);
                businessBlock.alertStaff(event, this.bungee.getProxy().getPlayers());

                return;
            }

            HostingBlock hostingBlock = new HostingBlock(ipData, this.bungee.getBungeeConfig());

            if (hostingBlock.blockAddress()) {
                businessBlock.kickPlayer(event);
                businessBlock.alertStaff(event, this.bungee.getProxy().getPlayers());

                return;
            }

            VPNBlock vpnBlock = new VPNBlock(ipData, this.bungee.getBungeeConfig());

            if (vpnBlock.blockAddress()) {
                businessBlock.kickPlayer(event);
                businessBlock.alertStaff(event, this.bungee.getProxy().getPlayers());

                return;
            }

            BlacklistProvider countryBlacklist = this.bungee.getBungeeConfig().getCountryBlacklist();

            if (countryBlacklist.getList().contains(ipData.getString("country"))) {
                List<String> list = countryBlacklist.getMessage();

                String msg = String.join("\n", list);

                msg = msg.replace("{COUNTRY}", ipData.getString("country"));

                player.disconnect(ChatColor.translateAlternateColorCodes('&', msg));

                for (ProxiedPlayer online : this.bungee.getProxy().getPlayers()) {
                    if (!online.hasPermission("yaantivpn.notify")) continue;

                    for (String line : countryBlacklist.getAlert()) {
                        String notifString = ChatColor.translateAlternateColorCodes('&', line);

                        notifString = notifString.replace("{USERNAME}", player.getName());
                        notifString = notifString.replace("{COUNTRY}", ipData.getString("country"));

                        online.sendMessage(notifString);
                    }
                }
                return;
            }

            BlacklistProvider ispBlacklist = this.bungee.getBungeeConfig().getIspBlacklist();

            if (ispBlacklist.getList().contains(ipData.getString("isp"))) {
                List<String> list = ispBlacklist.getMessage();

                String msg = String.join("\n", list);

                msg = msg.replace("{ISP}", ipData.getString("isp"));

                player.disconnect(ChatColor.translateAlternateColorCodes('&', msg));

                for (ProxiedPlayer online : this.bungee.getProxy().getPlayers()) {
                    if (!online.hasPermission("yaantivpn.notify")) continue;

                    for (String line : ispBlacklist.getAlert()) {
                        String notifString = ChatColor.translateAlternateColorCodes('&', line);

                        notifString = notifString.replace("{USERNAME}", player.getName());
                        notifString = notifString.replace("{ISP}", ipData.getString("isp"));

                        online.sendMessage(notifString);
                    }
                }
            }
        });
    }
}