package com.msa4hipgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ConfigurationPropertiesScan // 메인 클래스에 넣어주기
@EnableJpaAuditing // 어플리케이션 전체에 "JPA Auditing 기능을 사용할테니 준비해라." 선언
public class Msa4HipgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(Msa4HipgramApplication.class, args);
    }

}
