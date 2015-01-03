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

import com.chantake.MituyaProject.Bukkit.NoteBlockNote;
import com.chantake.MituyaProject.Exception.PlayerOfflineException;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import com.chantake.MituyaProject.Tool.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.block.NoteBlock;

/**
 * 音ブロック(NoteBlock)操作用コマンドクラス
 *
 * @author chantake
 */
public class NoteBlockCommands {

// <editor-fold defaultstate="collapsed" desc="noteblock">
    @Command(aliases = {"noteblock", "nb"}, usage = "", desc = "noteblock command",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.noteblock"})
    public static void noteblockBrush(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            if (message.getString(0).equals("show")) {
                if (message.argsLength() > 1) {
                    if (message.getString(1).equalsIgnoreCase("on") || message.getString(1).equalsIgnoreCase("オン") || message.getString(1).equalsIgnoreCase("true")) {
                        player.setShowNote(true);
                    } else if (message.getString(1).equalsIgnoreCase("off") || message.getString(1).equalsIgnoreCase("オフ") || message.getString(1).equalsIgnoreCase("false")) {
                        player.setShowNote(false);
                    } else {
                        player.sendAttention("/nb show [on/off]　音ブロッククリック時の音階表示を切り替えます");
                        return;
                    }
                } else {
                    player.setShowNote(!player.getShowNote());
                }
                player.sendInfo(ChatColor.GREEN + "NoteBlock", ChatColor.YELLOW + "音ブロッククリック時の音階表示を " + Tools.ReturnColorOnOff(player.getShowNote()) + ChatColor.YELLOW + " に設定しました ");
            } else {
                Block block = players.getTargetBlock(null, 25);
                if (block != null && block.getTypeId() == 25) {
                    if (message.getString(0).equals("info")) {
                        NoteBlock nb = (NoteBlock)block.getState();
                        if (plugin.canBuild(players, block)) {
                            player.sendInfo(ChatColor.GREEN + "NoteBlock", "音階：" + NoteBlockNote.GetNote(nb.getNote().getId(), false) + "　" + ChatColor.BLUE + "保護されていません");
                        } else {
                            player.sendInfo(ChatColor.GREEN + "NoteBlock", "音階：" + NoteBlockNote.GetNote(nb.getNote().getId(), false) + "　" + ChatColor.RED + "保護されています");
                        }
                    } else if (plugin.canBuild(players, block)) {
                        if (message.getString(0).equals("set")) {
                            if (message.argsLength() > 1) {
                                String txt = message.getString(1);
                                byte data;
                                if (Tools.CheckInteger(txt)) {
                                    data = (byte)Tools.StringToInteger(txt);
                                } else {
                                    data = NoteBlockNote.GetNoteData(txt);
                                }
                                if (data == -1) {
                                    player.sendAttention(txt + " は見つかりませんでした");
                                    return;
                                }
                                NoteBlock nb = (NoteBlock)block.getState();
                                nb.setRawNote(data);
                                players.playNote(block.getLocation(), Instrument.PIANO, nb.getNote());
                                player.sendInfo(ChatColor.GREEN + "NoteBlock", ChatColor.AQUA + NoteBlockNote.GetNote(data, false) + ChatColor.YELLOW + "　に設定しました ");
                            } else {
                                player.sendAttention("/nb set [音階]");
                            }
                        }
                    } else {
                        player.sendAttention("この音ブロックは保護されています");
                    }
                } else {
                    player.sendAttention("音ブロックを選択して下さい。");
                }
            }
        } else {
            player.sendAttention("音ブロックをレティクルに照らし合わせてください");
            player.sendAttention("/nb info          音ブロックの詳細を表示します");
            player.sendAttention("/nb set [音階]     音階を設定します");
            player.sendAttention("/nb show {on/off} 音ブロックをクリックした際の詳細を表示するかどうか切り替えます");
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="noteblock set">
    @Command(aliases = {"nset"}, usage = "", desc = "noteblock set command",
            flags = "", min = 0, max = -1)
    public static void noteblocksetBrush(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        StringBuilder sb = new StringBuilder();
        sb.append("nb set");
        if (args.argsLength() > 0) {
            sb.append(" ").append(args.getString(0));
        }
        plugin.handleCommand(players, player, sb.toString());
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="noteblock info">
    @Command(aliases = {"ninfo"}, usage = "", desc = "noteblock info command",
            flags = "", min = 0, max = -1)
    public static void noteblockinfoBrush(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        plugin.handleCommand(players, player, "nb info");
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="noteblock show">
    @Command(aliases = {"nshow"}, usage = "", desc = "noteblock show command",
            flags = "", min = 0, max = -1)
    public static void noteblockshowBrush(CommandContext args, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        StringBuilder sb = new StringBuilder();
        sb.append("nb show");
        if (args.argsLength() > 0) {
            sb.append(" ").append(args.getString(0));
        }
        plugin.handleCommand(players, player, sb.toString());
    }
// </editor-fold>

    private enum NoteBlockDurability {

        ソ(1, "G", "ト"),
        ラ(3, "A", "イ"),
        シ(5, "B", "ロ"),
        ド(6, "C", "ハ"),
        レ(8, "D", "ニ"),
        ミ(10, "E", "ホ"),
        ファ(11, "F", "ヘ");
        private final String en, jp;
        private final byte note;

        private NoteBlockDurability(int note, String english, String japanese) {
            this.note = (byte)note;
            this.en = english;
            this.jp = japanese;
        }

        public String getEnglish() {
            return this.en;
        }

        public String getItalian() {
            return this.toString();
        }

        public String getJapanese() {
            return this.jp;
        }

        public byte getDurability() {
            return this.note;
        }
    }
}
