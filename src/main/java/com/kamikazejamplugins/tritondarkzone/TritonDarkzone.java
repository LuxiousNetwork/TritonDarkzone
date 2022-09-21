package com.kamikazejamplugins.tritondarkzone;

import com.kamikazejamplugins.kamicommon.util.StringUtil;
import lombok.Getter;
import net.ess3.api.IUser;
import net.ess3.api.events.UserWarpEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class TritonDarkzone extends JavaPlugin implements Listener {
    private static TritonDarkzone plugin;
    @Getter private FileConfiguration config;

    @Override
    public void onEnable() {
        TritonDarkzone.plugin = this;
        config = this.getConfig();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        getServer().getPluginManager().registerEvents(this, this);

        this.getLogger().info(this.getDescription().getName() + " enabled");
    }

    @Override
    public void onDisable() {}

    public static TritonDarkzone getInstance() {
        return TritonDarkzone.plugin;
    }

    @EventHandler
    public void warp(UserWarpEvent event) {
        IUser user = event.getUser();
        if (user.getBase() == null || !user.getBase().isOnline()) { return; }
        Player player = user.getBase();

        ConfigurationSection warps = config.getConfigurationSection("warps");
        if (warps.contains(event.getWarp())) {
            if (warps.contains(event.getWarp() + ".messages.player") && warps.getStringList(event.getWarp() + ".messages.player").size() > 0) {
                player.sendMessage(StringUtil.rt(warps.getStringList(event.getWarp() + ".messages.player"), "{name}", player.getName()).toArray(new String[0]));

            }
            if (warps.contains(event.getWarp() + ".messages.global") && warps.getStringList(event.getWarp() + ".messages.global").size() > 0) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.sendMessage(StringUtil.rt(warps.getStringList(event.getWarp() + ".messages.global"), "{name}", player.getName()).toArray(new String[0]));
                }
            }
            if (warps.contains(event.getWarp() + ".serverCommands") && warps.getStringList(event.getWarp() + ".serverCommands").size() > 0) {
                for (String command : warps.getStringList(event.getWarp() + ".serverCommands")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{name}", player.getName()));
                }
            }
        }
    }
}