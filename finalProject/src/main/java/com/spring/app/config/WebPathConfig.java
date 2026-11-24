package com.spring.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebPathConfig implements WebMvcConfigurer {
	// URL 경로와 외부경로를 매핑시켜주는 설정 클래스 생성하기
	

	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		
		registry.addResourceHandler("/photoupload/**")
		        .addResourceLocations("file:/",
		        		              "classpath:/static/images/");
		// Spring boot 에게 웹브라우저로 부터 "/photoupload/** 로 요청이 오면 실제 파일시스템의 photoupload 폴더를 찾아라" 라고 설정해주는 것이다.
        // !!! 중요한 것은 꼭 접두어인 file: 과 접미어인 / 를 반드시 넣어주어야 한다.!!!  
		// Spring 은 다음의 순서대로 찾는다. 
		// 제일 먼저 외부 업로드 폴더를 먼저 검색하고(file_photoupload/~~~.jpg) 있으면 이것을 사용하고, 
		// 만약에 없으면 static 을 검색한다.(static/images/~~~.jpg) 
		
        // 그리고 스프링시큐리티 설정파일인 com.spring.app.security.config.SecurityConfig 에서 excludeUri 에 "/photoupload/**" 을 추가해 주어야 한다.
	
		registry.addResourceHandler("/productimages/**")
        		.addResourceLocations("file:/",
        		                      "classpath:/static/images/");
		// Spring boot 에게 웹브라우저로 부터 "/photoupload/** 로 요청이 오면 실제 파일시스템의 photoupload 폴더를 찾아라" 라고 설정해주는 것이다.
        // !!! 중요한 것은 꼭 접두어인 file: 과 접미어인 / 를 반드시 넣어주어야 한다.!!!  
		
	}
	
}