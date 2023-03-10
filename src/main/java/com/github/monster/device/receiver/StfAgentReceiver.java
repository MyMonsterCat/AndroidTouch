package com.github.monster.device.receiver;

public class StfAgentReceiver extends BaseServiceReceiver {

    public StfAgentReceiver() {
        super("minitouch agent");
    }

    @Override
    protected boolean isStarted(String line) {
        return line.equals("Listening on @stfagent");
    }

    @Override
    public void cancel() {
        super.cancel();
        //TODO how to check the stf agent is terminated
        try {
            //waiting for shutdown
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            //do nothing
        }
    }
}
