package org.andh.stocknotrecommend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.andh.stocknotrecommend") // 📌 패키지 내 모든 Bean 자동 등록
public class AppConfig {
    // 📌 별도의 Bean을 직접 정의하지 않고, @ComponentScan을 통해 자동 등록
}
