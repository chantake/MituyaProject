package com.chantake.MituyaProject.Midi;

// $Id$
/*
 * Tetsuuuu plugin for SK's Minecraft Server Copyright (C) 2010 sk89q <http://www.sk89q.com> All rights reserved.
 */
import com.chantake.MituyaProject.Bukkit.BlockType;
import com.chantake.MituyaProject.MituyaProject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

/**
 * A manager of play instances.
 *
 * @author sk89q
 */
public class JingleNoteManager {

    /**
     * List of instances.
     */
    protected Map<String, JingleNotePlayer> instances = new HashMap<>();
    /**
     * 再生しているﾉｰﾄブロックの位置
     */
    protected HashMap<Location, JingleNotePlayer> JLocs = new HashMap<>();

    public void play(Player player, JingleSequencer sequencer, int delay, MituyaProject plugin) {
        String name = player.getName();
        Location loc = findLocation(player);

        // Existing player found!
        if (instances.containsKey(name)) {
            JingleNotePlayer existing = instances.get(name);
            Location existingLoc = existing.getLocation();

            existing.stop(
                    existingLoc.getBlockX() == loc.getBlockX()
                    && existingLoc.getBlockY() == loc.getBlockY()
                    && existingLoc.getBlockZ() == loc.getBlockZ());

            instances.remove(name);
        }

        JingleNotePlayer notePlayer = new JingleNotePlayer(
                player, loc, sequencer, delay, plugin);
        Thread thread = new Thread(notePlayer);
        thread.setName("JingleNotePlayer for " + player.getName());
        thread.start();
        JLocs.put(notePlayer.loc, notePlayer);
        instances.put(name, notePlayer);
    }

    public void play(Player player, JingleSequencer sequencer, int delay, MituyaProject plugin, boolean all) {
        String name = player.getName();
        Location loc = findLocation(player);

        // Existing player found!
        if (all) {
            while (instances.containsKey(name)) {
                name = String.valueOf(new Random().nextInt());
            }
        } else {
            if (instances.containsKey(name)) {
                JingleNotePlayer existing = instances.get(name);
                Location existingLoc = existing.getLocation();

                existing.stop(
                        existingLoc.getBlockX() == loc.getBlockX()
                        && existingLoc.getBlockY() == loc.getBlockY()
                        && existingLoc.getBlockZ() == loc.getBlockZ());

                instances.remove(name);
            }
        }

        JingleNotePlayer notePlayer = new JingleNotePlayer(
                player, loc, sequencer, delay, all, plugin);
        Thread thread = new Thread(notePlayer);
        thread.setName("JingleNotePlayer for " + player.getName());
        thread.start();
        JLocs.put(notePlayer.loc, notePlayer);
        instances.put(name, notePlayer);
    }

    public void play(Player player, JingleSequencer sequencer, int delay, MituyaProject plugin, boolean all, int partI) {
        String name = player.getName();
        Location loc = findLocation(player);

        // Existing player found!
        if (instances.containsKey(name)) {
            JingleNotePlayer existing = instances.get(name);
            Location existingLoc = existing.getLocation();

            existing.stop(
                    existingLoc.getBlockX() == loc.getBlockX()
                    && existingLoc.getBlockY() == loc.getBlockY()
                    && existingLoc.getBlockZ() == loc.getBlockZ());

            instances.remove(name);
        }

        JingleNotePlayer notePlayer = new JingleNotePlayer(
                player, loc, sequencer, delay, all, plugin, partI);
        Thread thread = new Thread(notePlayer);
        thread.setName("JingleNotePlayer for " + player.getName());
        thread.start();
        JLocs.put(notePlayer.loc, notePlayer);
        instances.put(name, notePlayer);
    }

    public void stop(Player player) {
        String name = player.getName();

        // Existing player found!
        if (instances.containsKey(name)) {
            JingleNotePlayer existing = instances.get(name);
            existing.stop(false);
            JLocs.remove(existing.loc);
            instances.remove(name);
        }
    }

    public void stop(Location loc) {

        // Existing player found!
        if (JLocs.containsKey(loc)) {
            JingleNotePlayer existing = JLocs.get(loc);
            existing.stop(false);
            JLocs.remove(loc);
            instances.remove(existing.player.getName());
        }
    }

    public void stopAll() {
        for (JingleNotePlayer notePlayer : instances.values()) {
            notePlayer.stop(false);
        }

        instances.clear();
        JLocs.clear();
    }
    //ロケーションでJiggleNoteが再生されてるか確認

    public Boolean JiggleNoteCheck(Location loc) {
        return JLocs.containsKey(loc);
    }

    private Location findLocation(Player player) {
        World world = player.getWorld();
        Location loc = player.getLocation();
        loc.setY(loc.getY() - 2);

        if (!BlockType.canPassThrough(world.getBlockTypeIdAt(loc)) || !(loc.getBlock().getState() instanceof Chest)) {
            return loc;
        }
        loc.setY(loc.getY() + 2);
        while (loc.getBlock().getState() instanceof Chest) {
            loc.setY(loc.getY() + 1);
        }

        return loc;
    }
}
