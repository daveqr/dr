package com.davedaniels.nlp;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan( basePackages = "com.davedaniels.nlp" )
@PropertySource( "classpath:application.properties" )
public class AppConfig {}
