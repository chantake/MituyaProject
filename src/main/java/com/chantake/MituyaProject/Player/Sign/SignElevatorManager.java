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
package com.chantake.MituyaProject.Player.Sign;

import com.chantake.MituyaProject.MituyaManager;
import com.chantake.MituyaProject.MituyaProject;
import com.chantake.MituyaProject.Player.PlayerInstance;
import com.chantake.MituyaProject.Player.Sign.listener.SignElevatorListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class SignElevatorManager extends MituyaManager {

    private final SignElevatorListener listener = new SignElevatorListener(this);

    private static enum Type {

        Recv, Up, Down, None
    }

    public SignElevatorManager(MituyaProject plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        this.getPlugin().Log("看板エレベーターを初期化します");
        this.getPlugin().registerEvents(listener);
        this.getPlugin().Log("看板エレベーターを初期化しました");
    }

    private Type isLift(Block block) {
        BlockState state = block.getState();
        if (state instanceof Sign) {
            return this.isLift((Sign)state);
        }
        return Type.None;
    }

    private Type isLift(Sign sign) {
        String line = sign.getLines()[1];
        if (line.equalsIgnoreCase("[Lift Up]") || line.equalsIgnoreCase("[Elevator Up]") || line.equalsIgnoreCase("[リフト アップ]") || line.equalsIgnoreCase("[エレベーター アップ]")) {
            return Type.Up;
        }
        if (line.equalsIgnoreCase("[Lift Down]") || line.equalsIgnoreCase("[Elevator Down]") || line.equalsIgnoreCase("[リフト ダウン]") || line.equalsIgnoreCase("[エレベーター ダウン]")) {
            return Type.Down;
        }
        if (line.equalsIgnoreCase("[Lift]") || line.equalsIgnoreCase("[Elevator]") || line.equalsIgnoreCase("[リフト]") || line.equalsIgnoreCase("[エレベーター]")) {
            return Type.Recv;
        }
        return Type.None;
    }

    public boolean doElevator(Player player, PlayerInstance ins, Sign sign) {
        Type type = isLift(sign);
        if (type == Type.None) {
            return false;
        }
        World world = player.getWorld();
        Block bl = null;

        switch (type) {
            case Down:
                for (int i = sign.getY() - 1; i > 2; i--) {
                    Block bk = world.getBlockAt(sign.getX(), i, sign.getZ());
                    if (isLift(world.getBlockAt(sign.getX(), i, sign.getZ())) != Type.None) {
                        bl = bk;
                        break;
                    }
                }
                break;
            case Up:
                for (int i = sign.getY() + 1; i <= world.getMaxHeight(); i++) {
                    Block bk = world.getBlockAt(sign.getX(), i, sign.getZ());
                    if (isLift(bk) != Type.None) {
                        bl = bk;
                        break;
                    }
                }
                break;
            case Recv:
                for (int i = 2; i <= world.getMaxHeight(); i++) {
                    Block bk = world.getBlockAt(sign.getX(), i, sign.getZ());
                    if (isLift(bk) == Type.Recv && i != sign.getY()) {
                        bl = bk;
                        break;
                    }
                }
                break;
        }
        if (bl == null) {
            ins.sendAttention("移動先が見つかりません");
            return false;
        }

        //テレポート
        Location subspaceRift = player.getLocation().clone();
        subspaceRift.setY(bl.getY());
        player.teleport(subspaceRift);

        Sign to = (Sign)bl.getState();
        if (to.getLine(0).length() > 0) {
            ins.sendInfo(ChatColor.YELLOW + "Elevator", to.getLine(0));
        } else {
            ins.sendInfo(ChatColor.YELLOW + "Elevator", (sign.getY() > bl.getY() ? "下" : "上") + "のフロアに移動しました。");
        }
        return true;
    }
}
