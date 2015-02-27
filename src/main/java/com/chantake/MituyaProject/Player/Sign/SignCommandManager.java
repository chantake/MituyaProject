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
import com.chantake.MituyaProject.Player.Sign.listener.SignCommandListener;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author chantake
 */
public class SignCommandManager extends MituyaManager {

    private static final long CommandDelayTime = 2 * 2L;//0.2秒
    private final SignCommandListener listener = new SignCommandListener(this);

    public SignCommandManager(MituyaProject plugin) {
        super(plugin);
    }

    @Override
    public final void init() {
        this.getPlugin().Log("看板コマンドを初期化します");
        this.getPlugin().registerEvents(listener);
        this.getPlugin().Log("看板コマンドを初期化しました");
    }

    public boolean doEvent(Sign sign, Player pr, PlayerInstance ins) {
        String line = sign.getLine(0);
        if (line.equalsIgnoreCase("[Command]") || line.equalsIgnoreCase("[コマンド]")) {
            //別のタスクが実行されている場合
            List<String> tasks = ins.getCommandTasks();
            if (!tasks.isEmpty() || ins.isCheck()) {
                ins.sendAttention("別のタスクが実行されています。タスクが終了してから実行してください。");
                return true;
            }
            for (int i = 1; i < sign.getLines().length; i++) {
                String ln = sign.getLine(i);
                if (ln.length() > 1 && ln.charAt(0) == '/') {
                    tasks.add(ln);
                }
            }
            if (!tasks.isEmpty()) {
                ins.setCommandTasks(tasks);
                this.executionCommandTask(pr, ins);
            }
            return true;
        }
        return false;
    }

    public void executionCommandTask(final Player pr, final PlayerInstance ins) {
        Iterator<String> commandTask = ins.getCommandTask();
        if (commandTask.hasNext() && !ins.isCheck()) {
            this.executionCommandTask(pr, ins, commandTask.next(), CommandDelayTime);
            commandTask.remove();
        }
    }

    public void executionCommandTaskFromCheckInstance(final Player pr, final PlayerInstance ins) {
        Iterator<String> commandTask = ins.getCommandTask();
        if (commandTask.hasNext()) {
            this.executionCommandTask(pr, ins, commandTask.next(), CommandDelayTime);
            commandTask.remove();
        }
    }

    public void executionCommandTask(final Player pr, final PlayerInstance ins, final String command, Long delay) {
        final MituyaProject pl = this.getPlugin();
        this.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> pl.handleCommand(pr, ins, command), delay);
    }
}
