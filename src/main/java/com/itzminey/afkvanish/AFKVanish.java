package com.itzminey.afkvanish;

import com.earth2me.essentials.Essentials;
import net.ess3.api.IUser;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class AFKVanish extends JavaPlugin implements Listener {

    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        // Initialize EssentialsX
        Essentials essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");

        if (essentials == null) {
            getLogger().severe("Essentials not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize LuckPerms
        RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        } else {
            getLogger().severe("LuckPerms not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register event listener
        getServer().getPluginManager().registerEvents(this, this);

        // Log plugin startup
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
        IUser user = event.getAffected();
        UUID uuid = user.getBase().getUniqueId();
        UserManager userManager = luckPerms.getUserManager();
        net.luckperms.api.model.user.User lpUser = userManager.getUser(uuid);

        if (lpUser == null) {
            getLogger().warning("LuckPerms user not found for UUID: " + uuid);
            return;
        }

        if (event.getValue()) { // Player is AFK
            addPermission(lpUser, "essentials.vanish");
            addPermission(lpUser, "essentials.vanish.effect");

            // Ensure permissions are saved before executing the vanish command
            userManager.saveUser(lpUser).thenRun(() -> {
                Bukkit.getScheduler().runTask(this, () -> {
                    // Execute vanish command
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:vanish " + user.getName());
                });
            });

        } else { // Player is no longer AFK
            // Execute un-vanish command
            Bukkit.getScheduler().runTask(this, () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:vanish " + user.getName());
                // After the vanish command, remove permissions
                userManager.saveUser(lpUser).thenRun(() -> {
                    removePermission(lpUser, "essentials.vanish");
                    removePermission(lpUser, "essentials.vanish.effect");
                });
            });
        }
    }

    private void addPermission(net.luckperms.api.model.user.User user, String permission) {
        Node node = PermissionNode.builder(permission).build();
        luckPerms.getContextManager().getStaticContext();

        // Check if the user does not already have the permission
        if (!user.getNodes().contains(node)) {
            user.data().add(node);
            luckPerms.getUserManager().saveUser(user).join(); // Ensure the save operation completes
            getLogger().info("Added permission: " + permission + " to user: " + user.getUsername());
        }
    }

    private void removePermission(net.luckperms.api.model.user.User user, String permission) {
        Node node = PermissionNode.builder(permission).build();
        luckPerms.getContextManager().getStaticContext();

        // Check if the user has the permission
        if (user.getNodes().contains(node)) {
            user.data().remove(node);
            luckPerms.getUserManager().saveUser(user).join(); // Ensure the save operation completes
            getLogger().info("Removed permission: " + permission + " from user: " + user.getUsername());
        }
    }
}
