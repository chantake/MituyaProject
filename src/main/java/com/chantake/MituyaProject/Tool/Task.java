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
package com.chantake.MituyaProject.Tool;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author chantake
 */
public class Task implements Runnable {

    private final JavaPlugin plugin;
    private int id = -1;

    public Task(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public final JavaPlugin getPlugin() {
        return this.plugin;
    }

    public boolean isRunning() {
        return (this.id != -1) && (Bukkit.getServer().getScheduler().isCurrentlyRunning(this.id));
    }

    public boolean isQueued() {
        return Bukkit.getServer().getScheduler().isQueued(this.id);
    }

    public static boolean stop(Task task) {
        if (task == null) {
            return false;
        }
        task.stop();
        return true;
    }

    public Task stop() {
        if (this.id != -1) {
            Bukkit.getServer().getScheduler().cancelTask(this.id);
            this.id = -1;
        }
        return this;
    }

    public Task start() {
        this.id = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this);
        return this;
    }

    public Task start(long delay) {
        this.id = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this, delay);
        return this;
    }

    public Task start(long delay, long interval) {
        this.id = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, this, delay, interval);
        return this;
    }

    @Override
    public void run() {
    }
}
