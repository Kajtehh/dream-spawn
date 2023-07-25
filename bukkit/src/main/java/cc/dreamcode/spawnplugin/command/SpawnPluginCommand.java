package cc.dreamcode.spawnplugin.command;

import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.spawnplugin.config.MessageConfig;
import cc.dreamcode.spawnplugin.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class SpawnPluginCommand extends BukkitCommand {

    private final PluginConfig config;
    private final MessageConfig messageConfig;

    @Inject
    public SpawnPluginCommand(final PluginConfig config, final MessageConfig messageConfig) {
        super("spawnplugin");
        this.config = config;
        this.messageConfig = messageConfig;
    }

    @Override
    public void content(@NonNull CommandSender sender, @NonNull String[] args) {
        if(!sender.hasPermission(config.spawnConfig.adminPermission)) {
            messageConfig.noPermission.send(sender);
            return;
        }

        this.setUsage("/spawnplugin reload");

        if(args.length != 1) {
            messageConfig.usage.send(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            try {
                this.config.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

            messageConfig.configReloaded.send(sender);
        }
    }

    @Override
    public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
        if(args.length == 1 && sender.hasPermission(config.spawnConfig.adminPermission)) {
            return Collections.singletonList("reload");
        }
        return null;
    }
}
