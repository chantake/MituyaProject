package com.chantake.MituyaProject.RSC.Chip.IO;

import com.chantake.MituyaProject.RSC.Circuit.Circuit;

/**
 * @author taleisenberg
 */
public interface IOWriter {
    public void writeOut(Circuit circuit, boolean state, int index);
}
