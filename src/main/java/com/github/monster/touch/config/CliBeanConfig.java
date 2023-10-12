package com.github.monster.touch.config;

import com.android.ddmlib.AndroidDebugBridge;
import com.github.monster.touch.device.DeviceWrapper;
import com.github.monster.touch.device.cli.AdbCli;
import com.github.monster.touch.device.cli.MiniTouchCli;
import com.github.monster.touch.device.size.DefaultSize;
import com.github.monster.touch.device.stf.MiniTouchService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Bean配置
 * @author Monster
 */
@Configuration
@Slf4j
public class CliBeanConfig {

    private AndroidDebugBridge adb;

    @Bean(destroyMethod = "close")
    @Conditional(miniTouchCliCondition.class)
    @SneakyThrows
    public MiniTouchCli miniTouchCli() {
        AndroidDebugBridge adb = getBridgeAuto();
        Thread.sleep(1000L);
        DeviceWrapper device = new DeviceWrapper(adb.getDevices()[0]);
        MiniTouchService miniTouchService = new MiniTouchService(device);
        MiniTouchCli miniTouchCli = null;
        try {
            miniTouchService.init();
            String socket = miniTouchService.start();
            miniTouchCli = new MiniTouchCli(device, socket);
        } catch (Exception e) {
            log.error("无法启动miniTouch");
        }
        return miniTouchCli;
    }

    @Bean(destroyMethod = "close")
    @Conditional(adbCliCondition.class)
    @SneakyThrows
    public AdbCli adbCli() {
        AndroidDebugBridge adb = getBridgeAuto();
        Thread.sleep(1000L);
        DeviceWrapper device = new DeviceWrapper(adb.getDevices()[0]);
        AdbCli adbCli = new AdbCli(device, new DefaultSize(device));
        return adbCli;
    }


    /**
     * 根据系统环境使用不同ADB
     */
    private AndroidDebugBridge getBridgeAuto() throws Exception {
        if (adb == null) {
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


}
