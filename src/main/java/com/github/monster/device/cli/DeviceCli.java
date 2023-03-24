package com.github.monster.device.cli;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.TimeoutException;
import com.github.monster.device.DeviceWrapper;
import com.github.monster.device.IDeviceHandler;
import com.github.monster.device.size.DefaultSize;
import com.github.monster.device.stf.MiniTouchService;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;

/**
 * DeviceCli默认以miniTouch启动
 * miniTouch启动失败后，以adb进行交互操作
 * adb无法实现缩放、放大等多点触控
 */
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
            IDeviceHandler = new MiniTouchCli(device, socket);
        } catch (Exception e) {
            log.error("无法启动miniTouch，改用adb进行操作", e);
            IDeviceHandler = new AdbCli(device, new DefaultSize(device));
        }

    }

    /**
     * 模拟按下
     *
     * @param x 按下的坐标X轴
     * @param y 按下的坐标Y轴
     * @throws IOException
     */
    public void touchDown(int x, int y) throws IOException {
        IDeviceHandler.down(x, y);
    }

    /**
     * 模拟抬起
     *
     * @param x 按下的坐标X轴
     * @param y 按下的坐标Y轴
     * @throws IOException
     */
    public void touchUp(int x, int y) throws IOException {
        IDeviceHandler.up(x, y);
    }

    /**
     * 模拟移动
     *
     * @param x 按下的坐标X轴
     * @param y 按下的坐标Y轴
     * @throws IOException
     */
    public void touchMove(int x, int y) throws IOException {
        IDeviceHandler.move(x, y);
    }

    /**
     * 模拟滑动
     *
     * @param x1       开始坐标X轴
     * @param y1       开始坐标Y轴
     * @param x2       结束坐标X轴
     * @param y2       结束坐标Y轴
     * @param duration 移动时间
     * @throws IOException
     */
    public void swipe(int x1, int y1, int x2, int y2, int duration) throws IOException {
        IDeviceHandler.swipe(x1, y1, x2, y2, duration);
    }

    public void screenShot(String path) throws AdbCommandRejectedException, IOException, TimeoutException {
        IDeviceHandler.screenShot(path);
    }

    public void doubleFingerTouch(int x1, int y1, int x2, int y2, int x1Move, int y1Move, int x2Move, int y2Move) throws IOException {
        IDeviceHandler.doubleFingerTouch(x1, y1, x2, y2, x1Move, y1Move, x2Move, y2Move);
    }

    /**
     * 是否在使用miniTouch
     *
     * @return
     */
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
