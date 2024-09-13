package com.bmc.exercise.EmailEmulator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Model class of an email vendor
 */
@AllArgsConstructor
@Getter
public class Vendor {
    private final String serverAddress;

    private final String username;

    private final String password;

    private final String emailPostfix;

    @Override
    public String toString() {
        return "Vendor(" +
                "username='" + username + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", emailPostfix='" + emailPostfix + '\'' +
                ')';
    }
}
