package com.sikku.taskservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task")
public class HomeController {
	
	@GetMapping("/tasks")
	public String home() {
		return "Welcome to Task Service Application";
	}
}
