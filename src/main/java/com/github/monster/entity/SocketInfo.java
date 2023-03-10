package com.github.monster.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocketInfo {

    private int port;

    private String name;
}
