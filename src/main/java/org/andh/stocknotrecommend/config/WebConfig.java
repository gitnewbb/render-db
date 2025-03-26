package org.andh.stocknotrecommend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /**
     * 🖥️ `viewResolver()` : View Resolver 설정 (JSP 파일을 뷰로 사용 가능하게 함)
     * @return ViewResolver - JSP 뷰 리졸버 객체 반환
     *
     * ✅ 뷰의 경로 설정
     *    - `/WEB-INF/views/` 디렉토리에서 JSP 파일을 찾음
     *    - `.jsp` 확장자를 자동으로 붙여줌
     *
     * ✅ **예제**
     *   - 컨트롤러에서 `return "index";` 하면 `/WEB-INF/views/index.jsp` 파일이 렌더링됨
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        // 📌 뷰 파일이 위치하는 기본 경로 설정
        viewResolver.setPrefix("/WEB-INF/views/");

        // 📌 뷰 파일 확장자 설정
        viewResolver.setSuffix(".jsp");

        // 📌 뷰 리졸버의 우선순위 설정 (낮을수록 높은 우선순위)
        viewResolver.setOrder(1);

        return viewResolver;
    }
}