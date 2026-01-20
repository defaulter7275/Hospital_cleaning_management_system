package com.hospital.cleaning.repository;

import com.hospital.cleaning.model.Checklist;
import com.hospital.cleaning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findByAssignedTo(User user);
    List<Checklist> findByStatus(Checklist.ChecklistStatus status);
    long countByStatus(Checklist.ChecklistStatus status);
}
