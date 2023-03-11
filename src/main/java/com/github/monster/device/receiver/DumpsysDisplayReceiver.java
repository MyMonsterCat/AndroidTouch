package com.github.monster.device.receiver;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DumpsysDisplayReceiver extends BaseDisplayInfoReceiver {

    private static final Pattern DISPLAY_DEVICE = Pattern.compile("^mPhys=PhysicalDisplayInfo\\{(\\d+) x (\\d+).*\\}");

    @Override
    public void processNewLines(String[] lines) {
        for (String line : lines) {
            Matcher m = DISPLAY_DEVICE.matcher(line);
            if (m.matches()) {
                try {
                    getSize().setWidth(Integer.parseInt(m.group(1)));
                } catch (NumberFormatException e) {
                    log.warn("DisplayDevice width: Failed to parse " + m.group(1) + " as an integer");
                }

                try {
                    getSize().setHeight(Integer.parseInt(m.group(2)));
                } catch (NumberFormatException e) {
                    log.warn("DisplayDevice height: Failed to parse " + m.group(2) + " as an integer");
                }
                if (isCancelled()) {
                    break;
                }
            }
        }
    }

}
