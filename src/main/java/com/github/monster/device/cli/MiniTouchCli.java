package com.github.monster.device.cli;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.TimeoutException;
import com.github.monster.constant.Constants;
import com.github.monster.device.DeviceWrapper;
import com.github.monster.device.IDeviceHandler;
import com.github.monster.device.size.CommonDeviceSize;
import com.github.monster.device.size.DefaultSize;
import com.github.monster.device.size.IDeviceSize;
import com.github.monster.device.socket.PortManager;
import com.github.monster.entity.Point;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * MiniTouch命令行工具
 */
@Slf4j
public class MiniTouchCli implements IDeviceHandler {

    private static final byte[] COMMAND_COMMIT = "c\n".getBytes();

    private static final byte[] COMMAND_TOUCH_UP = "u 0\n".getBytes();

    private final OutputStream outputStream;
    private final InputStream inputStream;

    private final IDeviceSize IDeviceSize;

    private DeviceWrapper device;


    public MiniTouchCli(DeviceWrapper device, String socketName)
            throws AdbCommandRejectedException, IOException, TimeoutException {
        this.device = device;

        int port = PortManager.createForward(device.getDevice(), Constants.BIN_MINITOUCH, socketName, IDevice.DeviceUnixSocketNamespace.ABSTRACT);
        Socket socket = new Socket("localhost", port);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        this.IDeviceSize = getVirtualDeviceSize();
    }

    @SneakyThrows
    private IDeviceSize getVirtualDeviceSize() {
        StringBuffer stringBuffer = new StringBuffer();
        if (inputStream != null) {
            byte[] buffer = new byte[128];
            int bytesRead = inputStream.read(buffer, 0, 128);
            if (bytesRead > 0) {
                String data = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                stringBuffer.append(data);
            }
        }
        log.info("receive device info: {}", stringBuffer.toString());
        String[] split = stringBuffer.toString().split("[\n  ]");
        if (split.length <= 4) {
            return new DefaultSize(device);
        } else {
            return new CommonDeviceSize(device, Integer.parseInt(split[4]), Integer.parseInt(split[5]));
        }
    }

    @Override
    public void down(int x, int y) throws IOException {
        Point real = convertToRealPoint(x, y);
        executeCommand(String.format("d 0 %d %d 50\n", real.getX(), real.getY()));
        log.debug("按下 {},{}", real.getX(), real.getY());
    }

    @Override
    public void up(int x, int y) throws IOException {
        executeCommand(COMMAND_TOUCH_UP);
        log.debug("松开 {},{}", x, y);
    }

    @Override
    public void move(int x, int y) throws IOException {
        Point real = convertToRealPoint(x, y);
        executeCommand(String.format("m 0 %d %d 50\n", real.getX(), real.getY()));
        log.debug("移动 {},{}", real.getX(), real.getY());
    }

    @Override
    public void swipe(int x1, int y1, int x2, int y2, int duration) throws IOException {
        down(x1, y1);
        move(x2, y2);
        up(x2, y2);
    }

    @Override
    public String screenShot(String path) throws AdbCommandRejectedException, IOException, TimeoutException {
        return device.screenShot(path);
    }

    @Override
    public IDeviceSize getDeviceSize() {
        return IDeviceSize;
    }

    private void executeCommand(String command) throws IOException {
        executeCommand(command.getBytes());
    }

    private void executeCommand(byte[] command) throws IOException {
        outputStream.write(command);
        outputStream.write(COMMAND_COMMIT);
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        if (null != outputStream) {
            outputStream.close();
        }
        if (null != inputStream) {
            inputStream.close();
        }
        AndroidDebugBridge.terminate();
    }
}
