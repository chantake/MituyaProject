package com.chantake.MituyaProject.Midi;

import com.chantake.MituyaProject.MituyaProject;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class JingleNotePlayer implements Runnable {

    protected Player player;
    protected Location loc;
    protected JingleSequencer sequencer;
    protected int delay;
    protected boolean keepMusicBlock = false;
    protected boolean alluser = false;
    protected int selpart = -1;
    protected MituyaProject plugin;

    public JingleNotePlayer(Player player, Location loc, JingleSequencer seq,
            int delay, MituyaProject plugin) {
        this.player = player;
        this.loc = loc;
        this.sequencer = seq;
        this.delay = delay;
        this.plugin = plugin;
    }

    public JingleNotePlayer(Player player, Location loc, JingleSequencer seq,
            int delay, boolean alluser, MituyaProject plugin) {
        this.player = player;
        this.loc = loc;
        this.sequencer = seq;
        this.delay = delay;
        this.alluser = alluser;
        this.plugin = plugin;
    }

    public JingleNotePlayer(Player player, Location loc, JingleSequencer seq,
            int delay, boolean alluser, MituyaProject plugin, int selpart) {
        this.player = player;
        this.loc = loc;
        this.sequencer = seq;
        this.delay = delay;
        this.alluser = alluser;
        this.plugin = plugin;
        this.selpart = selpart;
    }

    @Override
    public void run() {
        try {
            if (delay > 0) {
                Thread.sleep(delay);
            }
            //int oldblock = loc.getBlock().getTypeId();
            // Create a fake note block
           /*
             * if (alluser) { Player[] onlinePlayers = plugin.getServer().getOnlinePlayers(); for (Player player1 : onlinePlayers) {
             * player1.sendBlockChange(loc, 25, (byte) 0); } } else { player.sendBlockChange(loc, 25, (byte) 0); }
             */
            //loc.getBlock().setTypeId(25);
            Thread.sleep(100);

            try {
                sequencer.run(this);
            }
            catch (InterruptedException t) {
            }

            Thread.sleep(500);

            if (!keepMusicBlock) {
                // Restore music block

                /*
                 * if (alluser) { Player[] onlinePlayers = plugin.getServer().getOnlinePlayers(); for (Player player1 : onlinePlayers) { int prevId =
                 * player1.getWorld().getBlockTypeIdAt(loc); byte prevData = player1.getWorld().getBlockAt(loc).getData(); player1.sendBlockChange(loc, prevId,
                 * prevData); } } else { int prevId = player.getWorld().getBlockTypeIdAt(loc); byte prevData = player.getWorld().getBlockAt(loc).getData();
                 * player.sendBlockChange(loc, prevId, prevData); }
                 */
                //ＭＩＤＩ再生終了
                //loc.getBlock().setTypeId(oldblock);
            }

            sequencer = null;
        }
        catch (InterruptedException e) {
        }
    }

    public boolean isActive() {
        return player.isOnline();
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return loc;
    }

    public void stop(boolean keepMusicBlock) {
        this.keepMusicBlock = keepMusicBlock;

        if (sequencer != null) {
            sequencer.stop();
        }
    }

    public void play(Sound instrument, float note, float volume) {
        /*
         * if (alluser) { Player[] onlinePlayers = plugin.getServer().getOnlinePlayers(); for (Player player1 : onlinePlayers) { player1.playNote(loc,
         * instrument, note); } } else { if (!player.isOnline()) { return; } player.playNote(loc, instrument, note); }
         */
        if (alluser) {
            for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                player1.playSound(player1.getLocation(), instrument, volume, note);
            }
        }
        player.playSound(player.getLocation(), instrument, volume, note);
        /*
         * if (loc.getBlock().getState() instanceof NoteBlock) { NoteBlock nb = (NoteBlock)loc.getBlock().getState(); nb.play(instrument, note); } else {
         * plugin.jingleNoteManager.stop(player); }
         */
    }
}
