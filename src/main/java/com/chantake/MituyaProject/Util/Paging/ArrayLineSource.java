package com.chantake.MituyaProject.Util.Paging;

/**
 * @author Tal Eisenberg
 */
public class ArrayLineSource implements LineSource {
    String[] lines;

    public ArrayLineSource(String[] lines) {
        this.lines = lines;
    }

    @Override
    public String getLine(int idx) {
        return lines[idx];
    }

    @Override
    public int getLineCount() {
        return lines.length;
    }
}
