package com.bmc.exercise.EmailEmulator.processor;

import com.bmc.exercise.EmailEmulator.dto.EmailMessageDTO;
import com.bmc.exercise.EmailEmulator.model.EmailMessage;
import com.bmc.exercise.EmailEmulator.model.Vendor;
import com.bmc.exercise.EmailEmulator.smtp.SMTPTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Service class for processing email messages
 */
@Service
public class EmailProcessor {
    private static final Logger LOGGER = LogManager.getLogger(EmailProcessor.class);

    private final Map<String, Vendor> vendorMap;

    private final ExecutorService smtpTaskExecutor;

    @Autowired
    public EmailProcessor(@Qualifier("vendors") Map<String, Vendor> inVendorMap,
                          @Qualifier("smtpTaskExecutor") ExecutorService inSMTPTaskExecutor) {
        vendorMap = inVendorMap;
        smtpTaskExecutor = inSMTPTaskExecutor;
    }

    /**
     * Checks which vendor matches the email message and create a task for sending the
     * message to that vendor
     *
     * @param inEmailMessageDTO The email message
     * @return Whether a matching vendor was found for that email
     */
    public boolean process(EmailMessageDTO inEmailMessageDTO) {
        EmailMessage lEmailMessage = convertDTOToModel(inEmailMessageDTO);
        Vendor lVendor = vendorMap.get(lEmailMessage.getToEmailDomain());
        if (lVendor == null) {
            LOGGER.warn("Vendor not found for {}", lEmailMessage.getToEmailDomain());
            return false;
        }

        SMTPTask lSMTPTask = new SMTPTask(lEmailMessage, lVendor);
        smtpTaskExecutor.execute(lSMTPTask);
        LOGGER.debug("Created task for sending {} to destination", lEmailMessage);
        return true;
    }

    /**
     * Convert an email message from DTO to model representation
     *
     * @param inEmailMessageDTO The email message as a DTO
     * @return The email message as a model object
     */
    private EmailMessage convertDTOToModel(EmailMessageDTO inEmailMessageDTO) {
        EmailMessage outEmailMessage = new EmailMessage();

        String[] lToEmailPartArr = inEmailMessageDTO.getToEmail().split("@");
        outEmailMessage.setToEmailName(lToEmailPartArr[0]);
        outEmailMessage.setToEmailDomain(lToEmailPartArr[1]);

        String[] lFromEmailPartArr = inEmailMessageDTO.getFromEmail().split("@");
        outEmailMessage.setFromEmailName(lFromEmailPartArr[0]);
        outEmailMessage.setFromEmailDomain(lFromEmailPartArr[1]);

        outEmailMessage.setBody(inEmailMessageDTO.getBody());
        return outEmailMessage;
    }
}
