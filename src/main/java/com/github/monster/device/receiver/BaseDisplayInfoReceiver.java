package com.github.monster.device.receiver;

import com.android.ddmlib.MultiLineReceiver;
import com.github.monster.entity.Size;
import lombok.Getter;

@Getter
public abstract class BaseDisplayInfoReceiver extends MultiLineReceiver {

    private final Size size = new Size();

    @Override
    public boolean isCancelled() {
        return size.isValid();
    }
}
