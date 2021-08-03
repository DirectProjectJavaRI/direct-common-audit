package org.nhindirect.common.audit;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.nhindirect.common.audit.impl"})
public class R2DBCTestConfiguration
{	
    public static void main(String[] args) 
    {
        new SpringApplicationBuilder(R2DBCTestConfiguration.class)
          .web(WebApplicationType.NONE).run(args);
    }  
}