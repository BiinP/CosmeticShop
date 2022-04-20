package com.asm.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asm.bean.Category;
import com.asm.bean.Order;
import com.asm.bean.OrderDetail;
import com.asm.bean.Product;
import com.asm.dao.OrderDetailRepo;
import com.asm.service.AccountService;
import com.asm.service.BrandService;
import com.asm.service.CategoryService;
import com.asm.service.OrderService;
import com.asm.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired BrandService bService;
	@Autowired CategoryService cService;
	@Autowired ProductService pService;
	@Autowired AccountService aService;
	@Autowired OrderService oService;
	
	@RequestMapping("/list")
	public String listOrder(Model model, Principal principal) {
		String username = principal.getName();
		List<Order> orders = oService.findByUsername(username);
//		model.addAttribute("orders", orders);

		List<Map<String, Object>> db = new ArrayList<Map<String,Object>>();
		for(Order order : orders ) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<OrderDetail> orderDetail = order.getOrderDetails();
			Double total = (double) 0;
			for(OrderDetail od : orderDetail) {
				total += od.getPrice() * od.getQuantity();
			}
			map.put("order", order);
			map.put("total", total);
			db.add(map);
		}
		model.addAttribute("orders", db);
		return "order/list";
	}
	@RequestMapping("/cart")
	public String cart() {
		return "order/cart";
	}
	@RequestMapping("/checkout")
	public String checkout(Model model, Principal principal) {
		String username = principal.getName();
		model.addAttribute("userDetail", aService.findByUsername(username));
		return "order/checkout";
	}
	@RequestMapping("/detail/{id}")
	public String detail(Model model, 
			Principal principal,
			@PathVariable("id") Long id) {
		Order order = oService.findById(id);
		String acc = principal.getName();
		if(!order.getAccount().getUsername().equals(acc)) {
			return "redirect:/security/unathorized";
		}else {
			model.addAttribute("order", order);
			List<OrderDetail> orderDetail = order.getOrderDetails();
			Double total = (double) 0;
			for(OrderDetail od : orderDetail) {
				total += od.getPrice() * od.getQuantity();
			}
			model.addAttribute("total", total);
			return "order/detail";
			
		}
	}
}
