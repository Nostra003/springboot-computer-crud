package com.example.controlej2e.service;

import com.example.controlej2e.dao.ComputerRepository;
import com.example.controlej2e.entities.Computer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComputerManager implements ComputerService {

    @Autowired
    private ComputerRepository computerRepository;

    @Override
    public Computer addComputer(Computer pc) {
        if (computerRepository.existsByMacAddress(pc.getMacAddress())) {
            throw new RuntimeException("Adresse MAC déjà existante !");
        }
        return computerRepository.save(pc);
    }

    @Override
    public List<Computer> getComputersByPrice(double price) {
        return computerRepository.findByPriceLessThan(price);
    }

    @Override
    public List<Computer> getComputersByProce(String proce) {
        return computerRepository.findByProce(proce);
    }

    @Override
    public void deleteComputer(Long id) {
        computerRepository.deleteById(id);
    }
}
