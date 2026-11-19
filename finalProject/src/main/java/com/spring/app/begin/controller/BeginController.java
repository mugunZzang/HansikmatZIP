package com.spring.app.begin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.begin.model.BeginDAO;

import lombok.RequiredArgsConstructor;

/*
DispatcherServlet 이 저장된 장소는 
Project and External Dependencies > spring-webmvc-7.0.9.jar > org.springframework.web.servlet > DispatcherServlet.class 에 있음. 

사용자 웹브라우저 요청(View) <==> Application 클래스(MyspringbootApplication) ==> DispatcherServlet ==> @Controller 클래스 <==>> Service단(핵심업무로직단, business logic단) <==>> Model단[Repository](DAO, DTO) <==>> myBatis <==>> DB(오라클)           
(http://localhost:9080/myspringboot/...  )                                                     |                                                                                                                              
 ↑                                                                           View Resolver
 |                                                                                ↓
 |                                                                           View단(.html 또는 Bean명)
 -------------------------------------------------------------------------------------------------------| 

사용자(클라이언트)가 웹브라우저에서 http://localhost:9080/myspringboot/myspring_insert 를 실행하면
Application 클래스인 com.spring.app.MyspringbootApplication 이 작동된다.
MyspringbootApplication 은 bean 으로 등록된 객체중 controller 빈을 찾아서 URL값이 "/myspringboot/myspring_insert" 으로
매핑된 메소드를 실행시키게 된다.                                               
Service(서비스)단 객체를 업무 로직단(비지니스 로직단)이라고 부른다.
Service(서비스)단 객체가 하는 일은 Model단에서 작성된 데이터베이스 관련 여러 메소드들 중 관련있는것들만을 모아 모아서
하나의 트랜잭션 처리 작업이 이루어지도록 만들어주는 객체이다.
여기서 업무라는 것은 데이터베이스와 관련된 처리 업무를 말하는 것으로 Model 단에서 작성된 메소드를 말하는 것이다.
이 서비스 객체는 @Controller 단에서 넘겨받은 어떤 값을 가지고 Model 단에서 작성된 여러 메소드를 호출하여 실행되어지도록 해주는 것이다.
실행되어진 결과값을 @Controller 단으로 넘겨준다.
*/

//@Component
/* 클래스명 앞에 @Component 어노테이션을 적어주면 해당 클래스는 bean으로 자동 등록된다. 
그리고 bean의 이름은 해당 클래스명에서 첫글자만 소문자로 바뀐 이름으로 되어진다. 
즉, 여기서 bean 이름은 beginController 인 것이다.
여기서 @Controller 를 사용하므로 @Controller 에는 @Component 기능이 이미 포함되어 있으므로 @Component 를 명기하지 않아도 BeginController 은 bean 으로 등록되어 스프링컨테이너가 자동적으로 관리해준다.  
*/


//@Controller 안에 @Component 가 존재하기에 Component 는 굳이 안 써도 됨
//@Component         // Bean이 되어짐

@Controller
@RequestMapping(value="/test/")       // value 제거해도 상관 없음
@RequiredArgsConstructor
//@RequiredArgsConstructor 는 Lombok 라이브러리에서 제공하는 애너테이션으로,
//final 필드 또는 @NonNull이 붙은 필드에 대해 이 필드만 포함하는 생성자를 자동으로 생성해준다.
public class BeginController {
	
	
	   // === 의존객체 주입하기(DI : Dependency Injection) ===
	   
	   // ※ 의존객체주입(DI : Dependency Injection) 
	   //  ==> 스프링은 객체를 관리해주는 컨테이너를 제공해주고 있다.
	   //      스프링 컨테이너는 bean으로 등록되어진 BeginController 클래스 객체가 사용되어질때, 
	   //      BeginController 클래스의 인스턴스 객체변수(의존객체)인 BeginService service 에 
	   //      자동적으로 bean 으로 등록되어 생성되어진 BeginService service 객체를  
	   //      BeginController 클래스의 인스턴스 변수 객체로 사용되어지게끔 넣어주는 것을 의존객체주입(DI : Dependency Injection)이라고 부른다. 
	   //      이것이 바로 IoC(Inversion of Control == 제어의 역전) 인 것이다.
	   //      즉, 개발자가 인스턴스 변수 객체를 필요에 의해 생성해주던 것에서 탈피하여 스프링은 컨테이너에 객체를 담아 두고, 
	   //      필요할 때에 컨테이너로부터 객체를 가져와 사용할 수 있도록 하고 있다. 
	   //      스프링은 객체의 생성 및 생명주기를 관리할 수 있는 기능을 제공하고 있으므로, 더이상 개발자에 의해 객체를 생성 및 소멸하도록 하지 않고
	   //      객체 생성 및 관리를 스프링 프레임워크가 가지고 있는 객체 관리기능을 사용하므로 Inversion of Control == 제어의 역전 이라고 부른다.  
	   //      그래서 스프링 컨테이너를 IoC 컨테이너라고도 부른다.
	      
	   //  IOC(Inversion of Control) 란 ?
	   //  ==> 스프링은 사용하고자 하는 객체를 빈형태로 이미 만들어 두고서 컨테이너(Container)에 넣어둔후
	   //      필요한 객체사용시 컨테이너(Container)에서 꺼내어 사용하도록 되어있다.
	   //      이와 같이 객체 생성 및 소멸에 대한 제어권을 개발자가 하는것이 아니라 스프링 Container 가 하게됨으로써 
	   //      객체에 대한 제어역할이 개발자에게서 스프링 Container로 넘어가게 됨을 뜻하는 의미가 제어의 역전 
	   //      즉, IOC(Inversion of Control) 이라고 부른다.
	      
	      
	   //  === 느슨한 결합 ===
	   //      스프링 컨테이너가 BeginController 객체에서 BeginService 객체를 사용할 수 있도록 
	   //      만들어주는 것을 "느슨한 결합" 이라고 부른다.
	   //      느스한 결합은 BeginController 객체가 메모리에서 삭제되더라도 BeginService service 객체는 메모리에서 동시에 삭제되는 것이 아니라 남아 있다.
	      
	   // ===> 단단한 결합(개발자가 인스턴스 변수 객체를 필요에 의해서 생성해주던 것)
	   // private BeginService service = new BeginService_imple(); 
	   // ===> BeginController 객체가 메모리에서 삭제 되어지면  BeginService service 객체는 멤버변수(필드)이므로 메모리에서 자동적으로 삭제되어진다.
	   
	   // >>> 의존 객체 자동 주입(Automatic Dependency Injection)은
	   //     스프링 컨테이너가 자동적으로 의존 대상 객체를 찾아서 해당 객체에 필요한 의존객체를 주입하는 것을 말한다. 
	   //     단, 의존객체는 스프링 컨테이너속에 bean 으로 등록되어 있어야 한다. 
	         
	         
	   //  의존 객체 자동 주입(Automatic Dependency Injection) 방법
	      
	   //  >>> 1. 필드 주입 (Field Injection) 법 : 코드가 간단하고 빠르게 작성 가능하지만, 불변성 깨짐으로 인한 단점이 있어서 추천되지 않음.   
	   //  @Autowired ==> Spring 에서 지원하는 어노테이션이다. 
	   //                 스프링 컨테이너에 담겨진 의존객체를 주입할때 타입을 찾아서 연결(의존객체주입)한다.
       // 다른 곳에서 new를 통해 새로 만들 수 있다.
	   // 새로운 곳에서 new를 해서 생성하면 문제가 발생할 수 있어 권장하지 않음
/*	@Autowired // 필드에 Autowired 어노테이션을 달아줌으로써 스프링부트가 자동으로 DI(의존객체 주입)를 해준다.
	              // Type에 따라 알아서 Bean 을 주입해준다. 
	private BeginService service;
    
	   // Type 에 따라 Spring 컨테이너가 알아서 bean 으로 등록된 com.spring.app.begin.service.BeginService_imple 의 bean 을 dao 에 주입시켜준다. 
	   // 그러므로 service 는 null 이 아니다.
*/
	
	// >>> 2. Setter 주입 (Setter Injection) 법 - Setter 메서드를 통해 주입. 불변성 깨짐으로 인한 단점이 있어서 추천되지 않음.
/*
	private BeginService service;
	
	@Autowired		// 필드에 Autowired 어노테이션을 달아줌으로써 스프링부트가 자동으로 DI(의존객체 주입)를 해준다.
    				// Type에 따라 알아서 Bean 을 주입해준다. 
	public void setService(BeginService service) {
		this.service = service;
	}
*/
	// >>> 3. 생성자 주입 (Constructor Injection) 법 - Spring 에서 권장하는 방식 - 의존성을 생성자의 파라미터로 전달 받아 주입함.
	//                                           - final 키워드를 사용하여 불변 객체를 만들 수 있어 안정성이 높아, Spring에서 가장 권장하는 방식임.
/*
	private final BeginService service;
	
//	@Autowired   // 생성자에 Autowired 어노테이션을 달아줌으로써 스프링부트가 자동으로 DI(의존객체 주입)를 해준다.
				 // 생성자가 오로지 하나만 있을 때에만 Spring 이 알아서 DI(의존성 주입) 해주므로 @Autowired 생략가능함(Spring Framework 4.3 부터 지원해줌).
				 // Spring Boot 4.1.1 은 Spring Framework 7.0.9 버전이다.
    			 // 하지만 생성자가 복수개 이라면 Spring 이 어느 생성자에 DI(의존성 주입)를 해줄지 결정을 내리자 못하므로 반드시 @Autowired 를 적어야 함. 
	public BeginController(BeginService service) {
		super();
		this.service = service;
	}
*/
	
	private final BeginDAO dao;
	@GetMapping("test")
	public ModelAndView test(ModelAndView mav) {
		
		int n = dao.selectSome();
		
		mav.addObject("n", n);
		mav.setViewName("test");
		
		return mav;
	}
}
