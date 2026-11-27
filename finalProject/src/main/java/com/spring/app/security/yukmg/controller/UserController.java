package com.spring.app.security.yukmg.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.app.security.yukmg.domain.UserDTO;
import com.spring.app.security.yukmg.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/")
public class UserController {

	private final UserService userservice;
	
	// 누구나 접근 가능한 URL
    // 조건이 없으므로, 로그인을 하지 않은 상태이거나 또는 로그인을 성공한 상태 모두 메뉴가 보여지는 것이다 
    @GetMapping(value="everybody")
    public String everybody(){
       
       return "security/yukmg/everybody";
    }
	    
    // 누구나 접근 가능한 URL. 쉐프 - 랭킹
    @GetMapping(value="chef/chef_list")
    public String chefRanking(@RequestParam(name="tab", defaultValue="ranking") String tab,
                              @RequestParam(name="sortType", defaultValue="follower") String sortType,
                              @RequestParam(name="currentShowPageNo", defaultValue="1") int currentShowPageNo,
                              Model model) {
        
        int sizePerPage = 20; // 한 페이지당 20명 노출
        int totalCount = userservice.getTotalChefCount(); // 총 셰프 수
        int totalPage = (int) Math.ceil((double) totalCount / sizePerPage); // 총 페이지 수
        
        // 잘못된 페이지 번호 방어 코드
        if (currentShowPageNo < 1) {
            currentShowPageNo = 1;
        } else if (totalPage > 0 && currentShowPageNo > totalPage) {
            currentShowPageNo = totalPage;
        }
        
        int startRno = ((currentShowPageNo - 1) * sizePerPage) + 1;
        int endRno = startRno + sizePerPage - 1;
        
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("sortType", sortType);
        paraMap.put("startRno", String.valueOf(startRno));
        paraMap.put("endRno", String.valueOf(endRno));
        
        List<UserDTO> userDTOList = userservice.getChefRanking(paraMap);
        
        // 페이지바(Pager) 시작/끝 번호 계산 (블럭당 10페이지 기준)
        int blockSize = 5;
        int loop = 1;
        int pageNo = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;
        
        int startPage = pageNo;
        int endPage = startPage + blockSize - 1;
        if (endPage > totalPage) {
            endPage = totalPage;
        }
        
        model.addAttribute("userDTOList", userDTOList);
        model.addAttribute("tab", tab);
        model.addAttribute("sortType", sortType);
        model.addAttribute("currentShowPageNo", currentShowPageNo);
        model.addAttribute("sizePerPage", sizePerPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        System.out.println("👉 totalCount: " + totalCount + ", totalPage: " + totalPage);
        return "chef/yukmg/chef_list";
        
    }
    
}
