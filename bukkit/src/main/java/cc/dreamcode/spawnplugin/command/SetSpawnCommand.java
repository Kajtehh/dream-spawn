package cc.dreamcode.spawnplugin.command;

import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.spawnplugin.SpawnManager;
import cc.dreamcode.spawnplugin.config.MessageConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredPlayer
public class SetSpawnCommand extends BukkitCommand {

    private final SpawnManager spawnManager;
    private final MessageConfig messageConfig;

    @Inject
    public SetSpawnCommand(final SpawnManager spawnManager, final MessageConfig messageConfig) {
        super("setspawn");
        this.spawnManager = spawnManager;
        this.messageConfig = messageConfig;
    }

    @Override
    public void content(@NonNull CommandSender sender, @NonNull String[] args) {
        Player player = (Player) sender;

        spawnManager.setSpawnLocation(player.getLocation());
        messageConfig.setSpawn.send(player);
    }

    @Override
    public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
        return null;
    }
}
