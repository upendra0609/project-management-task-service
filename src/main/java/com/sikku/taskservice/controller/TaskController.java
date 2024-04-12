package com.sikku.taskservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sikku.taskservice.feign.IUserServiceFeignClient;
import com.sikku.taskservice.model.Task;
import com.sikku.taskservice.model.TaskStatus;
import com.sikku.taskservice.model.UserDTO;
import com.sikku.taskservice.services.ITaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

	private final ITaskService taskService;

	private final IUserServiceFeignClient userServiceFeignClient;

	@PostMapping("/create")
	public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String jwtToken)
			throws Exception {
		ResponseEntity<UserDTO> userProfile = userServiceFeignClient.getUserProfile(jwtToken);
		String role = userProfile.getBody().getRole().name();
		return ResponseEntity.ok(taskService.createTask(task, role));
	}

	@GetMapping("/{taskId}")
	public ResponseEntity<Task> getTaskById(@PathVariable Long taskId, @RequestHeader("Authorization") String jwtToken)
			throws Exception {
		return ResponseEntity.ok(taskService.getTaskById(taskId));
	}

	@GetMapping("/user")
	public ResponseEntity<List<Task>> getAssignedUserTasks(@RequestParam(required = false) TaskStatus status,
			@RequestHeader("Authorization") String jwtToken) throws Exception {
		ResponseEntity<UserDTO> userProfile = userServiceFeignClient.getUserProfile(jwtToken);
		return ResponseEntity.ok(taskService.assignedUserTasks(userProfile.getBody().getId(), status));
	}

	@GetMapping("/all")
	public ResponseEntity<List<Task>> getAllTasks(@RequestParam(required = false) TaskStatus status,
			@RequestHeader("Authorization") String jwtToken) throws Exception {
		return ResponseEntity.ok(taskService.getAllTasks());
	}

	@PostMapping("/{taskId}/user/{userId}")
	public ResponseEntity<Task> assignTaskToUser(@PathVariable Long taskId, @PathVariable Long userId,
			@RequestHeader("Authorization") String jwtToken) throws Exception {
		Task taskToUser = taskService.assignTaskToUser(taskId, userId);
		return ResponseEntity.ok(taskToUser);
	}

	// updateTask
	@PatchMapping("/{taskId}")
	public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task task,
			@RequestHeader("Authorization") String jwtToken) throws Exception {
		ResponseEntity<UserDTO> userProfile = userServiceFeignClient.getUserProfile(jwtToken);
		String role = userProfile.getBody().getRole().name();
		if (role.equalsIgnoreCase("ADMIN")) {
			return ResponseEntity.ok(taskService.updateTask(taskId, task, userProfile.getBody().getId()));
		}
		throw new RuntimeException("Only Admin can update task");
	}

	@PutMapping("/{taskId}/complete")
	public ResponseEntity<Task> completeTask(@PathVariable Long taskId, @RequestHeader("Authorization") String jwtToken)
			throws Exception {
		return ResponseEntity.ok(taskService.completeTask(taskId));
	}

	@DeleteMapping("/{taskId}/delete")
	public String deleteTask(@PathVariable Long taskId, @RequestHeader("Authorization") String jwtToken)
			throws Exception {
		taskService.deleteTask(taskId);
		return "Task deleted successfully having id: " + taskId;
	}

}
