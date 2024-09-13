package com.bmc.exercise.EmailEmulator.smtp;

import com.bmc.exercise.EmailEmulator.model.Vendor;
import com.bmc.exercise.EmailEmulator.model.EmailMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class of a task of sending an email message to the recipient's vendor
 */
public class SMTPTask implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(SMTPTask.class);

    private final EmailMessage emailMessage;

    private final Vendor vendor;

    public SMTPTask(EmailMessage emailMessage, Vendor vendor) {
        this.emailMessage = emailMessage;
        this.vendor = vendor;
    }

    public EmailMessage getEmailMessage() {
        return emailMessage;
    }

    public Vendor getVendor() {
        return vendor;
    }

    @Override
    public void run() {
        LOGGER.info("Sending message {} to vendor {}", emailMessage, vendor);
    }
}
