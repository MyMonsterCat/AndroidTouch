package com.github.monster.device;

import com.android.ddmlib.*;
import com.github.monster.device.receiver.BaseDisplayInfoReceiver;
import com.github.monster.device.receiver.DumpsysDisplayReceiver;
import com.github.monster.device.receiver.DumpsysWindowReceiver;
import com.github.monster.device.receiver.WmSizeReceiver;
import com.github.monster.entity.Size;
import com.github.monster.entity.SizeUnmodifiable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Getter
@Slf4j
public class DeviceWrapper {

    private final IDevice device;

    private final Size size;

    public DeviceWrapper(IDevice device) throws Exception {
        this.device = device;
        size = new SizeUnmodifiable(loadSize());
    }

    public void sendKeyEvent(int value) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        String cmd = String.format("input keyevent %d", value);
        device.executeShellCommand(cmd, NullOutputReceiver.getReceiver());
    }

    public void tap(int x, int y) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        String cmd = String.format("input tap %d %d", x, y);
        device.executeShellCommand(cmd, NullOutputReceiver.getReceiver());
    }

    public void swipe(int x1, int y1, int x2, int y2, int duration) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        String cmd = String.format("input swipe %d %d %d %d %d", x1, y1, x2, y2, duration);
        device.executeShellCommand(cmd, NullOutputReceiver.getReceiver());
    }

    /**
     * 截屏
     *
     * @param path 图片保存路径（PC上）
     */
    public String screenShot(String path) throws AdbCommandRejectedException, IOException, TimeoutException {
        RawImage rawScreen = device.getScreenshot();
        BufferedImage bufferedImage = rawScreen.asBufferedImage();
        ImageIO.write(bufferedImage, "PNG", new File(path));
        return path;
    }


    private Size loadSize() throws Exception {
        BaseDisplayInfoReceiver receiver = new DumpsysDisplayReceiver();
        device.executeShellCommand("dumpsys display", receiver);
        if (receiver.getSize().isValid()) {
            return receiver.getSize();
        }

        receiver = new DumpsysWindowReceiver();
        device.executeShellCommand("dumpsys window", receiver);
        if (receiver.getSize().isValid()) {
            return receiver.getSize();
        }

        receiver = new WmSizeReceiver();
        device.executeShellCommand("wm size", receiver);
        if (receiver.getSize().isValid()) {
            return receiver.getSize();
        }
        throw new IllegalStateException("Unable to get display info");
    }
}
