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
package com.chantake.MituyaProject.Player;

/**
 *
 * @author fumitti
 */
public interface PlayerInstanceMBean {

    public long getMine();

    public int getMineId();

    public String getDisplayName();

    public String getPlayerListName();

    public float getExhaustion();

    public float getSaturation();

    public float getExp();

    public int getFoodLevel();

    public int getLevel();

    public int getTotalExperience();

    public double getHealth();

    public double getMaxHealth();

    public int getMaximumAir();

    public int getRemainingAir();

    public int getMaxFireTicks();

    public int getFireTicks();

    public void damage(int amount);

    public void shootArrow();

    public void throwEgg();

    public void throwEnderPearl();

    public void throwFireball();

    public void throwSmallFireBall();

    public void throwSnowball();

    public void SaveAll();

    public void chat(String str);

    public void setMine(long i);

    public void setHealth(double health);

    public void setMaximumAir(int ticks);

    public void setRemainingAir(int ticks);

    public void setDisplayName(String name);

    public void setExhaustion(float value);

    public void setExp(float exp);

    public void setFoodLevel(int value);

    public void setLevel(int level);

    public void setFireTicks(int ticks);

    public void setPlayerListName(String name);

    public void setSaturation(float value);

    public void setTotalExperience(int exp);

    public void showPlayer(String player);

    public void hidePlayer(String player);

    public void kickPlayer(String message);

    public void playEntityEffect(String type);

    public String checkEntityEffectType();

    public void playEffect(String effect, int data);

    public String checkEffectType();

    public void addPotionEffect(String type, int duration, int amplifier);

    public String checkPotionEffectType();

    public void summonMonsters(String monstername, int amount);

    public void addUnsafeEnchantment(String type, int level);

    public void addEnchantment(String type, int level);

    public String checkEnchantmentType();

    public void playSound(String sound, float volume, float note);

    public String checkSoundType();
}
