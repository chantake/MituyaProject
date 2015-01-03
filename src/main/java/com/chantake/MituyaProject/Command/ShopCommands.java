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

import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import com.chantake.mituyaapi.commands.NestedCommand;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class ShopCommands {

    @Command(aliases = {"shop", "sp", "しょｐ", "ショップ"}, desc = "shop commands", help = "コマンドショップコマンド")
    @NestedCommand({CommandShopCommands.class})
    public static void shop(CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) {
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
