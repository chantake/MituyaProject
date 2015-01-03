/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chantake.MituyaProject.World.Generator.listener;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.World.Generator.Skyland.SkyLandChunkGenerator;
import java.lang.reflect.Field;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.WorldInitEvent;

/**
 *
 * @author chantake
 */
public class SkyLandListener implements Listener {

    private final MituyaProject plugin;

    public SkyLandListener(MituyaProject plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Material changed = event.getChangedType();

        if (changed == Material.SAND || changed == Material.GRAVEL) {
            if (event.getBlock().getWorld().getGenerator() instanceof SkyLandChunkGenerator) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL && event.getLocation().getWorld().getGenerator() instanceof SkyLandChunkGenerator) {
            int total = 0;

            for (Entity entity : event.getLocation().getChunk().getEntities()) {
                if (entity.getClass().equals(event.getEntity().getClass())) {
                    ++total;

                    if (total > 4) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldInit(WorldInitEvent event) {
        World world = event.getWorld();

        if (((world.getGenerator() instanceof SkyLandChunkGenerator)) && (world.getEnvironment() == World.Environment.NORMAL)) {
            net.minecraft.server.v1_8_R1.WorldServer worldServer = ((CraftWorld)world).getHandle();
            try {
                Class worldData = worldServer.worldData.getClass();

                Field type = worldData.getDeclaredField("type");
                type.setAccessible(true);

                type.set(worldServer.worldData, WorldType.NORMAL);

                plugin.Log("The world type of '" + world.getName() + "' has been set to to normal.");
            }
            catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                plugin.Log("Could not change the world type of '" + world.getName() + "'.");
            }
        }
    }
}
