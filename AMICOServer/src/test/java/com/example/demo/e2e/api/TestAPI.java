 package com.example.demo.e2e.api;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Assert;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TestAPI {

	final static Logger log = getLogger(lookup().lookupClass());
	
	private static String profile_uri="/api/users/{user}";
	
	@Autowired
	protected WebApplicationContext webAppCtx;
	
	private MockMvc mvc; 

	@BeforeEach
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webAppCtx)
				.apply(springSecurity()).build();
	}
	
	@Test
	@Rollback
	public void checkShowProfile() throws Exception {
		String url = profile_uri.replace("{user}", "amico");
			
		MvcResult result = mvc.perform(get(url)).andReturn();
		
		int status = result.getResponse().getStatus();
		int expected = HttpStatus.UNAUTHORIZED.value();
		
		Assert.assertEquals("failure - expected HTTP status " + expected, expected, status);
		log.info("The response is correct");
	}
}
