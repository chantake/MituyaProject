package com.chantake.MituyaProject.RSC;

import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Monitors loading/unloading behavior of worlds on the server. Mainly important
 * for knowing when a world is in the process of unloading but has not unloaded
 * yet.
 *
 * @author taleisenberg
 */
public class WorldsObserver {
    private final static List<World> loadedWorlds = new ArrayList<>();
    private static World unloadingWorld = null;

    public static boolean isWorldLoaded(World w) {
        return loadedWorlds.contains(w);
    }

    public static void clearLoadedWorldsList() {
        loadedWorlds.clear();
    }

    public static void addLoadedWorld(World world) {
        loadedWorlds.add(world);
    }

    public static void removeLoadedWorld(World unloadedWorld) {
        loadedWorlds.remove(unloadedWorld);
    }

    public static boolean isWorldUnloading(World world) {
        return world != null && unloadingWorld == world;
    }

    public static World getUnloadingWorld() {
        return unloadingWorld;
    }

    public static void setUnloadingWorld(World world) {
        unloadingWorld = world;
    }

    public static boolean removeUnloadingWorld() {
        boolean ret = loadedWorlds.remove(unloadingWorld);
        unloadingWorld = null;
        return ret;
    }
}
