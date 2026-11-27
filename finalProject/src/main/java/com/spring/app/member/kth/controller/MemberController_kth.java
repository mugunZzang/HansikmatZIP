package com.spring.app.member.kth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.app.member.kth.domain.MemberDTO_kth;
import com.spring.app.member.kth.service.MemberService_kth;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletResponse;
import com.spring.app.member.kth.security.JwtToken_kth;

import org.springframework.web.bind.annotation.CookieValue;

@Controller
public class MemberController_kth {
	
	@Autowired
	private PasswordEncoder passwdEncoder; // com.spring.app.security.config.SecurityConfig 클래스에서 빈으로 생성해 두었다. 
	
	
	// 회원가입
	@GetMapping("/memberRegister")
	public String memberRegister(Authentication authentication) {

	    // 이미 로그인한 회원은 메인페이지로 이동
	    if(authentication != null && authentication.isAuthenticated()) {
	        return "redirect:/index";
	    }

	    return "member/kth/memberRegister";
	}
	
	
	// 로그인
	 //@GetMapping("/login")
	// public String login() {
	//		return "member/kth/login";
	//	}
		
		
		
		  @Autowired
		    private MemberService_kth service;


		    // 아이디 중복검사
		    @PostMapping("/member/useridCheck")
		    @ResponseBody
		    public Map<String, Boolean> useridCheck(@RequestParam("userid") String userid) {

		        Map<String, Boolean> map = new HashMap<>();

		        int n = service.idDuplicateCheck(userid);

		        if(n > 0) {
		            map.put("isExists", true);
		        }
		        else {
		            map.put("isExists", false);
		        }

		        return map;
		    }


		    // 닉네임 중복검사
		    @PostMapping("/member/nicknameCheck")
		    @ResponseBody
		    public Map<String, Boolean> nicknameCheck(@RequestParam("nickname") String nickname) {

		        Map<String, Boolean> map = new HashMap<>();

		        int n = service.nicknameDuplicateCheck(nickname);

		        if(n > 0) {
		            map.put("isExists", true);
		        }
		        else {
		            map.put("isExists", false);
		        }

		        return map;
		    }


		    // 이메일 중복검사
		    @PostMapping("/member/emailCheck")
		    @ResponseBody
		    public Map<String, Boolean> emailCheck(@RequestParam("email") String email) {

		        Map<String, Boolean> map = new HashMap<>();

		        int n = service.emailDuplicateCheck(email);

		        if(n > 0) {
		            map.put("isExists", true);
		        }
		        else {
		            map.put("isExists", false);
		        }

		        return map;
		    }
		    
		    
		    
		 // 이메일 인증번호 발송
		    @PostMapping("/member/sendEmail")
		    @ResponseBody
		    public Map<String, Boolean> sendEmail(@RequestParam("email") String email,
		                                          HttpSession session) {

		        Map<String, Boolean> map = new HashMap<>();

		        try {
		            // 기존 이메일 인증 정보 초기화
		            session.removeAttribute("emailVerified");
		            session.removeAttribute("verifiedEmail");
		            session.removeAttribute("certificationCode");
		            session.removeAttribute("certificationTime");
		            session.removeAttribute("certificationEmail");

		            // 인증번호 생성 및 이메일 발송
		            String certificationCode = service.sendEmail(email);

		            // 세션에 인증 정보 저장
		            session.setAttribute("certificationCode", certificationCode);
		            session.setAttribute("certificationEmail", email);
		            session.setAttribute("certificationTime", System.currentTimeMillis());
		            session.setAttribute("certificationAttempts", 0);

		            map.put("success", true);
		        }
		        catch(Exception e) {
		            e.printStackTrace();
		            map.put("success", false);
		        }

		        return map;
		    }
		    
		    
		    
		    
		 // 이메일 인증번호 확인
		    @PostMapping("/member/verifyEmail")
		    @ResponseBody
		    public Map<String, Boolean> verifyEmail(
		            @RequestParam("email") String email,
		            @RequestParam("certificationCode") String certificationCode,
		            HttpSession session) {

		        Map<String, Boolean> map = new HashMap<>();

		        String code = (String) session.getAttribute("certificationCode");
		        String savedEmail = (String) session.getAttribute("certificationEmail");
		        Long time = (Long) session.getAttribute("certificationTime");
		        Integer attempts = (Integer) session.getAttribute("certificationAttempts");

		        // 인증번호 발송 여부 확인
		        if(code == null || time == null || attempts == null) {
		            map.put("success", false);
		        }

		        // 인증 유효시간 5분 확인
		        else if(System.currentTimeMillis() - time > 5 * 60 * 1000) {
		            map.put("success", false);
		        }

		        // 인증 시도 횟수 확인
		        else if(attempts >= 5) {
		            map.put("success", false);
		        }

		        // 이메일 및 인증번호 일치 여부 확인
		        else if(code.equals(certificationCode) && email.equals(savedEmail)) {

		            session.setAttribute("emailVerified", true);
		            session.setAttribute("verifiedEmail", email);

		            session.removeAttribute("certificationCode");
		            session.removeAttribute("certificationTime");

		            map.put("success", true);
		        }
		        else {
		            session.setAttribute("certificationAttempts", attempts + 1);
		            map.put("success", false);
		        }

		        return map;
		    }
		    

			 // 회원가입 DB에 insert 하기
		    @PostMapping("/memberRegisterEnd")
		    @ResponseBody
		    public Map<String, Boolean> memberRegisterEnd(
		            MemberDTO_kth mbrdto, HttpSession session) {

		        Map<String, Boolean> map = new HashMap<>();

		        try {
		            // 이메일 인증 여부 확인
		            Boolean emailVerified =
		                (Boolean) session.getAttribute("emailVerified");

		            String verifiedEmail =
		                (String) session.getAttribute("verifiedEmail");

		            if(!Boolean.TRUE.equals(emailVerified)
		                    || !mbrdto.getEmail().equals(verifiedEmail)) {

		                map.put("success", false);
		                return map;
		            }

		            // 아이디, 닉네임, 이메일 중복 재검사
		            if(service.idDuplicateCheck(mbrdto.getUserid()) > 0
		                    || service.nicknameDuplicateCheck(mbrdto.getNickname()) > 0
		                    || service.emailDuplicateCheck(mbrdto.getEmail()) > 0) {

		                map.put("success", false);
		                return map;
		            }

		            // 사용자 비밀번호 암호화 하기
		            String hashedUserPwd =
		                passwdEncoder.encode(mbrdto.getPasswd());

		            mbrdto.setPasswd(hashedUserPwd);

		            // 회원정보 및 권한 저장
		            int n = service.insertMember(mbrdto);

		            if(n == 1) {
		                session.removeAttribute("emailVerified");
		                session.removeAttribute("verifiedEmail");

		                map.put("success", true);
		            }
		            else {
		                map.put("success", false);
		            }
		        }
		        catch(Exception e) {
		            e.printStackTrace();
		            map.put("success", false);
		        }

		        return map;
		    } 
				
		    
		 // 로그인 인증 form 페이지 보여주기
		    @GetMapping("/login")
		    public String login(HttpServletRequest request,
		                        Authentication authentication) {

		        // 이미 로그인한 회원은 메인페이지로 이동
		        if(authentication != null && authentication.isAuthenticated()) {
		            return "redirect:/index";
		        }

		        // 로그인 이전 페이지 주소 가져오기
		        String referer = request.getHeader("referer");

		        String contextPath = request.getContextPath();

		        String baseURL = request.getScheme() + "://"
		                       + request.getServerName() + ":"
		                       + request.getServerPort();

		        if(referer == null ||
		           !referer.startsWith(baseURL + contextPath + "/") ||
		           referer.endsWith("/login") ||
		           referer.contains("/login?") ||
		           referer.endsWith("/memberRegister") ||
		           referer.contains("/memberRegister?") ||
		           referer.endsWith("/security/noAuthenticated")) {

		            referer = contextPath + "/index";

		        }

		        // 로그인 이전 페이지 주소를 세션에 저장하기
		        HttpSession session = request.getSession();
		        session.setAttribute("prevURLPage", referer);

		        return "member/kth/login";

		    }


		 // JWT 로그인 인증하기
		    @PostMapping("/auth/login")
		    @ResponseBody
		    public ResponseEntity<?> loginEnd(
		            @RequestBody MemberDTO_kth mbrdto,
		            HttpServletRequest request,
		            HttpServletResponse response,
		            HttpSession session) {

		        try {

		            // 로그인 인증 및 JWT 발급
		            JwtToken_kth jwtToken =
		                    service.login(mbrdto.getUserid(),
		                                  mbrdto.getPasswd(),
		                                  request, response);

		            // 로그인 이전 페이지 주소 가져오기
		            String prevURLPage =
		                    (String) session.getAttribute("prevURLPage");

		            if(prevURLPage == null) {
		                prevURLPage = request.getContextPath() + "/index";
		            }

		            session.removeAttribute("prevURLPage");

		            // JWT 및 이전 페이지 주소 반환하기
		            Map<String, Object> map = new HashMap<>();

		            map.put("grantType", jwtToken.getGrantType());
		            map.put("accessToken", jwtToken.getAccessToken());
		            map.put("accessTokenExpiresIn", jwtToken.getAccessTokenExpiresIn());
		            //map.put("refreshToken", jwtToken.getRefreshToken());
		            map.put("prevURLPage", prevURLPage);

		            return ResponseEntity.ok(map);

		        }
		        catch(AuthenticationException e) {

		            Map<String, String> map = new HashMap<>();

		            map.put("message", "아이디 또는 비밀번호가 일치하지 않습니다.");

		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);

		        }

		    }
		    
		 // JWT 토큰 재발급하기
		    @PostMapping("/auth/reissue")
		    @ResponseBody
		    public ResponseEntity<?> reissue(
		            @CookieValue(value = "refreshToken", required = false)
		            String refreshToken,
		            HttpServletRequest request,
		            HttpServletResponse response) {

		        try {

		            // JWT 토큰 재발급하기
		            JwtToken_kth jwtToken =
		                    service.reissue(refreshToken, request, response);

		            // 재발급된 Access Token 정보 반환하기
		            Map<String, Object> map = new HashMap<>();

		            map.put("grantType", jwtToken.getGrantType());
		            map.put("accessToken", jwtToken.getAccessToken());
		            map.put("accessTokenExpiresIn",
		                    jwtToken.getAccessTokenExpiresIn());

		            return ResponseEntity.ok(map);

		        }
		        catch(AuthenticationException e) {

		            Map<String, String> map = new HashMap<>();

		            map.put("message", "로그인 인증이 만료되었습니다.");

		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);

		        }
		    }
		    
		    
		   // JWT 로그아웃하기
		    @PostMapping("/auth/logout")
		    @ResponseBody
		    public ResponseEntity<?> logout(
		            @CookieValue(value = "refreshToken", required = false)
		            String refreshToken,
		            HttpServletRequest request,
		            HttpServletResponse response) {

		        // 로그아웃 처리하기
		        service.logout(refreshToken, request, response);

		        Map<String, Boolean> map = new HashMap<>();

		        map.put("success", true);

		        return ResponseEntity.ok(map);
		        
		        

		    }
		    
		 // 로그인이 필요한 페이지에 접근한 경우
		    @GetMapping("/security/noAuthenticated")
		    public String noAuthenticated(Model model) {

		        model.addAttribute("message", "로그인이 필요한 서비스입니다.");
		        model.addAttribute("loc", "/login");

		        return "member/kth/securityMessage";
		    }


		    // 접근 권한이 없는 페이지에 접근한 경우
		    @GetMapping("/security/noAuthorized")
		    public String noAuthorized(Model model) {

		        model.addAttribute("message", "접근 권한이 없습니다.");
		        model.addAttribute("loc", "/index");

		        return "member/kth/securityMessage";
		    }
			
}
