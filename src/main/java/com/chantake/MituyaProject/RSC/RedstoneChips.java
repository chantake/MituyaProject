package com.chantake.MituyaProject.RSC;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.RSC.Circuit.Circuit;
import com.chantake.MituyaProject.RSC.Circuit.CircuitIndex;
import com.chantake.MituyaProject.RSC.Circuit.RCTypeReceiver;
import com.chantake.MituyaProject.RSC.Circuits.*;
import com.chantake.MituyaProject.RSC.Memory.Memory;
import com.chantake.MituyaProject.RSC.Session.UserSession;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * RedstoneChips Bukkit JavaPlugin implementation. The main entry point of the plugin.
 *
 * @author Tal Eisenberg
 */
public class RedstoneChips {

    private final MituyaProject plugin;
    private static final List<CircuitIndex> preloadedLibs = new ArrayList<>();
    private PrefsManager prefsManager;
    private CircuitManager circuitManager;
    private CircuitPersistence circuitPersistence;
    private CircuitLoader circuitLoader;
    private ChannelManager channelManager;
    /**
     * List of registered /rctype receivers.
     */
    public Map<Location, RCTypeReceiver> rcTypeReceivers = new HashMap<>();
    private final Map<String, UserSession> sessions = new HashMap<>();

    public RedstoneChips(MituyaProject plugin) {
        this.plugin = plugin;
    }

    public void initRSC() {
        getPlugin().getLogger().log(Level.INFO, "RSC初期化開始");
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        initManagers();
        preloadedLibs.add(new BuildInCircuits());
        loadLibraries();
        callLibraryRedstoneChipsEnable();
        prefsManager.loadPrefs();
        plugin.getServer().getPluginManager().registerEvents(new RCBukkitEventHandler(this), plugin);

        try {
            Memory.setupDataFolder(getDataFolder());
        }
        catch (RuntimeException e) {
            log(Level.WARNING, e.getMessage());
        }

        // schedule loading channel and old circuits file (if exists) until after server startup is complete.
        if (-1 == plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                postStartup();
            }
        })) {

            // couldn't schedule task. Try running it before server startup is finished (could fail).
            postStartup();
        }
        getPlugin().getLogger().log(Level.INFO, "RSC初期化完了");
    }

    private void postStartup() {
        if (!circuitPersistence.loadOldFile()) {
            for (World w : plugin.getServer().getWorlds()) {
                if (!circuitPersistence.isWorldLoaded(w)) {
                    circuitPersistence.loadCircuits(w);
                }
            }
        }

        circuitPersistence.loadChannels();
        log(Level.INFO, "Processing " + circuitManager.getCircuits().size() + " active chip(s).");
    }

    public void Disable() {
        for (UserSession s : sessions.values()) {
            s.playerQuit();
        }

        circuitPersistence.saveCircuits();
        circuitManager.shutdownAllCircuits();
        circuitPersistence.clearLoadedWorldsList();
        plugin.getLogger().info("RSC disabled");
    }

    private void initManagers() {
        prefsManager = new PrefsManager(this);
        circuitManager = new CircuitManager(this);
        circuitPersistence = new CircuitPersistence(this);
        circuitLoader = new CircuitLoader(this);
        channelManager = new ChannelManager(this);
    }

    /**
     * Tells the plugin to load circuit classes from this circuit library when it's enabled.
     *
     * @param lib Any object implementing the CircuitIndex interface.
     */
    public static void addCircuitLibrary(CircuitIndex lib) {
        preloadedLibs.add(lib);
    }

    /**
     * Sends a RedstoneChips log message to the console.
     *
     * @param level
     * @param message
     */
    public void log(Level level, String message) {
        plugin.getLogger().log(level, message);
    }

    /**
     * Returns the preference manager. The object responsible for loading, saving and editing the plugin preferences.
     */
    public PrefsManager getPrefs() {
        return prefsManager;
    }

    /**
     * Returns the circuit loader. The object responsible for creating new instances of Circuit classes.
     */
    public CircuitLoader getCircuitLoader() {
        return circuitLoader;
    }

    /**
     * Returns the circuit manager. The object responsible for creating and managing active circuits.
     */
    public CircuitManager getCircuitManager() {
        return circuitManager;
    }

    /**
     * Returns the channel manager. The object responsible for handling wireless broadcast channels.
     */
    public ChannelManager getChannelManager() {
        return channelManager;
    }

    /**
     * Returns the circuit persistence handle. The object responsible for saving and loading the active circuit list from storage.
     */
    public CircuitPersistence getCircuitPersistence() {
        return circuitPersistence;
    }

    /**
     * Returns the UserSession object tied to this username.
     *
     * @param username The player name.
     * @param create Whether to create a new UserSession if none exists yet or not.
     * @return The player UserSession or null if none was found and create is false.
     */
    public UserSession getUserSession(String username, boolean create) {
        UserSession s = sessions.get(username);
        if (s == null && create) {
            s = new UserSession(this, username, plugin);
            sessions.put(username, s);
        }

        return s;
    }

    /**
     * Returns the UserSession object tied to this player.
     *
     * @param player The requested UserSession player.
     * @param create Whether to create a new UserSession if none exists yet or not.
     * @return The player UserSession or null if none was found and create if false.
     */
    public UserSession getUserSession(Player player, boolean create) {
        return getUserSession(player.getName(), create);
    }

    /**
     * Removes the UserSession for the specified player.
     *
     * @param player
     * @return The UserSession object that was removed or null if it was not found.
     */
    public UserSession removeUserSession(Player player) {
        return removeUserSession(player.getName());
    }

    /**
     * Removes the UserSession for the specified username.
     *
     * @param name
     * @return The UserSession object that was removed or null if it was not found.
     */
    public UserSession removeUserSession(String name) {
        return sessions.remove(name);
    }

    /**
     * Registers a typingBlock to be used by an RCTypeReceiver. When a player points towards the typingBlock and uses the /rctype command the RCTypeReceiver
     * will receive the typed text.
     *
     * @param typingBlock The block to point towards while typing.
     * @param circuit The circuit that will receive the typed text.
     */
    public void addRCTypeReceiver(Location typingBlock, RCTypeReceiver circuit) {
        rcTypeReceivers.put(typingBlock, circuit);
    }

    /**
     * The RCTypeReceiver will no longer receive /rctype commands.
     *
     * @param circuit The rcTypeReceiver to remove.
     */
    public void removeRCTypeReceiver(RCTypeReceiver circuit) {
        List<Location> toremove = new ArrayList<>();

        for (Location l : rcTypeReceivers.keySet()) {
            if (rcTypeReceivers.get(l) == circuit) {
                toremove.add(l);
            }
        }

        for (Location l : toremove) {
            rcTypeReceivers.remove(l);
        }
    }

    private void loadLibraries() {
        for (CircuitIndex lib : preloadedLibs) {
            String libMsg = "Loading " + lib.getIndexName() + " " + lib.getVersion() + " > ";
            Class<? extends Circuit>[] classes = lib.getCircuitClasses();

            if (classes != null && classes.length > 0) {
                for (Class c : classes) {
                    libMsg += c.getSimpleName() + ", ";
                }

                libMsg = libMsg.substring(0, libMsg.length() - 2) + ".";
                plugin.getLogger().info(libMsg);

                circuitLoader.addCircuitIndex(lib);
            } else {
                libMsg += "No circuit classes were loaded.";
                plugin.getLogger().info(libMsg);
            }
        }
    }

    private void callLibraryRedstoneChipsEnable() {
        for (CircuitIndex lib : circuitLoader.getCircuitLibraries()) {
            lib.onRedstoneChipsEnable(this);
        }
    }

    public File getDataFolder() {
        return new File(plugin.getDataFolder(), "rsc/");
    }

    public MituyaProject getPlugin() {
        return plugin;
    }

    public Server getServer() {
        return plugin.getServer();
    }

}
