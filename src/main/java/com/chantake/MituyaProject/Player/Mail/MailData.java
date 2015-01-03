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
package com.chantake.MituyaProject.Player.Mail;

import java.sql.Timestamp;

/**
 *
 * @author chantake
 */
public class MailData {

    private int id;
    private final String From;
    private final String To;
    private final String subject;
    private final String text;
    private boolean unread;
    private final Timestamp timestamp;

    public MailData(int id, String From, String To, String subject, String text, boolean unread, Timestamp timestamp) {
        this.id = id;
        this.From = From;
        this.To = To;
        this.subject = subject;
        this.text = text;
        this.unread = unread;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getFrom() {
        return From;
    }

    public String getSubject() {
        return subject;
    }

    public String getTo() {
        return To;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public boolean getUnread() {
        return this.unread;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUnread(boolean b) {
        this.unread = b;
    }
}
