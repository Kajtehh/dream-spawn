package cc.dreamcode.spawnplugin;

import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.menu.bukkit.okaeri.MenuBuilderSerdes;
import cc.dreamcode.notice.minecraft.bukkit.serdes.BukkitNoticeSerdes;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.CommandComponentResolver;
import cc.dreamcode.platform.bukkit.component.ConfigurationComponentResolver;
import cc.dreamcode.platform.bukkit.component.ListenerComponentResolver;
import cc.dreamcode.platform.bukkit.component.RunnableComponentResolver;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.persistence.DreamPersistence;
import cc.dreamcode.platform.persistence.component.DocumentPersistenceComponentResolver;
import cc.dreamcode.platform.persistence.component.DocumentRepositoryComponentResolver;
import cc.dreamcode.spawnplugin.config.MessageConfig;
import cc.dreamcode.spawnplugin.config.PluginConfig;
import cc.dreamcode.spawnplugin.manager.SpawnManager;
import cc.dreamcode.spawnplugin.user.UserRepository;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import lombok.Getter;
import lombok.NonNull;

public final class SpawnPlugin extends DreamBukkitPlatform implements DreamBukkitConfig, DreamPersistence {

    @Getter private static SpawnPlugin spawnPlugin;
    @Getter private static SpawnManager spawnManager;

    @Override
    public void load(@NonNull ComponentManager componentManager) {
        spawnPlugin = this;
    }

    @Override
    public void enable(@NonNull ComponentManager componentManager) {
        this.registerInjectable(BukkitTasker.newPool(this));
        this.registerInjectable(BukkitMenuProvider.create(this));
        this.registerInjectable(BukkitCommandProvider.create(this, this.getInjector()));

        componentManager.registerResolver(CommandComponentResolver.class);
        componentManager.registerResolver(ListenerComponentResolver.class);
        componentManager.registerResolver(RunnableComponentResolver.class);

        componentManager.registerResolver(ConfigurationComponentResolver.class);
        componentManager.registerComponent(MessageConfig.class, messageConfig ->
                this.getInject(BukkitCommandProvider.class).ifPresent(bukkitCommandProvider -> {
                    bukkitCommandProvider.setRequiredPermissionMessage(messageConfig.noPermission.getText());
                    bukkitCommandProvider.setRequiredPlayerMessage(messageConfig.notPlayer.getText());
                }));

        componentManager.registerComponent(PluginConfig.class, pluginConfig -> {
            componentManager.setDebug(pluginConfig.debug);

            // register persistence + repositories
            //this.registerInjectable(pluginConfig.storageConfig);

            spawnManager = new SpawnManager(spawnPlugin, pluginConfig);
            
            componentManager.registerResolver(DocumentPersistenceComponentResolver.class);
            componentManager.registerResolver(DocumentRepositoryComponentResolver.class);

            componentManager.registerComponent(DocumentPersistence.class);
            componentManager.registerComponent(UserRepository.class);
        });
    }

    @Override
    public void disable() {
        // features need to be call when server is stopping
    }

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        return DreamVersion.create("Dream-SpawnPlugin", "1.0", "Kajteh_");
    }

    @Override
    public @NonNull OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> {
            registry.register(new BukkitNoticeSerdes());
            registry.register(new MenuBuilderSerdes());
        };
    }

    @Override
    public @NonNull OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {

        };
    }

}
