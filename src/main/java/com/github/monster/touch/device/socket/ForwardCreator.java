package com.github.monster.touch.device.socket;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.TimeoutException;

import java.io.IOException;

@FunctionalInterface
public interface ForwardCreator {

    /**
     * 在指定端口创建请求转发
     *
     * @param port
     * @throws TimeoutException            超时
     * @throws AdbCommandRejectedException 连接异常
     * @throws IOException                 io异常
     */
    void createForward(int port) throws TimeoutException,
            AdbCommandRejectedException,
            IOException;
}
