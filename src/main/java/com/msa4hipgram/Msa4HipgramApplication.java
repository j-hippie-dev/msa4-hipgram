package com.msa4hipgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan // 메인 클래스에 넣어주기
public class Msa4HipgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(Msa4HipgramApplication.class, args);
    }

}
