package com.hospital.cleaning.repository;

import com.hospital.cleaning.model.Notification;
import com.hospital.cleaning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserAndIsReadFalse(User user);
    long countByUserAndIsReadFalse(User user);
}
