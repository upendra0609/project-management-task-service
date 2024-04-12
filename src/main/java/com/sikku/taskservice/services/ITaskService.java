package com.sikku.taskservice.services;

import java.util.List;

import com.sikku.taskservice.model.Task;
import com.sikku.taskservice.model.TaskStatus;

public interface ITaskService {
	Task createTask(Task task, String requesterRole) throws Exception;

	Task getTaskById(Long id) throws Exception;

	List<Task> getAllTasks(TaskStatus status);
	List<Task> getAllTasks();

	List<Task> assignedUserTasks(Long userId, TaskStatus status) throws Exception;

	Task updateTask(Long id, Task task, Long UserId) throws Exception;

	void deleteTask(Long id) throws Exception;

	Task assignTaskToUser(Long taskId, Long userId) throws Exception;

	Task completeTask(Long taskId) throws Exception;
}
