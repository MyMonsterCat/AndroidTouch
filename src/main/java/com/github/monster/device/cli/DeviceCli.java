package com.github.monster.device.cli;

import com.github.monster.device.DeviceWrapper;
import com.github.monster.device.IDeviceHandler;
import com.github.monster.device.size.DefaultSize;
import com.github.monster.device.stf.MiniTouchService;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;

@Slf4j
public class DeviceCli implements Closeable {

    private final DeviceWrapper device;

    private final MiniTouchService miniTouch;


    private IDeviceHandler IDeviceHandler;


    public DeviceCli(DeviceWrapper device) {
        this.device = device;
        this.miniTouch = new MiniTouchService(this.device);

    }

    public void start() {
        try {
            miniTouch.init();
            String socket = miniTouch.start();
            IDeviceHandler = new MiniTouchCli(device, new DefaultSize(), socket);
        } catch (Exception e) {
            log.error("无法启动miniTouch，改用adb进行操作", e);
            IDeviceHandler = new AdbCli(device, new DefaultSize());
        }

    }

    public void touchDown(int x, int y) throws IOException {
        IDeviceHandler.down(x, y);
    }

    public void touchUp(int x, int y) throws IOException {
        IDeviceHandler.up(x, y);
    }

    public void touchMove(int x, int y) throws IOException {
        IDeviceHandler.move(x, y);
    }

    public void swipe(int x1, int y1, int x2, int y2, int duration) throws IOException {
        IDeviceHandler.swipe(x1, y1, x2, y2, duration);
    }

    public boolean isUseMiniTouch() {
        return IDeviceHandler instanceof MiniTouchCli;
    }

    @Override
    public void close() throws IOException {
        if (null != IDeviceHandler) {
            IDeviceHandler.close();
        }
        miniTouch.close();
    }

}
