package cc.dreamcode.spawn.controller;

import cc.dreamcode.spawn.SpawnManager;
import cc.dreamcode.spawn.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnController implements Listener {

    private final PluginConfig config;
    private final SpawnManager spawnManager;

    @Inject
    public PlayerRespawnController(final PluginConfig config, final SpawnManager spawnManager) {
        this.config = config;
        this.spawnManager = spawnManager;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(config.teleportAfterDeath) {
            event.setRespawnLocation(spawnManager.getSpawnLocation());
        }
    }
}
