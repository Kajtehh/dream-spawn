package cc.dreamcode.spawnplugin.helper;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TeleportEffect extends OkaeriConfig {

    @Getter
    private final PotionEffect potionEffect;

    public TeleportEffect(PotionEffectType effectType, int amplifier) {
        potionEffect = new PotionEffect(effectType, Integer.MAX_VALUE, amplifier, false, false);
    }
}
