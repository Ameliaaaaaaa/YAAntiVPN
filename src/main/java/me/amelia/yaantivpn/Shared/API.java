package me.amelia.yaantivpn.Shared;

import me.amelia.yaantivpn.Shared.Providers.ConfigProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class API {
    private final UnifiedLogger logger;

    private final ConfigProvider config;

    public API (UnifiedLogger logger, ConfigProvider config) {
        this.logger = logger;
        this.config = config;

        if (this.config.getApiKey() == null || this.config.getApiKey().isEmpty()) this.logger.warning("No API key provided. Please join our Discord (https://discord.gg/HUfEpGje83) to obtain one. Using no API key means rate limits are handled by IP address.");
    }

    public JSONObject checkIp(String address) throws IOException {
        URL url = new URL(String.format("https://ipdb.amelia.fun/query?address=%s", address));

        if (this.config.getApiKey() != null && !this.config.getApiKey().isEmpty()) url = new URL(String.format("https://ipdb.amelia.fun/query?key=%s&address=%s", this.config.getApiKey(), address));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();

        JSONObject jsonRes = new JSONObject();

        jsonRes.put("ip", address);
        jsonRes.put("types", new JSONArray().put("1"));
        jsonRes.put("isp", "None");
        jsonRes.put("country", "None");

        if (responseCode == 429) {
            this.logger.warning(String.format("CODE: %s, RESPONSE: %s", responseCode, "Rate limited. Please join our Discord (https://discord.gg/HUfEpGje83) for help."));
        } else if (responseCode == 403) {
            this.logger.warning(String.format("CODE: %s, RESPONSE: %s", responseCode, "Abuse from this IP has been detected or the provided API key is invalid. Please join our Discord (https://discord.gg/HUfEpGje83) for help."));
        } else {
            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }

            scanner.close();

            JSONObject res = new JSONObject(inline).getJSONObject("result");

            JSONObject asn = res.getJSONObject("asn");

            jsonRes = new JSONObject();

            jsonRes.put("ip", address);
            jsonRes.put("types", res.getJSONArray("types"));
            jsonRes.put("isp", asn.getString("description"));
            jsonRes.put("country", asn.getString("country"));

            return jsonRes;
        }

        return jsonRes;
    }
}