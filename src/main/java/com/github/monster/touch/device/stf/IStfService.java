package com.github.monster.touch.device.stf;

import java.io.Closeable;

public interface IStfService extends Closeable {

    /**
     * init env
     *
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * start service
     *
     * @return socket name
     * @throws InterruptedException
     */
    String start() throws InterruptedException;
}
