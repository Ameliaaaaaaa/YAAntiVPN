package me.amelia.yaantivpn.Shared.Providers;

import java.util.List;

public class BlacklistProvider {
    private final String blacklistType;

    private final List<String> list;

    private final List<String> message;

    private final List<String> alert;

    public BlacklistProvider(String blacklistType, List<String> list, List<String> message, List<String> alert) {
        this.blacklistType = blacklistType;
        this.list = list;
        this.message = message;
        this.alert = alert;
    }

    public String getBlacklistType() {
        return this.blacklistType;
    }

    public List<String> getList() {
        return this.list;
    }

    public List<String> getMessage() {
        return this.message;
    }

    public List<String> getAlert() {
        return this.alert;
    }
}