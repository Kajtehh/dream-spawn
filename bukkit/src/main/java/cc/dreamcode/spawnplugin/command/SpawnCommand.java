package cc.dreamcode.spawnplugin.command;

import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.spawnplugin.config.MessageConfig;
import cc.dreamcode.spawnplugin.manager.SpawnManager;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredPlayer
@RequiredPermission(permission = "dream-spawn-plugin.command.spawn")
public class SpawnCommand extends BukkitCommand {

    private final SpawnManager spawnManager;
    private final MessageConfig messageConfig;

    @Inject
    public SpawnCommand(SpawnManager spawnManager, MessageConfig messageConfig) {
        super("spawn");
        this.spawnManager = spawnManager;
        this.messageConfig = messageConfig;
    }

    @Override
    public void content(@NonNull CommandSender sender, @NonNull String[] args) {
        Player player = (Player) sender;

        if(spawnManager.isPlayerTeleporting(player)) {
            messageConfig.alreadyTeleporting.send(player);
            return;
        }

        spawnManager.teleport(player);
    }

    @Override
    public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
        return null;
    }
}
