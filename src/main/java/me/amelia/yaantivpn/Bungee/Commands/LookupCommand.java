package me.amelia.yaantivpn.Bungee.Commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.amelia.yaantivpn.Bungee.Bungee;
import me.amelia.yaantivpn.Shared.API;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

@CommandAlias("yaantivpn")
@CommandPermission("yaantivpn.admin")
public class LookupCommand extends BaseCommand {
    private final Bungee bungee = Bungee.getInstance();

    @Default()
    @Subcommand("lookup")
    @Syntax("[player]")
    @CommandCompletion("@yaantivpnlookup")
    @Description("Fetch IP information for a player.")
    public void execute(CommandSender sender, OnlinePlayer target) {
        this.bungee.getThreadPool().execute(() -> {
            Player player = target.getPlayer();

            if (player != null && player.isOnline()) {
                JSONObject ipData;

                try {
                    ipData = this.bungee.getApi().checkIp(player.getAddress().getAddress().getHostAddress());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                ArrayList<String> list = new ArrayList<>();

                list.add("&8---------------- &dYAAntiVPN &8----------------");
                list.add(String.format("&5%s's Information", player.getName()));
                list.add("");
                list.add(String.format("&5Address: &r&f%s", ipData.getString("ip")));
                list.add(String.format("&5Business: &r&f%s", ipData.getJSONArray("types").toList().contains(Integer.valueOf(2)) ? "Yes" : "No"));
                list.add(String.format("&5Cloud Gaming: &r&f%s", ipData.getJSONArray("types").toList().contains(Integer.valueOf(7)) ? "Yes" : "No"));
                list.add(String.format("&5Hosting: &r&f%s", ipData.getJSONArray("types").toList().contains(Integer.valueOf(3)) ? "Yes" : "No"));
                list.add(String.format("&5VPN: &r&f%s", ipData.getJSONArray("types").toList().contains(Integer.valueOf(4)) ? "Yes" : "No"));
                list.add(String.format("&5Country: &r&f%s", ipData.getString("country")));
                list.add(String.format("&5ISP: &r&f%s", ipData.getString("isp")));
                list.add("&8---------------- &dYAAntiVPN &8----------------");

                for (String line : list) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayer not online."));
            }
        });
    }
}