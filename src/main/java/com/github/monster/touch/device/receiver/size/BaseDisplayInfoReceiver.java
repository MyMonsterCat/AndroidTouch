package com.github.monster.touch.device.receiver.size;

import com.android.ddmlib.MultiLineReceiver;
import com.github.monster.touch.entity.Size;
import lombok.Getter;

/**
 * 基础信息接收器
 */
@Getter
public abstract class BaseDisplayInfoReceiver extends MultiLineReceiver {

    private final Size size = new Size();

    @Override
    public boolean isCancelled() {
        return size.isValid();
    }
}
