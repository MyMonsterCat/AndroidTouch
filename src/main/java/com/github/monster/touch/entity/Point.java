package com.github.monster.touch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 触摸点
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    /**
     * 触摸点X坐标
     */
    private int x;

    /**
     * 触摸点Y坐标
     */
    private int y;
}
