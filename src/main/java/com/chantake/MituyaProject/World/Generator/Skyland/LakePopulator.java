package com.chantake.MituyaProject.World.Generator.Skyland;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldGenReed;
import org.bukkit.Chunk;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

public class LakePopulator extends BlockPopulator {

    private final Random random;

    public LakePopulator(org.bukkit.World world) {
        this.random = new Random(world.getSeed());
    }

    @Override
    public void populate(org.bukkit.World world, Random random, Chunk chunk) {
        World mcWorld = (World)world;

        int worldChunkX = chunk.getX() * 16;
        int worldChunkZ = chunk.getZ() * 16;

        if (this.random.nextInt(4) == 0) {
            int x = worldChunkX + this.random.nextInt(16) + 8;
            int z = worldChunkZ + this.random.nextInt(16) + 8;

            if (!Arrays.asList(new Biome[]{Biome.JUNGLE, Biome.JUNGLE_HILLS}).contains(world.getBiome(x, z))) {
                int y = world.getHighestBlockYAt(x, z) + 2;

                if (this.random.nextInt(100) < 85) {
                    //new WorldGenLakes(Material.STATIONARY_WATER).a(mcWorld, this.random, x, y, z);
                    WorldGenReed worldGenReed = new WorldGenReed();
                    worldGenReed.generate(mcWorld, random, BlockPosition.ZERO);
                } else {
                    //new WorldGenLakes(Material.STATIONARY_LAVA.getId()).a(mcWorld, this.random, x, y, z);
                }
            }
        }
    }
}
