package com.spring.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebPathConfig implements WebMvcConfigurer {
	// URL 경로와 외부경로를 매핑시켜주는 설정 클래스 생성하기
	
	@Value("${file.profileimage-dir}")
	private String profileimageDir;   // 프로필 이미지 업로드를 위한 폴더 경로

	@Value("${file.qnaimage-dir}")
	private String qnaimageDir;   // QnA 이미지 업로드를 위한 폴더 경로
                      
	@Value("${file.eventimageimage-dir}")
	private String eventimageimageDir;   // 이벤트 배너 이미지 업로드를 위한 폴더 경로
	
	@Value("${file.reviewimage-dir}")
	private String reviewimageDir;   // 리뷰 이미지 업로드를 위한 폴더 경로
	  
	@Value("${file.recipeimage-dir}")
	private String recipeimageDir;   // 레시피 대표 이미지 업로드를 위한 폴더 경로
	  
	@Value("${file.rcpfinishedimage-dir}")
	private String rcpfinishedimageDir;   // 레시피 완성 이미지 업로드를 위한 폴더 경로
	  
	@Value("${file.cookorderimage-dir}")
	private String cookorderimageDir;   // 요리 순서 이미지 업로드를 위한 폴더 경로
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/profileimage/**")
        		.addResourceLocations("file:"+profileimageDir+"/",
        		              		  "classpath:/static/images/");
		
		registry.addResourceHandler("/qnaimage/**")
				.addResourceLocations("file:"+qnaimageDir+"/",
									  "classpath:/static/images/");
		
		registry.addResourceHandler("/eventimageimage/**")
				.addResourceLocations("file:"+eventimageimageDir+"/",
									  "classpath:/static/images/");
		
		registry.addResourceHandler("/reviewimage/**")
				.addResourceLocations("file:"+reviewimageDir+"/",
		              		  		  "classpath:/static/images/");
		
		registry.addResourceHandler("/recipeimage/**")
				.addResourceLocations("file:"+recipeimageDir+"/",
									  "classpath:/static/images/");
		
		registry.addResourceHandler("/rcpfinishedimage/**")
				.addResourceLocations("file:"+rcpfinishedimageDir+"/",
									  "classpath:/static/images/");
		  
		registry.addResourceHandler("/cookorderimage/**")
				.addResourceLocations("file:"+cookorderimageDir+"/",
							          "classpath:/static/images/");
		
	}
	
}