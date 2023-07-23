package cc.dreamcode.spawnplugin.helper;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;

public class RegionCooldown extends OkaeriConfig {

    @Getter private final String regionName;
    @Getter private final int cooldown;

    public RegionCooldown(String regionName, int cooldown) {
        this.regionName = regionName;
        this.cooldown = cooldown;
    }
}
