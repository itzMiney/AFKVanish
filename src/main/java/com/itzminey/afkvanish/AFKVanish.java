package com.itzminey.afkvanish;

import net.ess3.api.events.AfkStatusChangeEvent;
import net.ess3.api.events.VanishStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AFKVanish extends JavaPlugin implements Listener {

    private LuckPerms luckPerms;
    private final String vanishPermission = "essentials.vanish.effect";
    private final String afkTag = " AFK"; // The tag used to identify AFK players

    @Override
    public void onEnable() {
        // Initialize LuckPerms
        this.luckPerms = Bukkit.getServicesManager().getRegistration(LuckPerms.class).getProvider();

        // Register events
        Bukkit.getPluginManager().registerEvents(this, this);

        // Print custom startup banner
        printStartupBanner();
    }

    @Override
    public void onDisable() {
        // Cleanup logic if necessary
    }

    private void printStartupBanner() {
        getLogger().info(" ");
        getLogger().info(" █████╗ ███████╗██╗  ██╗██╗   ██╗ █████╗ ███╗   ██╗██╗███████╗██╗  ██╗");
        getLogger().info("██╔══██╗██╔════╝██║ ██╔╝██║   ██║██╔══██╗████╗  ██║██║██╔════╝██║  ██║");
        getLogger().info("███████║█████╗  █████╔╝ ██║   ██║███████║██╔██╗ ██║██║███████╗███████║");
        getLogger().info("██╔══██║██╔══╝  ██╔═██╗ ╚██╗ ██╔╝██╔══██║██║╚██╗██║██║╚════██║██╔══██║");
        getLogger().info("██║  ██║██║     ██║  ██╗ ╚████╔╝ ██║  ██║██║ ╚████║██║███████║██║  ██║");
        getLogger().info("╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝  ╚═══╝  ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝╚══════╝╚═╝  ╚═╝");
        getLogger().info(" ");
        getLogger().info(" AFKVanish Plugin Enabled!");
        getLogger().info(" Version: 1.0.0");
        getLogger().info(" Powered by EssentialsX and LuckPerms");
        getLogger().info(" ");
    }

    @EventHandler
    public void onAfkStatusChange(AfkStatusChangeEvent event) {
        Player player = event.getAffected().getBase();
        if (player != null) {
            // Check if the player's display name contains the AFK tag
            if (player.getDisplayName().contains(afkTag)) {
                handlePlayerAfk(player);
            } else {
                handlePlayerNotAfk(player);
            }
        }
    }

    private void handlePlayerAfk(Player player) {
        // Add AFK tag if not already present
        if (!player.getDisplayName().endsWith(afkTag)) {
            player.setDisplayName(player.getDisplayName() + afkTag);
        }

        // Temporarily grant vanish permission
        if (!player.hasPermission(vanishPermission)) {
            addVanishPermission(player);
        }

        // Execute vanish command
        Bukkit.getScheduler().runTask(this, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:vanish " + player.getName());
        });
    }

    private void handlePlayerNotAfk(Player player) {
        // Remove AFK tag if present
        if (player.getDisplayName().endsWith(afkTag)) {
            player.setDisplayName(player.getDisplayName().replace(afkTag, ""));
        }

        // Revoke vanish permission
        if (player.hasPermission(vanishPermission)) {
            removeVanishPermission(player);
        }

        // Execute unvanish command
        Bukkit.getScheduler().runTask(this, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:vanish " + player.getName());
        });
    }

    private void addVanishPermission(Player player) {
        UserManager userManager = luckPerms.getUserManager();
        User user = userManager.getUser(player.getUniqueId());
        if (user != null) {
            user.data().add(Node.builder(vanishPermission).build());
            userManager.saveUser(user);
            getLogger().info("Granted vanish permission to " + player.getName());
        }
    }

    private void removeVanishPermission(Player player) {
        UserManager userManager = luckPerms.getUserManager();
        User user = userManager.getUser(player.getUniqueId());
        if (user != null) {
            user.data().remove(Node.builder(vanishPermission).build());
            userManager.saveUser(user);
            getLogger().info("Revoked vanish permission from " + player.getName());
        }
    }
}