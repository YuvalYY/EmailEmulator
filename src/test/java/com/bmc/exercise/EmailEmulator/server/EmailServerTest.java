package com.bmc.exercise.EmailEmulator.server;

import com.bmc.exercise.EmailEmulator.dto.EmailMessageDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test class for {@link EmailServer}
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailServerTest {
    private static final String TO_KNOWN_EMAIL = "test.employee@testmail.com";

    private static final String TO_UNKNOWN_EMAIL = "test.employee@somemail.com";

    private static final String TO_INVALID_EMAIL = "test.employee-somemail.com";

    private static final String FROM_EMAIL = "some.one@fakemail.com";

    private static final String BODY = "test body";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test case for receiving an email when the vendor is known to the server
     */
    @Test
    public void testReceiveWithKnownVendor() {
        EmailMessageDTO lEmailMessageDTO = new EmailMessageDTO();
        lEmailMessageDTO.setToEmail(TO_KNOWN_EMAIL);
        lEmailMessageDTO.setFromEmail(FROM_EMAIL);
        lEmailMessageDTO.setBody(BODY);

        ResponseEntity<String> lResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/emailServer/receive", lEmailMessageDTO, String.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, lResponse.getStatusCode());
    }

    /**
     * Test case for receiving an email when the vendor is unknown to the server
     */
    @Test
    public void testReceiveWithUnknownVendor() {
        EmailMessageDTO lEmailMessageDTO = new EmailMessageDTO();
        lEmailMessageDTO.setToEmail(TO_UNKNOWN_EMAIL);
        lEmailMessageDTO.setFromEmail(FROM_EMAIL);
        lEmailMessageDTO.setBody(BODY);

        ResponseEntity<String> lResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/emailServer/receive", lEmailMessageDTO, String.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, lResponse.getStatusCode());
    }

    /**
     * Test case for receiving an email when one of the email arguments isn't a valid email
     */
    @Test
    public void testReceiveWithInvalidEmail() {
        EmailMessageDTO lEmailMessageDTO = new EmailMessageDTO();
        lEmailMessageDTO.setToEmail(TO_INVALID_EMAIL);
        lEmailMessageDTO.setFromEmail(FROM_EMAIL);
        lEmailMessageDTO.setBody(BODY);

        ResponseEntity<String> lResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/emailServer/receive", lEmailMessageDTO, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, lResponse.getStatusCode());
    }
}
