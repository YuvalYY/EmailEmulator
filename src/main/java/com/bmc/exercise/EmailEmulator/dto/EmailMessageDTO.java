package com.bmc.exercise.EmailEmulator.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO class of an email message
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailMessageDTO {
    @Email(message = "toEmail must be a valid email")
    private String toEmail;

    @Email(message = "fromEmail must be a valid email")
    private String fromEmail;

    private String body;
}
