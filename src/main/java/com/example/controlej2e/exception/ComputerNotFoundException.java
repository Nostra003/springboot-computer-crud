package com.example.controlej2e.exception;

public class ComputerNotFoundException extends RuntimeException {
    public ComputerNotFoundException(Long id) {
        super("Aucun ordinateur trouvé avec l'id " + id);
    }
}
