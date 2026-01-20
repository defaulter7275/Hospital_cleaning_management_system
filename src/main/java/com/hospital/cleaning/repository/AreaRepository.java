package com.hospital.cleaning.repository;

import com.hospital.cleaning.model.Area;
import com.hospital.cleaning.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    List<Area> findByFloor(Floor floor);
}
