package com.example.controlej2e.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Computer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPc;

    private String proce;
    private int ram;
    private int hardDrive;
    private double price;

    @Column(unique = true)
    private String macAddress;

}
