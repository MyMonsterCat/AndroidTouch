package com.github.monster.touch.config;

import com.android.ddmlib.AndroidDebugBridge;
import com.github.monster.touch.device.DeviceWrapper;
import com.github.monster.touch.device.cli.AdbCli;
import com.github.monster.touch.device.cli.MiniTouchCli;
import com.github.monster.touch.device.size.DefaultSize;
import com.github.monster.touch.device.stf.MiniTouchService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class CliBeanConfigTest {

    private MiniTouchCli miniTouchCli;
    private AndroidDebugBridge adb;
    private AdbCli adbCli;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        adb = loadAdb();
        miniTouchCli = loadMiniTouchCli(adb);
        adbCli = loadAdbCli(adb);
    }

    @Test
    @SneakyThrows
    void miniTouchCli() {
        miniTouchCli.swipe(100, 100, 600, 600, 500);
    }

    @Test
    @SneakyThrows
    void adbCli() {
        adbCli.swipe(100, 100, 600, 600, 500);
    }

    @AfterEach
    @SneakyThrows
    void tearDown() {
        miniTouchCli.close();
        adbCli.close();
    }

    @SneakyThrows
    private AndroidDebugBridge loadAdb() {
        if (adb == null) {
            // 获取ADB
            AndroidDebugBridge.init(false);
            String os = System.getProperty("os.name").toLowerCase();
            if (os.startsWith("win")) {
                // 如果是win系统
                adb = AndroidDebugBridge.createBridge("./libs/adb/adb-win.exe", false);
            } else if (os.startsWith("mac")) {
                // 如果是osx系统
                adb = AndroidDebugBridge.createBridge("./libs/adb/adb-mac", false);
            } else {
                adb = AndroidDebugBridge.createBridge("./libs/adb/adb-linux", true);
            }
            while (!adb.hasInitialDeviceList()) {
                Thread.sleep(500L);
                log.info("wait for device connect...");
            }
            log.info("device :{} connected", adb.getDevices());
        }
        return adb;
    }

    @SneakyThrows
    private MiniTouchCli loadMiniTouchCli(AndroidDebugBridge adb) {
        if (miniTouchCli == null) {
            Thread.sleep(1000L);
            DeviceWrapper device = new DeviceWrapper(adb.getDevices()[0]);
            MiniTouchService miniTouchService = new MiniTouchService(device);
            try {
                miniTouchService.init();
                String socket = miniTouchService.start();
                return new MiniTouchCli(device, socket);
            } catch (Exception e) {
                log.error("无法启动miniTouch");
            }
        }
        return miniTouchCli;
    }

    @SneakyThrows
    private AdbCli loadAdbCli(AndroidDebugBridge adb) {
        if (adbCli == null) {
            Thread.sleep(1000L);
            DeviceWrapper device = new DeviceWrapper(adb.getDevices()[0]);
            return new AdbCli(device, new DefaultSize(device));
        }
        return adbCli;
    }
}