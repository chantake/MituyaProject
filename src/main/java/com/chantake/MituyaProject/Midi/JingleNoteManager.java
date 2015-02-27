package com.chantake.MituyaProject.Midi;


import com.chantake.MituyaProject.Midi.Bukkit.BukkitJingleNotePlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * A manager of play instances.
 *
 * @author sk89q
 */
public class JingleNoteManager {

    /**
     * List of instances.
     */
    protected final Map<String, JingleNotePlayer> instances = new HashMap<String, JingleNotePlayer>();

    public void play(String player, JingleSequencer sequencer) {

        // Existing player found!
        if (instances.containsKey(player)) {
            JingleNotePlayer existing = instances.get(player);
            existing.stop();
            instances.remove(player);
        }

        JingleNotePlayer notePlayer = new BukkitJingleNotePlayer(player, sequencer);
        Thread thread = new Thread(notePlayer);
        thread.setDaemon(true);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setName("JingleNotePlayer for " + player);
        thread.start();

        instances.put(player, notePlayer);
    }

    public boolean stop(String player) {

        // Existing player found!
        if (instances.containsKey(player)) {
            JingleNotePlayer existing = instances.get(player);
            existing.stop();
            instances.remove(player);
            return true;
        }
        return false;
    }

    public void stopAll() {

        for (JingleNotePlayer notePlayer : instances.values()) {
            notePlayer.stop();
        }

        instances.clear();
    }
}