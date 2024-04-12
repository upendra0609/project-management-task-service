package com.sikku.taskservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sikku.taskservice.model.Task;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Long>{	
	List<Task> findByAssignedUserId(Long userId);

}
