package com.github.monster.device.size;

import com.github.monster.entity.Size;

public interface IDeviceSize {

    Size getSize();

    Size getOutputSize();

    /**
     * output / real
     */
    double getScale();
}
