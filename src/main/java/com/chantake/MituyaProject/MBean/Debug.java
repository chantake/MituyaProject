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
import com.chantake.MituyaProject.World.Jackpot;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 *
 * @author fumitti
 */
public class Debug implements DebugMBean {

    public List<Chunk> ll2 = new LinkedList<>();
    private final MituyaProject plugin;

    public Debug(MituyaProject plugin) {
        this.plugin = plugin;
    }

    @Override
    public int getEntitys() {
        int i = 0;
        for (World wr : plugin.getWorldManager().getWorlds()) {
            i = +wr.getEntities().size();
        }
        return i;
    }

    @Override
    public int getLivingEntitys() {
        int i = 0;
        for (World wr : plugin.getWorldManager().getWorlds()) {
            i = +wr.getLivingEntities().size();
        }
        return i;
    }

    public List<Chunk> getLl2() {
        return ll2;
    }

    @Override
    public int getLoadedChunks() {
        return ll2.size();
    }

    @Override
    public int getOnlineUser() {
        return plugin.getServer().getOnlinePlayers().size();
    }

    @Override
    public void Selfdestruct(String name, int amount) {
        try {
            Player player = plugin.getInstanceManager().matchSingleInstance(name).getPlayer();
            player.getWorld().createExplosion(player.getLocation(), amount, false);
        }
        catch (PlayerOfflineException ex) {
            Logger.getLogger(Debug.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void AMMOAIRDROP(String name, int amount) {
        try {
            Player player = plugin.getInstanceManager().matchSingleInstance(name).getPlayer();
            Location location = player.getLocation();
            location.setY(256);
            ThrownPotion potion = (ThrownPotion)location.getWorld().spawnEntity(location, EntityType.SPLASH_POTION);
            // Now apply whatever to the potion
            Collection<PotionEffect> effects = potion.getEffects();
            effects.clear();
            effects.add(new PotionEffect(PotionEffectType.HEAL, 10, amount));
            potion.setShooter(player);
        }
        catch (PlayerOfflineException ex) {
            Logger.getLogger(Debug.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void fireinthehole(String name, int amount, boolean napa) {
        try {
            Player player = plugin.getInstanceManager().matchSingleInstance(name).getPlayer();
            Location location = player.getLocation();
            location.setY(location.getY() + 10);
            TNTPrimed spawn = (TNTPrimed)location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
            spawn.setFuseTicks(amount);
            spawn.setIsIncendiary(napa);
            spawn.setYield(amount);
            spawn.setVelocity(new Vector(0, -10, 0));
        }
        catch (PlayerOfflineException ex) {
            Logger.getLogger(Debug.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void ExperienceOrbAIRDROP(String name, int amount, int exp) {
        try {
            Player player = plugin.getInstanceManager().matchSingleInstance(name).getPlayer();
            Location location = player.getLocation();
            location.setY(256);
            for (int i = 0; i < amount; i++) {
                ExperienceOrb spawn = (ExperienceOrb)location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
                spawn.setExperience(exp);
            }
        }
        catch (PlayerOfflineException ex) {
            Logger.getLogger(Debug.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void fireball(String name, int amount, boolean naparm) {
        try {
            Player player = plugin.getInstanceManager().matchSingleInstance(name).getPlayer();
            Location location = player.getLocation();
            location.setY(location.getY() - 2);
            Fireball spawn = (Fireball)location.getWorld().spawnEntity(location, EntityType.FIREBALL);
            spawn.setDirection(player.getEyeLocation().getDirection());
            spawn.setIsIncendiary(naparm);
            spawn.setYield(amount);
            spawn.setVelocity(new Vector(0, -10, 0));
        }
        catch (PlayerOfflineException ex) {
            Logger.getLogger(Debug.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getLimitJackpot1() {
        return Jackpot.LimitJackpot1;
    }

    @Override
    public int getjackpot2() {
        return Jackpot.jackpot2;
    }

    @Override
    public int getLimitJackpot2() {
        return Jackpot.LimitJackpot2;
    }

    @Override
    public void setLimitJackpot1(int a) {
        Jackpot.LimitJackpot1 = a;
    }

    @Override
    public void setjackpot2(int a) {
        Jackpot.jackpot2 = a;
    }

    @Override
    public void setLimitJackpot2(int a) {
        Jackpot.LimitJackpot2 = a;
    }

    @Override
    public long getJackpot() {
        return Parameter328.jackpot;
    }

    @Override
    public void setJackpot(long i) {
        Parameter328.jackpot = i;
    }

    @Override
    public void bottiPlayer(String name) {
        try {
            Player player = plugin.getInstanceManager().matchSingleInstance(name).getPlayer();
            player.getServer().getOnlinePlayers();
            for (Player pl : player.getServer().getOnlinePlayers()) {
                player.hidePlayer(pl);
            }
        }
        catch (PlayerOfflineException ex) {
        }
    }

    @Override
    public void unbochiPlayer(String name) {
        try {
            Player player = plugin.getInstanceManager().matchSingleInstance(name).getPlayer();
            player.getServer().getOnlinePlayers();
            for (Player pl : player.getServer().getOnlinePlayers()) {
                player.showPlayer(pl);
            }
        }
        catch (PlayerOfflineException ex) {
        }
    }
}
