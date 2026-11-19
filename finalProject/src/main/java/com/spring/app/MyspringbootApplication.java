package com.spring.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
	    ServletWebSecurityAutoConfiguration.class,
	    UserDetailsServiceAutoConfiguration.class,
	    SecurityFilterAutoConfiguration.class
	})
@EnableAspectJAutoProxy  // Application 클래스에 @EnableAspectJAutoProxy 를 추가하여 AOP(Aspect Oriented Programming)클래스를 찾을 수 있게 해준다. 우리는 com.spring.app.aop.CommonAop 가 AOP 클래스 이다.
@EnableScheduling        // @EnableScheduling 어노테이션을 사용하여 스프링스케줄러를 구현하도록 한다. Scheduler는 기본적으로 Spring Boot에 포함되어 있기 때문에 추가적으로 build.gradle 파일에 dependency 를 추가할 필요는 없고, Application 클래스에 @EnableScheduling 를 추가만 해주면 Scheduler 를 기능을 사용할 수 있게 된다.
public class MyspringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyspringbootApplication.class, args);
	}
	/*
	   스프링부트 애플리케이션은 내장 WAS(Tomcat, Jetty, Undertow 등)를 구동시키면 자동적으로 이 main() 메서드가 시작진입점으로서 자동적으로 실행된다.
       이 main() 메서드 속에 있는 SpringApplication.run() 메서드를 호출하여 실행시키는데, 이 SpringApplication.run() 메서드의 기능은 해당 프로젝트 웹 애플리케이션을 실행하는데 필요한 모든 빈들을 스캔해서 작동이 되도록 구성(세팅)시켜 주는 것이다. 
	*/
}
