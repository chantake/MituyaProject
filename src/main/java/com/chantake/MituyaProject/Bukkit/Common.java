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
package com.chantake.MituyaProject.Bukkit;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class Common {

    public static void ItemDrop(int id, int type, int ammount, World wr, Location lo) {
        final ItemStack is = new ItemStack(id);
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
            wr.dropItem(lo, is);
        } else {
            for (int i = 0; i < r; i++) {
                is.setAmount(64);
                wr.dropItem(lo, is);
            }
            if (rr > 0) {
                is.setAmount(rr);
                wr.dropItem(lo, is);
            }
        }
    }

    static public short getDyeColor(DyeColor color) {
        switch (color) {
            case WHITE:
                return 0xF;
            case ORANGE:
                return 0xE;
            case MAGENTA:
                return 0xD;
            case LIGHT_BLUE:
                return 0xC;
            case YELLOW:
                return 0xB;
            case LIME:
                return 0xA;
            case PINK:
                return 0x9;
            case GRAY:
                return 0x8;
            case SILVER:
                return 0x7;
            case CYAN:
                return 0x6;
            case PURPLE:
                return 0x5;
            case BLUE:
                return 0x4;
            case BROWN:
                return 0x3;
            case GREEN:
                return 0x2;
            case RED:
                return 0x1;
            case BLACK:
                return 0x0;
            default:
                return 0x0;
        }
    }

    static public short getFixedMaxDurability(Material m) {
        // If the maxstacksize is -1, then the values are the wrong way round
        return (short)(m.getMaxStackSize() < 1 ? m.getMaxStackSize() : m.getMaxDurability());
    }

    static public int getFixedMaxStackSize(Material m) {
        return m.getMaxStackSize() < 1 ? m.getMaxDurability() : m.getMaxStackSize();
    }

    static public short getWoolColor(DyeColor color) {
        switch (color) {
            case WHITE:
                return 0x0;
            case ORANGE:
                return 0x1;
            case MAGENTA:
                return 0x2;
            case LIGHT_BLUE:
                return 0x3;
            case YELLOW:
                return 0x4;
            case LIME:
                return 0x5;
            case PINK:
                return 0x6;
            case GRAY:
                return 0x7;
            case SILVER:
                return 0x8;
            case CYAN:
                return 0x9;
            case PURPLE:
                return 0xA;
            case BLUE:
                return 0xB;
            case BROWN:
                return 0xC;
            case GREEN:
                return 0xD;
            case RED:
                return 0xE;
            case BLACK:
                return 0xF;
            default:
                return 0xF;
        }
    }
}
