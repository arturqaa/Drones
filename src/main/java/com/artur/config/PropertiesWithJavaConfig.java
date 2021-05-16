package com.artur.config;

import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@PropertySource("classpath:application-dev.properties")
public class PropertiesWithJavaConfig {
}
