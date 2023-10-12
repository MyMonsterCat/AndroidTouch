package com.github.monster.touch.device.receiver.size;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * dumpsys window 命令用于获取与窗口管理系统相关的信息，例如当前窗口的层级、窗口尺寸和位置、窗口焦点等。
 */
@Slf4j
public class DumpsysWindowReceiver extends BaseDisplayInfoReceiver {
    private static final Pattern INIT = Pattern.compile(".*init=(\\d+)x(\\d+)\\s+.*");

    @Override
    public void processNewLines(String[] lines) {
        for (String line : lines) {
            Matcher m = INIT.matcher(line);
            if (m.matches()) {
                try {
                    getSize().setWidth(Integer.parseInt(m.group(1)));
                } catch (NumberFormatException e) {
                    log.warn("Display Width: Failed to parse " + m.group(1) + " as an integer");
                }

                try {
                    getSize().setHeight(Integer.parseInt(m.group(2)));
                } catch (NumberFormatException e) {
                    log.warn("Display Height: Failed to parse " + m.group(2) + " as an integer");
                }
                if (isCancelled()) {
                    break;
                }
            }
        }
    }
}
