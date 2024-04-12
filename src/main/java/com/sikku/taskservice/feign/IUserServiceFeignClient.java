package com.sikku.taskservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.sikku.taskservice.model.UserDTO;

@Service
@FeignClient(name = "USER-SERVICE")
public interface IUserServiceFeignClient {
	
	@GetMapping("/api/v1/user/profile")
	public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String jwtToken);

}
