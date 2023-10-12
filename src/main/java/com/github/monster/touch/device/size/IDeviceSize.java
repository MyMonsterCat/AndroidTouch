package com.github.monster.touch.device.size;

import com.github.monster.touch.entity.Size;

public interface IDeviceSize {

    Size getSize();

    Size getOutputSize();


    double getPercentX();

    double getPercentY();
}
