package cc.dreamcode.spawnplugin.config;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.util.Arrays;
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
}
