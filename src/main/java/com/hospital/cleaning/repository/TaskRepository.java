package com.hospital.cleaning.repository;

import com.hospital.cleaning.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByCompleted(boolean completed);
}
