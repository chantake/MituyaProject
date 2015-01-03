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
package com.chantake.MituyaProject.Event;

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Tool.Timer.TimerManager;
import com.chantake.MituyaProject.Tool.Tools;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

/**
 *
 * @author chantake
 */
public class EventManager {

    private final Invocable iv;
    final private MituyaProject plugin;
    private final Properties props = new Properties();
    private final String name;

    public EventManager(MituyaProject plugin, Invocable iv, String name) {
        this.iv = iv;
        this.plugin = plugin;
        this.name = name;
    }

    public MituyaProject getPlugin() {
        return this.plugin;
    }

    public void SpawnCreature(String creature, String world, int x, int y, int z) {
        World wd = this.getPlugin().getServer().getWorld(world);
        Location loc = new Location(wd, x, y, z);
        EntityType et = EntityType.fromName(creature);
        if (wd != null && et != null) {
            this.spawnEntity(et, loc);
        } else {
            this.plugin.ErrLog("SpawnCreature Null creature:" + creature + " world:" + world + " x:" + x + " y:" + y + " z:" + z);
        }
    }

    /**
     * Entityを召喚(Spawn)します
     *
     * @param entityType EntityType
     * @param location Locaiton
     */
    public void spawnEntity(EntityType entityType, Location location) {
        location.getWorld().spawnEntity(location, entityType);
    }

    public float getExp() {
        return Parameter328.Exp;
    }

    public void setExp(String exp) {
        Parameter328.Exp = Float.valueOf(exp);
    }

    public int getDropRate() {
        return Parameter328.DropRate;
    }

    public void setDropRate(int rate) {
        Parameter328.DropRate = rate;
    }

    public void broadcastEventMessage(String message) {
        this.getPlugin().broadcastMessage("[" + ChatColor.YELLOW + "Event" + ChatColor.WHITE + "] " + Tools.ChangeText(message, plugin, null));
    }

    public void cancel() {
        try {
            iv.invokeFunction("cancelSchedule", (Object)null);
        }
        catch (ScriptException | NoSuchMethodException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void schedule(final String methodName, long delay) {
        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin,
                new Runnable() {

                    @Override
                    public void run() {
                        try {
                            iv.invokeFunction(methodName, (Object)null);
                        }
                        catch (ScriptException | NoSuchMethodException ex) {
                            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }, delay);
    }

    public ScheduledFuture<?> scheduleAtTimestamp(final String methodName, long timestamp) {
        return TimerManager.getInstance().scheduleAtTimestamp(new Runnable() {

            @Override
            public void run() {
                synchronized (plugin.getServer()) {
                    try {
                        iv.invokeFunction(methodName, (Object)null);
                    }
                    catch (ScriptException | NoSuchMethodException ex) {
                        Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }, timestamp);
    }
    /*public void scheduleAtTimestamp(final String methodName, long timestamp) {
     this.getPlugin().getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
    
     @Override
     public void run() {
     try {
     iv.invokeFunction(methodName, (Object) null);
     } catch (ScriptException ex) {
     Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
     } catch (NoSuchMethodException ex) {
     Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
     }
     }
     }, ((timestamp - System.currentTimeMillis()) / 1000) * 20L);
     }*/
}
