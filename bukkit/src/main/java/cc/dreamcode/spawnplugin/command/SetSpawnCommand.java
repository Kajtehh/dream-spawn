package cc.dreamcode.spawnplugin.command;

import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import cc.dreamcode.spawnplugin.config.PluginConfig;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredPlayer
@RequiredPermission(permission = "dream-spawnplugin.command.setspawn")
public class SetSpawnCommand extends BukkitCommand {

    private final PluginConfig pluginConfig;

    public SetSpawnCommand(PluginConfig pluginConfig) {
        super("setspawn");
        this.pluginConfig = pluginConfig;
    }

    @Override
    public void content(@NonNull CommandSender sender, @NonNull String[] args) {
        Player player = (Player) sender;

        pluginConfig.spawnConfig.setSpawnLocation(player.getLocation());
        new BukkitNotice(MinecraftNoticeType.CHAT, "&aUstawiono lokalizacje spawna!");
    }

    @Override
    public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
        return null;
    }
}
