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
package com.chantake.MituyaProject.Command;

import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.mituyaapi.commands.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

/**
 * @author chantake
 */
public class ShopCommands {

    @Command(aliases = {"shop", "sp", "しょｐ", "ショップ"}, desc = "shop commands", help = "コマンドショップコマンド")
    @NestedCommand({CommandShopCommands.class})
    public static void shop(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) {
    }

    @Command(aliases = {"buyhead"},
            usage = "", desc = "",
            flags = "", min = 1, max = 2)
    //@CommandPermissions({"mituya.server.buyhead"})
    @CommandPermissions({"mituya.shop.buyhead"})
    public static void buyhead(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        ItemStack is = new ItemStack(Material.SKULL_ITEM);
        is.setDurability((short) 3);
        SkullMeta itemMeta = (SkullMeta) is.getItemMeta();
        itemMeta.setOwner(message.getString(0));
        is.setItemMeta(itemMeta);//no need?
        switch (message.getString(0)) {
            case "chantake":
            case "fumitti":
                player.sendYesNo(message.getString(0) + "の頭を 1000 Mineで購入します。よろしいですか？", () -> {
                    try {
                        if (player.checkMine(-1000) && player.gainItem(is)) {
                            player.gainMine(-1000);
                            player.sendSuccess(message.getString(0) + "の頭を購入しました。");
                        }
                    } catch (PlayerOfflineException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "MHF_Alex":
            case "MHF_Blaze":
            case "MHF_CaveSpider":
            case "MHF_Chicken":
            case "MHF_Cow":
            case "MHF_Creeper":
            case "MHF_Enderman":
            case "MHF_Ghast":
            case "MHF_Golem":
            case "MHF_Herobrine":
            case "MHF_LavaSlime":
            case "MHF_MushroomCow":
            case "MHF_Ocelot":
            case "MHF_Pig":
            case "MHF_PigZombie":
            case "MHF_Sheep":
            case "MHF_Skeleton":
            case "MHF_Slime":
            case "MHF_Spider":
            case "MHF_Squid":
            case "MHF_Steve":
            case "MHF_Villager":
            case "MHF_WSkeleton":
            case "MHF_Zombie":
            case "MHF_Cactus":
            case "MHF_Cake":
            case "MHF_Chest":
            case "MHF_CoconutB":
            case "MHF_CoconutG":
            case "MHF_Melon":
            case "MHF_OakLog":
            case "MHF_Present1":
            case "MHF_Present2":
            case "MHF_Pumpkin":
            case "MHF_TNT":
            case "MHF_TNT2":
            case "MHF_ArrowUp":
            case "MHF_ArrowDown":
            case "MHF_ArrowLeft":
            case "MHF_ArrowRight":
            case "MHF_Exclamation":
            case "MHF_Question":
                player.sendYesNo(message.getString(0) + "の頭を 3000 Mineで購入します。よろしいですか？", () -> {
                    try {
                        if (player.checkMine(-3000) && player.gainItem(is)) {
                            player.gainMine(-3000);
                            player.sendSuccess(message.getString(0) + "の頭を購入しました。");
                        }
                    } catch (PlayerOfflineException e) {
                        e.printStackTrace();
                    }
                });
                break;
            default:
                if (Objects.equals(player.getPlayer().getName(), message.getString(0))) {
                    player.sendYesNo("自分の頭を 5000 Mineで購入します。よろしいですか？", () -> {
                        try {
                            if (player.checkMine(-5000) && player.gainItem(is)) {
                                player.gainMine(-5000);
                                player.sendSuccess("自分の頭を購入しました。");
                            }
                        } catch (PlayerOfflineException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    player.sendYesNo(message.getString(0) + "様の頭を 50000 Mineで購入します。よろしいですか？", () -> {
                        try {
                            if (player.checkMine(-50000) && player.gainItem(is)) {
                                player.gainMine(-50000);
                                player.sendSuccess(message.getString(0) + "様の頭を購入しました。");
                            }
                        } catch (PlayerOfflineException e) {
                            e.printStackTrace();
                        }
                    });
                }

                break;
        }
    }

    /*
     // <editor-fold defaultstate="collapsed" desc="villager">
     @Command(aliases = {"villager", "村人", "むらびと", "murabito", "bira-zi", "人身売買", "vil"}, usage = "", desc = "Village Buy Command",
     flags = "", min = 0, max = -1)
     public static void VillageBuy(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
     if (message.argsLength() > 1) {
     if (message.getString(0).equals("buy")) {
     if (plugin.getWorldManager().getWorldData(players.getWorld()).isMobStop()) {
     player.sendAttention("Mobが制限されているこのワールドでは買えません");
     return;
     }//ワールドでMobを制限している場合買えないようにする。
     if (Integer.parseInt(message.getString(1)) <= 5)//負荷防止で５匹以上は沸かせない！(金持ちめ････)
     {
     final int count;
     if (message.argsLength() >= 1) {
     count = Integer.parseInt(message.getString(1));
     } else {
     count = 1;
     }
     //ここでいろいろと設定

     final int price = Parameter328.Village * count;//値段を設定
     player.sendMineYesNo(price, new Runnable() {
     @Override
     public void run() {
     for (int i = 0; i < count; i++) {
     Villager vg = (Villager)players.getWorld().spawnCreature(players.getLocation(), EntityType.VILLAGER);//この辺で村人を償還
     Random ra = new Random();
     switch (ra.nextInt(5)) {
     case 1:
     vg.setProfession(Villager.Profession.BLACKSMITH);
     break;
     case 2:
     vg.setProfession(Villager.Profession.BUTCHER);
     break;
     case 3:
     vg.setProfession(Villager.Profession.FARMER);
     break;
     case 4:
     vg.setProfession(Villager.Profession.LIBRARIAN);
     break;
     case 5:
     vg.setProfession(Villager.Profession.PRIEST);
     break;
     }
     vg.setHealth(20);
     }
                            
     player.sendInfo(ChatColor.RED + "村人", ChatColor.YELLOW + "村人を買いました。");
     }
     });
     } else {
     player.sendInfo(ChatColor.RED + "村人", "村人は最大５匹までです。");
     }
     } else {
     player.sendInfo(ChatColor.RED + "村人", "/villager buy 村人を購入します。(150000Mine)");
     }
     } else {
     player.sendInfo(ChatColor.RED + "村人", "/villager buy 村人を購入します。(150000Mine)");
     }
     }
     * */
// </editor-fold>
    /*
     * さくせーちゅー // <editor-fold defaultstate="collapsed" desc="villager"> @Command(aliases = {"shoplog","sl","しょｐぉｇ","ｓｌ"}, usage = "", desc = "ChestShopLog
     * Command", flags = "", min = 0, max = -1) public static void shoplog(CommandContext message, MituyaProject plugin, Player players, PlayerInstance
     * player)throws CommandException, PlayerOfflineException { player.sendMessage("| ID | Times | Item | Mine | Player | type |"); } public StringBuilder
     * logformat(PlayerInstance pl) { return (new StringBuilder()).append("day"); } public String types (boolean typ) { if(typ){ return "Buy"; } else { return
     * "Sell"; } }
     */
}
