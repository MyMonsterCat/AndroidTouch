package com.github.monster.touch.device.size;

import com.github.monster.touch.device.DeviceWrapper;
import com.github.monster.touch.entity.Size;
import com.github.monster.touch.entity.SizeUnmodifiable;

/**
 * 通用设备规格
 *
 * @author Monster
 * @date 2023/3/10 17:22
 */
public class CommonDeviceSize implements IDeviceSize {

    private Size size;
    private Size outputSize;

    private double percentX;

    private double percentY;

    public CommonDeviceSize(DeviceWrapper device) {
        new CommonDeviceSize(device, 1600, 900);
    }

    public CommonDeviceSize(DeviceWrapper device, int weight, int height) {
        size = device.getSize();
        percentX = weight / size.getWidth();
        percentY = height / size.getHeight();

        outputSize = new SizeUnmodifiable(new Size(weight, height));
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
    public double getPercentX() {
        return percentX;
    }

    @Override
    public double getPercentY() {
        return percentY;
    }
}
