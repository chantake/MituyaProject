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
package com.chantake.MituyaProject.MBean;

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Tool.PerfomanceCheck;
import com.chantake.MituyaProject.Tool.Timer.AutoSave;
import com.chantake.MituyaProject.Tool.Timer.AutoStop;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author fumitti
 */
public class Server implements ServerMBean {

    private final MituyaProject plugin;

    public Server(MituyaProject plugin) {
        this.plugin = plugin;
    }

    @Override
    public String checkCPU() {
        return String.valueOf(PerfomanceCheck.getCpuUsage()) + "%";
    }

    @Override
    public String checkMemory() {
        return PerfomanceCheck.getMemoryInfo();
    }

    @Override
    public void dropItem(String playername, int itemid, int type, int ammount) {
        try {
            final PlayerInstance p = plugin.getInstanceManager().matchSingleInstance(playername);
            final ItemStack is = new ItemStack(type);
            final Location lo = p.getPlayer().getLocation();
            int r = 0;
            int rr = 0;
            boolean rrr = false;
            if (ammount > 64) {
                r = ammount / 64;
                rr = ammount % 64;
                rrr = true;
            }
            is.setDurability((short)type);
            lo.setY(lo.getY() + 1);
            if (!rrr) {
                is.setAmount(ammount);
                p.getPlayer().getWorld().dropItem(lo, is);
            } else {
                for (int i = 0; i < r; i++) {
                    is.setAmount(64);
                    p.getPlayer().getWorld().dropItem(lo, is);
                }
                is.setAmount(rr);
                p.getPlayer().getWorld().dropItem(lo, is);
            }
        }
        catch (PlayerOfflineException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public long getJackpot() {
        return Parameter328.jackpot;
    }

    @Override
    public int getOnlineUser() {
        return plugin.getServer().getOnlinePlayers().size();
    }

    @Override
    public void runCommand(String playername, String command) {
        try {
            Player player = plugin.getInstanceManager().matchSingleInstance(playername).getPlayer();
            player.chat(command);
        }
        catch (PlayerOfflineException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sayMesseage(String command) {
        plugin.broadcastMessage(ChatColor.LIGHT_PURPLE + "[Server] " + command);
    }

    @Override
    public void setJackpot(long i) {
        Parameter328.jackpot = i;
    }

    @Override
    public void shutdown(int time) {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new AutoSave(plugin), 10 * 20L, 10 * 20L);
    }

    @Override
    public void shutdownnow() {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new AutoStop(plugin, 0, false));
    }

    @Override
    public void summonMonsters(String playername, String monstername, int amount) {
        try {
            final PlayerInstance p = plugin.getInstanceManager().matchSingleInstance(playername);
            EntityType et = EntityType.fromName(monstername);
            if (monstername != null && et != null) {
                for (int i = 0; i <= amount; i++) {
                    p.getPlayer().getLocation();
                    p.getPlayer().getWorld().spawnCreature(p.getPlayer().getLocation(), et);
                }
            }
        }
        catch (final PlayerOfflineException e) {
        }
    }

    @Override
    public long getTAX() {
        return Parameter328.taxmine;
    }
}
