package com.example.controlej2e.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représentation d'un ordinateur renvoyée par l'API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComputerResponse {
    private Long idPc;
    private String proce;
    private int ram;
    private int hardDrive;
    private double price;
    private String macAddress;
}
