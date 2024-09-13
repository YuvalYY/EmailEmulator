package com.bmc.exercise.EmailEmulator.processor;

import com.bmc.exercise.EmailEmulator.dto.EmailMessageDTO;
import com.bmc.exercise.EmailEmulator.model.EmailMessage;
import com.bmc.exercise.EmailEmulator.model.Vendor;
import com.bmc.exercise.EmailEmulator.smtp.SMTPTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Test class for {@link EmailProcessor}
 */
@ExtendWith(MockitoExtension.class)
public class EmailProcessorTest {
    private static final String KNOWN_EMAIL_POSTFIX = "known.com";

    private static final String TO_KNOWN_EMAIL = "test.employee@known.com";

    private static final String TO_UNKNOWN_EMAIL = "test.employee@unknown.com";

    private static final String FROM_EMAIL = "some.one@testmail.com";

    private static final String BODY = "test body";

    private final Vendor knownVendor = new Vendor("smtp.known.com",
            "admin", "admin", KNOWN_EMAIL_POSTFIX);

    @Mock
    private Map<String, Vendor> vendorMap;

    @Mock
    private ExecutorService smtpTaskExecutor;

    @InjectMocks
    private EmailProcessor emailProcessor;

    @BeforeEach
    public void setup() {
        Mockito.doAnswer(inInvocationOnMock -> KNOWN_EMAIL_POSTFIX.equals(inInvocationOnMock.getArgument(0))
                ? knownVendor : null).when(vendorMap).get(Mockito.anyString());
    }

    /**
     * Test case for when a vendor matching the recipient exists
     */
    @Test
    public void testProcessWhenVendorExists() {
        EmailMessageDTO lEmailMessageDTO = new EmailMessageDTO();
        lEmailMessageDTO.setToEmail(TO_KNOWN_EMAIL);
        lEmailMessageDTO.setFromEmail(FROM_EMAIL);
        lEmailMessageDTO.setBody(BODY);

        boolean lVendorFound = emailProcessor.process(lEmailMessageDTO);

        Assertions.assertTrue(lVendorFound);

        ArgumentCaptor<SMTPTask> lSMTPTaskCaptor = ArgumentCaptor.forClass(SMTPTask.class);
        Mockito.verify(smtpTaskExecutor).execute(lSMTPTaskCaptor.capture());
        SMTPTask lSMTPTask = lSMTPTaskCaptor.getValue();
        EmailMessage lEmailMessage = lSMTPTask.getEmailMessage();
        // Assert email message
        Assertions.assertEquals(TO_KNOWN_EMAIL, lEmailMessage.getToEmailName() +
                '@' + lEmailMessage.getToEmailDomain());
        Assertions.assertEquals(FROM_EMAIL, lEmailMessage.getFromEmailName() +
                '@' + lEmailMessage.getFromEmailDomain());
        Assertions.assertEquals(BODY, lEmailMessage.getBody());
        // Assert vendor
        Assertions.assertEquals(knownVendor, lSMTPTask.getVendor());
    }

    /**
     * Test case for when a vendor matching the recipient doesn't exist
     */
    @Test
    public void testProcessWhenVendorDoesntExist() {
        EmailMessageDTO lEmailMessageDTO = new EmailMessageDTO();
        lEmailMessageDTO.setToEmail(TO_UNKNOWN_EMAIL);
        lEmailMessageDTO.setFromEmail(FROM_EMAIL);
        lEmailMessageDTO.setBody(BODY);

        boolean lVendorFound = emailProcessor.process(lEmailMessageDTO);

        Assertions.assertFalse(lVendorFound);
        Mockito.verify(smtpTaskExecutor, Mockito.never()).execute(Mockito.any());
    }
}
