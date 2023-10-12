package com.github.monster.touch.device.receiver;

import com.android.ddmlib.MultiLineReceiver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class BaseServiceReceiver extends MultiLineReceiver {

    protected abstract boolean isStarted(String line);

    protected void peekLine(String line) {
        //do nothing;
    }

    private final Semaphore semaphore;

    @Getter
    private final String serviceName;

    private boolean started = false;

    private AtomicBoolean cancelled = new AtomicBoolean(false);

    public BaseServiceReceiver(String serviceName) {
        semaphore = new Semaphore(0);
        this.serviceName = serviceName;
    }

    @Override
    public void processNewLines(String[] lines) {
        for (String line : lines) {
            log.info(serviceName + " :" + line);
            peekLine(line);
            if (!started && isStarted(line)) {
                log.info(serviceName + " started .");
                started = true;
                semaphore.release();
            }
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled.get();
    }

    public void cancel() {
        log.info("Close " + serviceName);
        cancelled.set(true);
    }

    public boolean waitForStart(long timeout, TimeUnit unit) throws InterruptedException {
        return semaphore.tryAcquire(timeout, unit);
    }
}
