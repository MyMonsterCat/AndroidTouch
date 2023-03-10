package com.github.monster.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Size {
    private int width;

    private int height;

    public boolean isValid() {
        return width > 0 && height > 0;
    }
}
