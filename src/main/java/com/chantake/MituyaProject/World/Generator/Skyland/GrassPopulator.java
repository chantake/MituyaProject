package com.chantake.MituyaProject.World.Generator.Skyland;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class GrassPopulator extends BlockPopulator {

    private final Random random;

    public GrassPopulator(World world) {
        this.random = new Random(world.getSeed());
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        List iceBiomes = Arrays.asList(new Biome[]{Biome.TAIGA, Biome.TAIGA_HILLS, Biome.ICE_FLATS, Biome.ICE_MOUNTAINS, Biome.FROZEN_RIVER, Biome.FROZEN_OCEAN});

        int worldChunkX = chunk.getX() * 16;
        int worldChunkZ = chunk.getZ() * 16;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int y = world.getHighestBlockYAt(worldChunkX + x, worldChunkZ + z);

                if (y > 5) {
                    Block block = chunk.getBlock(x, y, z);
                    Block ground = block.getRelative(BlockFace.DOWN);

                    Biome biome = world.getBiome(worldChunkX + x, worldChunkZ + z);

                    if ((ground.getType() == Material.GRASS) || (ground.getType() == Material.SAND)) {
                        if (biome == Biome.PLAINS) {
                            if (this.random.nextInt(100) < 35) {
                                block.setType(Material.LONG_GRASS);
                                block.setData((byte)1);
                            }
                        } else if (biome == Biome.DESERT) {
                            if (this.random.nextInt(100) < 3) {
                                block.setType(Material.DEAD_BUSH);
                            }
                        } else if ((biome == Biome.TAIGA) || (biome == Biome.TAIGA_HILLS)) {
                            if (this.random.nextInt(100) < 1) {
                                block.setType(Material.LONG_GRASS);
                                block.setData((byte)2);
                            }
                        } else if (iceBiomes.contains(biome)) {
                            if (this.random.nextInt(100) < 1) {
                                block.setType(Material.LONG_GRASS);
                                block.setData((byte)1);
                            }
                        } else if (this.random.nextInt(100) < 14) {
                            block.setType(Material.LONG_GRASS);
                            block.setData((byte)(this.random.nextInt(100) < 85 ? 1 : 2));
                        }
                    }
                }
            }
        }
    }
}
