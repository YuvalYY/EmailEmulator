package com.bmc.exercise.EmailEmulator.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Model class of an email message
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailMessage {
    private String toEmailName;

    private String toEmailDomain;

    private String fromEmailName;

    private String fromEmailDomain;

    private String body;
}
