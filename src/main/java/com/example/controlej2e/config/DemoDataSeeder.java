package com.example.controlej2e.config;

import com.example.controlej2e.dao.ComputerRepository;
import com.example.controlej2e.entities.Computer;
import com.example.controlej2e.service.ComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Insère quelques ordinateurs de démo au démarrage.
 * Volontairement séparé de la classe principale @SpringBootApplication :
 * un CommandLineRunner posé directement sur celle-ci serait instancié
 * (avec ses dépendances) même dans les tests slicés (@WebMvcTest, etc.),
 * ce qu'un simple @Component évite puisqu'il est alors filtré.
 */
@Component
public class DemoDataSeeder implements CommandLineRunner {

    @Autowired
    private ComputerService computerService;

    @Autowired
    private ComputerRepository computerRepository;

    @Override
    public void run(String... args) {
        // Idempotent : ne réinsère pas les données de démo si la base
        // (Postgres persistant, contrairement à H2 en mémoire) en contient déjà.
        if (computerRepository.count() > 0) {
            return;
        }
        computerService.addComputer(new Computer(null, "i5", 8, 500, 6000, "00-AA-BB-CC-DD"));
        computerService.addComputer(new Computer(null, "i7", 16, 1000, 9500, "11-AA-BB-CC-DD"));
        computerService.addComputer(new Computer(null, "i3", 4, 256, 4000, "22-AA-BB-CC-DD"));
        computerService.addComputer(new Computer(null, "Ryzen5", 16, 512, 7500, "33-AA-BB-CC-DD"));
    }
}
