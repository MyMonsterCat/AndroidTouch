package com.github.monster.touch.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 配置扫描包
 *
 * @author Monster
 */
@ComponentScans(value =
        {
                @ComponentScan(value = "com.github.monster.touch")
        }
)
@Configuration
public class ScanConfig implements EnvironmentAware {

    @Override
    public void setEnvironment(Environment environment) {
    }
}
