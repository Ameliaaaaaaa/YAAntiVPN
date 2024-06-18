package me.amelia.yaantivpn.Shared.Providers;

import java.util.List;

public class BlockProvider {
    private final String blockType;

    private final boolean enabled;

    private final List<String> message;

    private final List<String> alert;

    public BlockProvider(String blockType, boolean enabled, List<String> message, List<String> alert) {
        this.blockType = blockType;
        this.enabled = enabled;
        this.message = message;
        this.alert = alert;
    }

    public String getBlockType() {
        return this.blockType;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public List<String> getMessage() {
        return this.message;
    }

    public List<String> getAlert() {
        return this.alert;
    }
}