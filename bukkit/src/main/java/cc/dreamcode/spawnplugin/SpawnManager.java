package cc.dreamcode.spawnplugin;

import cc.dreamcode.spawnplugin.config.PluginConfig;
import cc.dreamcode.spawnplugin.config.SpawnConfig;
import cc.dreamcode.spawnplugin.hook.PluginHookManager;
import cc.dreamcode.spawnplugin.hook.worldguard.WorldGuardHook;
import cc.dreamcode.utilities.TimeUtil;
import cc.dreamcode.utilities.builder.MapBuilder;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
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

        this.time = TimeUnit.SECONDS.toMillis(config.spawnConfig.teleportTime);

        SpawnConfig spawnConfig = config.spawnConfig;

        spawnConfig.groupCooldowns.forEach(groupCooldown -> {
            for(String key: groupCooldown.keySet()) {
                if(isPlayerInGroup(player, key)) {
                    this.time = TimeUnit.SECONDS.toMillis(groupCooldown.get(key));
                }
            }
        });

        Plugin worldGuard = plugin.getServer().getPluginManager().getPlugin("WorldGuard");

        if (worldGuard != null && spawnConfig.regionCooldownEnabled) {
            this.pluginHookManager.get(WorldGuardHook.class).ifPresent(worldGuardHook -> spawnConfig.regionCooldowns.forEach(regionCooldown -> {
                for (String key : regionCooldown.keySet()) {
                    ApplicableRegionSet playerRegions = worldGuardHook.getRegions(player.getLocation());
                    if (playerRegions.getRegions().stream().anyMatch(playerRegion -> playerRegion.getId().equals(key))) {
                        this.time = TimeUnit.SECONDS.toMillis(regionCooldown.get(key));
                    }
                }
            }));

        }

        long startTime = System.currentTimeMillis();

        addEffects(player);

        teleport.put(player.getUniqueId(), true);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> { // Task
            if(!player.isOnline()) {
                teleport.remove(player.getUniqueId());
                removeEffects(player);
                Bukkit.getScheduler().cancelTasks(plugin);
                return;
            }
            if (isPlayerMoved(player, playerOldLocation)) {
                spawnConfig.moveMessage.send(player);
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
                spawnConfig.successMessage.send(player);
                removeEffects(player);
                Bukkit.getScheduler().cancelTasks(plugin);
                return;
            }

            String formattedTime = TimeUtil.convertSeconds(TimeUnit.MILLISECONDS.toSeconds(remainingTime) + 1);

            spawnConfig.teleportMessage.forEach(bukkitNotice -> bukkitNotice.send(player, new MapBuilder<String, Object>()
                    .put("time", formattedTime).build()));
            spawnConfig.teleportSounds.forEach(sound -> {
                player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
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

        try {
            this.config.save();
        } catch (OkaeriException e) {
            e.printStackTrace();
        }
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
            player.addPotionEffect(effect.createEffect(Integer.MAX_VALUE, 1));
        });
    }

    private void removeEffects(Player player) {
        config.spawnConfig.teleportEffects.forEach(player::removePotionEffect);
    }

    private boolean isPlayerInGroup(Player player, String group) {
        return player.hasPermission("group." + group);
    }
}
