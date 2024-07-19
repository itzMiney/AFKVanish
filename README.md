# AFKVanish Plugin

AFKVanish is a Minecraft plugin that integrates with EssentialsX and LuckPerms to automatically manage player vanish status based on their AFK status. When a player goes AFK, they are automatically given vanish permissions, and when they come back from being AFK, those permissions are removed. This plugin helps manage player visibility and permissions efficiently.

## Features

- **Automatic Vanish**: Automatically adds the `essentials.vanish` and `essentials.vanish.effect` permissions when a player goes AFK.
- **Automatic Un-Vanish**: Removes the vanish permissions when a player is no longer AFK.
- **Integration**: Works seamlessly with EssentialsX and LuckPerms for permissions and vanish management.

## Supported Versions

- **Minecraft Version**: The plugin currently supports Minecraft version 1.20.4. It may work with higher versions, but compatibility is not guaranteed beyond 1.20.4.

## Installation

1. **Download the Plugin**:
   Download the latest version of the AFKVanish plugin JAR file from the [releases page](https://github.com/itzMiney/AFKVanish/releases) and place it in your server's `plugins` directory.

2. **Install EssentialsX**:
   - [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/) is required for vanish functionality. Download and install EssentialsX on your server.
   - Ensure that you have the `Essentials` and `EssentialsSpawn` modules installed.

3. **Install LuckPerms**:
   - [LuckPerms](https://www.spigotmc.org/resources/luckperms.28140/) is required for managing permissions. Download and install LuckPerms on your server.

4. **Restart Your Server**:
   Restart your Minecraft server to load the plugin and apply the changes.

## Configuration

The plugin does not have a configuration file as it works directly with EssentialsX and LuckPerms. Ensure that your EssentialsX and LuckPerms configurations are correctly set up to use the `vanish` command and manage permissions.

## Commands

- **No commands required**: The plugin operates automatically based on player AFK status changes.

## Permissions

- **essentials.vanish**: Required by EssentialsX for the vanish functionality.
- **essentials.vanish.effect**: Optional permission for additional vanish effects.

## Troubleshooting

- Ensure that both EssentialsX and LuckPerms are correctly installed and configured.
- Check the server logs for any errors related to AFKVanish to diagnose issues.
- Verify that the player has the correct permissions set in LuckPerms.

## Links

- [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/)
- [LuckPerms](https://www.spigotmc.org/resources/luckperms.28140/)
- [AFKVanish GitHub Repository](https://github.com/itzMiney/AFKVanish)

## License

This plugin is licensed under the MIT License. See [LICENSE](LICENSE) for more details.

## Support

If you encounter any issues or need assistance, feel free to open an issue on the [GitHub Issues page](https://github.com/itzMiney/AFKVanish/issues) or reach out for support.

---

Thank you for using AFKVanish! Enjoy your Minecraft server with enhanced vanish functionality.

**Note**: The plugin is currently designed to work with Minecraft version 1.20.4. While it may function with newer versions, compatibility is not guaranteed beyond 1.20.4.
