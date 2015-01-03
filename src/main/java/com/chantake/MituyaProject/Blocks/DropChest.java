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
package com.chantake.MituyaProject.Blocks;

import com.chantake.MituyaProject.MituyaProject;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

/**
 *
 * @author いんく 吸引力の変わらないただひとつのチェスト
 */
public class DropChest implements Runnable {

    MituyaProject plugin;

    public DropChest(MituyaProject plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (World world : plugin.getServer().getWorlds()) {
            for (Entity e : world.getEntities()) {

                if (e instanceof Item) {
                    Item item = (Item)e;
                    Block block = world.getBlockAt(item.getLocation().subtract(0, 1, 0));
                    if (block.isBlockPowered()) {
                        if (block.getType() == Material.CHEST)//Chestを設定
                        {

                            Chest chest = (Chest)block.getState();
                            if (chest.getInventory().firstEmpty() != -1) {
                                item.remove();
                                chest.getInventory().addItem(item.getItemStack());
                            }
                        }

                    }
                    if (block.getType() == Material.DISPENSER)//Dispenserを設定
                    {
                        Dispenser disp = (Dispenser)block.getState();
                        Inventory inv = disp.getInventory();
                        if (inv.firstEmpty() != -1) {
                            item.remove();
                            inv.addItem(item.getItemStack());
                        }
                    }
                }
                if (world.getBlockAt(e.getLocation().subtract(0, 1, 0)).isBlockPowered()) {
                    //<editor-fold defaultstate="Creeper" desc="creeper">
                    if (e instanceof Creeper) {
                        Creeper creeper = (Creeper)e;
                        Block block = world.getBlockAt(creeper.getLocation().subtract(0, 1, 0));
                        if (block.isBlockPowered()) {
                            if (block.getType() == Material.CHEST)//Chestを設定
                            {
                                Chest chest = (Chest)block.getState();
                                if (chest.getInventory().firstEmpty() != -1) {
                                    creeper.setHealth(0);
                                    creeper.getWorld().createExplosion(creeper.getLocation(), 0);
                                    Random ra = new Random();
                                    chest.getInventory().addItem(new ItemStack(289, ra.nextInt(5)));
                                }
                            }
                        }
                    }
                //</editor-fold>
                    //<editor-fold defaultstate="Zombie" desc="Zombie">
                    if (e instanceof Zombie) {
                        Zombie zombie = (Zombie)e;
                        Block block = world.getBlockAt(zombie.getLocation().subtract(0, 1, 0));
                        if (block.getType() == Material.CHEST)//Chestを設定
                        {
                            Chest chest = (Chest)block.getState();
                            if (chest.getInventory().firstEmpty() != -1) {
                                zombie.remove();
                                chest.getInventory().addItem(new ItemStack(367));
                            }
                        }

                    }
                //</editor-fold>
                    //<editor-fold defaultstate="Skeleton" desc="Skeleton">
                    if (e instanceof Skeleton) {
                        Skeleton skeleton = (Skeleton)e;
                        Block block = world.getBlockAt(skeleton.getLocation().subtract(0, 1, 0));
                        if (block.getType() == Material.CHEST)//Chestを設定
                        {
                            Chest chest = (Chest)block.getState();
                            if (chest.getInventory().firstEmpty() != -1) {
                                skeleton.remove();
                                Random ra = new Random();
                                Random item = new Random();
                                if (ra.nextBoolean()) {
                                    chest.getInventory().addItem(new ItemStack(262, item.nextInt(3)));
                                } else {
                                    chest.getInventory().addItem(new ItemStack(352, item.nextInt(3)));
                                }
                            }
                        }

                    }
                //</editor-fold>
                    //<editor-fold defaultstate="Spider" desc="Spider">
                    if (e instanceof Spider) {
                        Spider spider = (Spider)e;
                        Block block = world.getBlockAt(spider.getLocation().subtract(0, 1, 0));
                        if (block.getType() == Material.CHEST)//Chestを設定
                        {
                            Chest chest = (Chest)block.getState();
                            if (chest.getInventory().firstEmpty() != -1) {
                                spider.remove();
                                Random ra = new Random();
                                if (ra.nextBoolean()) {
                                    chest.getInventory().addItem(new ItemStack(375));
                                } else {
                                    chest.getInventory().addItem(new ItemStack(287, new Random().nextInt(4)));
                                }
                            }
                        }

                    }
                //</editor-fold>
                    //<editor-fold defaultstate="Enderman" desc="えんだあああああああああああああああいやああああああああああああ">
                    if (e instanceof Enderman) {
                        Enderman enderman = (Enderman)e;
                        Block block = world.getBlockAt(enderman.getLocation().subtract(0, 1, 0));
                        if (block.getType() == Material.CHEST)//Chestを設定
                        {
                            Chest chest = (Chest)block.getState();
                            if (chest.getInventory().firstEmpty() != -1) {
                                enderman.remove();
                                Random ra = new Random();
                                if (ra.nextInt(3) == 1) {
                                    chest.getInventory().addItem(new ItemStack(368));
                                }
                            }
                        }

                    }
                //</editor-fold>
                    //<editor-fold defaultstate="PigZombie" desc="PigZombie">
                    if (e instanceof PigZombie) {
                        PigZombie pigzombie = (PigZombie)e;
                        Block block = world.getBlockAt(pigzombie.getLocation().subtract(0, 1, 0));
                        if (block.getType() == Material.CHEST)//Chestを設定
                        {
                            Chest chest = (Chest)block.getState();
                            if (chest.getInventory().firstEmpty() != -1) {
                                pigzombie.remove();
                                Random ra = new Random();
                                switch (ra.nextInt(3)) {
                                    case 1:
                                        chest.getInventory().addItem(new ItemStack(371));
                                        break;
                                    case 2:
                                        chest.getInventory().addItem(new ItemStack(367));
                                        break;
                                    default:
                                        Random ras = new Random();
                                        if (ras.nextInt(6) == 2) {
                                            chest.getInventory().addItem(new ItemStack(266));
                                        }
                                        break;
                                }
                            }
                        }
                    }
                //</editor-fold>
                    //<editor-fold defaultstate="Pig" desc="(・。。・)">
                    if (e instanceof Pig) {
                        Pig pig = (Pig)e;
                        Block block = world.getBlockAt(pig.getLocation().subtract(0, 1, 0));
                        if (block.getType() == Material.CHEST)//Chestを設定
                        {
                            Chest chest = (Chest)block.getState();
                            if (chest.getInventory().firstEmpty() != -1) {
                                pig.remove();
                                Random ra = new Random();
                                if (pig.getFireTicks() > 1) {
                                    chest.getInventory().addItem(new ItemStack(320, ra.nextInt(4)));
                                } else {
                                    chest.getInventory().addItem(new ItemStack(319, ra.nextInt(4)));
                                }

                            }
                        }
                    }
                //</editor-fold>
                    //<editor-fold defaultstate="Cow" desc="(^・。。・^)">
                    if (e instanceof Cow) {
                        Cow cow = (Cow)e;
                        Block block = world.getBlockAt(cow.getLocation().subtract(0, 1, 0));
                        if (block.getType() == Material.CHEST)//Chestを設定
                        {
                            Chest chest = (Chest)block.getState();
                            if (chest.getInventory().firstEmpty() != -1) {
                                cow.remove();
                                Random ra = new Random();
                                if (cow.getFireTicks() > 1) {
                                    chest.getInventory().addItem(new ItemStack(364, ra.nextInt(4)));
                                } else {
                                    chest.getInventory().addItem(new ItemStack(363, ra.nextInt(4)));
                                }

                            }
                        }
                    }
                    //<editor-fold defaultstate="Sheep" desc="[・ω・]">
                    if (e instanceof Sheep) {
                        Sheep sheep = (Sheep)e;
                        Block block = world.getBlockAt(sheep.getLocation().subtract(0, 1, 0));
                        if (block.getType() == Material.CHEST)//Chestを設定
                        {
                            Chest chest = (Chest)block.getState();
                            if (chest.getInventory().firstEmpty() != -1) {
                                sheep.remove();
                                /*int scolor = 0;
                                 switch(sheep.getColor())
                                 {
                                 case BLACK:      scolor = 15;
                                 case RED:        scolor = 14;
                                 case GREEN:      scolor = 13;
                                 case BROWN:      scolor = 12;
                                 case BLUE:       scolor = 11;
                                 case PURPLE:     scolor = 10;
                                 case CYAN:       scolor =  9;
                                 case GRAY:       scolor =  7;
                                 case PINK:       scolor =  6;
                                 case LIME:       scolor =  5;
                                 case YELLOW:     scolor =  4;
                                 case LIGHT_BLUE: scolor =  3;
                                 case MAGENTA:    scolor =  2;
                                 case ORANGE:     scolor =  1;
                                 } OTL.....書いた後にsetColorがあるのに気づいた・・・・*/
                                Random ra = new Random();
                                Wool wo = new Wool();
                                wo.setColor(sheep.getColor());

                                chest.getInventory().addItem(wo.toItemStack(ra.nextInt(4)));

                            }
                        }
                    }
                    //</editor-fold>
                }
            }
        }
    }

}
