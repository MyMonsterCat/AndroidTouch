package com.github.monster.touch.device.cli;

import com.github.monster.touch.device.size.IDeviceSize;
import com.github.monster.touch.entity.Point;

import java.io.Closeable;
import java.io.IOException;

/**
 * 设备Cli接口，用于规范Cli功能
 */
public interface IDeviceCli extends Closeable {

    /**
     * 模拟手指 按下
     *
     * @param x x坐标
     * @param y y坐标
     * @throws IOException io异常
     */
    void down(int x, int y) throws IOException;

    /**
     * 模拟手指 抬起
     *
     * @param x x坐标
     * @param y y坐标
     * @throws IOException io异常
     */
    void up(int x, int y) throws IOException;

    /**
     * 模拟单根手指 滑动
     *
     * @param x x坐标
     * @param y y坐标
     * @throws IOException io异常
     */
    void move(int x, int y) throws IOException;

    /**
     * 模拟单根手指 从一个点滑动到另一个点
     *
     * @param x1       开始x坐标
     * @param y1       开始y坐标
     * @param x2       结束x坐标
     * @param y2       结束y坐标
     * @param duration 滑动时间，单位：ms
     * @throws IOException io异常
     */
    void swipe(int x1, int y1, int x2, int y2, int duration) throws IOException;

    /**
     * 截屏
     *
     * @param path 图片保存路径（PC上）
     * @return 图片保存路径
     * @throws Exception
     */
    String screenShot(String path) throws Exception;

    /**
     * 模拟双指触控
     *
     * @param x1     第1根手指头x按下坐标
     * @param y1     第1根手指头y按下坐标
     * @param x2     第2根手指头x按下坐标
     * @param y2     第2根手指头y按下坐标
     * @param x1Move 第1根手指头x松开坐标
     * @param y1Move 第1根手指头y松开坐标
     * @param x2Move 第2根手指头x松开坐标
     * @param y2Move 第2根手指头y松开坐标
     * @throws IOException io异常
     */
    void doubleFingerTouch(int x1, int y1, int x2, int y2, int x1Move, int y1Move, int x2Move, int y2Move) throws IOException;

    /**
     * 获取屏幕尺寸
     *
     * @return 尺寸信息
     */
    IDeviceSize getDeviceSize();

    /**
     * 转换为真实坐标
     */
    default Point convertToRealPoint(int x, int y) {
        return new Point((int) (x * getDeviceSize().getPercentX()), (int) (y * getDeviceSize().getPercentY()));
    }
}
