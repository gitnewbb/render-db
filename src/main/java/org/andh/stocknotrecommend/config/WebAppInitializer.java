package org.andh.stocknotrecommend.config;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {

    /**
     * 🛠️ `onStartup()` : 애플리케이션이 시작될 때 실행되는 메서드
     * @param servletContext - 현재 웹 애플리케이션의 `ServletContext`
     * @throws ServletException - 서블릿 초기화 중 예외 발생 가능
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 📌 Spring 애플리케이션 컨텍스트 생성
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        // 📌 AppConfig.java (Spring 설정 클래스) 등록
        context.register(org.andh.stocknotrecommend.config.AppConfig.class);

        // 📌 DispatcherServlet 생성 및 등록 (Spring MVC의 핵심 서블릿)
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcherServlet", dispatcherServlet);

        // 📌 애플리케이션이 시작될 때 DispatcherServlet을 가장 먼저 로드
        registration.setLoadOnStartup(1);

        // 📌 모든 요청("/")을 DispatcherServlet이 처리하도록 설정
        registration.addMapping("/");

        // 📌 UTF-8 인코딩 필터 등록
        FilterRegistration.Dynamic encodingFilter = servletContext.addFilter(
                "encodingFilter",
                new org.andh.stocknotrecommend.filter.EncodingFilter()
        );

        // 📌 모든 요청("/*")에 대해 인코딩 필터 적용
        encodingFilter.addMappingForUrlPatterns(null, true, "/*");
    }
}

