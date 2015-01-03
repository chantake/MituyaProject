/*
 * MituyaProject
 * Copyright (C) 2011-2015 chantake <http://328mss.com/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.chantake.MituyaProject.World.Generator;

import java.util.HashMap;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

/**
 *
 * @author chantake
 */
public class NoneGenerator extends ChunkGenerator {

    @Override
    public byte[] generate(World world, Random random, int chankX, int chunkZ) {

        byte[] blocks = new byte[16 * 16 * 256];
        if (chankX == 0 && chunkZ == 0) {
            HashMap<Integer, Byte> map = new HashMap<>();
            for (int x = 0; x < 10; x++) {
                for (int z = 0; z < 10; z++) {
                    map.put(this.xyzToByte(x, 61, z), (byte)Material.BEDROCK.getId());
                }
            }
            for (int x = 0; x < 10; x++) {
                for (int z = 0; z < 10; z++) {
                    map.put(this.xyzToByte(x, 62, z), (byte)Material.GRASS.getId());
                }
            }
            for (int i = 0; i < blocks.length; i++) {
                if (map.containsKey(i)) {
                    blocks[i] = map.get(i);
                } else {
                    blocks[i] = (byte)Material.AIR.getId();
                }
            }
        } else {
            for (int i = 0; i < blocks.length; i++) {
                blocks[i] = (byte)Material.AIR.getId();
            }
        }
        return blocks;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 64, 0);
    }

    public int xyzToByte(int x, int y, int z) {
        return (x * 16 + z) * 256 + y;
    }
}
