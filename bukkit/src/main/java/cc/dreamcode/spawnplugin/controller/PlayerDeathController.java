package cc.dreamcode.spawnplugin.controller;

import cc.dreamcode.spawnplugin.SpawnManager;
import cc.dreamcode.spawnplugin.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathController implements Listener {

    private final SpawnManager spawnManager;
    private final PluginConfig config;

    @Inject
    public PlayerDeathController(final SpawnManager spawnManager, final PluginConfig config) {
        this.spawnManager = spawnManager;
        this.config = config;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(config.spawnConfig.teleportAfterDeath) {
            Player player = event.getEntity();
            player.teleport(spawnManager.getSpawnLocation());
        }
    }
}
