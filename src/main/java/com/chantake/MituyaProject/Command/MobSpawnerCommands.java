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
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 *
 * @author chantake
 */
public class MobSpawnerCommands {

    public enum CreaturePrice {

        Pig(3600, EntityType.PIG, Environment.NORMAL),
        Cow(3600, EntityType.COW, Environment.NORMAL),
        Chiken(2600, EntityType.CHICKEN, Environment.NORMAL),
        Sheep(2200, EntityType.SHEEP, Environment.NORMAL),
        Squid(2000, EntityType.SQUID, Environment.NORMAL),
        Zombie(2500, EntityType.ZOMBIE, Environment.NORMAL),
        Skeleton(2000, EntityType.SKELETON, Environment.NORMAL),
        Creeper(2800, EntityType.CREEPER, Environment.NORMAL),
        Spider(2000, EntityType.SPIDER, Environment.NORMAL),
        Slime(3000, EntityType.SLIME, Environment.NORMAL),
        Endermen(2900, EntityType.ENDERMAN, Environment.NORMAL),
        Cave_Spider(1700, EntityType.CAVE_SPIDER, Environment.NORMAL),
        Silverfish(18000, EntityType.SILVERFISH, Environment.NORMAL),
        Zombie_PigMen(7000, EntityType.PIG_ZOMBIE, Environment.NETHER),
        Blaze(8000, EntityType.BLAZE, Environment.NETHER),
        MagmaCube(3500, EntityType.MAGMA_CUBE, Environment.NETHER),
        Mooshroom(18000, EntityType.MUSHROOM_COW, Environment.NORMAL);
        private final int price;
        private final EntityType cp;
        private final Environment en;

        private CreaturePrice(int price, EntityType cp, Environment en) {
            this.price = price;
            this.cp = cp;
            this.en = en;
        }

        public int getPrice() {
            return this.price;
        }

        public EntityType getType() {
            return this.cp;
        }

        public Environment getEnvironment() {
            return this.en;

        }
    }

// <editor-fold defaultstate="collapsed" desc="mobspawner">
    @Command(aliases = {"mobspawner", "ms"}, usage = "", desc = "mobspawner command",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.mobspawner"})
    public static void mobspawnerBrush(CommandContext message, MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            switch (message.getString(0)) {
                case "list":
                    player.sendInfo(ChatColor.DARK_GRAY + "MobSpawner", "一覧を表示します");
                    player.sendMessage(ChatColor.YELLOW + "***********変更できるモンスター一覧***********");
                    for (CreaturePrice cp : CreaturePrice.values()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("モンスター名:").append(cp.name()).append("    環境:");
                        if (cp.getEnvironment() == null) {
                            sb.append("取り扱い中止");
                        } else {
                            sb.append(cp.getEnvironment().toString());
                        }
                        sb.append("    値段:").append(cp.getPrice());
                        player.sendMessage(sb.toString());
                    }
                    break;
                case "price":
                    CreaturePrice cp = Creature(message.getString(1));
                    if (cp == null || cp.getEnvironment() == null) {
                        player.sendAttention("モンスター:" + message.getString(1) + " は存在しないか、取り扱っていません。");
                    } else if (cp.getEnvironment() != players.getWorld().getEnvironment()) {//環境が違う場合
                        player.sendAttention("モンスター:" + cp.name() + " は " + cp.getEnvironment().toString() + " でのみ設定することができます");
                    } else {
                        player.sendInfo(ChatColor.DARK_GRAY + "MobSpawner", "モンスター名:" + cp.name() + " 値段:" + cp.getPrice());
                    }
                    break;
                default:
                    if (isMobSpawnerTarget(players, player)) {
                        Block block = getTarget(players);
                        final CreatureSpawner cs = (CreatureSpawner)block.getState();
                        if (message.getString(0).equals("info")) {
                            if (plugin.canBuild(players, block)) {
                                player.sendInfo(ChatColor.DARK_GRAY + "MobSpawner", "スポーンブロック：" + cs.getCreatureTypeName());
                            } else {
                                player.sendInfo(ChatColor.DARK_GRAY + "MobSpawner", "スポーンブロック：" + cs.getCreatureTypeName() + " このスポーンブロックは保護されています");
                            }
                        } else if (message.getString(0).equals("protect") || message.getString(0).equals("hogo") || message.getString(0).equals("保護") || message.getString(0).equalsIgnoreCase("保護")) {
                            player.sendAttention("この機能は廃止されました。代わりにエリア保護をご利用ください");
                        } else if (message.getString(0).equals("set")) {
                            if (message.argsLength() > 1) {
                                final CreaturePrice cp2 = Creature(message.getString(1));
                                if (cp2 == null) {
                                    player.sendAttention("モンスター:" + message.getString(1) + " は存在しないか、取り扱っていません。");
                                } else if (!plugin.canBuild(players, block)) {//オーナーと同じじゃない場合 or 保護から参照
                                    player.sendAttention("保護がかかっています");
                                } else {
                                    if (player.checkMine(-cp2.getPrice(), true)) {
                                        player.sendMineYesNo(cp2.getPrice(), new Runnable() {
                                            @Override
                                            public void run() {
                                                player.gainMine(-cp2.getPrice());
                                                cs.setSpawnedType(cp2.getType());
                                                player.sendInfo(ChatColor.DARK_GRAY + "MobSpawner", ChatColor.YELLOW + "スポーンブロックのモンスターを変更いたしました。");
                                                player.sendInfo(ChatColor.DARK_GRAY + "MobSpawner", "スポーンブロック：" + cs.getCreatureTypeName());
                                            }
                                        });
                                    }
                                }
                            } else {
                                player.sendAttention("スポーンブロックをレティクルに照らし合わせてください");
                                player.sendAttention("/ms set [モンスター名]  スポーンブロックのモンスターを変更します");
                            }
                        } else {
                            player.sendAttention("スポーンブロックをレティクルに照らし合わせてください");
                            player.sendAttention("/ms info  選択したスポーンブロックの詳細が分かります");
                            player.sendAttention("/ms set   選択したスポーンブロックのモンスターを変更します");
                        }
                    }
                    break;
            }
        } else {
            player.sendAttention("スポーンブロックをレティクルに照らし合わせてください");
            player.sendAttention("/ms info      選択したスポーンブロックの詳細が分かります");
            player.sendAttention("/ms set       選択したスポーンブロックのモンスターを変更します");
            player.sendAttention("/ms list       変更できるモンスターの一覧");
            player.sendAttention("/ms price    モンスターの値段を調べます");
            player.sendAttention("/ms protect スポーンブロックを保護します");
        }
    }
// </editor-fold>

    public static CreaturePrice Creature(String creature) {
        creature = creature.replaceAll("_", "");
        if (creature.equalsIgnoreCase("pig") || creature.equalsIgnoreCase("buta") || creature.equalsIgnoreCase("ピグ") || creature.equalsIgnoreCase("ぶた") || creature.equalsIgnoreCase("豚")) {
            return CreaturePrice.Pig;
        } else if (creature.equalsIgnoreCase("cow") || creature.equalsIgnoreCase("usi") || creature.equalsIgnoreCase("うし") || creature.equalsIgnoreCase("牛")) {
            return CreaturePrice.Cow;
        } else if (creature.equalsIgnoreCase("chiken") || creature.equalsIgnoreCase("tikin") || creature.equalsIgnoreCase("tori") || creature.equalsIgnoreCase("チキン") || creature.equalsIgnoreCase("とり") || creature.equalsIgnoreCase("鳥")) {
            return CreaturePrice.Chiken;
        } else if (creature.equalsIgnoreCase("squid") || creature.equalsIgnoreCase("ika") || creature.equalsIgnoreCase("イカ")) {
            return CreaturePrice.Squid;
        } else if (creature.equalsIgnoreCase("sheep") || creature.equalsIgnoreCase("hituzi") || creature.equalsIgnoreCase("ひつじ") || creature.equalsIgnoreCase("羊")) {
            return CreaturePrice.Sheep;
        } else if (creature.equalsIgnoreCase("zombie") || creature.equalsIgnoreCase("zonbi") || creature.equalsIgnoreCase("ゾンビ")) {
            return CreaturePrice.Zombie;
        } else if (creature.equalsIgnoreCase("skeleton") || creature.equalsIgnoreCase("sukeruton") || creature.equalsIgnoreCase("スケルトン")) {
            return CreaturePrice.Skeleton;
        } else if (creature.equalsIgnoreCase("creeper") || creature.equalsIgnoreCase("kuri-pa-") || creature.equalsIgnoreCase("クリーパー") || creature.equalsIgnoreCase("匠")) {
            return CreaturePrice.Creeper;
        } else if (creature.equalsIgnoreCase("spider") || creature.equalsIgnoreCase("supaida-") || creature.equalsIgnoreCase("kumo") || creature.equalsIgnoreCase("スパイダー") || creature.equalsIgnoreCase("くも") || creature.equalsIgnoreCase("蜘蛛")) {
            return CreaturePrice.Spider;
        } else if (creature.equalsIgnoreCase("slime") || creature.equalsIgnoreCase("suraimu") || creature.equalsIgnoreCase("スライム")) {
            return CreaturePrice.Slime;
        } else if (creature.equalsIgnoreCase("endermen") || creature.equalsIgnoreCase("enda-man") || creature.equalsIgnoreCase("エンダーマン")) {
            return CreaturePrice.Endermen;
        } else if (creature.equalsIgnoreCase("cavespider") || creature.equalsIgnoreCase("keibusupaida-") || creature.equalsIgnoreCase("ケイブスパイダー")) {
            return CreaturePrice.Cave_Spider;
        } else if (creature.equalsIgnoreCase("silverfish") || creature.equalsIgnoreCase("siruba-hissyu") || creature.equalsIgnoreCase("シルバーフィッシュ")) {
            return CreaturePrice.Silverfish;
        } else if (creature.equalsIgnoreCase("zombiepigmen") || creature.equalsIgnoreCase("zonnbipiggumann") || creature.equalsIgnoreCase("ゾンビピッグマン")) {
            return CreaturePrice.Zombie_PigMen;
        } else if (creature.equalsIgnoreCase("blaze") || creature.equalsIgnoreCase("bureizu") || creature.equalsIgnoreCase("ブレイズ")) {
            return CreaturePrice.Blaze;
        } else if (creature.equalsIgnoreCase("magumacube") || creature.equalsIgnoreCase("magumakyu-bu") || creature.equalsIgnoreCase("マグマキューブ")) {
            return CreaturePrice.MagmaCube;
        } else if (creature.equalsIgnoreCase("mooshroom") || creature.equalsIgnoreCase("mu-syuru-mu") || creature.equalsIgnoreCase("ムーシュルーム")) {
            return CreaturePrice.Mooshroom;
        } else {
            return null;
        }
    }

    /**
     * 選択しているスポーンブロックを取得します
     *
     * @return スポーンブロックが存在しない場合はnullを返します
     * @see Sign
     */
    private static Block getTarget(Player player) {
        return player.getTargetBlock(null, 5);
    }

    /**
     * 選択しているスポーンブロックを取得します
     *
     * @return スポーンブロックが存在しない場合はnullを返します
     * @see Sign
     */
    private static boolean isMobSpawnerTarget(Player player, PlayerInstance ins) throws PlayerOfflineException {
        if (player.getTargetBlock(null, 5).getType() == Material.MOB_SPAWNER) {
            return true;
        } else {
            ins.sendAttention("スポーンブロックを選択して下さい。");
            return false;
        }
    }
}
