package com.github.monster.config;

import com.android.ddmlib.AndroidDebugBridge;
import com.github.monster.device.DeviceWrapper;
import com.github.monster.device.cli.DeviceCli;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Monster
 * @date 2023/3/9 15:50
 */
@Configuration
@Slf4j
public class AdbConfig {

    @Bean(initMethod = "start", destroyMethod = "close")
    @SneakyThrows
    public DeviceCli deviceCli() {

        AndroidDebugBridge.init(false);
        AndroidDebugBridge adb = AndroidDebugBridge.createBridge("./libs/adb/adb.exe", false);
        while (!adb.hasInitialDeviceList()) {
            Thread.sleep(500L);
            log.info("wait for device connect...");
        }

        Thread.sleep(1000L);
        DeviceWrapper device = new DeviceWrapper(adb.getDevices()[0]);

        DeviceCli deviceCli = new DeviceCli(device);
        return deviceCli;

    }

}
