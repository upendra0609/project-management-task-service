package com.sikku.taskservice.services.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sikku.taskservice.model.Task;
import com.sikku.taskservice.model.TaskStatus;
import com.sikku.taskservice.repositories.ITaskRepository;
import com.sikku.taskservice.services.ITaskService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImp implements ITaskService {
	private final ITaskRepository taskRepository;

	@Override
	public Task createTask(Task task, String requesterRole) throws Exception {
		if (requesterRole.equals("ADMIN")) {
			task.setStatus(TaskStatus.PENDING);
			task.setCreatedAt(LocalDateTime.now());
			return taskRepository.save(task);
		}
		throw new Exception("Only Admin can create task");
	}

	@Override
	public Task getTaskById(Long id) throws Exception {
		return taskRepository.findById(id).orElseThrow(() -> new Exception("Task not found with id: " + id + ""));
	}

	@Override
	public List<Task> getAllTasks(TaskStatus status) {
		List<Task> allTask = taskRepository.findAll();
		if (status == null) {
			return allTask;
		}
		List<Task> list = allTask.stream().filter(task -> task.getStatus().name().equalsIgnoreCase(status.toString()))
				.toList();
		return list;
	}

	@Override
	public List<Task> assignedUserTasks(Long userId, TaskStatus status) throws Exception {
		List<Task> list = taskRepository.findByAssignedUserId(userId);
		List<TaskStatus> list1 = Arrays.asList(TaskStatus.values());
		if (status == null) {
			return list;
		}

		if (status != null && list1.contains(status)) {
			return list.stream().filter(task -> task.getStatus().name().equalsIgnoreCase(status.toString())).toList();
		}
		throw new Exception("Invalid status");
	}

	@Override
	public Task updateTask(Long id, Task task, Long UserId) throws Exception {
		Task taskById = getTaskById(id);
//		if (taskById.getAssignedUserId().equals(UserId)) {
		if (task.getTitle() != null) {
			taskById.setTitle(task.getTitle());
		}
		if (task.getDescription() != null) {
			taskById.setDescription(task.getDescription());
		}
		if (task.getDeadline() != null) {
			taskById.setDeadline(task.getDeadline());
		}
		if (task.getTags().size()!=0) {
			taskById.setTags(task.getTags());
		}
		return taskRepository.save(taskById);
//		}
//		throw new Exception("You are not authorized to update this task");
	}

	@Override
	public void deleteTask(Long id) throws Exception {
		Task taskById = getTaskById(id);
		taskRepository.deleteById(taskById.getId());
	}

	@Override
	public Task assignTaskToUser(Long taskId, Long userId) throws Exception {
		Task task = getTaskById(taskId);
		task.setAssignedUserId(userId);
		task.setStatus(TaskStatus.ASSIGNED);
		return taskRepository.save(task);
	}

	@Override
	public Task completeTask(Long taskId) throws Exception {
		Task task = getTaskById(taskId);
		task.setStatus(TaskStatus.DONE);
		return taskRepository.save(task);

	}

	@Override
	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

}
