package com.asm.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asm.bean.Account;
import com.asm.bean.Category;
import com.asm.bean.Product;
import com.asm.bean.Role;
import com.asm.bean.RoleDetail;
import com.asm.service.AccountService;
import com.asm.service.BrandService;
import com.asm.service.CategoryService;
import com.asm.service.ProductService;
import com.asm.service.SessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class HomeController {
	@Autowired
	BrandService bService;
	@Autowired
	CategoryService cService;
	@Autowired
	ProductService pService;
	@Autowired
	SessionService session;
	@Autowired
	AccountService aService;

	@RequestMapping("/admin/index")
	public String admin() {
		return "redirect:/admin/assets/index.html";
	}

	@RequestMapping("/")
	public String home(Model model) {
		// load ds product xep theo ngay tao
		model.addAttribute("db", pService.findProductByCreateDateDESC());
		return "home/index";
	}

	@GetMapping("/brand/list")
	public String brandList(Model model) {
		return "brand/list";
	}

	@GetMapping("/register")
	public String register(Principal principal,
			@ModelAttribute Account account) {
		return "register";
	}
	@PostMapping("/register")
	public String signup(Model model,
			@ModelAttribute Account account) {
		if(aService.existsById(account.getUsername())) {
			model.addAttribute("error", "Đã tồn tại username "+account.getUsername());
			return "register";
		}else {
			account.setActivated(true);
			
			account.setPhoto("logo.jpg");
			
			Role r = new Role();
			r.setRole("user");
			RoleDetail rd = new RoleDetail();
			rd.setAccount(account);
			rd.setRole(r);
			
			aService.save(account);
			aService.saveRoleDetail(rd);
			return "redirect:/register/success";
		}
	}
	@RequestMapping("/register/success")
	public String registerSuccess(Model model) {
		model.addAttribute("message", "Đăng ký thành công");
		return "login";
	}

	@RequestMapping("/security/login/form")
	public String formLogin(Model model) {
		model.addAttribute("message", "Vui lòng đăng nhập");
		return "login";
	}

	@RequestMapping("/security/login/success")
	public String loginSuccess(Model model, Principal principal) {
		model.addAttribute("message", "Đăng nhập thành công");
		return "login";
	}

	@RequestMapping("/security/login/error")
	public String loginFail(Model model) {
		model.addAttribute("message", "Sai thông tin đăng nhập");
		return "login";
	}

	@RequestMapping("/security/unathorized")
	public String unauthorized(Model model) {
		model.addAttribute("message", "Không có quyền truy cập");
		return "login";
	}

	@RequestMapping("/security/logout/success")
	public String logoutSuccess(Model model) {
		model.addAttribute("message", "Đăng xuất thành công");
		return "login";
	}

}
