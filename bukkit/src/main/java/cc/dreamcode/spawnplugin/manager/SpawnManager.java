package cc.dreamcode.spawnplugin.manager;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import cc.dreamcode.spawnplugin.config.PluginConfig;
import cc.dreamcode.utilities.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SpawnManager {

    private final JavaPlugin plugin;
    private final PluginConfig config;
    private final Map<UUID, Boolean> teleport = new HashMap<>();

    public SpawnManager(JavaPlugin plugin, PluginConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void teleport(Player player) {
        Location spawnLocation = new Location(
                Bukkit.getWorld(config.spawnConfig.locationWorld),
                config.spawnConfig.locationX,
                config.spawnConfig.locationY,
                config.spawnConfig.locationZ
        );
        Location playerOldLocation = player.getLocation();

        long startTime = System.currentTimeMillis();
        long time = TimeUnit.SECONDS.toMillis(config.spawnConfig.teleportTime);

        teleport.put(player.getUniqueId(), true);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> { // Task
            if (isPlayerMoved(player, playerOldLocation)) {
                config.spawnConfig.moveMessage.send(player);
                teleport.remove(player.getUniqueId());
                Bukkit.getScheduler().cancelTasks(plugin);
                return;
            }

            long currentTime = System.currentTimeMillis();
            long remainingTime = time - (currentTime - startTime);

            if (remainingTime <= 0) {
                player.teleport(spawnLocation);
                teleport.remove(player.getUniqueId());
                config.spawnConfig.successMessage.send(player);
                Bukkit.getScheduler().cancelTasks(plugin);
                return;
            }

            String formattedTime = TimeUtil.convertMills(System.currentTimeMillis() + remainingTime);
            BukkitNotice spawnTeleportMessage = config.spawnConfig.teleportMessage;
            MinecraftNoticeType noticeType = spawnTeleportMessage.getType();
            String messageContent = spawnTeleportMessage.getText().replace("%time%", formattedTime);

            new BukkitNotice(noticeType, messageContent).send(player);
        }, 0, 10L);
    }

    public boolean isPlayerTeleporting(Player player) {
        return teleport.get(player.getUniqueId());
    }

    public boolean isPlayerMoved(Player player, Location oldLocation) {
        Location playerLocation = player.getLocation();

        return playerLocation.getBlockX() != oldLocation.getBlockX()
                || playerLocation.getBlockY() != oldLocation.getBlockY()
                || playerLocation.getBlockZ() != oldLocation.getBlockZ();
    }
}
