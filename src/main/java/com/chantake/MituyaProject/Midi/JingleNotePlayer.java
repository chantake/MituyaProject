package com.chantake.MituyaProject.Midi;


public abstract class JingleNotePlayer implements Runnable {

    protected final String player;
    protected JingleSequencer sequencer;

    /**
     * Constructs a new JingleNotePlayer
     *
     * @param player The player who is hearing this's name.
     * @param seq    The JingleSequencer to play.
     *               //* @param area The SearchArea for this player. (optional)
     */
    public JingleNotePlayer(String player, JingleSequencer seq) {
        this.player = player;
        sequencer = seq;
    }

    @Override
    public void run() {

        if (sequencer == null)
            return;
        try {
            try {
                sequencer.run(this);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sequencer.stop();
            sequencer = null;
        }
    }

    public String getPlayer() {
        return player;
    }

    public void stop() {
        if (sequencer != null) {
            sequencer.stop();
        }
    }

    public abstract void play(JingleSequencer.Note note);
}
