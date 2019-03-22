package com.example.demo.integration;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.integration.SSLClientFactory.HttpClientType;

public class TestAPIRestTemplate extends ElasTestBase{

	final static Logger log = getLogger(lookup().lookupClass());

    protected static String sutUrl;
    protected static String profile_uri;

    @BeforeAll
    public static void setupClass() {
        String sutHost = System.getenv("ET_SUT_HOST");
        String sutPort = System.getenv("ET_SUT_PORT");
        String sutProtocol = System.getenv("ET_SUT_PROTOCOL");

        if (sutHost == null) {
            sutUrl = "https://localhost:8443/";
        } else {
            sutPort = sutPort != null ? sutPort : "8080";
            sutProtocol = sutProtocol != null ? sutProtocol : "http";

            sutUrl = sutProtocol + "://" + sutHost + ":" + sutPort + "/";
        }
        
        profile_uri = sutUrl + "api/users/{user}";
        logger.info("Webapp URL: " + sutUrl);
    }
    
	@Test
	public void checkShowProfile() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		String url = profile_uri.replace("{user}", "amico");
		RestTemplate restTemplate = new RestTemplate(SSLClientFactory.getClientHttpRequestFactory(HttpClientType.HttpClient));

		HttpStatus status;
		HttpStatus expected = HttpStatus.UNAUTHORIZED;

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			status = response.getStatusCode();
		} catch (HttpStatusCodeException e) {
			status = e.getStatusCode();
		}

		Assert.assertEquals("failure - expected HTTP status " + expected, expected, status);
		log.info("The response is correct");
	}

}
