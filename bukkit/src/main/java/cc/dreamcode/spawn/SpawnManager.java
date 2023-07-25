package cc.dreamcode.spawn;

import cc.dreamcode.spawn.config.PluginConfig;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpawnManager {

    private final PluginConfig config;
    private final Map<UUID, Boolean> teleport = new HashMap<>();

    public void addTeleport(Player player) {
        teleport.put(player.getUniqueId(), true);
    }

    public void removeTeleport(Player player) {
        teleport.remove(player.getUniqueId());
    }

    public Location getSpawnLocation() {
        return config.spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        this.config.spawnLocation = location;
        try {
            this.config.save();
        } catch (OkaeriException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerTeleporting(Player player) {
        return teleport.containsKey(player.getUniqueId());
    }
}
