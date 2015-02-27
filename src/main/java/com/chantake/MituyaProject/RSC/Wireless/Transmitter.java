package com.chantake.MituyaProject.RSC.Wireless;

import com.chantake.MituyaProject.Util.BooleanArrays;
import com.chantake.MituyaProject.Util.BooleanSubset;

/**
 * A Wireless transmitter.
 *
 * @author Tal Eisenberg
 */
public class Transmitter extends Wireless {

    /**
     * Transmit a BitSet over the transmitter channel.
     *
     * @param bits   BitSet to send.
     * @param length Transmission bit length.
     */
    public void transmit(boolean[] bits, int length) {
        getChannel().transmit(bits, startBit, length);
    }

    /**
     * Transmit one bit over the transmitter channel.
     *
     * @param state a bit state.
     */
    public void transmit(boolean state) {
        getChannel().transmit(state, startBit);
    }

    /**
     * Transmit a {@link com.chantake.MituyaProject.Util.BooleanSubset BooleanSubset} over the channel.
     *
     * @param transmission
     */
    public void transmitSubset(BooleanSubset transmission) {
        getChannel().transmitSubset(transmission, startBit);
    }

    /**
     * Transmit a long integer over the transmitter channel.
     *
     * @param value  Transmitted value.
     * @param offset A bit offset that's added to the transmitter start bit.
     * @param length Transmission bit length.
     */
    public void transmit(long value, int offset, int length) {
        boolean[] bits = BooleanArrays.fromInt(value, length);
        getChannel().transmit(bits, startBit + offset, length);
    }

    /**
     * Transmit one bit over one of the channel bits.
     *
     * @param state  A bit state.
     * @param offset The channel bit to transmit on.
     */
    public void transmit(boolean state, int offset) {
        getChannel().transmit(state, startBit + offset);
    }
}
