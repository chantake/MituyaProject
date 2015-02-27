package com.chantake.MituyaProject.Midi.Bukkit;

import com.chantake.MituyaProject.Midi.Instrument;
import com.chantake.MituyaProject.Midi.JingleNotePlayer;
import com.chantake.MituyaProject.Midi.JingleSequencer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class BukkitJingleNotePlayer extends JingleNotePlayer {

    Player p = null;

    public BukkitJingleNotePlayer(String player, JingleSequencer seq) {
        super(player, seq);
    }

    @Override
    public void play(JingleSequencer.Note note) {

        if (p == null || !p.isOnline())
            p = Bukkit.getPlayerExact(player);

        if (p == null || !p.isOnline() || note == null)
            return;

        p.playSound(p.getLocation(), toSound(note.getInstrument()), note.getVelocity(), note.getNote());
    }

    public Sound toSound(Instrument instrument) {

        switch (instrument) {
            case PIANO:
                return Sound.NOTE_PIANO;
            case GUITAR:
                return Sound.NOTE_PLING;
            case BASS:
                return Sound.NOTE_BASS;
            case BASS_GUITAR:
                return Sound.NOTE_BASS_GUITAR;
            case STICKS:
                return Sound.NOTE_STICKS;
            case BASS_DRUM:
                return Sound.NOTE_BASS_DRUM;
            case SNARE_DRUM:
                return Sound.NOTE_SNARE_DRUM;
            default:
                return Sound.NOTE_PIANO;
        }
    }
}