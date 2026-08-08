package com.example.controlej2e.service;

import com.example.controlej2e.dao.ComputerRepository;
import com.example.controlej2e.entities.Computer;
import com.example.controlej2e.exception.ComputerNotFoundException;
import com.example.controlej2e.exception.DuplicateMacAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComputerManager implements ComputerService {

    @Autowired
    private ComputerRepository computerRepository;

    @Override
    public Computer addComputer(Computer pc) {
        if (computerRepository.existsByMacAddress(pc.getMacAddress())) {
            throw new DuplicateMacAddressException(pc.getMacAddress());
        }
        return computerRepository.save(pc);
    }

    @Override
    public Page<Computer> getAllComputers(Pageable pageable) {
        return computerRepository.findAll(pageable);
    }

    @Override
    public Computer getComputerById(Long id) {
        return computerRepository.findById(id)
                .orElseThrow(() -> new ComputerNotFoundException(id));
    }

    @Override
    public Computer updateComputer(Long id, Computer pc) {
        Computer existing = getComputerById(id);

        // Si l'adresse MAC change, elle ne doit pas entrer en conflit avec un autre PC
        if (!existing.getMacAddress().equals(pc.getMacAddress())
                && computerRepository.existsByMacAddress(pc.getMacAddress())) {
            throw new DuplicateMacAddressException(pc.getMacAddress());
        }

        existing.setProce(pc.getProce());
        existing.setRam(pc.getRam());
        existing.setHardDrive(pc.getHardDrive());
        existing.setPrice(pc.getPrice());
        existing.setMacAddress(pc.getMacAddress());

        return computerRepository.save(existing);
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
        if (!computerRepository.existsById(id)) {
            throw new ComputerNotFoundException(id);
        }
        computerRepository.deleteById(id);
    }
}
