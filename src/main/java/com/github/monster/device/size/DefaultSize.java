package com.github.monster.device.size;

import com.github.monster.entity.Size;

/**
 * 默认设备规格
 *
 * @author Monster
 * @date 2023/3/10 17:22
 */
public class DefaultSize implements IDeviceSize {


    @Override
    public Size getSize() {
        return new Size(1600, 900);
    }

    @Override
    public Size getOutputSize() {
        return new Size(1600, 900);
    }

    @Override
    public double getScale() {
        return 1;
    }
}
