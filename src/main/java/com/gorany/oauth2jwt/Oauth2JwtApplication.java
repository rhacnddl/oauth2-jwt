package com.gorany.oauth2jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.ZoneOffset;
import java.util.TimeZone;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class Oauth2JwtApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
        SpringApplication.run(Oauth2JwtApplication.class, args);
    }

}
