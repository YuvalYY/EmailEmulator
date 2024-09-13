package com.bmc.exercise.EmailEmulator.server;

import com.bmc.exercise.EmailEmulator.processor.EmailProcessor;
import com.bmc.exercise.EmailEmulator.dto.EmailMessageDTO;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * Controller class for receiving email messages
 */
@RestController
@RequestMapping("/emailServer")
public class EmailServer {
    private static final Logger LOGGER = LogManager.getLogger(EmailServer.class);

    private final EmailProcessor emailProcessor;

    @Autowired
    public EmailServer(EmailProcessor inEmailProcessor) {
        emailProcessor = inEmailProcessor;
    }

    /**
     * Receives an email message to send to the correct vendor
     *
     * @param inEmailMessageDTO The email message
     * @return An HTTP response entity. Status is 204 for success, 404 if the vendor is not found,
     * and 400 if one of the emails is invalid
     */
    @PostMapping("/receive")
    public ResponseEntity<String> receive(@Valid @RequestBody EmailMessageDTO inEmailMessageDTO) {
        LOGGER.debug("Received message {}", inEmailMessageDTO);
        boolean lVendorFound = emailProcessor.process(inEmailMessageDTO);
        if (lVendorFound) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vendor of toEmail not found");
    }

    /**
     * Exception handler for requests with invalid DTOs
     *
     * @param inEx The thrown exception
     * @return A new response appropriate for the exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleInvalidDTO(MethodArgumentNotValidException inEx) {
        String lErrorMessage;
        Object[] lErrorMessagePartArr = inEx.getDetailMessageArguments();
        if (lErrorMessagePartArr == null) {
            lErrorMessage = "";
        }
        else {
            lErrorMessagePartArr = Arrays.stream(lErrorMessagePartArr)
                    .filter(inMessagePart -> inMessagePart != null && !String.valueOf(inMessagePart).isEmpty()).toArray();
            lErrorMessage = Arrays.toString(lErrorMessagePartArr);
        }

        LOGGER.warn("Received invalid message {}", lErrorMessage);
        return ResponseEntity.status(inEx.getStatusCode()).body(lErrorMessage);
    }
}
