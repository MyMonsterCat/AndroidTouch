package com.github.monster.device.stf;

import com.android.ddmlib.*;
import com.github.monster.constant.Constants;
import com.github.monster.device.DeviceWrapper;
import com.github.monster.device.receiver.BaseServiceReceiver;
import com.github.monster.device.receiver.MinitouchReceiver;
import com.github.monster.device.receiver.StfAgentReceiver;
import com.github.monster.util.LibUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class MiniTouchService extends StfService {

    private String stfServiceInstalledPath;

    private final int apiLevel;

    public MiniTouchService(DeviceWrapper device) {
        super(device, Constants.BIN_MINITOUCH);
        String sdk = device.getDevice().getProperty(IDevice.PROP_BUILD_API_LEVEL);
        apiLevel = Integer.parseInt(sdk);
    }

    @Override
    public void init() throws TimeoutException,
            AdbCommandRejectedException,
            ShellCommandUnresponsiveException,
            IOException,
            SyncException,
            InstallException {
        if (isInitialized()) {
            return;
        }

        if (isUseTouch()) {
            String abi = device.getDevice().getProperty(IDevice.PROP_DEVICE_CPU_ABI);
            device.getDevice().pushFile(LibUtils.getMinitouchBin(abi, apiLevel), Constants.REMOTE_PATH_MINITOUCH);
            device.getDevice().executeShellCommand("chmod 0755 " + Constants.REMOTE_PATH_MINITOUCH, NullOutputReceiver.getReceiver());
            return;
        }

        if (null == stfServiceInstalledPath) {
            device.getDevice().installPackage(Constants.APK_STF_SERVICE, true);
            stfServiceInstalledPath = getStfServiceInstalledPath();
            if (null == stfServiceInstalledPath) {
                throw new IllegalStateException("Failed to install STFService.apk");
            }
        }
    }

    @Override
    public String start() throws InterruptedException {
        String socketName;
        String cmd;
        BaseServiceReceiver startReceiver;
        if (null == stfServiceInstalledPath) {
            startReceiver = new MinitouchReceiver();
            socketName = Constants.SOCKET_NAME_MINITOUCH;
            cmd = Constants.CMD_MINITOUCH;
        } else {
            startReceiver = new StfAgentReceiver();
            socketName = Constants.SOCKET_NAME_MINITOUCH_AGENT;
            cmd = "export CLASSPATH=\"" + stfServiceInstalledPath + "\"; exec app_process /system/bin " + Constants.CLS_STF_AGENT;
        }
        doStart(cmd, startReceiver);
        return socketName;
    }

    public boolean isUseTouch() {
        return apiLevel < Constants.MIN_API_LEVEL_TOUCH_AGENT;
    }

    private boolean isInitialized() throws TimeoutException,
            AdbCommandRejectedException,
            ShellCommandUnresponsiveException,
            IOException {
        if (isUseTouch()) {
            //use minitouch
            CollectingOutputReceiver receiver = new CollectingOutputReceiver();
            device.getDevice().executeShellCommand(
                    Constants.REMOTE_PATH_MINITOUCH + " -h", receiver
            );
            String output = receiver.getOutput().trim();
            return output.startsWith("Usage");
        }

        //use minitouch agent instead
        stfServiceInstalledPath = getStfServiceInstalledPath();
        return null != stfServiceInstalledPath;
    }

    private String getStfServiceInstalledPath() throws TimeoutException,
            AdbCommandRejectedException,
            ShellCommandUnresponsiveException,
            IOException {
        CollectingOutputReceiver receiver = new CollectingOutputReceiver();
        device.getDevice().executeShellCommand("pm path " + Constants.PKG_STF_SERVICE, receiver);
        String output = receiver.getOutput();
        if (output.startsWith("package:")) {
            return output.split(":")[1].trim();
        }
        return null;
    }
}
