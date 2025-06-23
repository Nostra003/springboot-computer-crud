package com.example.controlej2e.service;

import com.example.controlej2e.entities.Computer;

import java.util.List;

public interface ComputerService {
    Computer addComputer(Computer pc);
    List<Computer> getComputersByPrice(double price);
    List<Computer> getComputersByProce(String proce);
    void deleteComputer(Long id);
}

