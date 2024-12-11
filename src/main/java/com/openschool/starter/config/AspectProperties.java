package com.openschool.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix="openschool.starter.aspect")
public class AspectProperties {

    private final Map<String, String> log = new HashMap<>();

    public Map<String, String> getLog() {
        return log;
    }
}
