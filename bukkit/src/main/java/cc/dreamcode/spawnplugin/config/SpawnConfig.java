package cc.dreamcode.spawnplugin.config;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import cc.dreamcode.spawnplugin.helper.RegionCooldown;
import cc.dreamcode.spawnplugin.helper.TeleportEffect;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpawnConfig extends OkaeriConfig {

    @Comment("Wiadomość, która ma być wyświetlana podczas teleportacji.")
    public List<BukkitNotice> teleportMessage = Arrays.asList(
            new BukkitNotice(MinecraftNoticeType.SUBTITLE, "&aTrwa teleportacja..."),
            new BukkitNotice(MinecraftNoticeType.ACTION_BAR, "&aZostaniesz przeteleportowany za: &f%time%")
    );

    @Comment("Wiadomość, która ma zostać wysłana gdy gracz zostanie przeteleportowany na spawna.")
    public BukkitNotice successMessage = new BukkitNotice(MinecraftNoticeType.SUBTITLE, "&aTwoja teleportacja dobiegła końca!");

    @Comment("Wiadomość, która jest wysyłana gdy gracz się ruszy podczas teleportacji.")
    public BukkitNotice moveMessage = new BukkitNotice(MinecraftNoticeType.SUBTITLE, "&cTwoja teleportacja została przerwana!");

    @Comment("Czas teleportacji w sekundach.")
    public int teleportTime = 5;

    @Comment("Świat na którym znajduję się spawn.")
    public String locationWorld = "world";

    @Comment("Koordynaty X spawna.")
    public double locationX = 0;

    @Comment("Koordynaty Y spawna.")
    public double locationY = 0;

    @Comment("Koordynaty Z spawna.")
    public double locationZ = 0;

    @Comment("Efekty, które będą nadawane podczas teleportacji.")
    public List<TeleportEffect> teleportEffects = Collections.singletonList(
            new TeleportEffect(PotionEffectType.BLINDNESS, 1)
    );

    @Comment("Dźwięki, które będą odtwarzane podczas teleportacji.")
    public List<Sound> teleportSounds = Collections.singletonList(Sound.BLOCK_BAMBOO_STEP);

    @Comment("Wsparcie dla WorldGuard.")
    public boolean regionCooldownEnabled = false;

    @Comment("Cooldown teleportacji danego regionu WorldGuard.")
    public List<RegionCooldown> regionCooldowns = Collections.singletonList(
            new RegionCooldown("spawn", 0)
    );

    @Comment("Po włączeniu gracz będzie teleportowany na spawna po śmierci.")
    public boolean teleportAfterDeath = true;

    @Comment("Po włączeniu gracz będzie teleportowany na spawna podczas każdego wejścia na serwer.")
    public boolean teleportAfterJoin = false;

    @Comment("Po włączeniu gracz będzie teleportowany na spawna po swoim pierwszym wejściu na serwer.")
    public boolean teleportAfterFirstJoin = true;

    @Comment("Permisja, która pozwala na teleportowanie się na spawna bez cooldownu.")
    public String bypassPermission = "dream-spawn.bypass";

    @Comment("Permisja administratora do użycia komendy: /spawn [set/reload].")
    public String adminPermission = "dream-spawn.admin";
}
