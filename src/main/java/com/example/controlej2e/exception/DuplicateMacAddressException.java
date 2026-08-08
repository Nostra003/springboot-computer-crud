package com.example.controlej2e.exception;

public class DuplicateMacAddressException extends RuntimeException {
    public DuplicateMacAddressException(String macAddress) {
        super("Un ordinateur avec l'adresse MAC '" + macAddress + "' existe déjà");
    }
}
