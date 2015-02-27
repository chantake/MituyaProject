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

import com.chantake.MituyaProject.Exception.MituyaProjectException;
import com.chantake.MituyaProject.Exception.PlayerNotFoundException;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.Midi.MidiJingleSequencer;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Permissions.Rank;
import com.chantake.MituyaProject.Player.HideManager;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Util.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import com.sk89q.commandbook.CommandBook;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author fumitti
 */
public class GeneralCommands {
    /*
     * aliases=呼ばれるコマンド名 usage=文法 desc=説明 flags=使用フラグ(これ以外は例外返される+Usage) min=最小引数(これ未満は例外+Usage) max=最大引数(これ以上は例外+Usage,-1なら無限だーいｗｗｗｗｗｗｗｗｗｗｗ)
     */

// <editor-fold defaultstate="collapsed" desc="testcmd">
    @Command(aliases = {"testcmd"}, usage = "", desc = "Test Code.",
            flags = "t", min = 0, max = -1)
    //@CommandPermissions({"GM"})
    public static void testcmd(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        //plugin.canBuild(players, players.getTargetBlock(null, 20));
        Entity en = players.getWorld().spawnEntity(players.getLocation(), EntityType.SLIME);
        Slime sm = (Slime)en;
        int size = 8;
        if (message.argsLength() > 0) {
            size = message.getInteger(0);
        }
        sm.setSize(size);
        player.sendSuccess("すらいむーーー");
    }
// </editor-fold>se

// <editor-fold defaultstate="collapsed" desc="invite">
    @Command(aliases = {"invite"}, usage = "", desc = "invite",
            flags = "", min = 0, max = -1)
    //@CommandPermissions({"GM"})
    public static void invite(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        /*if (args.argsLength() > 1 && args.getString(0).equals("world")) {
         PlayerInstance ins = plugin.getInstanceManager().getInstance(args.getString(1));
         if (ins == null) {
         player.sendAttention("PlayerID : " + args.getString(1) + "is not found.");
         } else if (ins.getName().endsWith(player.getName())) {
         player.sendAttention("自分を招待することは出来ません。");

         } else {
         if (player.getMainWorldInvite())//ワールドをいじれる権限があるかチェック
         {
         ins.setMainWorldInvite(true);
         ins.sendMessage(player.getName() + "　様から world へ招待がきました");
         ins.sendMessage("/tp world  でワールドに飛べます");
         player.sendSuccess(ins.getName() + "　様に招待を送りました。");
         } else {
         player.sendAttention("ワールドに招待されてないため、権限をつけることが出来ません。");
         }
         }
         } else {
         player.sendAttention("/invite world PlayerID");
         }*/
        player.sendAttention("このコマンドは現在使えません");
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="sponge">
    @Command(aliases = {"sponge"},
            usage = "/sponge set PlayerID", desc = "SpongeCommand",
            flags = "", min = 2, max = -1)
    @CommandPermissions({"mituya.sponge"})
    public static void sponge(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        if (message.argsLength() >= 2) {
            PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(1));
            if (message.getString(0).endsWith("set")) {
                ins.setSponge(true);
                ins.sendInfo("スポンジ", ChatColor.YELLOW + "スポンジ機能を有効にしました。");
                player.sendInfo("スポンジ", ChatColor.YELLOW + ins.getName() + "さんのスポンジ機能を有効にしました。");
            } else if (message.getString(0).endsWith("reset")) {
                ins.setSponge(false);
                ins.sendInfo("スポンジ", ChatColor.YELLOW + "スポンジ機能を無効にしました。");
                player.sendInfo("スポンジ", ChatColor.YELLOW + ins.getName() + "さんのスポンジ機能を無効にしました。");
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "=====スポンジコマンド=====");
            player.sendMessage(ChatColor.YELLOW + "/sponge set プレーヤーID　　スポンジ使用権限を設定します。");
            player.sendMessage(ChatColor.YELLOW + "/sponge reset プレーヤーID　スポンジ使用権限を剥奪します。");
            player.sendMessage(ChatColor.YELLOW + "=====スポンジコマンド=====");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Midi">
    @Command(aliases = {"midi", "intro"}, usage = "", desc = "midi",
            flags = "ap", min = 0, max = -1)
    @CommandPermissions({"mituya.player.midi"})
    public static void midi(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        switch (args.getCommand()) {
            case "midi":
                Player target = players;
                if (args.hasFlag('p')) {
                    target = plugin.getInstanceManager().getInstance(args.getFlag('p')).getPlayer();
                }

                if (args.argsLength() == 0) {
                    if (plugin.jingleNoteManager.stop(target.getName())) {
                        target.sendMessage(ChatColor.YELLOW + "All music stopped.");
                    }
                }
                String filename = args.getString(0);

                if (!filename.matches("^[A-Za-z0-9 \\-_\\.~\\[\\]\\(\\$),]+$")) {
                    throw new CommandException("Invalid filename specified!");
                }

                File[] trialPaths = {
                        new File(CommandBook.inst().getDataFolder(), "midi/" + filename),
                        new File(CommandBook.inst().getDataFolder(), "midi/" + filename + ".mid"),
                        new File(CommandBook.inst().getDataFolder(), "midi/" + filename + ".midi"),
                        new File("midi", filename),
                        new File("midi", filename + ".mid"),
                        new File("midi", filename + ".midi"),
                };

                File file = null;

                for (File f : trialPaths) {
                    if (f.exists()) {
                        file = f;
                        break;
                    }
                }

                if (file == null) {
                    throw new CommandException("The specified MIDI file was not found.");
                }

                try {
                    MidiJingleSequencer sequencer = new MidiJingleSequencer(file, false);
                    plugin.jingleNoteManager.play(target.getName(), sequencer);
                    target.sendMessage(ChatColor.YELLOW + "Playing " + file.getName() + "... Use '/midi' to stop.");
                } catch (MidiUnavailableException e) {
                    throw new CommandException("Failed to access MIDI: "
                            + e.getMessage());
                } catch (InvalidMidiDataException e) {
                    throw new CommandException("Failed to read intro MIDI file: "
                            + e.getMessage());
                } catch (FileNotFoundException e) {
                    throw new CommandException("The specified MIDI file was not found.");
                } catch (IOException e) {
                    throw new CommandException("Failed to read intro MIDI file: "
                            + e.getMessage());
                }
            case "intro":
                try {
                    MidiJingleSequencer sequencer = new MidiJingleSequencer(new File(plugin.getDataFolder(), "intro.mid"), false);
                    plugin.jingleNoteManager.play(player.getPlayer().getName(), sequencer);
                    players.sendMessage(ChatColor.YELLOW + "Playing intro.midi...");
                }
                catch (MidiUnavailableException e) {
                    throw new CommandException("Failed to access MIDI: "
                            + e.getMessage());
                }
                catch (InvalidMidiDataException | IOException e) {
                    throw new CommandException("Failed to read intro MIDI file: "
                            + e.getMessage());
                }
                break;
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Item">
    @Command(aliases = {"item", "i"}, usage = "", desc = "Item",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.player.item"})
    public static void Item(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() == 0) {
            player.sendAttention("Usage: Onry myself /item [ID<:Type>] <Amount>");
        } else {
            try {
                int itemid;
                int type = 0;
                String[] itemtype;
                try {
                    itemtype = message.getString(0).split(":");
                    if (itemtype.length <= 1) {// タイプ未記入
                    } else {
                        type = Integer.parseInt(itemtype[1]);
                    }
                    itemid = Integer.parseInt(itemtype[0]);
                }
                catch (final NumberFormatException e) {
                    itemid = message.getInteger(0);
                }
                int amount = 1;
                if (message.argsLength() >= 1) {
                    amount = message.getInteger(1);
                }
                player.gainItem(itemid, (short)type, amount);

            }
            catch (final PlayerOfflineException | NumberFormatException e) {
                player.sendAttention("Usage: Onry myself /item [ID] [Amount]");
            }
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="inventry">
    @Command(aliases = {"clearinventory", "cli"}, usage = "", desc = "clear inventry",
            flags = "t", min = 0, max = -1)
    @CommandPermissions({"mituya.player.clearinventory"})
    public static void inventry(CommandContext args, MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        player.sendYesNo("インベントリがすべて消えますがよろしいでしょうか？", () -> {
            players.getInventory().clear();
            players.sendMessage(ChatColor.DARK_GRAY + "インベントリ内のアイテムをすべて削除しました");
        });
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="rocket">
    @Command(aliases = {"rocket"},
            usage = "[target]", desc = "Rocket a player", flags = "hs",
            min = 0, max = -1)
    @CommandPermissions({"mituya.player.rocket"})
    public static void rocket(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        Player targets = null;

        // Detect arguments based on the number of arguments provided
        if (args.argsLength() == 0) {
            targets = player.getPlayer();
        } else if (args.argsLength() == 1) {
            if (!player.hasPermission(Rank.Moderator)) {
                player.sendAttention("現在他のユーザーを指定することができません");
                return;
            }
            targets = plugin.getInstanceManager().matchSingleInstance(args.getString(0)).getPlayer();
        }
        if (args.hasFlag('h')) {
            targets.setVelocity(new Vector(0, 50, 0));
        } else {
            targets.setVelocity(new Vector(0, 5, 0));
        }
        // Tell the user
        if (targets.equals(player)) {
            targets.sendMessage(ChatColor.YELLOW + "ロケット！");
        } else if (!args.hasFlag('s')) {
            targets.sendMessage(ChatColor.YELLOW + "You've been rocketed by " + player.getPlayer().getName() + ".");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="boost">
    @Command(aliases = {"boost"},
            usage = "[target]", desc = "boost a player", flags = "hs",
            min = 0, max = -1)
    @CommandPermissions({"mituya.player.boost"})
    public static void boost(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        Player targets = null;

        // Detect arguments based on the number of arguments provided
        if (args.argsLength() == 0) {
            targets = player.getPlayer();
        } else if (args.argsLength() == 1) {
            if (!player.hasPermission(Rank.GM)) {
                player.sendAttention("現在他のユーザーを指定することができません");
                return;
            }
            targets = plugin.getInstanceManager().matchSingleInstance(args.getString(0)).getPlayer();
        }
        if (args.hasFlag('h')) {
            Vector velocity = targets.getEyeLocation().getDirection();
            targets.setVelocity(new Vector(velocity.getX() * 5, velocity.getY() * 5, velocity.getZ() * 5));
        } else {
            Vector velocity = targets.getEyeLocation().getDirection();
            targets.setVelocity(new Vector(velocity.getX() * 3, velocity.getY() * 3, velocity.getZ() * 3));
        }
        // Tell the user
        if (targets.equals(player)) {
            targets.sendMessage(ChatColor.YELLOW + "ロケット！");
        } else if (!args.hasFlag('s')) {
            targets.sendMessage(ChatColor.YELLOW + "You've been rocketed by " + player.getPlayer().getName() + ".");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="onara">
    @Command(aliases = {"onara"},
            usage = "[target]", desc = "Poooooooooop!", flags = "d",
            min = 0, max = -1)
    @CommandPermissions({"mituya.player.onara"})
    public static void onara(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        Player targets = null;

        // Detect arguments based on the number of arguments provided
        if (args.argsLength() == 0) {
            targets = player.getPlayer();
        } else if (args.argsLength() == 1) {
            if (!player.hasPermission(Rank.Moderator)) {
                player.sendAttention("現在他のユーザーを指定することができません");
                return;
            }
            targets = plugin.getInstanceManager().matchSingleInstance(args.getString(0)).getPlayer();
        }
        targets.getWorld().createExplosion(targets.getLocation(), 0, false);
        // Damage the user
        double y = Math.random();
        if (args.hasFlag('d')) {
            targets.damage(20);
            plugin.broadcastMessage(targets.getDisplayName() + "はオナラで逝った...");
        } else {
            if (y > 0.8) {
                int damage = (int)(Math.random() * 20);
                double nowhelth = targets.getHealth();
                targets.damage(damage);
                if (nowhelth <= damage) {
                    plugin.broadcastMessage(targets.getDisplayName() + "はオナラで逝った...");
                }
            }
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="account">
    @Command(aliases = {"account"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    //@CommandPermissions({"GM"})
    public static void account(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (args.argsLength() > 0) {
            if (args.getString(0).equals("pass")) {
                if (args.argsLength() > 1) {
                    player.setPass(args.getString(1));
                    player.sendSuccess("Password set Complete.");
                } else {
                    player.sendAttention("/account pass [Passworld]");
                }
            } else {
                player.sendAttention("/account pass");
            }
        } else {
            player.sendAttention("/account pass");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="gobaku">
    @Command(aliases = {"gobaku"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.command.gobaku"})
    public static void gobaku(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        player.setGobaku();
        player.sendSuccess("誤爆防止 : " + Tools.ReturnColorOnOff(player.getGobaku()));
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="check">
    @Command(aliases = {"check"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.player.check"})
    public static void check(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        player.setCheck();
        player.sendSuccess("確認の設定をしました　確認通知 : " + Tools.ReturnColorOnOff(!player.isCheckSkip()));
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="compuss">
    @Command(aliases = {"compuss", "hougaku", "connpasu", "コンパス"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.player.compuss"})
    public static void compus(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        int r = (int)player.getCorrectedYaw();
        String me, mj;
        if (r < 23) {
            me = ChatColor.GREEN + "West";
            mj = "西";
        } else if (r < 68) {
            me = "Northwest";
            mj = "北西";
        } else if (r < 113) {
            me = ChatColor.RED + "North";
            mj = "北";
        } else if (r < 158) {
            me = "Northeast";
            mj = "北東";
        } else if (r < 203) {
            me = ChatColor.YELLOW + "East";
            mj = "東";
        } else if (r < 248) {
            me = "Southeast";
            mj = "南西";
        } else if (r < 293) {
            me = ChatColor.BLUE + "South";
            mj = "南";
        } else if (r < 338) {
            me = "Southwest";
            mj = "南東";
        } else {
            me = ChatColor.GREEN + "West";
            mj = "西";
        }
        player.getPlayer().sendMessage(ChatColor.GRAY + me + ChatColor.GRAY + "(" + ChatColor.DARK_GRAY + mj + ChatColor.GRAY + ")");
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="give">
    @Command(aliases = {"give"},
            usage = "[-d] <PlayerName> <ID[:Type]> [Amount]", desc = "Give item other user",
            flags = "d", min = 2, max = -1)
    //@CommandPermissions({"GM"})
    public static void give(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
    }
    // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="heal ヒール"> 現在工事中
    @Command(aliases = {"heal", "h", "cure"},
            usage = "", desc = "heal",
            flags = "", min = 0, max = -1)
    //@CommandPermissions({"GM"})
    public static void heal(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        player.sendAttention("このコマンドは工事中です");
        /*
         * final int tax = 100; final int mine = 30; if (message.argsLength() > 0) { final PlayerInstance ins =
         * InstanceManager.getInstance(message.getString(0)); Player cp = null; if (ins != null) { cp = Misc.playerMatch(ins.getName()); } if (cp == null) {
         * player.sendJapaneseAttension("プレーヤー " + ChatColor.YELLOW + message.getString(0) + ChatColor.RED + " はみつかりませんでした。", "Player " + ChatColor.YELLOW +
         * message.getString(0) + ChatColor.RED + " is doesn't exist."); } else if (!cp.isOnline()) { player.sendJapaneseAttension("プレーヤー " + ChatColor.YELLOW +
         * cp.getName() + ChatColor.RED + " はオフラインです。", "Player " + ChatColor.YELLOW + cp.getName() + ChatColor.RED + " is offline."); } else if (cp.getHealth()
         * <= 0) { player.sendJapaneseAttension(cp.getName() + " はすでにしんでいます。", cp.getName() + " is already dead."); } else { final int heal = 20 -
         * cp.getHealth(); final int tmine = heal * mine + tax; if (player.getCheck()) { if (player.gainMineCheck(-tmine)) { cp.setHealth(20);
         * player.getPlayer().sendMessage(ChatColor.AQUA + "[Heal]" + ChatColor.LIGHT_PURPLE + " Heal to " + ins.getName() + " : " + ChatColor.YELLOW + heal +
         * ChatColor.DARK_AQUA + " Lost : " + ChatColor.YELLOW + tmine + "Mine " + ChatColor.DARK_AQUA + " Tax : " + ChatColor.YELLOW + tax + "Mine");
         * cp.sendMessage(ChatColor.AQUA + "[Heal]" + ChatColor.LIGHT_PURPLE + " Heal from " + ins.getName() + " : " + ChatColor.YELLOW + heal + " Heal!"); } }
         * else { player.sendMineYesNo(tmine, message.getSlice(0)); } } } else { final int hl = 20 - player.getPlayer().getHealth(); final int tmine = hl * mine
         * + tax; if (player.getCheck()) { if (player.gainMineCheck(-tmine)) { player.gainTax(tax); if (player.getPlayer().getHealth() <= 0) {
         * player.sendJapaneseAttension("あなたはすでにしんでします。", "You are already dead."); } else { player.getPlayer().setHealth(20);
         * player.getPlayer().sendMessage(ChatColor.AQUA + "[Heal]" + ChatColor.LIGHT_PURPLE + " Heal : " + ChatColor.YELLOW + hl + ChatColor.DARK_AQUA + " Lost
         * : " + ChatColor.YELLOW + tmine + "Mine " + ChatColor.DARK_AQUA + " Tax : " + ChatColor.YELLOW + tax + "Mine"); } } } else {
         * player.sendMineYesNo(tmine, message.getSlice(0)); } }
         */
    }
    // </editor-fold>

//<editor-fold defaultstate="collapsed" desc="Jackpot">
    @Command(aliases = {"jackpot", "jp"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.jackpot"})
    public static void jackpot(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        player.sendInfo(ChatColor.GOLD + "JackPot", "ジャックポット: " + ChatColor.GOLD + Parameter328.jackpot + ChatColor.YELLOW + " Mine");
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="notice">
    @Command(aliases = {"notice"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.server.notice"})
    public static void notice(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        plugin.broadcastMessage(ChatColor.LIGHT_PURPLE + "[Server] " + message.getJoinedStrings(0));
    }

    @Command(aliases = {"say"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.command.say"})
    public static void say(final CommandContext message, final MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (player.hasPermission(Rank.Supporter)) {
            plugin.broadcastMessage(ChatColor.AQUA + "[" + players.getDisplayName() + "] " + Tools.SignColor(message.getJoinedStrings(0)));
        } else {
            player.sendMineYesNo(Parameter328.say, () -> {
                player.gainMine(-Parameter328.say);
                plugin.broadcastMessage(ChatColor.AQUA + "[" + players.getName() + "] " + Tools.SignColor(message.getJoinedStrings(0)));
            });
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="setrank">
    @Command(aliases = {"setrank"},
            usage = "/setrank [accountID] [Rank]", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.rank.set"})
    public static void setrank(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, MituyaProjectException {
        if (message.argsLength() > 1) {
            PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(0));
            if (ins == null) {
                throw new PlayerNotFoundException(message.getString(0));
            }
            Rank oldrank = ins.getRank();
            Rank rank;
            if (Tools.CheckInteger(message.getString(1))) {
                rank = Rank.getRank(message.getInteger(1));
            } else {
                rank = Rank.getRank(message.getString(1));
            }
            if (rank == null) {
                player.sendAttention("ランク：" + message.getString(1) + " は見つかりませんでした");
                return;
            }
            boolean changeRank = ins.changeRank(rank);
            if (changeRank) {
                player.sendSuccess("ランクを変更しました。 プレーヤー:" + ins.getName() + " " + oldrank.toString() + " → " + rank.toString());
            } else {
                player.sendAttention("ランク変更に失敗しました");
            }
        } else {
            player.sendAttention("/rank [playerid] [rankname or id]");
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="setsp">
    @Command(aliases = {"setsp"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.rank.setsp"})
    public static void setsp(CommandContext message, MituyaProject plugin, final Player players, final PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            final PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(0));
            if (ins == null) {
                player.sendAttention(message.getString(0) + " は見つかりませんでした。");
            } else if (!Tools.CheckInteger(message.getString(1))) {
                player.sendAttention("金額を入力してください。");
            } else if (Tools.StringToInteger(message.getString(1)) <= 0) {
                player.sendAttention("0以上を入力してください");
            } else {
                final int mine = Integer.valueOf(message.getString(1)) * 1000;
                player.sendYesNo("以下の内容で登録しますがよろしいですか？ PlayerID:" + ins.getName() + " Mine:" + mine, () -> {
                    ins.gainMine(mine);
                    ins.setRank(Rank.Supporter);
                    ins.setDisplayName();
                    ins.SaveAll();
                    player.sendSuccess("以下の内容で登録しました PlayerID:" + ins.getName() + " Mine:" + mine);
                });
            }
        } else {
            player.sendAttention("/setsp playerid 金額");
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="pvp">
    @Command(aliases = {"pvp"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.player.pvp"})
    public static void pvp(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() == 0) {
            player.togglePvP();
            player.sendInfo(ChatColor.DARK_GRAY + "PvP", " PvPは " + Tools.ReturnColorOnOff(player.getPvP()) + "です。");
        } else if (message.getString(0).equalsIgnoreCase("on")) {
            player.setPvP(true);
            player.sendInfo(ChatColor.DARK_GRAY + "PvP", " PvPは " + Tools.ReturnColorOnOff(player.getPvP()) + "です。");
        } else if (message.getString(0).equalsIgnoreCase("off")) {
            player.setPvP(false);
            player.sendInfo(ChatColor.DARK_GRAY + "PvP", " PvPは " + Tools.ReturnColorOnOff(player.getPvP()) + "です。");
        } else {
            player.sendAttention("/pvp or /pvp [on/off]");
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="summon">
    @Command(aliases = {"summon"},
            usage = "", desc = "",
            flags = "pirhw", min = 0, max = -1)
    @CommandPermissions({"mituya.summon"})
    public static void summon(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws MituyaProjectException {
        if (args.argsLength() > 0) {
            EntityType entityType = EntityType.fromName(args.getString(0));
            int amount = 1;
            if (entityType == null) {
                player.sendAttention("Entity：" + args.getString(0) + " は見つかりませんでした。");
                return;
            }
            if (args.argsLength() > 1) {
                amount = args.getInteger(1);
                if (amount > 1000 || amount < 1) {
                    player.sendAttention("1以上1000以下で設定してください");
                    return;
                }
            }
            Location location = players.getLocation();
            if (args.hasFlag('p')) {
                String pname = args.getFlag('p', "null");
                if (pname.equals("null")) {
                    player.sendAttention("プレーヤー名を入力してください (-p プレーヤー名)");
                    return;
                }
                PlayerInstance instance = plugin.getInstanceManager().matchSingleInstance(pname);
                if (instance == null) {
                    throw new PlayerNotFoundException(pname);
                }
                try {
                    location = instance.getPlayer().getLocation();
                }
                catch (NullPointerException e) {
                    throw new PlayerOfflineException(instance.getName());
                }
            }
            LivingEntity spawnCreature = (LivingEntity)location.getWorld().spawnEntity(location, entityType);
            double health = args.getFlagDouble('h', spawnCreature.getMaxHealth());
            boolean b = false;
            if (health < 1) {
                health = spawnCreature.getMaxHealth();
            }
            spawnCreature.setHealth(health);
            if (args.hasFlag('i')) {
                spawnCreature.setFireTicks(20 * 25);
            }
            if (args.hasFlag('r')) {
                spawnCreature.setVelocity(new Vector(0, 2, 0));
            }
            if (args.hasFlag('w') && spawnCreature instanceof Creeper) {
                b = true;
            }
            if (amount > 1) {
                for (int i = 0; i < amount - 1; i++) {
                    LivingEntity entity = (LivingEntity)location.getWorld().spawnEntity(location, entityType);
                    spawnCreature.setHealth(health);
                    if (args.hasFlag('i')) {
                        entity.setFireTicks(20 * 25);
                    }
                    if (args.hasFlag('r')) {
                        entity.setVelocity(new Vector(0, 2, 0));
                    }
                    if (b) {
                        ((Creeper)entity).setPowered(true);
                    }
                }
            }
            player.sendSuccess("モンスターを召喚しました : " + entityType.getName() + "(amount:" + amount + " health:" + health + ")");
        } else {
            player.sendAttention("/summon モンスター {Amount}  モンスターを召喚します");
            player.sendAttention("フラグ一覧: -p -i -r -h -w");
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="weather">
    @Command(aliases = {"weather"},
            usage = "<'stormy'|'sunny'> [duration] [world]", desc = "Change the world weather",
            min = 1, max = -1)
    @CommandPermissions({"mituyaproject.rank.gm"})
    public static void weather(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {

        World world = null;
        String weatherStr = message.getString(0);
        int duration
                = -1;

        if (message.argsLength() == 1) {
            world = players.getWorld();
        } else if (message.argsLength() == 2) {
            world = players.getWorld();
            duration
                    = message.getInteger(1);
        } else { // A world was specified!

            duration = message.getInteger(1);
        }

        if (weatherStr.equalsIgnoreCase("stormy")
                || weatherStr.equalsIgnoreCase("rainy")
                || weatherStr.equalsIgnoreCase("snowy")
                || weatherStr.equalsIgnoreCase("rain")
                || weatherStr.equalsIgnoreCase("snow")
                || weatherStr.equalsIgnoreCase("on")) {

            world.setStorm(true);

            if (duration > 0) {
                world.setWeatherDuration(duration * 20);
            }

            players.sendMessage(ChatColor.YELLOW
                    + "Stormy weather enabled.");

        } else if (weatherStr.equalsIgnoreCase("clear")
                || weatherStr.equalsIgnoreCase("sunny")
                || weatherStr.equalsIgnoreCase("snowy")
                || weatherStr.equalsIgnoreCase("rain")
                || weatherStr.equalsIgnoreCase("snow")
                || weatherStr.equalsIgnoreCase("off")) {

            world.setStorm(false);

            if (duration > 0) {
                world.setWeatherDuration(duration * 20);
            }

            // Tell console, since console won't get the broadcast message. if
            players.sendMessage(ChatColor.YELLOW
                    + "Stormy weather disabled.");

        } else {
            throw new CommandException(
                    "Unknown weather state! Acceptable states : sunny or stormy ");
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="thunder">
    @Command(aliases = {"thunder"},
            usage = "<'on'|'off'> [duration] [world]", desc = "Change the thunder state",
            min = 1, max = -1)
    @CommandPermissions({"mituyaproject.rank.gm"})
    public static void thunder(CommandContext args, MituyaProject plugin, Player sender, PlayerInstance player) throws CommandException {

        World world;
        String weatherStr = args.getString(0);
        int duration
                = -1;

        if (args.argsLength() == 1) {
            world = sender.getWorld();
        } else if (args.argsLength() == 2) {
            world = sender.getWorld();
            duration = args.getInteger(1);
        } else { // A world was specified! 
            world = sender.getWorld();
            duration = args.getInteger(1);
        }

        if (weatherStr.equalsIgnoreCase("on")) {
            world.setThundering(true);

            if (duration > 0) {
                world.setThunderDuration(duration * 20);
            }

            sender.sendMessage(ChatColor.YELLOW + "Thunder enabled.");
        } else if (weatherStr.equalsIgnoreCase("off")) {
            world.setThundering(false);

            if (duration > 0) {
                world.setThunderDuration(duration * 20);
            }

            sender.sendMessage(ChatColor.YELLOW + "Thunder disabled.");
        } else {
            throw new CommandException("Unknown thunder state! Acceptable states:on or off");
        }
    }
//</editor-fold>

//<editor-fold defaultstate="gametime" desc="notice">
    @Command(aliases = {"gametime"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.server.gametime"})
    public static void gametime(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        player.sendInfo(ChatColor.GRAY + "GameTime", "只今の時刻は " + Tools.getWorldTime(players.getWorld()) + " です。");
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="time">
    @Command(aliases = {"time", "時間"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.server.time"})
    public static void time(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        player.sendInfo(ChatColor.GRAY + "Time", "只今の時刻は " + Tools.getRealTime() + " です。");
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="debug">
    @Command(aliases = {"debug", "mituyadebug"}, desc = "Debugging commands")
    @CommandPermissions({"mituya.debug"})
    public static void debug(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        player.setDebug(!player.isDebug());
        player.sendMessage("Debugモード:" + Tools.ReturnColorOnOff(player.isDebug()));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="yes">
    @Command(aliases = {"yes", "いえす", "いぇｓ"}, desc = "check commands")
    @CommandPermissions({"mituya.check.yes"})
    public static void yes(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        //確認メッセージがあるかどうか
        if (player.isCheck()) {
            player.executionCheckInstance();
        } else {
            player.sendAttention("現在有効な確認メッセージはありません");
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="no">
    @Command(aliases = {"no", "の"}, desc = "check nocommands")
    @CommandPermissions({"mituya.check.no"})
    public static void no(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        //確認メッセージがあるかどうか
        if (player.isCheck()) {
            player.removeCheckInstance();
            player.sendAttention("**キャンセルしました**");
            players.openInventory(players.getOpenInventory());
            BeaconInventory bi;

        } else {
            player.sendAttention("現在有効な確認メッセージはありません");
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="hide">
    @Command(aliases = {"hide"}, desc = "Hide to users")
    @CommandPermissions({"mituya.hide"})
    public static void hide(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        HideManager hideManager = plugin.getInstanceManager().getHideManager();
        boolean ishide = hideManager.isHidePlayer(players);
        if (ishide) {
            hideManager.removeHidePlayer(players);
        } else {
            hideManager.addHidePlayer(players);
        }
        player.sendSuccess("HideModeを " + Tools.ReturnColorOnOff(!ishide) + " にしました");
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="horse">
    @Command(aliases = {"horse"}, desc = "Horse")
    @CommandPermissions({"mituya.horse"})
    public static void horse(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        Location l = players.getLocation();
        Horse h = (Horse)players.getWorld().spawnEntity(l, EntityType.HORSE);
        h.setMaxHealth(30.0);
        h.setStyle(Horse.Style.NONE);
        h.setVariant(Horse.Variant.HORSE);
        h.setColor(Horse.Color.WHITE);
        h.setTamed(true);
        h.setJumpStrength(2);
        //((EntityInsentient)((CraftLivingEntity)h).getHandle()).getAttributeInstance(GenericAttributes.d).setValue(20);
        player.sendSuccess("ｳﾏﾄﾞﾝﾅｧｧｧｧ");
    }
//</editor-fold>

    /*
     //<editor-fold defaultstate="collapsed" desc="tax">
     @Command(aliases = {"tax"}, usage = "", desc = "", flags = "", min = 0, max = -1)
     @CommandPermissions({"mituyaproject.rank.admin"})
     public static void tax(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {

     if (message.getString(0).endsWith("info")) {
     player.sendInfo(ChatColor.YELLOW + "TaxInfo", "計" + Parameter328.taxmine + "Mine");
     } else if (message.getString(0).endsWith("give")) {
     if (message.argsLength() > 2) {
     PlayerInstance ins = plugin.getInstanceManager().getInstance(message.getString(1));
     if (ins == null) {
     player.sendAttention(message.getString(1) + " は見つかりませんでした。");
     } else if (!Tools.CheckInteger(message.getString(2))) {
     player.sendAttention("金額を入力してください。");
     } else if (Tools.StringToInteger(message.getString(2)) <= 0) {
     player.sendAttention("0以上を入力してください");
     } else {
     int mine = Integer.valueOf(message.getString(2));
     ins.gainMine(mine);
     Parameter328.taxmine -= mine;
     player.sendSuccess("Mineを送金しました。:" + mine + "Mine");
     player.sendSuccess("現在のTax :" + Parameter328.taxmine + "Mine");
     }
     } else {
     player.sendAttention("記入ミス\'/tax [give/info]\'");
     }
     } else if (message.getString(0).endsWith("buy")) {
     } else {
     player.sendAttention("記入ミス\'/tax [give/info]\'");
      
     }
     */
//</editor-fold>
    /*
     * @Command(aliases = {"fly"}, desc = "fly to users") @CommandPermissions({"mituyaproject.rank.moderator"}) public static void fly(CommandContext message,
     * MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException { if (message.argsLength() > 0) {
     * PlayerInstance ins = plugin.getInstanceManager().getInstance(message.getString(0)); Player player1 = ins.getPlayer(); boolean fly =
     * player1.getAllowFlight(); player1.setAllowFlight(!fly); if (fly) { player1.setFlying(false); } ins.sendSuccess("Flyを " + Tools.ReturnColorOnOff(!fly) + "
     * にしました。"); player.sendSuccess(ins.getName()+" にFlyを " + Tools.ReturnColorOnOff(!fly) + " にしました。"); } else { boolean fly = players.getAllowFlight();
     * players.setAllowFlight(!fly); if (fly) { players.setFlying(false); } player.sendSuccess("Flyを " + Tools.ReturnColorOnOff(!fly) + " にしました。"); } }
     */

    /*
     * @Command(aliases = {""}, usage = "", desc = "", flags = "", min = 0, max = -1)
     */
//@CommandPermissions({"GM"})
    public static void sample(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
    }
}
