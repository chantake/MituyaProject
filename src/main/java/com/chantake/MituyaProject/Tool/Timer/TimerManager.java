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
package com.chantake.MituyaProject.Tool.Timer;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class TimerManager implements TimerManagerMBean {

    private static class LoggingSaveRunnable implements Runnable {

        Runnable r;

        public LoggingSaveRunnable(Runnable r) {
            this.r = r;
        }

        @Override
        public void run() {
            try {
                r.run();
            }
            catch (final Throwable t) {
            }
        }
    }

    private static final TimerManager instance = new TimerManager();

    public static TimerManager getInstance() {
        return TimerManager.instance;
    }

    private ScheduledThreadPoolExecutor ses;

    private TimerManager() {
        final MBeanServer mBeanServer = ManagementFactory
                .getPlatformMBeanServer();
        try {
            mBeanServer.registerMBean(this, new ObjectName(
                    "328:type=TimerManger"));
        }
        catch (final MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
        }
    }

    @Override
    public long getActiveCount() {
        return ses.getActiveCount();
    }

    @Override
    public long getCompletedTaskCount() {
        return ses.getCompletedTaskCount();
    }

    @Override
    public int getQueuedTasks() {
        return ses.getQueue().toArray().length;
    }

    @Override
    public long getTaskCount() {
        return ses.getTaskCount();
    }

    @Override
    public boolean isShutdown() {
        return ses.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return ses.isTerminated();
    }

    public ScheduledFuture<?> register(Runnable r, long repeatTime) {
        return ses.scheduleAtFixedRate(new LoggingSaveRunnable(r), 0,
                repeatTime, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> register(Runnable r, long repeatTime, long delay) {
        return ses.scheduleAtFixedRate(new LoggingSaveRunnable(r), delay,
                repeatTime, TimeUnit.MILLISECONDS);
    }

    public void remove(Runnable r) {
        ses.remove(r);
    }

    public ScheduledFuture<?> schedule(Runnable r, long delay) {
        return ses.schedule(new LoggingSaveRunnable(r), delay,
                TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> scheduleAtTimestamp(Runnable r, long timestamp) {
        return schedule(r, timestamp - System.currentTimeMillis());
    }

    public void start() {
        if (ses != null && !ses.isShutdown() && !ses.isTerminated()) {
            return;
        }
        final ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(
                4, new ThreadFactory() {
                    private final AtomicInteger threadNumber = new AtomicInteger(
                            1);

                    @Override
                    public Thread newThread(Runnable r) {
                        final Thread t = new Thread(r);
                        t.setName("TimerManager-Worker-"
                                + threadNumber.getAndIncrement());
                        return t;
                    }
                });
        // this is a no-no, it actually does nothing..
        stpe.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        ses = stpe;
    }

    public void stop() {
        ses.shutdown();
    }
}
