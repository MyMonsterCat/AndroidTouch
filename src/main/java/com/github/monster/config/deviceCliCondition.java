package com.github.monster.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class deviceCliCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取 YAML 配置属性
        String conditionValue = context.getEnvironment().getProperty("adb-loader.device-cli");
        if (conditionValue == null) {
            conditionValue = "true";
        }
        // 根据条件值决定是否加载 bean
        return "true".equalsIgnoreCase(conditionValue);
    }
}
