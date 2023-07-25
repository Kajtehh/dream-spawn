package cc.dreamcode.spawnplugin.config;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnConfig extends OkaeriConfig {

    @Comment("Wiadomość, która ma być wyświetlana podczas teleportacji.")
    public List<BukkitNotice> teleportMessage = Collections.singletonList(
            new BukkitNotice(MinecraftNoticeType.TITLE_SUBTITLE, "&aTrwa teleportacja... %NEWLINE% &f{time}")
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
    public List<PotionEffectType> teleportEffects = Collections.singletonList(
            PotionEffectType.BLINDNESS
    );

    @Comment("Dźwięki, które będą odtwarzane podczas teleportacji.")
    public List<Sound> teleportSounds = Collections.singletonList(Sound.BLOCK_BAMBOO_STEP);

    @Comment("Wsparcie dla WorldGuard.")
    public boolean regionCooldownEnabled = false;

    @Comment("Cooldown teleportacji danego regionu WorldGuard.")
    public List<Map<String, Integer>> regionCooldowns = Collections.singletonList(
            new HashMap<String, Integer>(){{ put("spawn", 0); }}
    );

    @Comment("Cooldown teleportacji dla danej rangi Luckperms.")
    public List<Map<String, Integer>> groupCooldowns = Collections.singletonList(
            new HashMap<String, Integer>(){{ put("vip", 3); }}
    );

    @Comment("Po włączeniu gracz będzie teleportowany na spawna po śmierci oraz nie będzie musiał klikać przycisku respawn.")
    public boolean teleportAfterDeath = true;

    @Comment("Po włączeniu gracz będzie teleportowany na spawna podczas każdego wejścia na serwer.")
    public boolean teleportAfterJoin = false;

    @Comment("Po włączeniu gracz będzie teleportowany na spawna po swoim pierwszym wejściu na serwer.")
    public boolean teleportAfterFirstJoin = true;

    @Comment("Permisja, która pozwala na teleportowanie się na spawna bez cooldownu.")
    public String bypassPermission = "dream-spawn.bypass";

    @Comment("Permisja administratora do użycia komendy: /spawnplugin reload.")
    public String adminPermission = "dream-spawn.admin";
}
