package com.chantake.MituyaProject.RSC.Wireless;

import com.chantake.MituyaProject.RSC.BitSet.BitSet7;

/**
 * A Wireless Receiver.
 *
 * @author Tal Eisenberg
 */
public abstract class Receiver extends Wireless {

    /**
     * Called when the receives receives a message.
     *
     * @param bits Transmitted bits.
     */
    public abstract void receive(BitSet7 bits);
}
