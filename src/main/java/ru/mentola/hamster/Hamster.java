package ru.mentola.hamster;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import ru.mentola.annotatedconfigmodel.AnnotatedConfigFactory;
import ru.mentola.hamster.command.HamsterCommand;
import ru.mentola.hamster.listener.InventoryListener;
import ru.mentola.hamster.manager.UserManager;
import ru.mentola.hamster.model.HamsterUser;
import ru.mentola.hamster.model.config.ConfigurationMySql;
import ru.mentola.hamster.model.config.ConfigurationPlugin;
import ru.mentola.hamster.pool.HamsterUserPool;
import ru.mentola.hamster.pool.HamsterGuiPool;

import java.util.List;

@Getter
public final class Hamster extends JavaPlugin {
    private ConfigurationMySql mySqlConfig;
    private ConfigurationPlugin pluginConfig;
    private UserManager userManager;
    private HamsterUserPool userPool;
    private HamsterGuiPool hamsterGuiPool;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        try {
            this.mySqlConfig = AnnotatedConfigFactory.build(this.getConfig(), ConfigurationMySql.class);
            this.pluginConfig = AnnotatedConfigFactory.build(this.getConfig(), ConfigurationPlugin.class);
            this.userPool = new HamsterUserPool();
            this.hamsterGuiPool = new HamsterGuiPool();
            this.userManager = new UserManager();
            this.userManager.init();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            final List<HamsterUser> hamsterUsers = this.userManager.getAll();
            hamsterUsers.forEach((user) -> this.userPool.add(user));
        });

        final HamsterCommand hamsterCommand = new HamsterCommand();
        Bukkit.getPluginCommand("hamster").setExecutor(hamsterCommand);
        Bukkit.getPluginCommand("hamster").setTabCompleter(hamsterCommand);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
    }

    @Override
    public void onDisable() {
        try {
            if (this.hamsterGuiPool != null) {
                this.hamsterGuiPool.getUnModifiableCache().forEach((gui) -> gui.getInventory().close());
                this.hamsterGuiPool.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (this.userPool != null) {
                try {
                    if (this.userManager != null) {
                        this.userPool.getUnModifiableCache().forEach((user) -> this.userManager.sync(user));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                this.userPool.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (this.userManager != null) {
                this.userManager.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().cancelTasks(this);
    }

    @Nullable
    public static Hamster getInstance() {
        final Plugin plugin = Bukkit.getPluginManager().getPlugin("Hamster");
        if (plugin instanceof Hamster hamster) return hamster;
        return null;
    }
}
