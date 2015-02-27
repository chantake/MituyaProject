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
package com.chantake.MituyaProject.Util.Script;

import com.chantake.MituyaProject.MituyaProject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author chantake
 */
public abstract class AbstractScriptManager {

    private final ScriptEngineManager sem;
    protected ScriptEngine engine;

    protected AbstractScriptManager() {
        sem = new ScriptEngineManager();
    }

    protected Invocable getInvocable(String path, MituyaProject plugin) {
        try {
            path = plugin.scriptPath + path;
            engine = null;
            if (plugin != null) {
                engine = plugin.getScriptEngine(path);
            }
            if (engine == null) {
                File scriptFile = new File(path);
                if (!scriptFile.exists()) {
                    return null;
                }
                engine = sem.getEngineByName("javascript");
                if (plugin != null) {
                    plugin.setScriptEngine(path, engine);
                }
                //FileReader fr = new FileReader(scriptFile);
                FileInputStream in = new FileInputStream(scriptFile);
                InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                engine.eval(isr);
                in.close();
                isr.close();
            }
            return (Invocable)engine;
        }
        catch (ScriptException | IOException e) {
            plugin.ErrLog("スクリプト getInvocable:" + e);
            return null;
        }
    }

    protected void resetContext(String path, MituyaProject plugin) {
        path = plugin.scriptPath + path;
        plugin.removeScriptEngine(path);
    }
}
