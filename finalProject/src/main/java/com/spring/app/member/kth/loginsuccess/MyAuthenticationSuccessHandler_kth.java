package com.spring.app.member.kth.loginsuccess;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class MyAuthenticationSuccessHandler_kth
        extends SimpleUrlAuthenticationSuccessHandler {

    // 로그인 성공 시 처리하기
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        // 로그인하기 이전 페이지 주소 가져오기
        String prevURLPage = (String) session.getAttribute("prevURLPage");

        // 세션에 저장된 이전 페이지 주소 제거하기
        session.removeAttribute("prevURLPage");

        // 기본 이동 페이지
        String contextPath = request.getContextPath();
        String targetUrl = contextPath + "/index";

        // 현재 프로젝트의 주소인지 확인하기
        String baseURL = request.getScheme() + "://"
                       + request.getServerName() + ":"
                       + request.getServerPort();

        if(prevURLPage != null) {

            if(prevURLPage.startsWith(baseURL + contextPath + "/")) {
                targetUrl = prevURLPage;
            }
            else if(prevURLPage.startsWith(contextPath + "/")) {
                targetUrl = prevURLPage;
            }
        }

        // 인증 과정에서 발생한 오류 정보 제거하기
        clearAuthenticationAttributes(request);

        // 페이지 이동하기
        response.sendRedirect(targetUrl);
    }

}