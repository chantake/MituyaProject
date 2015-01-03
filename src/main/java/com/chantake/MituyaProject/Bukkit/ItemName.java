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

import com.chantake.MituyaProject.Tool.StringUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author chantake
 */
public class ItemName {

    public static String getItemName(String id) {
        short data = 0;
        String[] split = id.split(":");
        int i = Integer.valueOf(split[0]);
        if (split.length > 1) {
            data = Short.valueOf(split[1]);
        }
        return getItemName(i, data);
    }

    public static String getItemName(ItemStack itemstack) {
        return getItemName(itemstack.getTypeId(), itemstack.getDurability());
    }

    public static String getItemName(int typeid, short data) {
        Material material = Material.getMaterial(typeid);
        String[] split = material.toString().toLowerCase().split("_");
        String name = "";
        for (String sp : split) {
            //先頭文字を大文字に変換
            name += StringUtil.capitalize(sp);
        }
        name = getDataName(name, typeid, data);
        return name;
    }

    public static int getItem(String name) {
        name = name.toLowerCase();
        for (Material mt : Material.values()) {
            if (mt.toString().toLowerCase().startsWith(name)) {
                return mt.getId();
            }
        }
        return 0;
    }

    private static String getDataName(String itemname, int typeid, short data) {
        String name = "";
        switch (typeid) {
            case 6:
                switch (data) {
                    case 1:
                        name = "Redwood";
                        break;
                    case 2:
                        name = "Birch";
                        break;
                }
                return name + itemname;
            case 17:
                switch (data) {
                    case 1:
                        name = "Redwood";
                        break;
                    case 2:
                        name = "Birch";
                        break;
                    default:
                        name = "Wood";
                        break;
                }
                return name;
            case 18:
                switch (data) {
                    case 1:
                        name = "Redwood";
                        break;
                    case 2:
                        name = "Birch";
                        break;
                }
                return name + itemname;
            case 35:
                switch (data) {
                    case 1:
                        name = "Orange";
                        break;
                    case 2:
                        name = "Magenta";
                        break;
                    case 3:
                        name = "LightBlue";
                        break;
                    case 4:
                        name = "Yellow";
                        break;
                    case 5:
                        name = "LightGreen";
                        break;
                    case 6:
                        name = "Pink";
                        break;
                    case 7:
                        name = "Gray";
                        break;
                    case 8:
                        name = "LightGray";
                        break;
                    case 9:
                        name = "Cyan";
                        break;
                    case 10:
                        name = "Purple";
                        break;
                    case 11:
                        name = "Blue";
                        break;
                    case 12:
                        name = "Brown";
                        break;
                    case 13:
                        name = "DarkGreen";
                        break;
                    case 14:
                        name = "Red";
                        break;
                    case 15:
                        name = "Black";
                        break;
                    default:
                        name = "White";
                        break;
                }
                return name + itemname;
            case 43:
                switch (data) {
                    case 1:
                        name = "Sandstone";
                        break;
                    case 2:
                        name = "Wooden";
                        break;
                    case 3:
                        name = "Cobblestone";
                        break;
                    case 4:
                        name = "Brick";
                        break;
                    case 5:
                        name = "StoneBrick";
                        break;
                    default:
                        name = "Stone";
                        break;
                }
                return "Double" + name + "Step";
            case 44:
                switch (data) {
                    case 1:
                        name = "Sandstone";
                        break;
                    case 2:
                        name = "Wooden";
                        break;
                    case 3:
                        name = "Cobblestone";
                        break;
                    case 4:
                        name = "Brick";
                        break;
                    case 5:
                        name = "StoneBrick";
                        break;
                    default:
                        name = "Stone";
                        break;
                }
                return name + "Step";
            case 97:
                switch (data) {
                    case 1:
                        name = "Cobblestone";
                        break;
                    case 2:
                        name = "Stone Brick";
                        break;
                    default:
                        name = "Stone";
                        break;
                }
                return name;
            case 98:
                switch (data) {
                    case 1:
                        name = "Mossy";
                        break;
                    case 2:
                        name = "Cracked";
                        break;
                }
                return name + "StoneBrick";
            case 263:
                switch (data) {
                    case 1:
                        name = "Charcoal";
                        break;
                    default:
                        name = "Coal";
                        break;
                }
                return name;
            case 351:
                switch (data) {
                    case 1:
                        name = "RoseRed";
                        break;
                    case 2:
                        name = "CactusGreen";
                        break;
                    case 3:
                        name = "CocoBeans";
                        break;
                    case 4:
                        name = "LapisLazuli";
                        break;
                    case 5:
                        name = "PurpleDye";
                        break;
                    case 6:
                        name = "CyanDye";
                        break;
                    case 7:
                        name = "LightGrayDye";
                        break;
                    case 8:
                        name = "GrayDye";
                        break;
                    case 9:
                        name = "PinkDye";
                        break;
                    case 10:
                        name = "LimeDye";
                        break;
                    case 11:
                        name = "DandelionYellow";
                        break;
                    case 12:
                        name = "LightBlueDye";
                        break;
                    case 13:
                        name = "MagentaDye";
                        break;
                    case 14:
                        name = "OrangeDye";
                        break;
                    case 15:
                        name = "BoneMeal";
                        break;
                    default:
                        name = "InkSack";
                        break;
                }
                return name;
            case 373:
                switch (data) {
                    case 16:
                        name = "AwkwardPotion";
                        break;
                    case 32:
                        name = "ThickPotion";
                        break;
                    case 64:
                        name = "MundanePotion";
                        break;
                    case 8193:
                        name = "RegenerationPotion";
                        break;
                    case 8194:
                        name = "SwiftnessPotion";
                        break;
                    case 8195:
                        name = "PoisonPotion";
                        break;
                    case 8196:
                        name = "LightGrayDye";
                        break;
                    case 8197:
                        name = "HealingPotion";
                        break;
                    case 8200:
                        name = "WeaknessPotion";
                        break;
                    case 8201:
                        name = "StrengthPotion";
                        break;
                    case 8202:
                        name = "SlownessPotion";
                        break;
                    case 8204:
                        name = "HarmingPotion";
                        break;
                    case 8225:
                        name = "RegenerationPotionII";
                        break;
                    case 8226:
                        name = "SwiftnessPotionII";
                        break;
                    case 8228:
                        name = "PoisonPotionII";
                        break;
                    case 8229:
                        name = "HealingPotionII";
                        break;
                    case 8233:
                        name = "StrengthPotionII";
                        break;
                    case 8257:
                        name = "RegenerationPotion";
                        break;
                    case 8258:
                        name = "SwiftnessPotion";
                        break;
                    case 8259:
                        name = "FireResistancePotion";
                        break;
                    case 8260:
                        name = "PoisonPotion";
                        break;
                    case 8264:
                        name = "WeaknessPotion";
                        break;
                    case 8265:
                        name = "StrengthPotion";
                        break;
                    case 8266:
                        name = "SlownessPotion";
                        break;
                    case 16378:
                        name = "FireResistanceSplash";
                        break;
                    case 16385:
                        name = "RegenerationSplash";
                        break;
                    case 16386:
                        name = "SwiftnessSplash";
                        break;
                    case 16388:
                        name = "PoisonSplash";
                        break;
                    case 16389:
                        name = "HealingSplash";
                        break;
                    case 16392:
                        name = "WeaknessSplash";
                        break;
                    case 16393:
                        name = "StrengthSplash";
                        break;
                    case 16394:
                        name = "SlownessSplash";
                        break;
                    case 16396:
                        name = "HarmingSplash";
                        break;
                    case 16418:
                        name = "SwiftnessSplashII";
                        break;
                    case 16420:
                        name = "PoisonSplashII";
                        break;
                    case 16425:
                        name = "StrengthSplashII";
                        break;
                    case 16428:
                        name = "HarmingSplashII";
                        break;
                    case 16449:
                        name = "RegenerationSplash";
                        break;
                    case 16450:
                        name = "SwiftnessSplash";
                        break;
                    case 16451:
                        name = "FireResistanceSplash";
                        break;
                    case 16452:
                        name = "PoisonSplash";
                        break;
                    case 16456:
                        name = "WeaknessSplash";
                        break;
                    case 16457:
                        name = "StrengthSplash";
                        break;
                    case 16458:
                        name = "SlownessSplash";
                        break;
                    case 16471:
                        name = "RegenerationSplashII";
                        break;
                    default:
                        name = "WaterBottle";
                        break;
                }
                return name;
        }
        return itemname;
    }
}
