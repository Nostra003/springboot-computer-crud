package com.example.controlej2e.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload envoyé par le client pour créer ou mettre à jour un ordinateur.
 * Séparé de l'entité JPA pour ne jamais exposer le modèle de persistance
 * directement dans l'API, et pour pouvoir valider les entrées.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComputerRequest {

    @NotBlank(message = "Le processeur (proce) est obligatoire")
    private String proce;

    @Positive(message = "La RAM doit être un nombre positif (en Go)")
    private int ram;

    @Positive(message = "Le disque dur doit être un nombre positif (en Go)")
    private int hardDrive;

    @Positive(message = "Le prix doit être positif")
    private double price;

    @NotBlank(message = "L'adresse MAC est obligatoire")
    private String macAddress;
}
