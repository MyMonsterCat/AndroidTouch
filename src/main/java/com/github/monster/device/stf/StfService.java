package com.github.monster.device.stf;

import com.github.monster.concurrent.NamedThreadFactory;
import com.github.monster.device.DeviceWrapper;
import com.github.monster.device.receiver.BaseServiceReceiver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class StfService implements IStfService {

    @Getter
    protected final DeviceWrapper device;

    private ExecutorService threadPool;

    private final Object closeLock = new Object();

    private boolean closed;

    private BaseServiceReceiver serviceStartReceiver;

    public StfService(DeviceWrapper device, String serviceName) {
        this.device = device;
        this.threadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(),
                new NamedThreadFactory("dev-" + device.getDevice().getSerialNumber() + '-' + serviceName));
    }

    protected void closeOthers() throws IOException {

    }

    /**
     * @param cmd
     * @param receiver
     * @return local socket port
     * @throws InterruptedException
     */
    protected void doStart(String cmd, BaseServiceReceiver receiver)
            throws InterruptedException {
        serviceStartReceiver = receiver;
        threadPool.execute(() -> {
            try {
                log.info("start " + receiver.getServiceName());
                device.getDevice().executeShellCommand(cmd, receiver, 0, TimeUnit.MILLISECONDS);
                log.info(receiver.getServiceName() + " stopped .");
            } catch (Exception e) {
                log.error("Failed to start " + receiver.getServiceName() + " service .", e);
            }
        });
        if (!receiver.waitForStart(3, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Failed to start " + receiver.getServiceName());
        }
        log.info(receiver.getServiceName() + " start success .");
    }

    public boolean isClosed() {
        synchronized (closeLock) {
            return closed;
        }
    }

    @Override
    public synchronized void close() throws IOException {
        synchronized (closeLock) {
            if (isClosed()) {
                return;
            }
            if (serviceStartReceiver != null) {
                serviceStartReceiver.cancel();
            }
            threadPool.shutdown();
            closeOthers();
            closed = true;
        }
    }
}
