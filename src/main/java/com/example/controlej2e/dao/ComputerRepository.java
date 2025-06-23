package com.example.controlej2e.dao;

import com.example.controlej2e.entities.Computer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComputerRepository extends JpaRepository<Computer, Long> {
    List<Computer> findByPriceLessThan(double price);
    List<Computer> findByProce(String proce);
    boolean existsByMacAddress(String macAddress);
}
