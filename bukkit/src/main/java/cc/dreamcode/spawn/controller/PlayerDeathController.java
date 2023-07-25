package cc.dreamcode.spawn.controller;

import cc.dreamcode.spawn.SpawnPlugin;
import cc.dreamcode.spawn.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathController implements Listener {

    private final SpawnPlugin spawnPlugin;
    private final PluginConfig config;

    @Inject
    public PlayerDeathController(final SpawnPlugin spawnPlugin, final PluginConfig config) {
        this.spawnPlugin = spawnPlugin;
        this.config = config;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(config.autoRespawn) {
            Player player = event.getEntity();
            Bukkit.getScheduler().runTaskLater(this.spawnPlugin, () -> player.spigot().respawn(), 0);
        }
    }
}
