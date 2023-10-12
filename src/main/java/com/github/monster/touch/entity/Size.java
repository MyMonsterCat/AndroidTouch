package com.github.monster.touch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备尺寸
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Size {
    /**
     * 长
     */
    private int width;
    /**
     * 宽
     */
    private int height;

    public boolean isValid() {
        return width > 0 && height > 0;
    }
}
