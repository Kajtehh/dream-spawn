package cc.dreamcode.spawn.controller;

import cc.dreamcode.spawn.SpawnManager;
import cc.dreamcode.spawn.SpawnPlugin;
import cc.dreamcode.spawn.config.MessageConfig;
import cc.dreamcode.spawn.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TeleportController implements Listener {

    private final SpawnPlugin spawnPlugin;
    private final SpawnManager spawnManager;
    private final PluginConfig config;
    private final MessageConfig messageConfig;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(spawnManager.isPlayerTeleporting(player)) {
            spawnManager.removeTeleport(player);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if ((event.getFrom().getBlockX() == Objects.requireNonNull(event.getTo()).getBlockX()) && (event.getFrom().getBlockZ() == event.getTo().getBlockZ()) &&
                (event.getFrom().getBlockY() == event.getTo().getBlockY())) {
            return;
        }

        if(!spawnManager.isPlayerTeleporting(event.getPlayer())) return;

        Player player = event.getPlayer();

        messageConfig.moveMessage.send(player);
        spawnManager.removeTeleport(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(config.teleportAfterDeath) {
            event.setRespawnLocation(spawnManager.getSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(config.teleportAfterJoin) {
            player.teleport(spawnManager.getSpawnLocation());
            return;
        }

        if(config.teleportAfterFirstJoin && !player.hasPlayedBefore()) {
            player.teleport(spawnManager.getSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(spawnManager.isPlayerTeleporting(player)) {
            spawnManager.removeTeleport(player);
        }
        if(config.autoRespawn) {
            Bukkit.getScheduler().runTaskLater(this.spawnPlugin, () -> player.spigot().respawn(), 0);
        }
    }
}
