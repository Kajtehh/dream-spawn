package cc.dreamcode.spawn.controller;

import cc.dreamcode.spawn.SpawnManager;
import cc.dreamcode.spawn.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinController implements Listener {

    private final SpawnManager spawnManager;
    private final PluginConfig config;

    @Inject
    public PlayerJoinController(final SpawnManager spawnManager, final PluginConfig config) {
        this.spawnManager = spawnManager;
        this.config = config;
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
}
