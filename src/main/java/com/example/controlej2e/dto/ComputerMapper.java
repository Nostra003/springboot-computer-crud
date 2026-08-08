package com.example.controlej2e.dto;

import com.example.controlej2e.entities.Computer;

/**
 * Conversion manuelle Entity <-> DTO (pas besoin de MapStruct pour un
 * modèle aussi simple).
 */
public final class ComputerMapper {

    private ComputerMapper() {
    }

    public static Computer toEntity(ComputerRequest request) {
        Computer computer = new Computer();
        computer.setProce(request.getProce());
        computer.setRam(request.getRam());
        computer.setHardDrive(request.getHardDrive());
        computer.setPrice(request.getPrice());
        computer.setMacAddress(request.getMacAddress());
        return computer;
    }

    public static ComputerResponse toResponse(Computer computer) {
        return ComputerResponse.builder()
                .idPc(computer.getIdPc())
                .proce(computer.getProce())
                .ram(computer.getRam())
                .hardDrive(computer.getHardDrive())
                .price(computer.getPrice())
                .macAddress(computer.getMacAddress())
                .build();
    }
}
