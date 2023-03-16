package com.github.monster.device.size;

import com.github.monster.device.DeviceWrapper;
import com.github.monster.entity.Size;
import com.github.monster.entity.SizeUnmodifiable;

/**
 * 默认设备规格
 *
 * @author Monster
 * @date 2023/3/10 17:22
 */
public class DefaultSize implements IDeviceSize {

    private Size size;
    private Size outputSize;

    public DefaultSize(DeviceWrapper device) {
        size = device.getSize();
        outputSize = new SizeUnmodifiable(device.getSize());
    }

    @Override
    public Size getSize() {
        return new Size(1600, 900);
    }

    @Override
    public Size getOutputSize() {
        return new Size(1600, 900);
    }

    @Override
    public double getPercentX() {
        return 1;
    }

    @Override
    public double getPercentY() {
        return 1;
    }
}
