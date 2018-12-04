package org.nhindirect.common.audit;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.nhindirect.common.audit.impl"})
@EnableAutoConfiguration
public class JPATestConfiguration
{

}
