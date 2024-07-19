package com.itzminey.afkvanish;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import net.ess3.api.IUser;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AFKVanish extends JavaPlugin implements Listener {

    private Essentials essentials;
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        // Initialize EssentialsX
        essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");

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
        IUser user = (User) event.getAffected();
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
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:vanish " + user.getName());
        } else { // Player is no longer AFK
            removePermission(lpUser, "essentials.vanish");
            removePermission(lpUser, "essentials.vanish.effect");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:vanish " + user.getName());
        }
    }

    private void addPermission(net.luckperms.api.model.user.User user, String permission) {
        Node node = PermissionNode.builder(permission).build();
        ImmutableContextSet context = luckPerms.getContextManager().getStaticContext();
        if (user.data().contains(node, (NodeEqualityPredicate) context).asBoolean()) {
            user.data().add(node);
            luckPerms.getUserManager().saveUser(user);
        }
    }

    private void removePermission(net.luckperms.api.model.user.User user, String permission) {
        Node node = PermissionNode.builder(permission).build();
        ImmutableContextSet context = luckPerms.getContextManager().getStaticContext();
        if (user.data().contains(node, (NodeEqualityPredicate) context).asBoolean()) {
            user.data().remove(node);
            luckPerms.getUserManager().saveUser(user);
        }
    }
}