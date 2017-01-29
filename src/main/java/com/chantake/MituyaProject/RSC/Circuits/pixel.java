package com.chantake.MituyaProject.RSC.Circuits;

import com.chantake.MituyaProject.RSC.BitSet.BitSet7;
import com.chantake.MituyaProject.RSC.BitSet.BitSetUtils;
import com.chantake.MituyaProject.RSC.Circuit.Circuit;
import com.chantake.MituyaProject.RSC.Circuit.IO.InterfaceBlock;
import com.chantake.MituyaProject.RSC.Wireless.Receiver;
import com.chantake.MituyaProject.Tool.Locations;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;

/**
 * // dyes wool if present on output block
 *
 * @author Tal Eisenberg
 */
public class pixel extends Circuit {

    private boolean indexedColor = false;
    private byte[] colorIndex;
    private int distance = 3;
    private static final BlockFace[] faces = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private Receiver receiver;

    @Override
    public void inputChange(int inIdx, boolean on) {
        if (inputs.length == 1) {
            updatePixel();
        } else if (inIdx == 0 && on) { // clock pin
            updatePixel();
        }
    }

    @Override
    protected boolean init(CommandSender sender, String[] args) {
        // needs to have 5 inputs 1 clock 4 data

        if (args.length > 0) {
            String channelString = null;

            List<Byte> colorList = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                try {
                    colorList.add(DyeColor.valueOf(args[i].toUpperCase()).getWoolData());
                }
                catch (IllegalArgumentException ie) {
                    // not dye color
                    try {
                        int val = Integer.decode(args[i]);
                        colorList.add((byte)val);
                    }
                    catch (NumberFormatException ne) {
                        // not dye number also, treat as broadcast channel if last or distance argument;
                        if ((args[i].startsWith("d{") || args[i].startsWith("dist{")) && args[i].endsWith("}")) {
                            try {
                                distance = Integer.decode(args[i].substring(args[i].indexOf("{") + 1, args[i].length() - 1));
                            }
                            catch (NumberFormatException ne2) {
                                error(sender, "Bad distance argument: " + args[i] + ". Expecting d{<distance>} or dist{<distance>}.");
                                return false;
                            }
                        } else if (i == args.length - 1) {
                            channelString = args[i];
                        } else {
                            error(sender, "Unknown color name: " + args[i]);
                            return false;
                        }
                    }
                }

            }

            if (!colorList.isEmpty()) {
                colorIndex = new byte[colorList.size()];
                for (int i = 0; i < colorList.size(); i++) {
                    colorIndex[i] = colorList.get(i);
                }
                indexedColor = true;
            }

            if (channelString != null) {
                try {
                    receiver = new PixelReceiver();
                    int len;
                    if (!indexedColor) {
                        len = 4;
                    } else {
                        len = (int)Math.ceil(Math.log(colorIndex.length) / Math.log(2));
                    }

                    receiver.init(sender, channelString, len, this);
                }
                catch (IllegalArgumentException ie) {
                    error(sender, ie.getMessage());
                    return false;
                }
            }
        }

        if (inputs.length > 5) {
            error(sender, "Too many inputs. Requires 1 clock pin and no more than 4 data pins.");
            return false;
        }

        if (this.interfaceBlocks.length == 0) {
            error(sender, "Expecting at least 1 interface block.");
            return false;
        }
        return true;
    }

    private void updatePixel() {
        for (InterfaceBlock i : interfaceBlocks) {
            if (!i.getLocation().getChunk().isLoaded()) {
                return;
            }
        }

        int val;
        if (inputs.length <= 1) {
            val = BitSetUtils.bitSetToUnsignedInt(inputBits, 0, inputBits.length());
        } else {
            val = BitSetUtils.bitSetToUnsignedInt(inputBits, 1, inputBits.length() - 1);
        }

        byte color;

        if (indexedColor) {
            int index = val;
            if (index >= colorIndex.length) {
                if (hasDebuggers()) {
                    debug("Color index out of bounds: " + index);
                }
                return;
            }
            color = colorIndex[index];
        } else {
            color = (byte)val;
        }

        if (hasDebuggers()) {
            debug("Setting pixel color to " + DyeColor.getByWoolData(color));
        }

        for (InterfaceBlock i : interfaceBlocks) {
            colorBlocks(i.getLocation(), color);
        }

    }

    private void colorBlocks(Location origin, byte color) {
        List<Location> wool = new ArrayList<>();
        findWoolAround(origin, origin, wool, distance, 0);
        for (Location l : wool) {
            l.getBlock().setData(color);
        }
    }

    class PixelReceiver extends Receiver {

        @Override
        public void receive(BitSet7 bits) {
            // if we have 0 or 1 inputs there's no clock to adjust. just use the incoming bits.        
            if (inputs.length <= 1) {
                inputBits = bits.get(0, (inputs.length == 0 ? 5 : inputs.length));
            } else {
                for (int i = 0; i < bits.length(); i++) {
                    inputBits.set(i + 1, bits.get(i));
                }
                inputBits.clear(0);
            }
            updatePixel();
        }
    }

    private void findWoolAround(Location origin, Location curLocation, List<Location> wool, int range, int curDist) {
        if (curDist >= range) {
            return;
        }

        curDist++;
        for (BlockFace face : faces) {
            Location f = Locations.getFace(curLocation, face);
            if (world.getBlockTypeIdAt(f) == Material.WOOL.getId()) {
                if (!wool.contains(f) && !f.equals(origin) && inCube(origin, f, range)) {
                    wool.add(f);
                }
                findWoolAround(origin, f, wool, range, curDist);
            }
        }
    }

    private boolean inCube(Location origin, Location f, int rad) {
        int dx = (int)Math.abs(origin.getX() - f.getX());
        int dy = (int)Math.abs(origin.getY() - f.getY());
        int dz = (int)Math.abs(origin.getZ() - f.getZ());

        if (rad > 2) {
            rad--;
        } else {
            rad++;
        }

        return (dx < rad && dy < rad && dz < rad);
    }
}
