package com.github.monster.device.cli;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.github.monster.device.DeviceWrapper;
import com.github.monster.device.IDeviceHandler;
import com.github.monster.device.size.IDeviceSize;
import com.github.monster.entity.Point;

import java.io.IOException;

/**
 * ADB命令行工具
 */
public class AdbCli implements IDeviceHandler {

    private final DeviceWrapper device;

    private final IDeviceSize IDeviceSize;

    private Point point;

    public AdbCli(DeviceWrapper device, IDeviceSize IDeviceSize) {
        this.device = device;
        this.IDeviceSize = IDeviceSize;
    }


    @Override
    public void down(int x, int y) throws IOException {
        //do nothing
        point = convertToRealPoint(x, y);
    }

    @Override
    public void up(int x, int y) throws IOException {
        Point real = convertToRealPoint(x, y);

        try {
            if(point.equals(real)) {
                device.tap(real.getX(), real.getY());
            } else {
                doSwipe(point.getX(), point.getY(), real.getX(), real.getY(), 100);
            }
        } catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void move(int x, int y) throws IOException {

    }

    @Override
    public void swipe(int x1, int y1, int x2, int y2, int duration) throws IOException {
        Point real1 = convertToRealPoint(x1, y1);
        Point real2 = convertToRealPoint(x2, y2);
        doSwipe(real1.getX(), real1.getY(), real2.getX(), real2.getY(), duration);
    }

    @Override
    public String screenShot(String path) throws AdbCommandRejectedException, IOException, TimeoutException {
        return device.screenShot(path);
    }

    @Override
    public IDeviceSize getDeviceSize() {
        return IDeviceSize;
    }

    @Override
    public void close() {
        AndroidDebugBridge.terminate();
    }

    private void doSwipe(int x1, int y1, int x2, int y2, int duration) throws IOException {
        try {
            device.swipe(x1, y1, x2, y2, duration);
        } catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException e) {
            throw new IOException(e);
        }
    }
}
