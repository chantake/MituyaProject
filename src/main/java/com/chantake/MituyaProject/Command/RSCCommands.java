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
import com.chantake.MituyaProject.RSC.BitSet.BitSet7;
import com.chantake.MituyaProject.RSC.BitSet.BitSetUtils;
import com.chantake.MituyaProject.RSC.Circuit.Circuit;
import com.chantake.MituyaProject.RSC.Circuit.CircuitIndex;
import com.chantake.MituyaProject.RSC.Circuit.Filter.*;
import com.chantake.MituyaProject.RSC.Circuit.IO.IOBlock;
import com.chantake.MituyaProject.RSC.Circuit.IO.IOBlock.Type;
import com.chantake.MituyaProject.RSC.Circuit.IO.InputPin;
import com.chantake.MituyaProject.RSC.Circuit.IO.InterfaceBlock;
import com.chantake.MituyaProject.RSC.Circuit.IO.OutputPin;
import com.chantake.MituyaProject.RSC.Circuit.RCTypeReceiver;
import com.chantake.MituyaProject.RSC.Circuit.Scan.ScanParameters;
import com.chantake.MituyaProject.RSC.CommandUtils;
import com.chantake.MituyaProject.RSC.PrefsManager;
import com.chantake.MituyaProject.RSC.RedstoneChips;
import com.chantake.MituyaProject.RSC.Session.Debugger.Flag;
import com.chantake.MituyaProject.RSC.Session.UserSession.Mode;
import com.chantake.MituyaProject.RSC.Session.*;
import com.chantake.MituyaProject.RSC.Wireless.BroadcastChannel;
import com.chantake.MituyaProject.RSC.Wireless.Receiver;
import com.chantake.MituyaProject.RSC.Wireless.Transmitter;
import com.chantake.MituyaProject.Tool.ChunkLocation;
import com.chantake.MituyaProject.Tool.Page.ArrayLineSource;
import com.chantake.MituyaProject.Tool.Page.Pager;
import com.chantake.MituyaProject.Tool.Parsing.ParsingUtils;
import com.chantake.MituyaProject.Tool.Parsing.Tokenizer;
import com.chantake.mituyaapi.commands.Command;
import com.chantake.mituyaapi.commands.CommandContext;
import com.chantake.mituyaapi.commands.CommandException;
import com.chantake.mituyaapi.commands.CommandPermissions;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 *
 * @author fumitti
 */
public class RSCCommands {

    static RedstoneChips rc;

    public RSCCommands() {
    }

    @Command(aliases = {"rcactivate"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    //@CommandPermissions({"GM"})
    public static void rcActivate(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        String args[] = command.getNonCommandNameArgs();
        if (sender == null) {
        }

        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }

        Block target = CommandUtils.targetBlock(sender);
        if (target.getType() == Material.WALL_SIGN) {
            MaterialData inputBlockType = null, outputBlockType = null, interfaceBlockType = null;

            int verboseLevel = -1;
            if (args.length > 0) {
                String lastArg = args[args.length - 1];
                if (lastArg.equalsIgnoreCase("-v")) {
                    verboseLevel = 1;
                } else if (lastArg.startsWith("-v")) {
                    String sl = lastArg.substring(2);
                    if (ParsingUtils.isInt(sl)) {
                        verboseLevel = Integer.parseInt(sl);
                    } else {
                        sender.sendMessage(rc.getPrefs().getErrorColor() + "Unknown option: " + lastArg);
                    }
                }

                try {
                    if (args.length >= (verboseLevel != -1 ? 2 : 1)) {
                        inputBlockType = PrefsManager.findMaterial(args[0]);
                    }
                    if (args.length >= (verboseLevel != -1 ? 3 : 2)) {
                        outputBlockType = PrefsManager.findMaterial(args[1]);
                    }
                    if (args.length >= (verboseLevel != -1 ? 4 : 3)) {
                        interfaceBlockType = PrefsManager.findMaterial(args[2]);
                    }

                    if (verboseLevel == -1) {
                        verboseLevel = 0;
                    }
                }
                catch (IllegalArgumentException ie) {
                    sender.sendMessage(rc.getPrefs().getErrorColor() + ie.getMessage());

                }

            }

            if (inputBlockType == null) {
                inputBlockType = rc.getPrefs().getInputBlockType();
            }
            if (outputBlockType == null) {
                outputBlockType = rc.getPrefs().getOutputBlockType();
            }
            if (interfaceBlockType == null) {
                interfaceBlockType = rc.getPrefs().getInterfaceBlockType();
            }

            activate(target, inputBlockType, outputBlockType, interfaceBlockType, sender, verboseLevel, rc);

        } else {
            sender.sendMessage(rc.getPrefs().getErrorColor() + "You need to point at a wall sign.");
        }

    }

    @Command(aliases = {"rcarg"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcArg(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        Player player = CommandUtils.checkIsPlayer(rc, sender);
        if (player == null) {
        }
        String cmdArgs[] = command.getNonCommandNameArgs();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }

        Block target = CommandUtils.targetBlock(player);
        Circuit c = rc.getCircuitManager().getCircuitByStructureBlock(target.getLocation());
        if (c == null) {
            player.sendMessage(rc.getPrefs().getErrorColor() + "You need to point at a block of the circuit you wish to reset.");

        }

        if (cmdArgs.length < 2 || (cmdArgs.length % 2) != 0) {
            player.sendMessage(rc.getPrefs().getErrorColor() + "Bad syntax. Expecting /rcarg <arg index/clear/add> <arg value>...<arg index> <arg value>");

        }

        String[] args = new String[c.args.length];
        System.arraycopy(c.args, 0, args, 0, args.length);

        for (int i = 0; i < cmdArgs.length; i += 2) {
            String[] editArgs = RSCCommands.editArgs(sender, args, cmdArgs[i], cmdArgs[i + 1]);
            if (editArgs != null) {
                args = editArgs;
            }
        }

        String oldArgs[] = new String[c.args.length];
        System.arraycopy(c.args, 0, oldArgs, 0, oldArgs.length);

        editSignArgs(c, args);

        if (!rc.getCircuitManager().resetCircuit(c, sender)) {
            sender.sendMessage(rc.getPrefs().getErrorColor() + "Could not reactivate circuit with new sign arguments. ");

            // revert to old args.
            editSignArgs(c, oldArgs);
            rc.getCircuitManager().resetCircuit(c, sender);
        }

    }

    @Command(aliases = {"rcbreak"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcBreak(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        String args[] = command.getNonCommandNameArgs();
        Circuit c;
        if (args.length > 0) { // use chip id
            if (!CommandUtils.checkPermission(rc, sender, command.getCommand() + ".id", true, false)) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permission to remotely deactivate a circuit.");

            }

            c = rc.getCircuitManager().getCircuitById(args[0]);

            if (c == null) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "There's no activated circuit with id " + args[0]);

            }
        } else { // use target block.
            c = CommandUtils.findTargetCircuit(rc, sender);
            if (c == null) {
            }
        }

        if (rc.getCircuitManager().destroyCircuit(c, sender, false)) {
            sender.sendMessage(ChatColor.YELLOW + c.getChipString() + rc.getPrefs().getInfoColor() + " was deactivated.");
        }

    }

    @Command(aliases = {"rcchannels"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcChannels(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        String args[] = command.getNonCommandNameArgs();
        if (rc.getChannelManager().getBroadcastChannels().isEmpty()) {
            sender.sendMessage(rc.getPrefs().getInfoColor() + "There are no active broadcast channels.");
        } else {
            if (args.length > 0 && rc.getChannelManager().getBroadcastChannels().containsKey(args[0])) {
                if (!(checkChanPermissions(sender, args[0]))) {
                }
                printChannelInfo(sender, args[0]);
            } else {
                List<String> lines = new ArrayList<>();
                for (BroadcastChannel channel : rc.getChannelManager().getBroadcastChannels().values()) {
                    if (channel.checkChanPermissions(sender, false)) {
                        lines.add(ChatColor.YELLOW + channel.name + ChatColor.WHITE + " - " + channel.getLength() + " bits, " + channel.getTransmitters().size() + " transmitters, " + channel.getReceivers().size() + " receivers." + ChatColor.GREEN + (channel.isProtected() ? " Protected" : ""));
                    }
                }

                if (lines.isEmpty()) {
                    sender.sendMessage(rc.getPrefs().getInfoColor() + "There are no active broadcast channels.");

                }
                String[] outputLines = lines.toArray(new String[lines.size()]);
                sender.sendMessage("");
                Pager.beginPaging(sender, "Active wireless broadcast channels", new ArrayLineSource(outputLines), rc.getPrefs().getInfoColor(), rc.getPrefs().getErrorColor(), Pager.MaxLines - 1);
                sender.sendMessage("Use " + ChatColor.YELLOW + "/rcchannels <channel name>" + ChatColor.WHITE + " for more info about it.");
            }
        }

    }

    @Command(aliases = {"rcclasses"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcClasses(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        List<CircuitIndex> libs = rc.getCircuitLoader().getCircuitLibraries();

        if (libs.isEmpty()) {
            sender.sendMessage(rc.getPrefs().getInfoColor() + "There are no circuit classes installed.");
        } else {
            printClassesList(sender, libs);
        }

    }

    @Command(aliases = {"rcdebug"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcDebug(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        Player p = CommandUtils.checkIsPlayer(rc, sender);
        if (p == null) {
        }
        String args[] = command.getNonCommandNameArgs();
        if (args.length == 0) {
            // toggle debug on target chip.
            Circuit c = CommandUtils.findTargetCircuit(rc, sender);
            if (c == null) {
            } else {
                Debugger d = rc.getUserSession(p, true).getDebugger();
                toggleCircuitDebug(sender, d, c);
            }

        } else {
            if ("list".startsWith(args[0].toLowerCase())) {
                listDebuggedCircuits(p);

            }

            Debugger d = rc.getUserSession(p, true).getDebugger();
            if ("clear".startsWith(args[0].toLowerCase())) {
                d.clear();
                sender.sendMessage(rc.getPrefs().getInfoColor() + "Cleared debug list.");
            } else if (args[0].equals(".")) {
                pauseDebugger(sender, d);

            } else if (args[0].equalsIgnoreCase("io")) { // toggle io messages on target chip.
                Circuit c = CommandUtils.findTargetCircuit(rc, sender);
                if (c == null) {
                } else {
                    toggleCircuitIODebug(sender, d, c);
                }

            } else if (args.length >= 2 && args[1].equalsIgnoreCase("io")) { // toggle io messages using chip id.
                Circuit c = rc.getCircuitManager().getCircuitById(args[0]);
                if (c == null) {
                    sender.sendMessage(rc.getPrefs().getErrorColor() + "Unknown circuit id or bad argument: " + args[0]);

                } else {
                    toggleCircuitIODebug(sender, d, c);
                }

            } else { // toggle debug using chip id.
                Circuit c = rc.getCircuitManager().getCircuitById(args[0]);
                if (c == null) {
                    sender.sendMessage(rc.getPrefs().getErrorColor() + "Unknown circuit id or bad argument: " + args[0]);

                } else {
                    toggleCircuitDebug(sender, d, c);
                }
            }
        }

    }

    @Command(aliases = {"rcdestroy"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcDestroy(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        Circuit c = CommandUtils.findTargetCircuit(rc, sender);
        if (c != null) {
            if (rc.getCircuitManager().destroyCircuit(c, sender, true)) {
                sender.sendMessage(rc.getPrefs().getInfoColor() + "The " + c.getCircuitClass() + " chip is destroyed.");
            }
        }

    }

    @Command(aliases = {"rcdisable"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcDisable(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        String args[] = command.getNonCommandNameArgs();
        Circuit c;
        if (args.length == 0) { // use target circuit
            c = CommandUtils.findTargetCircuit(rc, sender);
            if (c == null) {
            }
        } else {
            if (!CommandUtils.checkPermission(rc, sender, command.getCommand() + ".id", true, false)) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permissions to remotely disable a circuit.");

            }

            c = rc.getCircuitManager().getCircuitById(args[0]);
            if (c == null) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "There's no activated chip with id " + args[0]);

            }

        }

        if (!c.isDisabled()) {
            c.disable();
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Disabled " + c.getChipString() + ".");
        } else {
            sender.sendMessage(rc.getPrefs().getDebugColor() + "The chip is already disabled.");
        }

    }

    @Command(aliases = {"rcenable"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcEnable(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        String args[] = command.getNonCommandNameArgs();
        Circuit c;
        if (args.length == 0) { // use target circuit
            c = CommandUtils.findTargetCircuit(rc, sender);
            if (c == null) {
            }
        } else {
            if (!CommandUtils.checkPermission(rc, sender, command.getCommand() + ".id", true, false)) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permissions to remotely enable a circuit.");

            }

            c = rc.getCircuitManager().getCircuitById(args[0]);
            if (c == null) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "There's no activated chip with id " + args[0]);

            }

        }

        if (c.isDisabled()) {
            c.enable();
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Enabled " + c.getChipString() + ".");
        } else {
            sender.sendMessage(rc.getPrefs().getDebugColor() + "The chip is already enabled.");
        }

    }

    @Command(aliases = {"rcfixioblocks"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcFixIOBlocks(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        Circuit c;
        String args[] = command.getNonCommandNameArgs();
        if (args.length > 0) { // use circuit id.
            if (!CommandUtils.checkPermission(rc, sender, command.getCommand() + ".id", true, false)) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permission to use this command with a circuit id.");

            }
            c = rc.getCircuitManager().getCircuitById(args[0]);
            if (c == null) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "Unknown circuit id: " + args[0] + ".");

            }
        } else { // use targeted circuit
            if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
            }
            c = CommandUtils.findTargetCircuit(rc, sender);
            if (c == null) {
            }
        }

        int blockCount = c.fixIOBlocks();

        sender.sendMessage(rc.getPrefs().getInfoColor() + "Finished fixing i/o blocks of " + c.getChipString() + ". " + blockCount + " blocks were replaced.");

    }

    @Command(aliases = {"rcinfo"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcInfo(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        String args[] = command.getNonCommandNameArgs();
        Circuit c;
        if (args.length == 0) { // use target circuit
            c = CommandUtils.findTargetCircuit(rc, sender);
            if (c == null) {
            }
        } else {

            c = rc.getCircuitManager().getCircuitById(args[0]);
            if (c == null) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "There's no activated chip with id " + args[0]);

            }

        }

        printCircuitInfo(sender, c, rc);

    }

    @Command(aliases = {"rclist"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcList(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        String args[] = command.getNonCommandNameArgs();
        List<CircuitFilter> filters = new ArrayList<>();

        boolean bthis = false;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("all")); else if (args[0].equalsIgnoreCase("this")) {
                if (sender instanceof Player) {
                    bthis = true;
                } else {
                    sender.sendMessage("You have to be a player to use the 'this' keyword.");
                }
            } else {
                CircuitFilter f = new WorldFilter().setPlugin(rc);
                try {
                    f.parse(sender, tokenizeFilter(args[0]));
                }
                catch (IllegalArgumentException ie) {
                    sender.sendMessage(rc.getPrefs().getErrorColor() + ie.getMessage());
                }
                filters.add(f);
            }
        }

        if (bthis || (filters.isEmpty() && (sender instanceof Player))) {
            CircuitFilter f = new WorldFilter().setPlugin(rc);
            ((WorldFilter)f).world = ((Player)sender).getWorld();
            filters.add(f);
        }

        if (args.length > 1) {
            StringBuilder concat = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                concat.append(args[i]);
                concat.append(" ");
            }

            String sfilters = concat.toString().trim();
            String[] afilters = new Tokenizer(sfilters, ';').getTokens();

            try {
                for (String sf : afilters) {
                    CircuitFilter f = parseFilter(sender, sf);
                    filters.add(f);
                }
            }
            catch (IllegalArgumentException ie) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + ie.getMessage());

            }
        }

        Collection<Circuit> circuits = filterCircuits(rc.getCircuitManager().getCircuits().values(),
                filters.toArray(new CircuitFilter[filters.size()]));
        TreeMap<Integer, Circuit> sorted = new TreeMap<>();
        for (Circuit c : circuits) {
            sorted.put(c.id, c);
        }

        if (circuits.isEmpty()) {
            sender.sendMessage(rc.getPrefs().getInfoColor() + "There are no active chips that match the criteria.");
        } else {
            printCircuitList(sender, sorted.values(), null, rc);
        }

    }

    @Command(aliases = {"rcload"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcLoad(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), true, true)) {
        }
        for (World world : plugin.getServer().getWorlds()) {
            rc.getCircuitPersistence().loadCircuits(world);
        }

        if (sender instanceof Player) {
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Done loading " + rc.getCircuitManager().getCircuits().size() + " chip(s). Note: Errors and warnings are only printed to the server console.");
        } else {
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Done loading " + rc.getCircuitManager().getCircuits().size() + " chip(s).");
        }

    }

    @Command(aliases = {"rcname"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcName(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        String args[] = command.getNonCommandNameArgs();
        Circuit c = null;
        if (args.length == 1) { // use target block.
            c = CommandUtils.findTargetCircuit(rc, sender);
            if (c == null) {
            }
        } else if (args.length == 2) { // use circuit id.
            if (!CommandUtils.checkPermission(rc, sender, command.getCommand() + ".id", true, false)) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permission to remotely name a circuit.");

            }

            c = rc.getCircuitManager().getCircuitById(args[0]);
            if (c == null) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "There's no activated circuit with id " + args[0]);

            }
        } else {
            sender.sendMessage("Bad /rcname command.");

        }

        String name = args[args.length - 1];

        if (ParsingUtils.isInt(name)) {
            sender.sendMessage(rc.getPrefs().getErrorColor() + "Chip name can't be a number.");

        }

        if (name.equalsIgnoreCase("remove")) {
            name = null;
        }

        Circuit b = rc.getCircuitManager().getCircuitById(name);
        if (b != null) {
            sender.sendMessage(rc.getPrefs().getErrorColor() + "Chip name is already used.");

        }

        c.name = name;

        if (name != null) {
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Renamed chip: " + ChatColor.YELLOW + c.getChipString());
        } else {
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Removed name: " + ChatColor.YELLOW + c.getChipString());

        }

    }

    @Command(aliases = {"rcp"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcP(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        // /rcp <page no> or /rcp <prev|next|last>
        String args[] = command.getNonCommandNameArgs();
        if (Pager.hasPageInfo(sender)) {
            if (args.length > 1) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "Bad rcp syntax. Expecting /rcp <page no.|prev|next|last>.");

            } else if (args.length == 0) {
                Pager.nextPage(sender);

            } else if (args.length == 1) {
                if (ParsingUtils.isNumber(args[0])) {
                    Pager.gotoPage(sender, Integer.valueOf(args[0]));
                } else if ("previous".startsWith(args[0])) {
                    Pager.previousPage(sender);
                } else if ("next".startsWith(args[0])) {
                    Pager.nextPage(sender);
                } else if ("last".startsWith(args[0])) {
                    Pager.lastPage(sender);
                } else {
                    sender.sendMessage(rc.getPrefs().getErrorColor() + "Bad rcp syntax. Expecting /rcp <page no.|prev|next|last>.");
                }
            }
        } else {
            sender.sendMessage(rc.getPrefs().getErrorColor() + "You need to use a command with pages before using /rcp.");
        }

    }

    @Command(aliases = {"rcpin"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcPin(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        Player player = CommandUtils.checkIsPlayer(rc, sender);
        if (player == null) {
        }
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }

        Block target = CommandUtils.targetBlock(player);
        try {
            printPinInfo(target, player, rc);
        }
        catch (IllegalArgumentException e) {
            sender.sendMessage(rc.getPrefs().getErrorColor() + e.getMessage());
        }

    }

    @Command(aliases = {"rcprefs"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcPrefs(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        String args[] = command.getNonCommandNameArgs();
        if (args.length == 0) { // list preferences
            rc.getPrefs().printYaml(sender, rc.getPrefs().getPrefs());
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Type /rcprefs <name> <value> to make changes.");
        } else if (args.length == 1) { // show one key value pair
            Object o = rc.getPrefs().getPrefs().get(args[0]);
            if (o == null) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "Unknown preferences key: " + args[0]);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put(args[0], o);

                rc.getPrefs().printYaml(sender, map);
            }
        } else if (args.length >= 2) { // set value
            if (!CommandUtils.checkPermission(rc, sender, command.getCommand() + ".set", true, false)) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permissions to change preferences values.");

            }

            String val = "";
            for (int i = 1; i < args.length; i++) {
                val += args[i] + " ";
            }

            try {
                Map<String, Object> map = rc.getPrefs().setYaml(args[0] + ": " + val);
                rc.getPrefs().printYaml(sender, map);
            }
            catch (IllegalArgumentException ie) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + ie.getMessage());

            }
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Saving changes...");
            rc.getPrefs().savePrefs();
        } else {
            sender.sendMessage(rc.getPrefs().getErrorColor() + "Bad rcprefs syntax.");
        }

    }

    public static enum ProtCommand {

        PROTECT, UNPROTECT, ADD, REMOVE
    }

    @Command(aliases = {"rcprotect"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcProtect(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        ChatColor extraColor = ChatColor.YELLOW;
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        String args[] = command.getNonCommandNameArgs();
        if (args.length == 0) {
            //todo error
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("listchannels")) {
                String protectedChannels = "";
                for (BroadcastChannel curChannel : rc.getChannelManager().getBroadcastChannels().values()) {
                    if (curChannel.isProtected()) {
                        protectedChannels += curChannel.name + ", ";
                    }
                }

                if (!protectedChannels.isEmpty()) {
                    if (sender != null) {
                        sender.sendMessage(rc.getPrefs().getInfoColor() + "Protected Channels: " + protectedChannels.substring(0, protectedChannels.length() - 2));
                    }
                } else {
                    if (sender != null) {
                        sender.sendMessage(rc.getPrefs().getInfoColor() + "There are no protected channels.");
                    }
                }

            } else if (rc.getChannelManager().getBroadcastChannels().containsKey(args[0])) {
                BroadcastChannel curChannel = rc.getChannelManager().getChannelByName(args[0], false);
                if (curChannel.isProtected()) {
                    if (!curChannel.checkChanPermissions(sender, false)) {
                        if (sender != null) {
                            sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permissions to list channel " + args[0] + ".");
                        }

                    }

                    String owners = "";
                    String users = "";
                    for (String owner : curChannel.owners) {
                        owners += owner + ", ";
                    }

                    for (String user : curChannel.users) {
                        users += user + ", ";
                    }

                    if (!owners.isEmpty()) {
                        if (sender != null) {
                            sender.sendMessage(rc.getPrefs().getInfoColor() + "admins: " + extraColor + owners.substring(0, owners.length() - 2));
                        }
                    }
                    if (!users.isEmpty()) {
                        if (sender != null) {
                            sender.sendMessage(rc.getPrefs().getInfoColor() + "users: " + extraColor + users.substring(0, users.length() - 2));
                        }
                    }
                } else {
                    if (sender != null) {
                        sender.sendMessage(rc.getPrefs().getErrorColor() + "Channel " + args[0] + " is not protected.");
                    }
                }
            } else {
                if (sender != null) {
                    sender.sendMessage(rc.getPrefs().getErrorColor() + "Channel " + args[0] + " not found.");
                }
            }
        }

        if (args.length >= 2) {
            ProtCommand protCommand = null;
            BroadcastChannel curChannel;

            try {
                protCommand = ProtCommand.valueOf(args[1].toUpperCase());
            }
            catch (IllegalArgumentException ie) {
                if (sender != null) {
                    sender.sendMessage(rc.getPrefs().getErrorColor() + "Unknown command: " + args[1]);
                }

            }

            switch (protCommand) {
                case PROTECT:
                    curChannel = rc.getChannelManager().getChannelByName(args[0], true);
                    if (!curChannel.isProtected()) {
                        if (!(sender instanceof Player) && args.length < 3) {
                            if (sender != null) {
                                sender.sendMessage(rc.getPrefs().getErrorColor() + "Usernames must be specified if run from console.");
                            }

                        }

                        if (args.length > 2) {
                            if (!addUsers(args, curChannel)) {
                                if (sender != null) {
                                    sender.sendMessage(rc.getPrefs().getErrorColor() + "Unable to parse user list.");
                                }

                            }
                        }

                        if (sender instanceof Player) {
                            if (!curChannel.owners.contains(((Player)sender).getName().toLowerCase())) {
                                curChannel.owners.add(((Player)sender).getName().toLowerCase());
                            }
                        }

                        if (sender != null) {
                            sender.sendMessage(rc.getPrefs().getInfoColor() + "Channel " + args[0] + " has been protected.");
                        }
                    } else {
                        if (sender != null) {
                            sender.sendMessage(rc.getPrefs().getErrorColor() + "Channel " + args[0] + " is already protected.");
                        }
                    }
                    break;
                case UNPROTECT:
                    if (rc.getChannelManager().getBroadcastChannels().containsKey(args[0])) {
                        curChannel = rc.getChannelManager().getChannelByName(args[0], false);
                        if (!curChannel.checkChanPermissions(sender, true)) {
                            if (sender != null) {
                                sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permissions to modify channel " + args[0] + ".");
                            }

                        }
                        if (curChannel.isProtected()) {
                            curChannel.owners.clear();
                            curChannel.users.clear();
                            if (sender != null) {
                                sender.sendMessage(rc.getPrefs().getInfoColor() + "Channel " + args[0] + " has been unprotected.");
                            }
                        } else {
                            if (sender != null) {
                                sender.sendMessage(rc.getPrefs().getErrorColor() + "Channel " + args[0] + " is not protected.");
                            }
                        }
                    } else {
                        if (sender != null) {
                            sender.sendMessage(rc.getPrefs().getErrorColor() + "Channel " + args[0] + " not found.");
                        }
                    }
                    break;
                case ADD:
                    if (rc.getChannelManager().getBroadcastChannels().containsKey(args[0])) {
                        curChannel = rc.getChannelManager().getChannelByName(args[0], false);
                        if (!curChannel.checkChanPermissions(sender, true)) {
                            if (sender != null) {
                                sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permissions to modify channel " + args[0] + ".");
                            }

                        }
                        if (curChannel.isProtected()) {
                            if (args.length > 2) {
                                if (!addUsers(args, curChannel)) {
                                    if (sender != null) {
                                        sender.sendMessage(rc.getPrefs().getErrorColor() + "Unable to parse user list.");
                                    }

                                }
                                if (sender != null) {
                                    sender.sendMessage(rc.getPrefs().getInfoColor() + "Channel " + args[0] + " has been updated.");
                                }
                            } else {
                                if (sender != null) {
                                    sender.sendMessage(rc.getPrefs().getErrorColor() + "No usernames passed.");
                                }
                            }
                        } else {
                            if (sender != null) {
                                sender.sendMessage(rc.getPrefs().getErrorColor() + "Channel " + args[0] + " is not protected.");
                            }
                        }
                    } else {
                        if (sender != null) {
                            sender.sendMessage(rc.getPrefs().getErrorColor() + "Channel " + args[0] + " not found.");
                        }
                    }
                    break;
                case REMOVE:
                    if (rc.getChannelManager().getBroadcastChannels().containsKey(args[0])) {
                        curChannel = rc.getChannelManager().getChannelByName(args[0], true);
                        if (!curChannel.checkChanPermissions(sender, true)) {
                            if (sender != null) {
                                sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permissions to modify channel " + args[0] + ".");
                            }

                        }
                        if (curChannel.isProtected()) {
                            if (args.length > 2) {
                                if (!removeUsers(args, curChannel)) {
                                    if (sender != null) {
                                        sender.sendMessage(rc.getPrefs().getErrorColor() + "Unable to parse user list.");
                                    }

                                }
                                if (sender != null) {
                                    sender.sendMessage(rc.getPrefs().getInfoColor() + "Channel " + args[0] + " has been updated.");
                                }
                            } else {
                                if (sender != null) {
                                    sender.sendMessage(rc.getPrefs().getErrorColor() + "No usernames passed.");
                                }
                            }
                        } else {
                            if (sender != null) {
                                sender.sendMessage(rc.getPrefs().getErrorColor() + "Channel " + args[0] + " is not protected.");
                            }
                        }
                    } else {
                        if (sender != null) {
                            sender.sendMessage(rc.getPrefs().getErrorColor() + "Channel " + args[0] + " not found.");
                        }
                    }
                    break;
            }
        }

    }

    @Command(aliases = {"rcreset"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcReset(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        Circuit c = null;
        String args[] = command.getNonCommandNameArgs();
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("all")) {
                if (CommandUtils.checkPermission(rc, sender, command.getCommand() + ".all", true, false)) {
                    resetAllCircuits(sender);
                } else {
                    sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permissions to reset all chips.");
                }

            } else {
                if (CommandUtils.checkPermission(rc, sender, command.getCommand() + ".id", true, false)) {
                    c = rc.getCircuitManager().getCircuitById(args[0]);
                    if (c == null) {
                        sender.sendMessage(rc.getPrefs().getErrorColor() + "Unknown chip id: " + args[0] + ".");

                    }
                } else {
                    sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permissions to remotely reset a chip.");

                }
            }
        } else { // use targeted circuit
            if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
            }
            c = CommandUtils.findTargetCircuit(rc, sender);
            if (c == null) {
            }
        }

        rc.getCircuitManager().resetCircuit(c, sender);

    }

    @Command(aliases = {"rcsave"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcSave(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), true, true)) {
        }
        rc.getCircuitPersistence().saveCircuits();
        if (sender instanceof Player) {
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Done saving " + rc.getCircuitManager().getCircuits().size() + " chips.");
        }

    }
    public static final ChatColor color = ChatColor.AQUA;

    public static enum SelCommand {

        WORLD(null), CUBOID(null), ID(null), TARGET(null), ACTIVATE("Activated"), RESET("Reset"), BREAK("Deactivated"), LIST(null),
        DESTROY("Destroyed"), FIXIOBLOCKS("Fixed"), CLEAR(null), ENABLE("Enabled"), DISABLE("Disabled");
        String verb;

        SelCommand(String verb) {
            this.verb = verb;
        }

        public static SelCommand startsWith(String s) {
            s = s.toLowerCase();
            for (SelCommand c : SelCommand.values()) {
                if (c.name().toLowerCase().startsWith(s)) {
                    return c;
                }
            }

            return null;
        }
    }

    @Command(aliases = {"rcsel"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcSel(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        Player p = CommandUtils.checkIsPlayer(rc, sender);
        if (p == null) {
        }
        String args[] = command.getNonCommandNameArgs();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }

        SelCommand selCommand = null;

        if (args.length == 0) {
            UserSession session = rc.getUserSession(p, true);

            if (session.getMode() == Mode.SELECTION) {
                session.setMode(Mode.NORMAL);
                p.sendMessage(color + "Stopped selecting chips.");
            } else {
                session.setMode(Mode.SELECTION);
                p.sendMessage(rc.getPrefs().getInfoColor() + "rcselection");
                p.sendMessage(rc.getPrefs().getInfoColor() + "-----------------");
                p.sendMessage(color + "Right-click a chip block to select it. Run " + ChatColor.YELLOW + "/rcsel" + color + " again to stop selecting.");
                p.sendMessage(color + "Run " + ChatColor.YELLOW + "/rchelp rcsel" + color + " for more selection commands.");
            }

            printSelMessage(p, session);

        } else {
            selCommand = SelCommand.startsWith(args[0]);
            if (selCommand == null) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "Unknown selection command: " + args[0]);

            }
        }

        if (selCommand == SelCommand.CUBOID) {
            UserSession session = rc.getUserSession(p, true);
            Location[] sel = null;
            if (WorldEditHook.isWorldEditInstalled(rc)) {
                sel = WorldEditHook.getWorldEditSelection(p, rc);
            }

            if (sel != null) {
                p.sendMessage(rc.getPrefs().getInfoColor() + "Using WorldEdit selection.");
                session.selectChipsInCuboid(sel, false);
                session.setCuboid(sel);
                printSelMessage(p, session);
            } else {
                if (session.getMode() == Mode.CUBOID_DEFINE) {
                    session.clearCuboid();
                }

                session.defineCuboid();

                p.sendMessage(rc.getPrefs().getInfoColor() + "Right-click 2 blocks at opposite corners of your cuboid.");
            }

        } else if (selCommand == SelCommand.ID) {
            selectById(p, args);
        } else if (selCommand == SelCommand.TARGET) {
            selectTarget(p);
        } else if (selCommand == SelCommand.WORLD) {
            selectWorld(p, args);
        } else if (selCommand == SelCommand.ACTIVATE) {
            UserSession session = rc.getUserSession(p, true);
            massActivate(p, args, session);
        } else {
            UserSession session = rc.getUserSession(p, false);
            if (session == null || session.getSelection().isEmpty()) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "Selection is empty.");

            }

            if (selCommand == SelCommand.LIST) {
                printCircuitList(p, session.getSelection(), "Selection (" + session.getSelection().size() + ")", rc);

            } else if (selCommand == SelCommand.CLEAR) {
                session.getSelection().clear();
                p.sendMessage(rc.getPrefs().getInfoColor() + "Cleared chip selection.");
            } else {
                massCommand(selCommand, p, session);
            }
        }

    }

    @Command(aliases = {"rcsend"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcSend(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }
        String args[] = command.getNonCommandNameArgs();
        if (args.length < 2) {
            //return false;
        }

        BroadcastChannel c = rc.getChannelManager().getChannelByName(args[0], false);
        if (c == null) {
            sender.sendMessage(rc.getPrefs().getErrorColor() + "Wireless channel does not exist: " + args[0]);

        }

        if (c.isProtected() && !c.checkChanPermissions(sender, false)) {
            sender.sendMessage(rc.getPrefs().getErrorColor() + "You do not have permissions to transmit over this channel.");

        }

        for (int i = 1; i < args.length; i++) {
            try {
                String arg = args[i];
                String data;
                int startBit = 0;
                if (!arg.contains(":")) {
                    data = arg;
                } else {
                    data = arg.substring(arg.indexOf(":") + 1);
                    String sb = arg.substring(0, arg.indexOf(":"));
                    if (ParsingUtils.isInt(sb)) {
                        startBit = Integer.parseInt(sb);
                    } else {
                        sender.sendMessage(rc.getPrefs().getErrorColor() + "Bad start bit string: " + sb);

                    }
                }

                BitSet7 bits = null;
                int length = 0;
                try {
                    int ret = Integer.decode(data);
                    if (ret == 0) {
                        length = 1;
                    } else {
                        length = (int)Math.ceil(Math.log(ret) / Math.log(2)) + 1;
                    }
                    bits = BitSetUtils.intToBitSet(ret, length);
                }
                catch (NumberFormatException ne) {
                    if (data.length() == 1) {
                        bits = BitSetUtils.intToBitSet((int)data.charAt(0), 8);
                        length = 8;
                    } else if (data.startsWith("b")) {
                        String sbits = data.substring(1);
                        bits = BitSetUtils.stringToBitSet(sbits);
                        length = sbits.length();
                    } else {
                        sender.sendMessage(rc.getPrefs().getErrorColor() + "Bad data: " + data + ". Expecting either an integer number, an ascii character or a binary number starting with b.");

                    }
                }

                sender.sendMessage(rc.getPrefs().getInfoColor() + "Transmitting "
                        + ChatColor.YELLOW + BitSetUtils.bitSetToString(bits, length) + rc.getPrefs().getInfoColor() + " over channel " + c.name + " bit " + startBit + ".");

                c.transmit(bits, startBit, length);
            }
            catch (IllegalArgumentException ie) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + ie.getMessage());

            }
        }

    }

    @Command(aliases = {"rctool"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcTool(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        Player player = CommandUtils.checkIsPlayer(rc, sender);
        if (player == null) {
        }
        String args[] = command.getNonCommandNameArgs();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }

        if (args.length > 0) {
            processArg(player, args[0]);
        } else {
            setToItemInHand(rc, player);
        }

    }

    @Command(aliases = {"rctype"}, usage = "", desc = "", flags = "", min = 0, max = -1)
    public static void rcType(CommandContext command, MituyaProject plugin, Player sender, PlayerInstance playeri) {
        rc = plugin.getRedstoneChips();
        Player player = CommandUtils.checkIsPlayer(rc, sender);
        if (player == null) {
        }
        String args[] = command.getNonCommandNameArgs();
        if (!CommandUtils.checkPermission(rc, sender, command.getCommand(), false, true)) {
        }

        Block block = CommandUtils.targetBlock(player);
        RCTypeReceiver t = rc.rcTypeReceivers.get(block.getLocation());

        if (t == null) {
            player.sendMessage(rc.getPrefs().getErrorColor() + "You must point towards a typing block (check the docs of your chip) to type.");
        } else {
            player.sendMessage(rc.getPrefs().getInfoColor() + "Input sent.");
            t.type(args, player);
        }

    }

    public static void processArg(Player player, String arg) {
        try {
            Material m = PrefsManager.findMaterial(arg).getItemType();
            setToType(rc, player, m);
        }
        catch (IllegalArgumentException e) {
            if ("clear".startsWith(arg)) {
                clearTools(rc, player);
            } else {
                player.sendMessage(rc.getPrefs().getErrorColor() + e.getMessage());
            }
        }
    }

    public static void setToItemInHand(RedstoneChips rc, Player player) {
        ItemStack item = player.getItemInHand();
        Material type = item.getType();

        setToType(rc, player, type);
    }

    public static void setToType(RedstoneChips rc, Player player, Material type) {
        try {
            UserSession session = rc.getUserSession(player, true);
            Tool t = new ChipProbe();
            t.setItem(type);
            session.addTool(t);
        }
        catch (IllegalArgumentException ie) {
            player.sendMessage(rc.getPrefs().getErrorColor() + ie.getMessage());
            return;
        }

        player.sendMessage(rc.getPrefs().getInfoColor() + "Chip probe set to " + ChatColor.YELLOW + type.name().toLowerCase() + ". "
                + rc.getPrefs().getInfoColor() + "Right-click a chip block to for info.");
    }

    public static void clearTools(RedstoneChips rc, Player player) {
        UserSession session = rc.getUserSession(player, true);
        session.getTools().clear();

        player.sendMessage(rc.getPrefs().getInfoColor() + "Tools cleared.");
    }

    public static void massCommand(SelCommand selCommand, CommandSender sender, UserSession session) {
        long start = System.nanoTime();
        int count = 0;

        for (Circuit c : session.getSelection()) {
            switch (selCommand) {
                case RESET:
                    if (rc.getCircuitManager().resetCircuit(c, sender)) {
                        count++;
                    }
                    break;
                case BREAK:
                    if (rc.getCircuitManager().destroyCircuit(c, sender, false)) {
                        count++;
                    }
                    break;
                case DESTROY:
                    if (rc.getCircuitManager().destroyCircuit(c, sender, true)) {
                        count++;
                    }
                    break;
                case FIXIOBLOCKS:
                    if (c.fixIOBlocks() > 0) {
                        count++;
                    }
                    break;
                case ENABLE:
                    if (c.isDisabled()) {
                        c.enable();
                        count++;
                    }
                    break;
                case DISABLE:
                    if (!c.isDisabled()) {
                        c.disable();
                        count++;
                    }
            }
        }

        long delta = System.nanoTime() - start;
        String timing = String.format("%.3fms", (float)delta / 1000000d);
        sender.sendMessage(color + "Mass edit finished in " + timing + ".");

        if (selCommand.verb != null) {
            sender.sendMessage(color + selCommand.verb + " " + count + " chip(s)");
        }

    }

    public static void selectById(Player p, String[] args) {
        UserSession session = rc.getUserSession(p, true);
        List<Circuit> selection = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            Circuit c = rc.getCircuitManager().getCircuitById(args[i]);
            if (c != null) {
                selection.add(c);
            } else {
                p.sendMessage(ChatColor.DARK_PURPLE + "Can't find chip " + args[i]);
            }
        }

        session.getSelection().addAll(selection);
        p.sendMessage(color + "Added " + selection.size() + " chip(s) to selection. Selection contains " + session.getSelection().size() + " chip(s).");
    }

    public static void selectWorld(Player p, String[] args) {
        UserSession session = rc.getUserSession(p, true);
        session.getSelection().clear();

        Map<Integer, Circuit> clist;
        if (args.length < 2) {
            clist = rc.getCircuitManager().getCircuits(p.getWorld());
            if (clist != null) {
                session.getSelection().addAll(clist.values());
            }
        } else {
            for (int i = 1; i < args.length; i++) {
                World world = rc.getPlugin().getServer().getWorld(args[i]);
                if (world == null) {
                    p.sendMessage(ChatColor.DARK_PURPLE + "Unknown world name: " + args[i]);
                } else {
                    clist = rc.getCircuitManager().getCircuits(world);
                    if (clist != null) {
                        session.getSelection().addAll(clist.values());
                    }
                }
            }
        }

        p.sendMessage(color + "Selected " + session.getSelection().size() + " chip(s).");
    }

    public static void selectTarget(Player p) {
        Circuit c = CommandUtils.findTargetCircuit(rc, p, 20, true);

        if (c != null) {
            UserSession session = rc.getUserSession(p, true);
            if (!session.getSelection().contains(c)) {
                session.getSelection().add(c);
                p.sendMessage(color + "Selected " + ChatColor.YELLOW + c.getChipString() + color + ".");
            } else {
                session.getSelection().remove(c);
                p.sendMessage(color + "Removed " + ChatColor.YELLOW + c.getChipString() + color + " from selection.");
            }

        }
    }

    public static void printSelMessage(Player p, UserSession s) {
        p.sendMessage(color + "Selection contains " + s.getSelection().size() + " active chip(s).");
    }

    public static void massActivate(Player p, String[] args, UserSession session) {
        if (session.getCuboid() == null) {
            p.sendMessage(rc.getPrefs().getErrorColor() + "You must define a cuboid before using this command.");
            return;
        }

        MaterialData inputBlockType = null, outputBlockType = null, interfaceBlockType = null;

        try {
            if (args.length >= 2) {
                inputBlockType = PrefsManager.findMaterial(args[1]);
            }
            if (args.length >= 3) {
                outputBlockType = PrefsManager.findMaterial(args[2]);
            }
            if (args.length >= 4) {
                interfaceBlockType = PrefsManager.findMaterial(args[2]);
            }
        }
        catch (IllegalArgumentException ie) {
            p.sendMessage(ie.getMessage());
            return;
        }

        if (inputBlockType == null) {
            inputBlockType = rc.getPrefs().getInputBlockType();
        }
        if (outputBlockType == null) {
            outputBlockType = rc.getPrefs().getOutputBlockType();
        }
        if (interfaceBlockType == null) {
            interfaceBlockType = rc.getPrefs().getInterfaceBlockType();
        }

        Location[] cuboid = session.getCuboid();
        int lowx = Math.min(cuboid[0].getBlockX(), cuboid[1].getBlockX());
        int highx = Math.max(cuboid[0].getBlockX(), cuboid[1].getBlockX());

        int lowy = Math.min(cuboid[0].getBlockY(), cuboid[1].getBlockY());
        int highy = Math.max(cuboid[0].getBlockY(), cuboid[1].getBlockY());

        int lowz = Math.min(cuboid[0].getBlockZ(), cuboid[1].getBlockZ());
        int highz = Math.max(cuboid[0].getBlockZ(), cuboid[1].getBlockZ());

        int wallSignId = Material.WALL_SIGN.getId();

        int count = 0;

        for (int x = lowx; x <= highx; x++) {
            for (int y = lowy; y <= highy; y++) {
                for (int z = lowz; z <= highz; z++) {
                    Block b = cuboid[0].getWorld().getBlockAt(x, y, z);
                    if (b.getTypeId() == wallSignId) {
                        if (activate(b, inputBlockType, outputBlockType, interfaceBlockType, p, 0, rc)) {
                            count++;
                        }
                    }
                }
            }
        }

        p.sendMessage(color + "Activated " + count + " circuit(s).");
    }

    public static void resetAllCircuits(CommandSender sender) {
        List<Circuit> failed = new ArrayList<>();
        List<Circuit> allCircuits = new ArrayList<>();
        allCircuits.addAll(rc.getCircuitManager().getCircuits().values());

        for (Circuit c : allCircuits) {
            if (!rc.getCircuitManager().resetCircuit(c, sender)) {
                failed.add(c);
            }
        }

        if (sender != null) {
            if (!failed.isEmpty()) {
                String ids = "";
                for (Circuit c : failed) {
                    ids += (c.name == null ? c.id : c.name) + ", ";
                }

                ids = ids.substring(0, ids.length() - 2);
                sender.sendMessage(rc.getPrefs().getErrorColor() + "Some chip could not reactivate: " + ids);
            } else {
                sender.sendMessage(ChatColor.AQUA + "Successfully reset all active chips.");
            }
        }

    }

    public static boolean addUsers(String[] args, BroadcastChannel curChannel) {
        String[] userList;
        for (int i = 2; i < 4; i++) {
            if (i == args.length) {
                return true;
            }
            userList = args[i].toLowerCase().split("[:,]");
            switch (userList[0]) {
                case "users":
                    for (int j = 1; j < userList.length; j++) {
                        if (!curChannel.users.contains(userList[j])) {
                            curChannel.users.add(userList[j]);
                        }
                    }
                    break;
                case "admins":
                    for (int j = 1; j < userList.length; j++) {
                        if (!curChannel.owners.contains(userList[j])) {
                            curChannel.owners.add(userList[j]);
                        }
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public static boolean removeUsers(String[] args, BroadcastChannel curChannel) {
        String[] userList;
        for (int i = 2; i < 4; i++) {
            if (i == args.length) {
                return true;
            }
            userList = args[i].toLowerCase().split("[:,]");
            switch (userList[0]) {
                case "users":
                    for (int j = 1; j < userList.length; j++) {
                        curChannel.users.remove(userList[j]);
                    }
                    break;
                case "admins":
                    for (int j = 1; j < userList.length; j++) {
                        curChannel.owners.remove(userList[j]);
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public static void printCircuitList(CommandSender sender, Iterable<Circuit> circuits, String title, RedstoneChips rc) {
        String lines = "";
        int chipCount = 0;

        for (Circuit c : circuits) {
            lines += (makeCircuitDescriptionLine(c, rc.getPrefs().getInfoColor())) + "\n";
            chipCount++;
        }

        if (title == null) {
            title = chipCount + " active chip(s)";
        }

        sender.sendMessage("");

        Pager.beginPaging(sender, title, lines,
                rc.getPrefs().getInfoColor(), rc.getPrefs().getErrorColor());
    }

    public static String makeCircuitDescriptionLine(Circuit c, ChatColor argsColor) {
        StringBuilder builder = new StringBuilder();
        for (String arg : c.args) {
            builder.append(arg);
            builder.append(" ");
        }

        String cargs = "";
        if (builder.length() > 0) {
            cargs = builder.toString().substring(0, builder.length() - 1);
        }

        if (cargs.length() > 20) {
            cargs = cargs.substring(0, 17) + "...";
        }
        cargs = "[ " + cargs + " ]";

        String sworld = c.world.getName() + " ";
        ChatColor nameColor = (c.isDisabled() ? ChatColor.GRAY : ChatColor.YELLOW);

        return c.id + ": " + nameColor + c.getClass().getSimpleName() + (c.name != null ? ChatColor.AQUA + " (" + c.name + ")" : "") + ChatColor.WHITE + " @ "
                + c.activationBlock.getX() + "," + c.activationBlock.getY() + "," + c.activationBlock.getZ()
                + " " + sworld + argsColor + cargs;
    }

    public static Collection<Circuit> filterCircuits(Collection<Circuit> values, CircuitFilter[] filters) {
        Collection<Circuit> circuits = new ArrayList<>();
        circuits.addAll(values);

        for (CircuitFilter filter : filters) {
            circuits = filter.filter(circuits);
        }

        return circuits;
    }

    public static String[] tokenizeFilter(String string) {
        return new Tokenizer(string, ',').getTokens();
    }

    public static CircuitFilter parseFilter(CommandSender sender, String sf) throws IllegalArgumentException {
        int colonIdx = sf.indexOf(":");
        if (colonIdx == -1) {
            throw new IllegalArgumentException("Bad filter syntax: " + sf);
        }

        String type = sf.substring(0, colonIdx).trim().toLowerCase();

        if (type.length() <= 1) {
            throw new IllegalArgumentException("Bad filter syntax: " + sf);
        }

        CircuitFilter f;

        if ("location".startsWith(type)) {
            f = new LocationFilter().setPlugin(rc);
        } else if ("chunk".startsWith(type)) {
            f = new ChunkFilter().setPlugin(rc);
        } else if ("class".startsWith(type)) {
            f = new ClassFilter().setPlugin(rc);
        } else {
            throw new IllegalArgumentException("Unknown filter type: " + type);
        }

        f.parse(sender, tokenizeFilter(sf.substring(colonIdx + 1)));

        return f;
    }

    public static void printCommandList(CommandSender sender, Map commands, String[] args, ChatColor infoColor, ChatColor errorColor) {
        List<String> lines = new ArrayList<>();

        for (Object command : commands.keySet()) {
            lines.add(ChatColor.YELLOW + command.toString() + ChatColor.WHITE + " - " + ((Map)commands.get(command)).get("description"));
        }

        Collections.sort(lines);
        Pager.beginPaging(sender, "RedstoneChips commands", new ArrayLineSource(lines.toArray(new String[0])), infoColor, errorColor, Pager.MaxLines - 1);
        sender.sendMessage("Use " + ChatColor.YELLOW + (sender instanceof Player ? "/" : "") + "rchelp <command name>" + ChatColor.WHITE + " for help on a specific command.");
    }

    public static void listDebuggedCircuits(Player player) {
        UserSession s = rc.getUserSession(player, false);
        Debugger d = s.getDebugger();
        List<Circuit> circuits = d.getCircuits();

        if (circuits.isEmpty()) {
            player.sendMessage(rc.getPrefs().getInfoColor() + "Your debug list is empty.");
        } else {
            String title;
            if (d.isPaused()) {
                title = circuits.size() + " debugged IC(s) " + ChatColor.AQUA + "(Debugging Paused)" + rc.getPrefs().getInfoColor();
            } else {
                title = circuits.size() + " debugged IC(s)";
            }

            printCircuitList(player, circuits, title, rc);
        }
    }

    public static void pauseDebugger(CommandSender s, Debugger d) {
        if (d.isPaused()) {
            d.setPaused(false);
            s.sendMessage(rc.getPrefs().getInfoColor() + "Unpaused debugging.");
        } else {
            d.setPaused(true);
            s.sendMessage(rc.getPrefs().getInfoColor() + "Paused debugging. Type '/rcdebug .' again to resume.");
        }
    }

    public static void toggleCircuitIODebug(CommandSender sender, Debugger d, Circuit c) {
        if (d.isDebugFlagSet(c, Flag.IO)) {
            d.removeFlag(c, Flag.IO);
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Stopped IO debugging " + c.getChipString() + ".");
        } else {
            if (!d.isDebugging(c)) {
                d.addCircuit(c);
            }
            d.addFlag(c, Flag.IO);
            sender.sendMessage(rc.getPrefs().getInfoColor() + "IO debugging " + c.getChipString() + ".");
        }
    }

    public static void toggleCircuitDebug(CommandSender sender, Debugger d, Circuit c) {
        if (d.isDebugging(c)) {
            d.removeCircuit(c);
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Stopped debugging " + c.getChipString() + ".");
        } else {
            d.addCircuit(c);
            sender.sendMessage(rc.getPrefs().getInfoColor() + "Debugging " + c.getChipString() + ".");
        }
    }

    public static void printClassesList(CommandSender sender, List<CircuitIndex> libs) {
        String list = "";
        List<String> libNames = new ArrayList<>();
        for (CircuitIndex lib : libs) {
            libNames.add(lib.getIndexName());
        }
        Collections.sort(libNames);

        for (String libName : libNames) {
            CircuitIndex lib = findLibrary(libs, libName);
            if (lib == null) {
                continue;
            }

            list += "\n";
            ChatColor color = rc.getPrefs().getInfoColor();

            List<String> names = new ArrayList<>();
            for (Class c : lib.getCircuitClasses()) {
                names.add(c.getSimpleName());
            }
            Collections.sort(names);

            list += ChatColor.WHITE + lib.getIndexName() + " " + lib.getVersion() + ":\n   ";
            for (String name : names) {
                list += color + name + ", ";
                if (color == rc.getPrefs().getInfoColor()) {
                    color = ChatColor.YELLOW;
                } else {
                    color = rc.getPrefs().getInfoColor();
                }
            }
            list = list.substring(0, list.length() - 2) + "\n";

        }

        if (!list.isEmpty()) {
            Pager.beginPaging(sender, "Installed circuit classes", list, rc.getPrefs().getInfoColor(), rc.getPrefs().getErrorColor());
        }
    }

    public static CircuitIndex findLibrary(List<CircuitIndex> libs, String libName) {
        for (CircuitIndex lib : libs) {
            if (lib.getIndexName().equals(libName)) {
                return lib;
            }
        }

        return null;
    }

    public static void printChannelInfo(CommandSender sender, String channelName) {
        ChatColor infoColor = rc.getPrefs().getInfoColor();
        ChatColor errorColor = rc.getPrefs().getErrorColor();
        ChatColor extraColor = ChatColor.YELLOW;

        BroadcastChannel channel = rc.getChannelManager().getChannelByName(channelName, false);
        if (channel == null) {
            sender.sendMessage(errorColor + "Channel " + channelName + " doesn't exist.");
        } else {
            String sTransmitters = "";
            for (Transmitter t : channel.getTransmitters()) {
                String range = "[";
                if (t.getChannelLength() > 1) {
                    range += "bits " + t.getStartBit() + "-" + (t.getChannelLength() + t.getStartBit() - 1) + "]";
                } else {
                    range += "bit " + t.getStartBit() + "]";
                }

                sTransmitters += t.getCircuit().getChipString() + " " + range + ", ";
            }

            String sReceivers = "";
            for (Receiver r : channel.getReceivers()) {
                String range = "[";
                if (r.getChannelLength() > 1) {
                    range += "bits " + r.getStartBit() + "-" + (r.getChannelLength() + r.getStartBit() - 1) + "]";
                } else {
                    range += "bit " + r.getStartBit() + "]";
                }
                sReceivers += r.getCircuit().getChipString() + " " + range + ", ";
            }

            String owners = "";
            String users = "";
            if (channel.isProtected()) {
                for (String owner : channel.owners) {
                    owners += owner + ", ";
                }

                for (String user : channel.users) {
                    users += user + ", ";
                }
            }

            sender.sendMessage("");
            sender.sendMessage(extraColor + channel.name + ":");
            sender.sendMessage(extraColor + "----------------------");

            sender.sendMessage(infoColor + "last broadcast: " + extraColor + BitSetUtils.bitSetToBinaryString(channel.bits, 0, channel.getLength()) + infoColor + " length: " + extraColor + channel.getLength());

            if (!sTransmitters.isEmpty()) {
                sender.sendMessage(infoColor + "transmitters: " + extraColor + sTransmitters.substring(0, sTransmitters.length() - 2));
            }
            if (!sReceivers.isEmpty()) {
                sender.sendMessage(infoColor + "receivers: " + extraColor + sReceivers.substring(0, sReceivers.length() - 2));
            }
            if (!owners.isEmpty()) {
                sender.sendMessage(infoColor + "admins: " + extraColor + owners.substring(0, owners.length() - 2));
            }
            if (!users.isEmpty()) {
                sender.sendMessage(infoColor + "users: " + extraColor + users.substring(0, users.length() - 2));
            }
        }
    }

    public static boolean checkChanPermissions(CommandSender sender, String name) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (!(rc.getChannelManager().getBroadcastChannels().containsKey(name))) {
            return true;
        }

        if (!(rc.getChannelManager().getBroadcastChannels().get(name).isProtected())) {
            return true;
        }

        String playerName = ((Player)sender).getName();
        return ((Player)sender).hasPermission("redstonechips.channel.admin")
                || rc.getChannelManager().getChannelByName(name, false).users.contains(playerName.toLowerCase())
                || rc.getChannelManager().getChannelByName(name, false).owners.contains(playerName.toLowerCase());
    }

    public static void editSignArgs(Circuit c, String[] args) {
        final Sign sign = (Sign)c.activationBlock.getBlock().getState();
        String line = "";
        int curLine = 1;

        for (String a : args) {
            String added = line + " " + a;
            if (added.length() > 13 && curLine != 3) {
                sign.setLine(curLine, line);
                line = a;
                curLine++;
            } else {
                line = added;
            }
        }

        sign.setLine(curLine, line);

        if (curLine < 3) {
            for (int i = curLine + 1; i < 4; i++) {
                sign.setLine(i, "");
            }
        }

        sign.update();
    }

    public static String[] editArgs(CommandSender sender, String[] args, String index, String value) {
        int idx;

        if (index.equalsIgnoreCase("clear")) {
            int clearIdx;

            try {
                clearIdx = Integer.decode(value) - 1;
            }
            catch (NumberFormatException ne) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "Bad argument number: " + value);
                return null;
            }

            if (clearIdx >= args.length || clearIdx < 0) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "Argument number out of bounds: " + (clearIdx + 1));
                return null;
            }

            String[] tempArgs = new String[args.length - 1];
            int tempIdx = 0;
            for (int i = 0; i < args.length; i++) {
                if (i != clearIdx) {
                    tempArgs[tempIdx] = args[i];
                    tempIdx++;
                }
            }

            sender.sendMessage(rc.getPrefs().getInfoColor() + "Removing argument #" + (clearIdx + 1) + ": " + args[clearIdx]);

            args = tempArgs;
        } else {
            if (index.equalsIgnoreCase("add")) {
                idx = args.length;
            } else {
                try {
                    idx = Integer.decode(index) - 1;
                }
                catch (NumberFormatException ne) {
                    sender.sendMessage(rc.getPrefs().getErrorColor() + "Bad argument number: " + index);
                    return null;
                }
            }

            if (idx > args.length || idx < 0) {
                sender.sendMessage(rc.getPrefs().getErrorColor() + "Argument number out of bounds: " + (idx + 1));
                return null;
            } else {
                if (idx == args.length) {
                    // add to last
                    sender.sendMessage(rc.getPrefs().getInfoColor() + "Adding argument #" + (idx + 1) + ": " + value);
                    String[] tempArgs = new String[args.length + 1];
                    System.arraycopy(args, 0, tempArgs, 0, args.length);
                    tempArgs[tempArgs.length - 1] = value;
                    args = tempArgs;
                } else {
                    sender.sendMessage(rc.getPrefs().getInfoColor() + "Setting argument #" + (idx + 1) + " to " + value + " (was " + args[idx] + ").");
                    args[idx] = value;
                }
            }
        }

        return args;
    }

    public static boolean activate(Block target, MaterialData inputBlockType, MaterialData outputBlockType,
            MaterialData interfaceBlockType, CommandSender sender, int verboseLevel, RedstoneChips rc) {

        Map<Type, MaterialData> iom = new EnumMap<>(IOBlock.Type.class);
        iom.put(Type.INPUT, inputBlockType);
        iom.put(Type.OUTPUT, outputBlockType);
        iom.put(Type.INTERFACE, interfaceBlockType);
        if (rc.getCircuitManager().checkForCircuit(ScanParameters.generate(target, iom), sender, verboseLevel) == -2) {
            sender.sendMessage(rc.getPrefs().getErrorColor() + "Could not activate chip.");
            return false;
        } else {
            return true;
        }
    }

    public static void printPinInfo(Block pinBlock, CommandSender sender, RedstoneChips rc) {
        boolean success = false;

        List<InputPin> inputList = rc.getCircuitManager().getInputPinBySource(pinBlock.getLocation());

        if (inputList != null) {
            try {
                printInputInfo(sender, inputList, rc);
                success = true;
            }
            catch (CloneNotSupportedException ex) {
                Logger.getLogger(RSCCommands.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        InputPin input = rc.getCircuitManager().getInputPin(pinBlock.getLocation());
        if (input != null) {
            try {
                List<InputPin> i = new ArrayList<>();
                i.add(input);
                printInputInfo(sender, i, rc);
                success = true;
            }
            catch (CloneNotSupportedException ex) {
                Logger.getLogger(RSCCommands.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        List<OutputPin> outputList = rc.getCircuitManager().getOutputPinByOutputBlock(pinBlock.getLocation());

        if (outputList != null) {
            try {
                printOutputInfo(sender, outputList, rc);
                success = true;
            }
            catch (CloneNotSupportedException ex) {
                Logger.getLogger(RSCCommands.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        OutputPin o = rc.getCircuitManager().getOutputPin(pinBlock.getLocation());
        if (o != null) {
            try {
                List<OutputPin> list = new ArrayList<>();
                list.add(o);
                printOutputInfo(sender, list, rc);
                success = true;
            }
            catch (CloneNotSupportedException ex) {
                Logger.getLogger(RSCCommands.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Circuit c = rc.getCircuitManager().getCircuitByStructureBlock(pinBlock.getLocation());
        if (c != null && c.interfaceBlocks != null) {

            InterfaceBlock i = null;
            Location pinLoc = pinBlock.getLocation();

            for (InterfaceBlock bl : c.interfaceBlocks) {
                if (bl.getLocation().equals(pinLoc)) {
                    i = bl;
                    break;
                }
            }

            if (i != null) {
                printInterfaceInfo(sender, i, rc);
                success = true;
            }
        }

        if (!success) {
            throw new IllegalArgumentException("You must point at a chip io block.");
        }
    }

    public static void printInputInfo(CommandSender sender, List<InputPin> inputList, RedstoneChips rc) throws CloneNotSupportedException {
        for (InputPin io : inputList) {
            Circuit c = io.getCircuit();
            int i = io.getIndex();
            sender.sendMessage(rc.getPrefs().getInfoColor() + c.getClass().getSimpleName() + ": " + ChatColor.WHITE + "input pin "
                    + i + " - " + (c.getInputBits().get(i) ? ChatColor.RED + "on" : ChatColor.WHITE + "off"));
        }
    }

    public static void printOutputInfo(CommandSender sender, List<OutputPin> outputList, RedstoneChips rc) throws CloneNotSupportedException {
        for (OutputPin o : outputList) {
            Circuit c = o.getCircuit();
            int i = o.getIndex();
            sender.sendMessage(rc.getPrefs().getInfoColor() + c.getClass().getSimpleName() + ": " + ChatColor.YELLOW + "output pin "
                    + i + " - " + (c.getOutputBits().get(i) ? ChatColor.RED + "on" : ChatColor.WHITE + "off"));
        }
    }

    public static void printInterfaceInfo(CommandSender sender, InterfaceBlock in, RedstoneChips rc) {
        Circuit c = in.getCircuit();
        int i = in.getIndex();
        sender.sendMessage(rc.getPrefs().getInfoColor() + c.getClass().getSimpleName() + ": " + ChatColor.YELLOW + "interface block "
                + i + ".");
    }

    public static void printCircuitInfo(CommandSender sender, Circuit c, RedstoneChips rc) {
        try {
            ChatColor infoColor = rc.getPrefs().getInfoColor();
            ChatColor extraColor = ChatColor.YELLOW;
            
            String disabled;
            if (c.isDisabled()) {
                disabled = ChatColor.GRAY + " - disabled";
            } else {
                disabled = "";
            }
            
            String loc = c.activationBlock.getBlockX() + ", " + c.activationBlock.getBlockY() + ", " + c.activationBlock.getBlockZ();
            String chunkCoords = "";
            for (ChunkLocation l : c.circuitChunks) {
                chunkCoords += (l.isChunkLoaded() ? extraColor : ChatColor.WHITE) + "[" + l.getX() + ", " + l.getZ() + " " + (l.isChunkLoaded() ? "L" : "u") + "]" + infoColor + ", ";
            }
            
            if (!chunkCoords.isEmpty()) {
                chunkCoords = chunkCoords.substring(0, chunkCoords.length() - 2);
            }
            
            String name;
            if (c.name == null) {
                name = "unnamed";
            } else {
                name = c.name;
            }
            
            sender.sendMessage("");
            sender.sendMessage(infoColor + c.getChipString() + disabled);
            sender.sendMessage(extraColor + "----------------------------------------");
            
            sender.sendMessage(infoColor + "" + c.inputs.length + " input(s), " + c.outputs.length + " output(s) and " + c.interfaceBlocks.length + " interface blocks.");
            sender.sendMessage(infoColor
                    + "location: " + extraColor + loc + infoColor + " @ " + extraColor + c.world.getName());
            sender.sendMessage(infoColor + " chunks: " + chunkCoords);
            
            int inputInt = BitSetUtils.bitSetToUnsignedInt(c.getInputBits(), 0, c.inputs.length);
            int outputInt = BitSetUtils.bitSetToUnsignedInt(c.getOutputBits(), 0, c.outputs.length);
            
            if (c.inputs.length > 0) {
                sender.sendMessage(infoColor + "input states: " + extraColor + BitSetUtils.bitSetToBinaryString(c.getInputBits(), 0, c.inputs.length)
                        + infoColor + " (0x" + Integer.toHexString(inputInt) + ")");
            }
            
            if (c.outputs.length > 0) {
                sender.sendMessage(infoColor + "output states: " + extraColor + BitSetUtils.bitSetToBinaryString(c.getOutputBits(), 0, c.outputs.length)
                        + infoColor + " (0x" + Integer.toHexString(outputInt) + ")");
            }
            
            String signargs = "";
            for (String arg : c.args) {
                signargs += arg + " ";
            }
            
            sender.sendMessage(infoColor + "sign args: " + extraColor + signargs);
            
            Map<String, String> internalState = c.getInternalState();
            if (!internalState.isEmpty()) {
                sender.sendMessage(infoColor + "internal state:");
                String internal = "   ";
                for (String key : internalState.keySet()) {
                    internal += infoColor + key + ": " + extraColor + internalState.get(key) + infoColor + ", ";
                }
                
                sender.sendMessage(internal.substring(0, internal.length() - 2));
            }
        }
        catch (CloneNotSupportedException ex) {
            Logger.getLogger(RSCCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
