package cc.dreamcode.spawnplugin.controller;

import cc.dreamcode.spawnplugin.SpawnManager;
import cc.dreamcode.spawnplugin.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnController implements Listener {

    private final SpawnManager spawnManager;
    private final PluginConfig config;

    @Inject
    public PlayerRespawnController(final SpawnManager spawnManager, final PluginConfig config) {
        this.spawnManager = spawnManager;
        this.config = config;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(config.spawnConfig.teleportAfterDeath) {
            event.setRespawnLocation(spawnManager.getSpawnLocation());
        }
    }
}
