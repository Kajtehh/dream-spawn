package cc.dreamcode.spawnplugin;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import cc.dreamcode.spawnplugin.config.PluginConfig;
import cc.dreamcode.utilities.TimeUtil;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnManager {

    private final SpawnPlugin plugin;
    private final PluginConfig config;
    private final Map<UUID, Boolean> teleport = new HashMap<>();

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
            long elapsedTime = currentTime - startTime;
            long remainingTime = time - elapsedTime;

            if (remainingTime <= 0) {
                player.teleport(spawnLocation);
                teleport.remove(player.getUniqueId());
                config.spawnConfig.successMessage.send(player);
                Bukkit.getScheduler().cancelTasks(plugin);
                return;
            }

            String formattedTime = TimeUtil.convertSeconds(TimeUnit.MILLISECONDS.toSeconds(remainingTime) + 1);

            config.spawnConfig.teleportMessage.forEach(message -> {
                MinecraftNoticeType noticeType = message.getType();
                String messageContent = message.getText().replace("%time%", formattedTime);

                new BukkitNotice(noticeType, messageContent).send(player);
            });
        }, 0, 20L);
    }

    public void setSpawnLocation(Location location) {
        this.config.spawnConfig.locationX = location.getX();
        this.config.spawnConfig.locationY = location.getY();
        this.config.spawnConfig.locationZ = location.getZ();

        this.config.save();
    }

    public boolean isPlayerTeleporting(Player player) {
        return teleport.containsKey(player.getUniqueId());
    }

    public boolean isPlayerMoved(Player player, Location oldLocation) {
        Location playerLocation = player.getLocation();

        return playerLocation.getBlockX() != oldLocation.getBlockX()
                || playerLocation.getBlockY() != oldLocation.getBlockY()
                || playerLocation.getBlockZ() != oldLocation.getBlockZ();
    }
}
