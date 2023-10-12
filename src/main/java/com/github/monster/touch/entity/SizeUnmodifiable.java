package com.github.monster.touch.entity;


/**
 * 不可变大小
 */
public class SizeUnmodifiable extends Size {

    public SizeUnmodifiable(int width, int height) {
        super(width, height);
    }

    public SizeUnmodifiable(Size size) {
        this(size.getWidth(), size.getHeight());
    }

    @Override
    public void setWidth(int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHeight(int height) {
        throw new UnsupportedOperationException();
    }
}
