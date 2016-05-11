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

public class FlowerPopulator extends BlockPopulator {

    private final Random random;

    public FlowerPopulator(World world) {
        this.random = new Random(world.getSeed());
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        List iceBiomes = Arrays.asList(new Biome[]{Biome.TAIGA, Biome.TAIGA_HILLS, Biome.ICE_FLATS, Biome.ICE_MOUNTAINS, Biome.FROZEN_RIVER, Biome.FROZEN_OCEAN});

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y++) {
                    if (y > 4) {
                        Block block = chunk.getBlock(x, y, z);
                        Block ground = block.getRelative(BlockFace.DOWN);
                        Biome biome = block.getBiome();

                        if (ground.getType() == Material.GRASS) {
                            if (biome == Biome.PLAINS) {
                                if (this.random.nextInt(100) < 7) {
                                    block.setType(this.random.nextInt(100) < 75 ? Material.YELLOW_FLOWER : Material.RED_ROSE);
                                }
                            } else {
                                if ((biome == Biome.DESERT) || (iceBiomes.contains(biome))
                                        || (this.random.nextInt(100) >= 2)) {
                                    continue;
                                }
                                block.setType(this.random.nextInt(100) < 75 ? Material.YELLOW_FLOWER : Material.RED_ROSE);
                            }
                        }
                    }
                }
            }
        }
    }
}
