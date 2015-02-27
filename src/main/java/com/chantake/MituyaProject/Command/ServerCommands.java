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
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Parameter.Parameter328;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Timer.AutoStop;
import com.chantake.MituyaProject.Util.PerfomanceCheck;
import com.chantake.MituyaProject.Util.Tools;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import com.sk89q.worldguard.protection.GlobalRegionManager;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author fumitti
 */
public class ServerCommands {

    @Command(aliases = {"serverinfo", "si", "サーバーインフォ"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.info.serverinfo"})
    public static void serverinfo(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        players.sendMessage(ChatColor.BLUE
                + "*************************Sever Info*************************");
        players.sendMessage("Server Name : 328mssMinecraftServer");
        players.sendMessage("Contory : Japan");
        players.sendMessage("Server HP : http://328mss.com/");
        players.sendMessage("Minecraft version : " + plugin.getServer().getVersion());
        players.sendMessage("Admin : " + ChatColor.AQUA + "chantake" + ChatColor.WHITE + "  SubAdmin : " + ChatColor.DARK_AQUA + "ezura fumitti");
        players.sendMessage("SuperGM : " + ChatColor.GREEN + "yukaseri" + ChatColor.WHITE + "  GMs : " + ChatColor.DARK_GREEN + "reafa John_Yamada");
        players.sendMessage("E-Mail : info@328mss.com");
        players.sendMessage("ServerAddress : 328mss.com");
        players.sendMessage("Port : " + plugin.getServer().getPort());
    }

    @Command(aliases = {"online", "who", "list", "オンライン"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.info.online"})
    public static void online(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        final StringBuilder sb = new StringBuilder();
        sb.append("Online Players(").append(ChatColor.RED).append(plugin.getServer().getOnlinePlayers().size()).append(ChatColor.WHITE).append(") ");
        for (final Player needle : plugin.getServer().getOnlinePlayers()) {
            PlayerInstance ins = plugin.getInstanceManager().getInstance(needle);
            sb.append(needle.getDisplayName());
            if (ins.isNickName()) {
                sb.append("(").append(ChatColor.GRAY).append(needle.getName()).append(ChatColor.WHITE).append(")");
            }
            sb.append(", ");
        }
        players.sendMessage(sb.toString());
    }

    @Command(aliases = {"saveall", "save-all"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.server.saveall"})
    public static void saveall(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        player.sendProcessing("Start save all");
        plugin.SaveAll();
        players.sendMessage(ChatColor.LIGHT_PURPLE + "Save world complete.");
        players.sendMessage(ChatColor.LIGHT_PURPLE + "Save player complete.");
        players.sendMessage(ChatColor.LIGHT_PURPLE + "Save playerinstance complete.");
    }

    /*@Command(aliases = {"plugin", "pg"},
     usage = "", desc = "",
     flags = "", min = 0, max = -1)
     @CommandPermissions({"Admin"})
     public static void plugin(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
     StringBuilder sb = new StringBuilder();
     sb.append("Plugins:");
     for(Plugin pg : plugin.getServer().getPluginManager().getPlugins()) {
     sb.append(" "+pg.getDescription());
     }
     players.sendMessage("======PluginManager=====");
     }*/
    @Command(aliases = {"stop"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.server.stop"})
    public static void stop(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        int timer;
        if (message.argsLength() > 0) {
            if (message.getString(0).equals("now")) {
                timer = 0;
            } else {
                timer = Integer.valueOf(message.getString(0));
            }
        } else {
            timer = 1;
        }
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        if (timer == 0) {
            scheduler.scheduleSyncDelayedTask(plugin, new AutoStop(plugin, 0, true));
        } else {
            scheduler.scheduleSyncRepeatingTask(plugin, new AutoStop(plugin, timer, true), 0, 10 * 20L);
        }
        player.sendSuccess("Server AutoStop Register Complete.");
    }

    @Command(aliases = {"restart"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.server.restart"})
    public static void restart(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        int timer;
        if (message.argsLength() > 0) {
            if (message.getString(0).equals("now")) {
                timer = 0;
            } else {
                timer = Integer.valueOf(message.getString(0));
            }
        } else {
            timer = 1;
        }
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        if (timer == 0) {
            scheduler.scheduleSyncDelayedTask(plugin, new AutoStop(plugin, 0, true));
        } else {
            scheduler.scheduleAsyncRepeatingTask(plugin, new AutoStop(plugin, 0, true), 10 * 20L, 10 * 20L);
        }
        player.sendSuccess("Server AutoStop Register Complete.");
    }

    @Command(aliases = {"remove-mob"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.remove-mob"})
    public static void removemob(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
        plugin.getMobManager().removeOtherMob();
    }

    @Command(aliases = {"gamemode"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.player.gamemode"})
    public static void gamemode(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (players.getGameMode().equals(GameMode.SURVIVAL)) {
            players.setGameMode(GameMode.CREATIVE);
            player.sendSuccess("ゲームモードをクリエイティブモードに変更しました");
        } else if (players.getGameMode().equals(GameMode.CREATIVE)) {
            players.setGameMode(GameMode.SURVIVAL);
            player.sendSuccess("ゲームモードをサバイバルモードに変更しました");
        }
    }

    @Command(aliases = {"ban"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.ban"})
    public static void ban(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, MituyaProjectException {
        if (message.argsLength() > 0) {
            PlayerInstance ins = plugin.getInstanceManager().matchSingleInstance(message.getString(0));
            if (ins == null) {
                throw new PlayerNotFoundException(message.getString(0));
            }
            String reason = "その他";
            try {
                Player pr = ins.getPlayer();
                if (message.argsLength() > 1) {
                    reason = message.getString(1);
                }
                pr.kickPlayer("BANされました 理由:" + reason + " 詳しくは http://328mss.com/ まで");
            }
            catch (PlayerOfflineException ex) {
            }
            plugin.getServer().getOfflinePlayer(ins.getRawName()).setBanned(true);
            if(!ins.getRawName().equals(ins.getName())) {
                plugin.broadcastGMMessage(ChatColor.RED + player.getName() + "が プレーヤー:" + ins.getName() + "("+ins.getRawName()+") をBanしました. 理由:" + reason);
            } else {
                plugin.broadcastGMMessage(ChatColor.RED + player.getName() + "が プレーヤー:" + ins.getName() + " をBanしました. 理由:" + reason);
            }
        } else {
            players.sendMessage("/ban playerID {理由}");
        }
    }

    @Command(aliases = {"unban"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.unban"})
    public static void unban(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, MituyaProjectException {
        if (message.argsLength() > 0) {
            OfflinePlayer pr = plugin.getServer().getOfflinePlayer(message.getString(0));
            if (pr == null) {
                throw new PlayerNotFoundException(message.getString(0));
            } else {
                pr.setBanned(false);
                plugin.broadcastGMMessage(ChatColor.RED + player.getName() + "が プレーヤー:" + pr.getName() + " をUnBanしました.");
            }
        } else {
            players.sendMessage("/unban playerID");
        }
    }

    @Command(aliases = {"cpu"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.info.cpu"})
    public static void cpu(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        float cpu = PerfomanceCheck.getCpuUsage();
        player.sendInfo(ChatColor.LIGHT_PURPLE + "ServerInfo", "CPU使用率：" + cpu);
        PerfomanceCheck.getCpuUsage();
    }

    /*@Command(aliases = {"op"},
     usage = "", desc = "",
     flags = "", min = 0, max = -1)
     @CommandPermissions({"SuperGM"})
     public static void op(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException {
    
     if (message.argsLength() > 0) {
     PlayerInstance ins = InstanceManager.getInstance(message.getString(0));
     Player pr = ins.getPlayer();
     if (pr == null) {
     if(pr.isOnline()) {
    
     } else {
    
     }
     OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(message.getString(0));
     if (offlinePlayer == null) {
     player.sendJapaneseAttension("プレーヤー"+message.getString(0)+" は見つかりませんでした。", "Player : "+message.getString(0)+" is not found.");
     } else {
     offlinePlayer.setOp(!offlinePlayer.isOp());
    
     }
     } else {
     pr.setBanned(false);
     plugin.broadcastGMJapaneseMessage(ChatColor.RED + player.getName() + "が プレーヤー:" + pr.getName() + " をUnBanしました.", ChatColor.RED + player.getName() + "  Player:" + pr.getName() + " set Unban! (banwo kaizyo simasita)");
     }
     } else {
     players.setOp(!players.isOp());
     }
     }*/
    @Command(aliases = {"tntdenyall"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.worldguard.tntdenyall"})
    public static void wgtnt(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        GlobalRegionManager globalRegionManager = plugin.getWorldGuard().getGlobalRegionManager();
        for (World wd : plugin.getServer().getWorlds()) {
            RegionManager mgr = globalRegionManager.get(wd);
            //個別にtntが許可されているところはスキップ
            mgr.getRegions().values().stream().filter(region -> region.getFlag(DefaultFlag.TNT) != StateFlag.State.ALLOW).forEach(region -> region.setFlag(DefaultFlag.TNT, StateFlag.State.DENY));
        }
        player.sendInfo(ChatColor.YELLOW + "WorldGuard", ChatColor.YELLOW + "全エリア保護にTNTフラグを設定しました");
    }

    @Command(aliases = {"creeperdenyall"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.worldguard.creeperdenyall"})
    public static void wgcreeper(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        GlobalRegionManager globalRegionManager = plugin.getWorldGuard().getGlobalRegionManager();
        for (World wd : plugin.getServer().getWorlds()) {
            RegionManager mgr = globalRegionManager.get(wd);
            //個別に匠が許可されているところはスキップ
            mgr.getRegions().values().stream().filter(region -> region.getFlag(DefaultFlag.CREEPER_EXPLOSION) != StateFlag.State.ALLOW).forEach(region -> region.setFlag(DefaultFlag.CREEPER_EXPLOSION, StateFlag.State.DENY));
        }
        player.sendInfo(ChatColor.YELLOW + "WorldGuard", ChatColor.YELLOW + "全エリア保護にクリーパーフラグを設定しました");
    }

    @Command(aliases = {"removeworld", "rwd", "deleteworld", "dwd"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.world.delete"})
    public static void deleteworld(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        plugin.DeleteWorld(message.getString(0));
        player.sendMessage("削除したっぽい");
    }

    @Command(aliases = {"tweet", "twitter"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.twitter.tweet"})
    public static void twitter(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        String msg = Tools.SignColor(message.getJoinedStrings(0));
        plugin.getTwitterManager().send328Tweet(msg);
        player.sendSuccess("Twitterにメッセージを投稿しました メッセージ：" + ChatColor.WHITE + msg);
    }

    @Command(aliases = {"pos"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.teleport.pos"})
    public static void pos(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 2) {
            Location location = new Location(players.getWorld(), message.getInteger(0), message.getInteger(1), message.getInteger(2));
            if (!getBorder(location, plugin.getWorldManager().getWorldData(location.getWorld()).getBorder())) {
                player.sendAttention("エリア外です");
                return;
            }
            players.teleport(location);
            player.sendSuccess("x:" + location.getX() + " y:" + location.getY() + " z:" + location.getZ() + " にテレポート中・・・・");
        } else {
            player.sendAttention("/pos x y z");
        }
    }

    @Command(aliases = {"exp"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.event.exp"})
    public static void exp(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            try {
                float f = Float.valueOf(message.getString(0));
                if (f <= 0) {
                    player.sendAttention("/exp 倍率 0.1 ~");
                } else {
                    Parameter328.Exp = f;
                }
                player.sendAttention("経験値倍率設定完了 倍率：" + f);
            }
            catch (NumberFormatException ex) {
                player.sendAttention("/exp 倍率");
            }
        } else {
            player.sendAttention("/exp 倍率");
            player.sendAttention("現在の倍率:" + Parameter328.Exp);
        }
    }

    @Command(aliases = {"setlv"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.player.setlv"})
    public static void setlv(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        if (message.argsLength() > 0) {
            players.setLevel(message.getInteger(0));
            player.sendSuccess("Levelを設定しました");
        } else {
            player.sendAttention("/setlv lv");
        }
    }

    @Command(aliases = {"mentenance", "mts", "メンテナンス"},
            usage = "", desc = "",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.server.mentenance"})
    public static void mentenance(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        Parameter328.Mentenance = !Parameter328.Mentenance;
        player.sendSuccess("メンテナンスモードを切り替えました メンテナンス:" + Tools.ReturnColorOnOff(Parameter328.Mentenance));
    }

    public static boolean getBorder(Location lo, int border) {
        return Math.abs(lo.getX()) <= border && Math.abs(lo.getZ()) <= border;
    }
}
