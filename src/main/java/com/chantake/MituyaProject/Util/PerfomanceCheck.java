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
package com.chantake.MituyaProject.Util;

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PerfomanceCheck {

    private static final PerfomanceCheck singleton = new PerfomanceCheck();
    private AnalyzerThread analyzer = null;

    private PerfomanceCheck() {
        analyzer = new AnalyzerThread();
        analyzer.setDaemon(true);
        try {
            analyzer.start();
        } catch (final UnsupportedOperationException e) {
            System.err.println("CpuUsageAnalyzer does not support this platform.");
        }
    }

    public static float getCpuUsage() {
        return PerfomanceCheck.singleton.analyzer.getCpuUsage();
    }

    public static int getCpuUsageint() {
        return PerfomanceCheck.singleton.analyzer.getCpuUsageint();
    }

    public static String getMemoryInfo() {
        final DecimalFormat f1 = new DecimalFormat("#,###KB");
        final DecimalFormat f2 = new DecimalFormat("##.#");
        final long free = Runtime.getRuntime().freeMemory() / 1024;
        final long total = Runtime.getRuntime().totalMemory() / 1024;
        final long max = Runtime.getRuntime().maxMemory() / 1024;
        final long used = total - free;
        final double ratio = used * 100 / (double) total;
        final String info = "memory info : total=" + f1.format(total) + "," + "use now=" + f1.format(used) + " (" + f2.format(ratio) + "%)," + "can use max memory=" + f1.format(max);
        return info;
    }

    private static class AnalyzerThread extends Thread {

        private final AtomicInteger cpuUsage = new AtomicInteger(
                Float.floatToIntBits(-1F));
        private final CountDownLatch startupLatch = new CountDownLatch(1);
        private long prevCpuTime = 0L;
        private long prevUpTime = 0L;

        @SuppressWarnings("rawtypes")
        private void checkCompatibility() {
            final OperatingSystemMXBean osmx = ManagementFactory.getOperatingSystemMXBean();
            if (osmx == null) {
                throw new UnsupportedOperationException("Failed to get OperatingSystemMXBean");
            }
            final Class[] interfaces = osmx.getClass().getInterfaces();
            boolean hasInterface = false;
            if (interfaces != null) {
                for (final Class<?> i : interfaces) {
                    if ("com.sun.management.OperatingSystemMXBean".equals(i.getName()) || "com.sun.management.UnixOperatingSystemMXBean".equals(i.getName())) {
                        hasInterface = true;
                        break;
                    }
                }
            }
            if (!hasInterface) {
                throw new UnsupportedOperationException("Incompatible OperatingSystemMXBean class: " + osmx.getClass().getName());
            }
            final RuntimeMXBean rtmx = ManagementFactory.getRuntimeMXBean();
            if (rtmx == null) {
                throw new UnsupportedOperationException("Failed to get RuntimeMXBean");
            }
        }

        private long getCpuTime() {
            final OperatingSystemMXBean osmx = ManagementFactory.getOperatingSystemMXBean();
            try {
                final Method getProcessCpuTime = osmx.getClass().getDeclaredMethod("getProcessCpuTime");
                getProcessCpuTime.setAccessible(true);
                final Object o = getProcessCpuTime.invoke(osmx);
                if (o instanceof Long) {
                    return (Long)o;
                }
            }
            catch (final NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            }
            return 0L;
        }

        private float getCpuUsage() {
            try {
                startupLatch.await(1500L, TimeUnit.MILLISECONDS);
            }
            catch (final InterruptedException e) {
            }
            return Float.intBitsToFloat(cpuUsage.get());
        }

        private int getCpuUsageint() {
            try {
                startupLatch.await(1500L, TimeUnit.MILLISECONDS);
            }
            catch (final InterruptedException e) {
            }
            return cpuUsage.get();
        }

        private long getUpTime() {
            final RuntimeMXBean rtmx = ManagementFactory.getRuntimeMXBean();
            return rtmx.getUptime();
        }

        @Override
        public void run() {
            setName("CpuUsageAnalyzer");
            try {
                checkCompatibility();
            }
            catch (final UnsupportedOperationException e) {
                startupLatch.countDown();
                throw e;
            }
            while (true) {
                updateCpuUsage();
                sleep1Sec();
            }
        }

        private void sleep1Sec() {
            final long after1Sec = System.currentTimeMillis() + 500L;
            while (System.currentTimeMillis() < after1Sec) {
                try {
                    Thread.sleep(50L);
                }
                catch (final InterruptedException e) {
                }
            }
        }

        private void updateCpuUsage() {
            final long cpuTime = getCpuTime();
            final long upTime = getUpTime();
            final long elapsedCpu = cpuTime - prevCpuTime;
            final long elapsedTime = upTime - prevUpTime;
            final int numProcessors = Runtime.getRuntime().availableProcessors();
            final float percentile = Math.min(100F, elapsedCpu / (elapsedTime * 10000F * numProcessors));
            prevCpuTime = cpuTime;
            prevUpTime = upTime;
            cpuUsage.set(Float.floatToIntBits(percentile));
            if (startupLatch.getCount() > 0) {
                startupLatch.countDown();
            }
        }
    }
}
