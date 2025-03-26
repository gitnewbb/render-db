package org.andh.stocknotrecommend.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 📌 `EncodingFilter` : HTTP 요청과 응답의 인코딩을 UTF-8로 설정하는 필터
 * ✅ 모든 요청(Request)과 응답(Response)의 문자 인코딩을 UTF-8로 변경
 * ✅ 다국어(한글 등) 지원을 위해 사용
 */
public class EncodingFilter implements Filter {

    /**
     * 🔄 `doFilter()` : 필터를 통해 요청과 응답을 가로채서 처리
     * @param req  - 원본 `ServletRequest` (HTTP 요청)
     * @param resp - 원본 `ServletResponse` (HTTP 응답)
     * @param chain - 필터 체인 (다음 필터로 넘기는 역할)
     * @throws IOException - 입출력 예외
     * @throws ServletException - 서블릿 관련 예외
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        // 🔍 ServletRequest → HttpServletRequest 형 변환 (HTTP 요청으로 다루기 위해)
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 🌍 모든 요청(Request)의 문자 인코딩을 UTF-8로 설정
        request.setCharacterEncoding("UTF-8");

        // 🌍 모든 응답(Response)의 문자 인코딩을 UTF-8로 설정
        response.setCharacterEncoding("UTF-8");

        // 📌 응답의 Content-Type을 UTF-8로 설정하여 HTML에서 한글(또는 다국어) 정상 출력
        response.setContentType("text/html;charset=UTF-8");

        // ⏩ 필터 체인을 통해 다음 필터 또는 컨트롤러로 요청을 넘김
        chain.doFilter(req, resp);
    }
}
