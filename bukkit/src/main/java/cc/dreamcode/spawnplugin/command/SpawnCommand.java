package cc.dreamcode.spawnplugin.command;

import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.spawnplugin.SpawnManager;
import cc.dreamcode.spawnplugin.config.MessageConfig;
import cc.dreamcode.spawnplugin.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpawnCommand extends BukkitCommand {

    private final SpawnManager spawnManager;
    private final PluginConfig config;
    private final MessageConfig messageConfig;

    @Inject
    public SpawnCommand(final SpawnManager spawnManager, final PluginConfig config, final MessageConfig messageConfig) {
        super("spawn");
        this.spawnManager = spawnManager;
        this.config = config;
        this.messageConfig = messageConfig;
    }

    @Override
    public void content(@NonNull CommandSender sender, @NonNull String[] args) {
        switch (args.length) {
            case 0:
                if(!(sender instanceof Player)) {
                    messageConfig.notPlayer.send(sender);
                    return;
                }
                Player player = (Player) sender;

                if (spawnManager.isPlayerTeleporting(player)) {
                    messageConfig.alreadyTeleporting.send(player);
                    return;
                }

                if(player.hasPermission(config.spawnConfig.bypassPermission)) {
                    config.spawnConfig.successMessage.send(player);
                    player.teleport(spawnManager.getSpawnLocation());
                    return;
                }

                spawnManager.teleport(player);
                break;
            case 1:
                if(!sender.hasPermission(config.spawnConfig.adminPermission)) {
                    messageConfig.noPermission.send(sender);
                    return;
                }

                Player targetPlayer = Bukkit.getPlayerExact(args[0]);

                if(targetPlayer == null || !targetPlayer.isOnline()) {
                    messageConfig.noPlayer.send(sender);
                    return;
                }

                config.spawnConfig.successMessage.send(targetPlayer);
                targetPlayer.teleport(spawnManager.getSpawnLocation());
                break;
        }
    }

    @Override
    public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
        return null;
    }
}
