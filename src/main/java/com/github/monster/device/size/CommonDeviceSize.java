package com.github.monster.device.size;

import com.github.monster.device.DeviceWrapper;
import com.github.monster.entity.Size;
import com.github.monster.entity.SizeUnmodifiable;

/**
 * 通用设备规格
 *
 * @author Monster
 * @date 2023/3/10 17:22
 */
public class CommonDeviceSize implements IDeviceSize {

    private Size size;
    private Size outputSize;

    private double scale;

    public CommonDeviceSize(DeviceWrapper device) {
        new CommonDeviceSize(720, device);
    }

    public CommonDeviceSize(int maxHeight, DeviceWrapper device) {
        size = device.getSize();
        if (maxHeight > 0 && size.getHeight() > maxHeight) {
            scale = maxHeight * 1.0 / size.getHeight();
            outputSize = new SizeUnmodifiable((int) (scale * size.getWidth()), maxHeight);
        } else {
            scale = 1;
            outputSize = new SizeUnmodifiable(size);
        }
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public Size getOutputSize() {
        return outputSize;
    }

    @Override
    public double getScale() {
        return scale;
    }
}
