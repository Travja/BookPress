package me.Travja.BookPress;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public Logger log;
    public Listener dropListener = new dropListener(this);
    public FileConfiguration config;

    public void onEnable() {
        log = this.getLogger();
        config = this.getConfig();
        config.options().copyDefaults(true);
        saveConfig();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(dropListener, this);
        log.info("BookPress has been enabled!");
    }

    public void onDisable() {
        log.info("BookPress has been disabled!");
    }
}
