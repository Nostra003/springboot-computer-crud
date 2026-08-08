
package com.example.controlej2e;

import com.example.controlej2e.entities.Computer;
import com.example.controlej2e.service.ComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ControleJ2eApplication implements CommandLineRunner {

    @Autowired
    private ComputerService computerService;

    public static void main(String[] args) {
        SpringApplication.run(ControleJ2eApplication.class, args);
    }

    @Override
    public void run(String... args) {
        computerService.addComputer(new Computer(null, "i5", 8, 500, 6000, "00-AA-BB-CC-DD"));
        computerService.addComputer(new Computer(null, "i7", 16, 1000, 9500, "11-AA-BB-CC-DD"));
        computerService.addComputer(new Computer(null, "i3", 4, 256, 4000, "22-AA-BB-CC-DD"));
        computerService.addComputer(new Computer(null, "Ryzen5", 16, 512, 7500, "33-AA-BB-CC-DD"));
    }
}
