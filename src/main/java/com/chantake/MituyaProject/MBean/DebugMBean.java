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

/**
 *
 * @author fumitti
 */
public interface DebugMBean {

    public void bottiPlayer(String name);

    public void unbochiPlayer(String name);

    public int getEntitys();

    public int getLivingEntitys();

    public int getLoadedChunks();

    public int getLimitJackpot1();

    public int getjackpot2();

    public int getLimitJackpot2();

    public void setLimitJackpot1(int a);

    public void setjackpot2(int a);

    public void setLimitJackpot2(int a);

    public long getJackpot();

    public void setJackpot(long i);

    public void Selfdestruct(String name, int amount);

    public void fireinthehole(String name, int amount, boolean naparm);

    public void fireball(String name, int amount, boolean naparm);

    public void AMMOAIRDROP(String name, int amount);

    public void ExperienceOrbAIRDROP(String name, int amount, int exp);

    public int getOnlineUser();
}
