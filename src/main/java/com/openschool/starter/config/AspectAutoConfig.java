package com.openschool.starter.config;

import ch.qos.logback.classic.Level;
import com.openschool.starter.aspect.MainAspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableConfigurationProperties(AspectProperties.class)
public class AspectAutoConfig {

    private final AspectProperties aspectProperties;

    @Value("${openschool.starter.aspect.log.level}")
    private String logLevelProperty;

    public AspectAutoConfig(AspectProperties aspectProperties) {
        this.aspectProperties = aspectProperties;
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "openschool.starter.aspect.log",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true)
    public MainAspect mainAspect() {
        return new MainAspect();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "openschool.starter.aspect.log",
            name = "log.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public Logger mainAspectLogger() {
        Logger logger = LoggerFactory.getLogger(MainAspect.class.getName());
        Level logLevel = Level.INFO;
        switch (logLevelProperty.toUpperCase()) {
            case "TRACE":
                logLevel = Level.TRACE;
                break;
            case "DEBUG":
                logLevel = Level.DEBUG;
                break;
            case "WARN":
                logLevel = Level.WARN;
                break;
            case "ERROR":
                logLevel = Level.ERROR;
                break;
            case "OFF":
                logLevel = Level.OFF;
                break;
        }
        ((ch.qos.logback.classic.Logger) logger).setLevel(logLevel);
        return logger;
    }

}
