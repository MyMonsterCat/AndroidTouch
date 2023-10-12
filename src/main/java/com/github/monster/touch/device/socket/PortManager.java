package com.github.monster.touch.device.socket;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.TimeoutException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 端口管控工具
 */
public class PortManager {

    private static final Object LOCK = new Object();

    private static final Map<String, Integer> PORTS = new HashMap<>();

    private static final int MIN_PORT = 11017;

    private static final int MAX_PORT = 61017;

    public static int createForward(IDevice device, String name, int remotePort)
            throws TimeoutException, AdbCommandRejectedException, IOException {
        return doCreateForward(device, name, port -> device.createForward(port, remotePort));
    }

    public static int createForward(IDevice device, String name,
                                    String remoteSocketName, IDevice.DeviceUnixSocketNamespace namespace)
            throws TimeoutException, AdbCommandRejectedException, IOException {
        return doCreateForward(device, name, port -> device.createForward(port, remoteSocketName, namespace));
    }

    private static int doCreateForward(IDevice device, String name, ForwardCreator creator) throws
            TimeoutException,
            IOException,
            AdbCommandRejectedException {
        String portKey = device.getSerialNumber() + ':' + name;
        synchronized (LOCK) {
            Integer port = PORTS.get(portKey);
            if (null != port) {
                return port;
            }
            for (int i = MIN_PORT; i < MAX_PORT; ++i) {
                if (PORTS.containsValue(i)) {
                    continue;
                }
                try {
                    creator.createForward(i);
                    PORTS.put(portKey, i);
                    return i;
                } catch (AdbCommandRejectedException e) {
                    if (e.getMessage().contains("cannot bind listener") || e.getMessage().contains("internal error")) {
                        continue;
                    }
                    throw e;
                }
            }
            throw new IOException("No port available");
        }
    }
}
