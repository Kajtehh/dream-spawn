package cc.dreamcode.spawnplugin;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import cc.dreamcode.spawnplugin.config.PluginConfig;
import cc.dreamcode.spawnplugin.helper.RegionCooldown;
import cc.dreamcode.spawnplugin.hook.PluginHookManager;
import cc.dreamcode.spawnplugin.hook.worldguard.WorldGuardHook;
import cc.dreamcode.utilities.TimeUtil;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnManager {

    private final SpawnPlugin plugin;
    private final PluginConfig config;
    private final PluginHookManager pluginHookManager;
    private final Map<UUID, Boolean> teleport = new HashMap<>();
    private long time;

    public void teleport(Player player) {
        Location spawnLocation = getSpawnLocation();
        Location playerOldLocation = player.getLocation();

        WorldGuardHook worldGuardHook = this.pluginHookManager.get(WorldGuardHook.class).get();

        ApplicableRegionSet playerRegions = worldGuardHook.getRegions(player.getLocation());

        this.time = TimeUnit.SECONDS.toMillis(config.spawnConfig.teleportTime);

        if(config.spawnConfig.regionCooldownEnabled) {
            Optional<RegionCooldown> optionalRegionCooldown = this.config.spawnConfig.regionCooldowns.stream()
                    .filter(region -> playerRegions.getRegions()
                            .stream().anyMatch(playerRegion -> playerRegion.getId().equals(region.getRegionName())))
                    .findFirst();

            if (optionalRegionCooldown.isPresent()) {
                RegionCooldown regionCooldown = optionalRegionCooldown.get();
                long cooldown = regionCooldown.getCooldown();
                this.time = TimeUnit.SECONDS.toMillis(cooldown);
            }
        }

        long startTime = System.currentTimeMillis();

        addEffects(player);

        config.spawnConfig.teleportSounds.forEach(sound -> {
            player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
        });

        teleport.put(player.getUniqueId(), true);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> { // Task
            if(!player.isOnline()) {
                teleport.remove(player.getUniqueId());
                Bukkit.getScheduler().cancelTasks(plugin);
                return;
            }
            if (isPlayerMoved(player, playerOldLocation)) {
                config.spawnConfig.moveMessage.send(player);
                teleport.remove(player.getUniqueId());
                removeEffects(player);
                Bukkit.getScheduler().cancelTasks(plugin);
                return;
            }

            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            long remainingTime = this.time - elapsedTime;

            if (remainingTime <= 0) {
                player.teleport(spawnLocation);
                teleport.remove(player.getUniqueId());
                config.spawnConfig.successMessage.send(player);
                removeEffects(player);
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

    public Location getSpawnLocation() {
        return new Location(
                Bukkit.getWorld(config.spawnConfig.locationWorld),
                config.spawnConfig.locationX,
                config.spawnConfig.locationY,
                config.spawnConfig.locationZ
        );
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

    private void addEffects(Player player) {
        config.spawnConfig.teleportEffects.forEach(effect -> {
            player.addPotionEffect(effect.getPotionEffect());
        });
    }

    private void removeEffects(Player player) {
        config.spawnConfig.teleportEffects.forEach(effect -> {
           player.removePotionEffect(effect.getPotionEffect().getType());
        });
    }
}
