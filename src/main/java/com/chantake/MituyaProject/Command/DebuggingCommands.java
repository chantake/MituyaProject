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
import com.chantake.MituyaProject.Tool.Map.ImageRenderer;
import com.chantake.MituyaProject.Tool.Map.RenderUtils;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

/**
 *
 * @author fumitti
 */
public class DebuggingCommands {

    @Command(aliases = {"clock"},
            usage = "", desc = "Tests the clock rate of your server",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.clock"})
    public static void testClock(CommandContext args, MituyaProject plugin, final Player sender, PlayerInstance player) throws CommandException {

        int expected = 5;

        if (args.argsLength() == 1) {
            expected = Math.min(30, Math.max(1, args.getInteger(0)));
        }

        sender.sendMessage(ChatColor.DARK_RED
                + "Timing clock test for " + expected + " IN-GAME seconds...");
        sender.sendMessage(ChatColor.DARK_RED
                + "DO NOT CHANGE A WORLD'S TIME OR PERFORM A HEAVY OPERATION.");

        final World world = plugin.getServer().getWorlds().get(0);
        final double expectedTime = expected * 1000;
        final double expectedSecs = expected;
        final int expectedTicks = 20 * (int)expectedSecs;
        final long start = System.currentTimeMillis();
        final long startTicks = world.getFullTime();

        Runnable task;
        task = new Runnable() {

            @Override
            public void run() {
                long now = System.currentTimeMillis();
                long nowTicks = world.getFullTime();

                long elapsedTime = now - start;
                double elapsedSecs = elapsedTime / 1000.0;
                int elapsedTicks = (int)(nowTicks - startTicks);

                double error = (expectedTime - elapsedTime) / elapsedTime * 100;
                double clockRate = elapsedTicks / elapsedSecs;

                if (expectedTicks != elapsedTicks) {
                    sender.sendMessage(ChatColor.DARK_RED
                            + "Warning: Bukkit scheduler inaccurate; expected "
                            + expectedTicks + ", got " + elapsedTicks);
                }

                if (Math.round(clockRate) == 20) {
                    sender.sendMessage(ChatColor.YELLOW + "Clock test result: "
                            + ChatColor.GREEN + "EXCELLENT");
                } else {
                    if (elapsedSecs > expectedSecs) {
                        if (clockRate < 19) {
                            sender.sendMessage(ChatColor.YELLOW + "Clock test result: "
                                    + ChatColor.DARK_RED + "CLOCK BEHIND");
                            sender.sendMessage(ChatColor.DARK_RED
                                    + "WARNING: You have potential block respawn issues.");
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + "Clock test result: "
                                    + ChatColor.DARK_RED + "CLOCK BEHIND");
                        }
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "Clock test result: "
                                + ChatColor.DARK_RED + "CLOCK AHEAD");
                    }
                }

                sender.sendMessage(ChatColor.GRAY + "Expected time elapsed: " + expectedTime + "ms");
                sender.sendMessage(ChatColor.GRAY + "Time elapsed: " + elapsedTime + "ms");
                sender.sendMessage(ChatColor.GRAY + "Error: " + error + "%");
                sender.sendMessage(ChatColor.GRAY + "Actual clock rate: " + clockRate + " ticks/sec");
                sender.sendMessage(ChatColor.GRAY + "Expected clock rate: 20 ticks/sec");
            }
        };

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, expectedTicks);
    }

    @Command(aliases = {"info"},
            usage = "", desc = "Get server information",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituya.info"})
    public static void serverInfo(CommandContext message, MituyaProject plugin, Player sender, PlayerInstance player) throws CommandException {
        Runtime rt = Runtime.getRuntime();

        sender.sendMessage(ChatColor.YELLOW
                + String.format("System: %s %s (%s)",
                        System.getProperty("os.name"),
                        System.getProperty("os.version"),
                        System.getProperty("os.arch")));
        sender.sendMessage(ChatColor.YELLOW
                + String.format("Java: %s %s (%s)",
                        System.getProperty("java.vendor"),
                        System.getProperty("java.version"),
                        System.getProperty("java.vendor.url")));
        sender.sendMessage(ChatColor.YELLOW
                + String.format("JVM: %s %s %s",
                        System.getProperty("java.vm.vendor"),
                        System.getProperty("java.vm.name"),
                        System.getProperty("java.vm.version")));

        sender.sendMessage(ChatColor.YELLOW + "Available processors: "
                + rt.availableProcessors());

        sender.sendMessage(ChatColor.YELLOW + "Available total memory: "
                + Math.floor(rt.maxMemory() / 1024.0 / 1024.0) + " MB");

        sender.sendMessage(ChatColor.YELLOW + "JVM allocated memory: "
                + Math.floor(rt.totalMemory() / 1024.0 / 1024.0) + " MB");

        sender.sendMessage(ChatColor.YELLOW + "Free allocated memory: "
                + Math.floor(rt.freeMemory() / 1024.0 / 1024.0) + " MB");
    }

    @Command(aliases = {"uptime"},
            usage = "", desc = "Get server uptime",
            flags = "", min = 0, max = -1)
    @CommandPermissions({"mituyaproject.rank.admin"})
    public static void uptime(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) throws CommandException, PlayerOfflineException {
        final Calendar cal1 = plugin.uptime;
        final int year = cal1.get(Calendar.YEAR);
        final int month = cal1.get(Calendar.MONTH) + 1;
        final int day = cal1.get(Calendar.DATE);
        final int hour = cal1.get(Calendar.HOUR_OF_DAY);
        final int minute = cal1.get(Calendar.MINUTE);
        final int second = cal1.get(Calendar.SECOND);
        player.sendSystem("uptime:"
                + year
                + "/"
                + month
                + "/"
                + day
                + " "
                + hour
                + ":"
                + minute
                + ":"
                + second
                + " Uptime"
                + (System.currentTimeMillis() - plugin.uptime_start));
    }

    @Command(aliases = {"maptest"},
            usage = "", desc = "maptest",
            flags = "ls", min = 0, max = 1)
    @CommandPermissions({"mituyaproject.rank.admin"})
    public static void maptest(CommandContext message, MituyaProject plugin, Player players, PlayerInstance player) {
        MapView viewport = plugin.getServer().getMap(players.getItemInHand().getDurability());
        //MapView viewport = plugin.getServer().createMap(players.getWorld());
        ImageRenderer renderer;

        try {
            URL toDraw = message.hasFlag('l') ? new File(plugin.getDataFolder().getPath() + "img/", message.getString(0)).toURL() : new URL(message.getString(0));
            renderer = new ImageRenderer(toDraw);
            RenderUtils.removeRenderers(viewport);
            viewport.addRenderer(renderer);
            players.sendMap(viewport);

            players.sendMessage(ChatColor.AQUA + "[Map] Rendering " + message.getString(0) + "!");
        } catch (IOException e) {
            player.sendMessage("Encountered error while grabbing the image.");
            e.printStackTrace();
        }
    }
}
