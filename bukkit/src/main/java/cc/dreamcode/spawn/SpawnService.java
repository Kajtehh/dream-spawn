package cc.dreamcode.spawn;

import cc.dreamcode.spawn.config.MessageConfig;
import cc.dreamcode.spawn.config.PluginConfig;
import cc.dreamcode.spawn.hook.PluginHookManager;
import cc.dreamcode.spawn.hook.worldguard.WorldGuardHook;
import cc.dreamcode.utilities.TimeUtil;
import cc.dreamcode.utilities.builder.MapBuilder;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnService {

    private final PluginConfig config;
    private final SpawnPlugin plugin;
    private final SpawnManager spawnManager;
    private final MessageConfig messageConfig;
    private final PluginHookManager pluginHookManager;
    private long time;

    public void teleport(Player player) {
        Location spawnLocation = spawnManager.getSpawnLocation();
        Location playerOldLocation = player.getLocation();

        this.time = config.teleportTime.toMillis();

        config.groupCooldowns.forEach(groupCooldown -> {
            for(String key : groupCooldown.keySet()) {
                if(isPlayerInGroup(player, key)) {
                    this.time = TimeUnit.SECONDS.toMillis(groupCooldown.get(key));
                }
            }
        });

        if (config.regionCooldownEnabled) {
            this.pluginHookManager.get(WorldGuardHook.class).ifPresent(worldGuardHook -> config.regionCooldowns.forEach(regionCooldown -> {
                for (String key : regionCooldown.keySet()) {
                    ApplicableRegionSet playerRegions = worldGuardHook.getRegions(player.getLocation());
                    if (playerRegions.getRegions().stream().anyMatch(playerRegion -> playerRegion.getId().equals(key))) {
                        this.time = TimeUnit.SECONDS.toMillis(regionCooldown.get(key));
                    }
                }
            }));

        }

        long startTime = System.currentTimeMillis();

        config.teleportEffects.forEach(effect -> player.addPotionEffect(effect.createEffect(Integer.MAX_VALUE, 1)));

        spawnManager.addTeleport(player);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if(!player.isOnline()) {
                spawnManager.removeTeleport(player);
                config.teleportEffects.forEach(player::removePotionEffect);
                Bukkit.getScheduler().cancelTasks(plugin);
                return;
            }
            if (didPlayerMoved(player, playerOldLocation)) {
                messageConfig.moveMessage.send(player);
                spawnManager.removeTeleport(player);
                config.teleportEffects.forEach(player::removePotionEffect);
                Bukkit.getScheduler().cancelTasks(plugin);
                return;
            }

            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            long remainingTime = this.time - elapsedTime;

            if (remainingTime <= 0) {
                player.teleport(spawnLocation);
                spawnManager.removeTeleport(player);
                messageConfig.successMessage.send(player);
                config.teleportEffects.forEach(player::removePotionEffect);
                Bukkit.getScheduler().cancelTasks(plugin);
                return;
            }

            String formattedTime = TimeUtil.convertSeconds(TimeUnit.MILLISECONDS.toSeconds(remainingTime) + 1);

            messageConfig.teleportMessage.forEach(bukkitNotice -> bukkitNotice.send(player, new MapBuilder<String, Object>()
                    .put("time", formattedTime).build()));

            config.teleportSounds.forEach(sound -> player.playSound(player.getLocation(), sound, 1.0F, 1.0F));
        }, 0, 20L);
    }

    public boolean didPlayerMoved(Player player, Location oldLocation) {
        Location playerLocation = player.getLocation();

        return playerLocation.getBlockX() != oldLocation.getBlockX()
                || playerLocation.getBlockY() != oldLocation.getBlockY()
                || playerLocation.getBlockZ() != oldLocation.getBlockZ();
    }

    private boolean isPlayerInGroup(Player player, String group) {
        return player.hasPermission("group." + group);
    }
}
