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
package com.chantake.MituyaProject.Tool.Page;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author fumitti
 */
public class PageInfo {

    public int page = 1;
    public int pageCount;
    public LineSource src;
    public String title;
    public ChatColor infoColor, errorColor;
    public int linesPerPage;
    public CommandSender sender;

    public PageInfo(CommandSender sender, String title, int pageCount, LineSource src, int linesPerPage, ChatColor infoColor, ChatColor errorColor) {
        this.pageCount = pageCount;
        this.src = src;
        this.linesPerPage = linesPerPage;
        this.title = title;
        this.infoColor = infoColor;
        this.errorColor = errorColor;
        this.sender = sender;
    }

    public void nextPage() {
        if (page < pageCount) {
            page = page + 1;
        } else {
            page = 1;
        }

    }

    public void prevPage() {
        if (page > 1) {
            page = page - 1;
        } else {
            page = pageCount;
        }
    }

    public void lastPage() {
        page = pageCount;
    }

    void gotoPage(int page) {
        this.page = page;
    }
}
