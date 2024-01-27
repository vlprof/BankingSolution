package com.vlprof.banking.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ComponentScan(value = "com.vlprof.banking", excludeFilters = {
        @ComponentScan.Filter(RestController.class),
        @ComponentScan.Filter(Controller.class)
})
public class RootConfig {
}
