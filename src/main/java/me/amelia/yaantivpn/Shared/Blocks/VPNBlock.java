package me.amelia.yaantivpn.Shared.Blocks;

import me.amelia.yaantivpn.Bukkit.BukkitConfig;
import me.amelia.yaantivpn.Bungee.BungeeConfig;
import me.amelia.yaantivpn.Shared.Providers.BlockProvider;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

public class VPNBlock {
    private final JSONObject ipData;

    private final BlockProvider blockProvider;

    public VPNBlock(JSONObject ipData, BungeeConfig bungeeConfig) {
        this.ipData = ipData;
        this.blockProvider = bungeeConfig.getBlocks().get("VPN");
    }

    public VPNBlock(JSONObject ipData, BukkitConfig bukkitConfig) {
        this.ipData = ipData;
        this.blockProvider = bukkitConfig.getBlocks().get("VPN");
    }

    public boolean blockAddress() {
        return (this.blockProvider.isEnabled() && this.ipData.getJSONArray("types").toList().contains(Integer.valueOf(4)));
    }

    public void kickPlayer(PostLoginEvent event) {
        List<String> list = this.blockProvider.getMessage();

        String msg = String.join("\n", list);

        msg = msg.replace("{IP}", event.getPlayer().getAddress().getAddress().getHostAddress());

        event.getPlayer().disconnect(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void kickPlayer(AsyncPlayerPreLoginEvent event) {
        List<String> list = this.blockProvider.getMessage();

        String msg = String.join("\n", list);

        msg = msg.replace("{IP}", event.getAddress().getHostAddress());

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void alertStaff(PostLoginEvent event, Collection<ProxiedPlayer> players) {
        for (ProxiedPlayer online : players) {
            if (!online.hasPermission("yaantivpn.notify")) continue;

            for (String line : this.blockProvider.getAlert()) {
                String notifString = ChatColor.translateAlternateColorCodes('&', line);

                notifString = notifString.replace("{USERNAME}", event.getPlayer().getName());
                notifString = notifString.replace("{IP}", event.getPlayer().getAddress().getAddress().getHostAddress());

                online.sendMessage(notifString);
            }
        }
    }

    public void alertStaff(AsyncPlayerPreLoginEvent event, Collection players) {
        for (Object online : players) {
            Player plyr = (Player)online;

            if (!plyr.hasPermission("yaantivpn.notify")) continue;

            for (String line : this.blockProvider.getAlert()) {
                String notifString = ChatColor.translateAlternateColorCodes('&', line);

                notifString = notifString.replace("{USERNAME}", event.getName());
                notifString = notifString.replace("{IP}", event.getAddress().getHostAddress());

                plyr.sendMessage(notifString);
            }
        }
    }
}