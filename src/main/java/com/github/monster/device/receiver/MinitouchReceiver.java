package com.github.monster.device.receiver;

import com.github.monster.constant.Constants;

public class MinitouchReceiver extends BaseServiceReceiver {

    public MinitouchReceiver() {
        super(Constants.BIN_MINITOUCH);
    }

    @Override
    protected boolean isStarted(String line) {
        return line.contains("detected on");
    }
}
