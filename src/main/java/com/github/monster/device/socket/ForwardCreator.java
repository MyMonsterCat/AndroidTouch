package com.github.monster.device.socket;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.TimeoutException;

import java.io.IOException;

@FunctionalInterface
public interface ForwardCreator {

    void createForward(int port) throws TimeoutException,
            AdbCommandRejectedException,
            IOException;
}
