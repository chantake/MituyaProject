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
public interface ServerMBean {

    public String checkCPU();

    public String checkMemory();

    public void dropItem(String playername, int itemid, int type, int amount);

    public long getJackpot();

    public long getTAX();

    public int getOnlineUser();

    public void runCommand(String playername, String command);

    public void sayMesseage(String command);

    public void setJackpot(long i);

    public void shutdown(int time);

    public void shutdownnow();

    public void summonMonsters(String chara, String monstername, int amount);
}
