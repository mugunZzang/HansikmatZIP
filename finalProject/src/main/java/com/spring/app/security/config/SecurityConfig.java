package com.spring.app.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import com.spring.app.member.kth.service.CustomUserDetailsService_kth;
import com.spring.app.member.kth.security.JwtAuthenticationFilter_kth;
import jakarta.servlet.DispatcherType;

@Configuration        // Spring 컨테이너가 처리해주는 클래스로서,
                      // 클래스내에 하나 이상의 @Bean 메서드를 선언만 해주면
                      // 런타임시 해당 빈에 대해 정의되어진 대로 요청을 처리해준다.
@EnableWebSecurity    // SecurityConfig 클래스로 시큐리티를 제어하고 싶다면 @EnableWebSecurity 어노테이션을 해주어야 한다. 
					  // 만약에 @EnableWebSecurity을 어노테이션을 붙이지 않으면 인덱스 홈페이지가 "/login"으로 리다이렉션된다.
@EnableMethodSecurity // Controller 및 Service 상에서 생성한 메서드에 @PreAuthorize 를 사용하기 위한 전제조건인 것이다.
					  // @PreAuthorize 는 "메서드를 실행하기 전에, Spring Security에게 권한을 검사해달라" 는 뜻으로서, 메서드를 실행하기에 앞서서 "이 사람 권한 있어?" 하고 체크하고, 권한이 없으면 바로 막아버리는 것이다.
					  /*
					     @PreAuthorize("hasRole('ROLE_ADMIN')")
					     @GetMapping("/admin")
					     public String adminPage() {
					         return "admin/adminPage";
					     } 
					     1. 사용자가 /admin URL을 호출함
					     2. 메서드 실행 전에 Spring Security가 먼저 체크함
					     3. ROLE_ADMIN 권한이 있으면 → 메서드 실행
					     4. ROLE_ADMIN이 없으면 → 403 Forbidden(접근 거부)
					     즉, @PreAuthorize 은 서버에서 메서드 단위로 문지기 역할을 하는 어노테이션이다.
					
					     ※ 왜 좋을까?
					      - Thymeleaf 나 Javascript 에서 메뉴를 숨긴다고 해도 사실 그냥 "보이는 것만" 막는 것이다. 주소만 치면 들어올 수도 있기 때문이다.
					        하지만 @PreAuthorize는 서버 레벨에서 진짜로 차단시켜준다. 그래서 보안적으로 완전 중요한것이다.
					       
					      ▶ 여러 권한 허용        => @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
					      ▶ 로그인 된 사용자만 허용 => @PreAuthorize("isAuthenticated()")
					     
					      ※ 서비스단 에서도 사용 가능하다. 
					      @Service
				          public class AdminService {
					
					          @PreAuthorize("hasRole('ROLE_ADMIN')")
					         public void deleteUser(Long id) {
					          // ...
					          }
					       }
				  	
						이렇게 "컨트롤러가 아니라 서비스 메서드까지 보안 걸기" 도 가능해서 정말 많이 사용된다.
						*/
public class SecurityConfig {
	
	/*
    비밀번호 암호화를 위해서 Bcrypt 라는 알고리즘을 사용한다. 
    Bcrypt 알고리즘은 내부에서 랜덤으로 salt(암호화키)를 생성하여 이를 평문에 붙이고 여러번 해싱을 해주는 기능으로, spring-security에서 제공해주고 있다. 
    비밀번호 암호화를 위해 BCryptPasswordEncodr의 객체를 이용하여 비밀번호를 암호화하여 DB에 저장하도록 한다.
    이를 위해 BCryptPasswordEncoder 를 빈으로 등록하여 사용한다.
    */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    
    
    
 // 사용자 로그인 인증을 처리하기 위한 AuthenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(
            CustomUserDetailsService_kth customUserDetailsService) {

        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(customUserDetailsService);

        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authenticationProvider);

    }


    // JWT 필터가 서블릿 필터로 중복 등록되는 것을 방지하기
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter_kth> jwtFilterRegistration(
            JwtAuthenticationFilter_kth jwtAuthenticationFilter) {

        FilterRegistrationBean<JwtAuthenticationFilter_kth> registration =
                new FilterRegistrationBean<>(jwtAuthenticationFilter);

        registration.setEnabled(false);

        return registration;

    }
    
    
    
    
    
    
    
    
    
    
    
    
    
 // == 인증 실패시 401 에러 관련 예외처리(유효한 자격증명을 제공하지 않고 접근하려 할때 발생하는 에러이며 에러번호가 401임) == //  
    @Bean
    AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authenticationEntryPointException) -> {
            response.sendRedirect(request.getContextPath()+"/security/noAuthenticated");
            // 유효한 자격증명을 제공하지 않고 접근하려 할때 이동할 주소 지정 
        };
    }
    
    
    // == 권한 실패시 403 에러 관련 예외처리(접근권한이 필요한 페이지에 접근권한이 없는 유저가 접속할 경우에 발생하는 에러이며 에러번호가 403임) == //  
    @Bean
    AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect(request.getContextPath()+"/security/noAuthorized");
            // 접근권한이 필요한 페이지에 접근권한이 없는 유저가 접속할 경우 이동할 주소 지정 
        };
    }
    
    
    // SecurityFilterChain은 Spring Security에서 HTTP 요청을 처리하는 보안 필터들의 체인(순서 있는 목록)을 정의하는 인터페이스이다. 
    // 간단히 말해, 어떤 요청이 들어왔을 때 어떤 보안 필터들이 적용될지를 결정하는 역할을 하는 것이다.
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            JwtAuthenticationFilter_kth jwtAuthenticationFilter) throws Exception {
    	/*
        CORS란?
         - HTTP 요청은 기본적으로 Cross-Site HTTP Requests 가 사용가능하다. 
           간단히 말하면, 다른 도메인의 Resource를 사용하는것이 가능하다는 말이다. 
           하지만 Cross-Site HTTP Requests는 Same Origin Policy를 적용 받기 때문에 요청이 불가하다. 
           즉 프로토콜, 호스트명, 포트가 같아야만 요청이 가능하다.
   
         >> same-origin <<   
         http://domainA/page1.html  --->  http://domainA/page2.html 
          
         >> cross-origin <<   
         http://domainA/page1.html  --->  http://domainB/page.html   
         */

        
     /*
        ==== HttpSecurity 클래스(스프링시큐리티의 거의 대부분설정을 담당하는 클래스)의 메서드 설명 ====
        ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
             메서드                                       설명
        ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------  
         authorizeHttpRequests()                   권한설정하기 위한 기능을 가진 상위 메서드 
            
         requestMatchers()                           특정 리소스에 대한 권한 설정하기 위해 사용
         anyRequest()                               requestMatchers() 로 지정된 리소스를 제외한 모든 리소스들을 묶어서 표현
         authenticated()                            인증을 완료해야 접근이 가능함 
         hasRole("롤명")                               antMatchers 뒤에 붙으며, 권한의 이름을 적어 그 권한을 가진 사용자만 접근을 허용
         hasAnyRole("롤명1","롤명2","롤명3",롤명여러개)   antMatchers 뒤에 붙으며, 나열된 권한의 이름을 적어 나열된 권한 중 하나라도 가진 사용자만 접근을 허용
         permitAll()                               인증절차없이 허용해줌을 의미
         formLogin()                               사용자 정의 로그인 기능과 로그아웃 기능을 가진 상위 메서드
         loginPage()                               사용자가 따로 만든 로그인 페이지를 사용하려고 할때 설정한다. 만약에 따로 설정하지 않으면 디폴트 URL이 "/login" 이기 때문에 "/login" 으로 호출하면 스프링이 제공하는 기본 로그인페이지가 호출된다.  
         loginProcessingUrl()                       해당 주소에서 로그인 프로세스를 진행해줌.
                                                    로그인 form 에서 아이디와 비번을 입력하고 확인을 클릭하면 "/auth/login" 를 호출 하게 되었들 때 인증처리하는 필터가 호출되어 아이디와 비번을 받아와 인증처리가 수행된다. 즉 인증을 처리하는 기본필터 UsernamePasswordAuthenticationFilter 가 실행 되는 것이다.   
         defaultSuccessUrl()                       인증이 성공했을 때, 리다이렉션할 페이지(이동하는 페이지)를 설정한다. 만약에 설정하지 않는경우 디폴트값은 "/" 이다. 
         successHandler()                           인증이 성공했을 때, 그 이후 별도의 처리가 필요한 경우 커스텀 핸들러를 생성하여 등록할 수 있다. 
                                                    커스텀 핸들러를 생성하여 등록하면 인증성공 후 사용자가 추가한 로직을 수행하고 성공 페이지("/main")로 이동한다.  
                                                        
         failureUrl()                                인증이 실패 했을 경우 이동하는 페이지를 설정한다
         failureHandler()                            인증 실패 후, 별도의 처리가 필요한경우 커스텀 핸들러를 생성하여 등록할 수 있다.
                                                        커스텀 핸들러를 생성하여 등록하면 인증실패 후 사용자가 추가한 로직을 수행하고 실패 페이지("/login-fail")로 이동한다.
     */                                                      
     
       //   기본적으로 Spring security 에서는 아무 설정도 하지 않으면 CSRF(Cross-Site Request Forgery 데이터 위변조 방지)가 활성화된다. 
      //   CSRF 가 활성화된 상태에서 POST, PUT, DELETE 전송 방식에서는 서버에서 생성해준 csrf 토큰값(인증키라고 생각하자)을 다시 서버로 보내주어야만 성공되어진다.    
      // 아래는 데이터 위변조 방지에 대한 CSRF 의 사용유무를 체크하는 것으로, 이 기능을 사용하지 않으려면 아래와 같이 하면 된다.   
      
        http
            .csrf((csrfConfig) -> csrfConfig
                 .disable()
            );
      
    /*
        http
              .csrf((csrfConfig) -> csrfConfig
                     
                 //  POST, DELETE, PUT 매핑 중 하나를 사용하는 urlA 와 urlB 는 CSRF 적용에서 제외시키기     
                 // .ignoringRequestMatchers("/urlA", "/urlB")
                 
                 
                    .ignoringRequestMatchers("/test/**",
                                             "/testRestApi/**",
                                             "/board/image/multiplePhotoUpload")
                    // 연습용 요청 URL은 CSRF 적용에서 제외시키기
                    // /test/** 의 뜻은 /test/ 하위의 모든 깊이(다계층)의 경로를 매칭한다.
                    // 매칭 가능 예시: /test, 
                    //              /test/, 
                    //              /test/abc, 
                    //              /test/a/b/c 모두 예외 처리함
                    
                    // 스마트에디터의 사진 업로드 요청 URL은 CSRF 적용에서 제외시키기
                    // 피해자 계정의 정보가 수정되거나, 결제가 일어나는 등의 직접적인 피해가 발생하지 않는 이미지 파일 업로드는 해커에게는 공격할 가치가 없으므로 CSRF 적용에서 제외시켜도 보안상 문제가 되지 않는다.  
          );
        */
    	
        // Spring security 를 사용하면 기본적으로 CSRF 가 사용하는 것으로 설정되기 때문에, 
        // 모든 POST, PUT, DELETE 전송 방식의 데이터 전송에 있어서 토큰 값이 있어야 한다. 
        // 만약 POST, PUT, DELETE 전송 방식에서 토큰 값이 없는 경우라면, 
        // Spring Security 는 서버를 보호하기 위해 요청을 거부하고 403 Forbidden (권한 없음) 에러를 발생시킨다.
        /*
             CSRF (Cross-Site Request Forgery) 란? 
               사이트 간 요청 위조(또는 크로스 사이트 요청 위조, 영어: Cross-Site Request Forgery, CSRF, XSRF)는 
               웹사이트 취약점 공격의 하나로, 사용자가 자신의 의지와는 무관하게 공격자가 의도한 행위(수정, 삭제, 등록 등)를 특정 웹사이트에 요청하게 하는 공격을 말한다.
               즉, 자신도 모르게 해킹공격코드가 설치된 사용자는 원본사이트에 로그인하여 글쓰기를 할 때, 해킹코드로 인하여 원본사이트와 동일하게 만든 피싱사이트(URL 주소가 거의 비슷하고, 내용물은 동일한 사이트)에 접속하게 되어  
               글내용을 전송할 경우 실제 전송되는 내용은 hidden 타입으로 숨겨진 데이터들이 피싱사이트로 전송되도록 하는 공격을 말한다.
               유명 경매 사이트인 옥션에서 발생한 개인정보 유출 사건이 바로 이러한 피싱사이트를 통해서 개인정보를 유출해간 것이다.
                 
             - CSRF 가 사용하는 것으로 설정된 상태에서 CSRF 토큰을 설정해주지 않으면 
               JSP 나 Thymeleaf 에서 보내는 POST 요청을 모두 막기 때문에 로그인, 회원가입 기능이 작동하지 않아 인증 과정을 진행할 수 없게 된다.
             - 이를 위해 JSP 나 Thymeleaf 에서 POST 요청을 보낼 때 CSRF 토큰에 값을 넣어 함께 보내고, 
               Spring Security가 토큰 값을 확인하여 자신이 내려준 값이 맞는지 확인하는 방식으로 CSRF 공격을 판단한다.  

             아래가 CSRF Token 에 값을 넣어주는 코드이다.  
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />  
              - POST 요청을 보내도 Token에 담긴 값으로 CSRF 공격이 아니라고 판단하여 POST 요청을 막지 않고 허용해준다.  
                 
             또는 번거롭게 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 을 넣을 필요없이 
             대신에 form 태그속에 security 태그인 <s:csrfInput/> 을 넣어주면 된다.  
         
             Spring Security 에서는 기본적으로 CSRF 공격을 방어해 주는데,
             GET 방식은 허용해주고 POST, PUT, DELETE 전송 방식에 대해서는 방어해서 막아버린다.
             이와같이 CSRF 토큰 필터는 GET 방식의 데이터 전송에는 관여하지 않고, 오로지 POST, PUT, DELETE 전송 방식의 데이터 전송에만 관여한다.
             GET 방식의 데이터 전송에 대해서 CSRF 토큰 필터를 사용하지 않는 이유는 
             만약에 GET 방식까지 막아버리면 다른 사이트에서 링크를 타고 들어오는 요청이나, RESTful API 등을 처리할 수가 없게 되기 때문이다.                    
         
             참조사이트 : https://velog.io/@wooryung/Spring-Boot-Spring-Security%EB%A5%BC-%EC%A0%81%EC%9A%A9%ED%95%98%EC%97%AC-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0
                       https://codevang.tistory.com/282
         */
        
        String[] excludeUri = {"/test/**",
                                "/security/everybody",
                                "/chef/chef_list",
                             };
    	
        http
           .authorizeHttpRequests((auth) -> auth
//           .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR).permitAll()  
        		   // DispatcherType 을 import 시 jakarta.servlet.DispatcherType 으로 함. 
        		   // - DispatcherType.FORWARD 은 Spring MVC에서 뷰 템플릿(JSP, Thymeleaf 등)으로 포워딩하는 경우 허용하는 것임.

		   
        		   // - DispatcherType.INCLUDE 은 내부 include(forward) 는 모두 허용하는 것임. 예를들어, /WEB-INF/views/mycontent1/main/index.jsp 파일 속에 <jsp:include page="../../header/header1.jsp" /> 을 허용한다는 것임.  
        		   // JSP 를 사용하는 경우라면 DispatcherType.INCLUDE 을 반드시 넣어주어야 한다.
  
        		   // - DispatcherType.ERROR 은 Spring Security 6부터는 에러 페이지로 리다이렉트되는 내부 요청(ERROR 타입)에 대해서도 보안 필터가 기본 적용될 수 있으므로 내부 요청(ERROR 타입)에 대해서는 모두 허용을 하는 것이다.
        		   
        		   .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
        		   // DispatcherType 을 import 시 jakarta.servlet.DispatcherType 으로 함. 
        		   // - DispatcherType.FORWARD 은 Spring MVC에서 뷰 템플릿(JSP, Thymeleaf 등)으로 포워딩하는 경우 허용하는 것임.
	     
        		   // Thymeleaf 를 사용하는 경우라면 DispatcherType.INCLUDE 을 넣어줄 필요가 없다. 
        		   // 왜냐하면 Thymeleaf는 레이아웃을 조립할 때 th:replace나 th:insert를 사용한다. 
        		   // 이것은 서블릿 컨테이너에 다시 요청을 보내는 것이 아니라, Thymeleaf 엔진 내부에서 파일을 직접 읽어와 메모리 상에서 합치는 방식이기 때문에 DispatcherType.INCLUDE 을 넣어줄 필요가 없다. 
	     
        		   // - DispatcherType.ERROR 은 Spring Security 6부터는 에러 페이지로 리다이렉트되는 내부 요청(ERROR 타입)에 대해서도 보안 필터가 기본 적용될 수 있으므로 내부 요청(ERROR 타입)에 대해서는 모두 허용을 하는 것이다.
        		  
        		   .requestMatchers(excludeUri).permitAll()
		   
        		   // excludeUri 에 있는 경로는 전부 허락
//   		       .requestMatchers("/bootstrap-5.3.8-dist/**", 
        		   //                           "/css/**", 
        		   //                           "/fullcalendar_5.10.1/**",
        		   //                           "/Highcharts-10.3.1/**",
        		   //                           "/images/**", 
        		   //                           "/js/**", 
        		   //                           "/smarteditor/**").permitAll() // 정적 리소스 허용하기.  
        		   //  permitAll() 은 "필터는 타지만 인증 검사만 안 한다"는 의미이고, ignoring() 은 "아예 필터를 타지 않는다"는 의미이다. 
        		   //  그래서 정적 리소스 허용은 permitAll() 도 가능하지만 우리는 아래에 ignoring() 을 사용하였다. 
        		   
        		   // 관리자 전용 페이지 및 요청 접근 제한
        		   .requestMatchers("/km/admin/**","/admin/**").hasRole("ADMIN")
        		   .anyRequest().permitAll()
 
			);
        
        http
	        .exceptionHandling((exceptionConfig) -> exceptionConfig
	              .authenticationEntryPoint(customAuthenticationEntryPoint())
	              // 인증 실패시 401 에러 관련 예외처리(유효한 자격증명을 제공하지 않고 접근하려 할때 발생하는 에러이며 에러번호가 401임)
	              // customAuthenticationEntryPoint() 은 맨 위에 설정해 두었음.
	              
	              .accessDeniedHandler(customAccessDeniedHandler())
	              // 권한 실패시 403 에러 관련 예외처리(접근권한이 필요한 페이지에 접근권한이 없는 유저가 접속할 경우에 발생하는 에러이며 에러번호가 403임) 
	               // customAccessDeniedHandler() 은 맨 위에 설정해 두었음.
            );
        
        
        http
	        .headers((headerConfig) -> headerConfig
	                 .frameOptions((frameOptionsConfig) -> frameOptionsConfig
	                       .sameOrigin())
	        ); // 동일한 사이트 주소를 가지는 src 의 iframe 만 동작하도록 허용해주는 것이다.
        
        
        
     // JWT와 세션을 함께 사용하기 위한 설정
        http
            .sessionManagement((sessionConfig) -> sessionConfig
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );

        // JWT 인증 필터 등록하기
        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );  
        
        
        
        
        
		return http.build();    // 메서드로 빈을 생성하는 것이므로 return 해줘야 한다.
    }
    
    
    
    // 정적 리소스 허용하기
    // permitAll() 은 "필터는 타지만 인증 검사만 안 한다"는 의미이고, ignoring() 은 "아예 필터를 타지 않는다"는 의미이다. 
    // 그래서 정적 리소스 허용은 permitAll() 도 가능하지만 우리는 ignoring() 을 사용하였다. 
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/bootstrap-5.3.8-dist/**", 
                                 "/css/**", 
                                 "/fullcalendar_5.10.1/**",
                                 "/Highcharts-10.3.1/**",
                                 "/images/**", 
                                 "/js/**", 
                                 "/smarteditor/**");  
    }
    
    

    
}
