package cc.dreamcode.spawnplugin.config;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Location;

public class SpawnConfig extends OkaeriConfig {

    @Comment
    public BukkitNotice teleportMessage = new BukkitNotice(MinecraftNoticeType.ACTION_BAR, "&aTeleportacja za: %time%");

    @Comment
    public BukkitNotice successMessage = new BukkitNotice(MinecraftNoticeType.SUBTITLE, "&aTwoja teleportacja dobiegła końca!");

    @Comment
    public BukkitNotice moveMessage = new BukkitNotice(MinecraftNoticeType.SUBTITLE, "&cTwoja teleportacja została przerwana!");

    @Comment("Czas teleportacji w sekundach.")
    public int teleportTime = 5;

    @Comment("Lokalizacja spawna")
    public String locationWorld = "world";

    @Comment
    public double locationX = 0;

    @Comment
    public double locationY = 0;

    @Comment
    public double locationZ = 0;

    public void setSpawnLocation(Location location) {
        this.locationX = location.getX();
        this.locationY = location.getY();
        this.locationZ = location.getZ();
    }
}
