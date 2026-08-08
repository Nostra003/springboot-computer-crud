package com.example.controlej2e.service;

import com.example.controlej2e.entities.Computer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComputerService {
    Computer addComputer(Computer pc);
    Page<Computer> getAllComputers(Pageable pageable);
    Computer getComputerById(Long id);
    Computer updateComputer(Long id, Computer pc);
    List<Computer> getComputersByPrice(double price);
    List<Computer> getComputersByProce(String proce);
    void deleteComputer(Long id);
}
