
package com.spring.app.index.kth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/index")
    public String main() {
    	return "fragment/kth/index";
    }
}
