package com.mparaz.lss;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LearnSpringSecurityApplication.class)
@WebAppConfiguration
public class LearnSpringSecurityApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnSpringSecurityApplicationTests.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void setUp() {

        // Need to apply(springSecurity()) to pass authentication
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void shouldNotAccessUrlInsecurely() throws Exception {
        // Without any credentials, it will return an HTTP 403 Forbidden.
        // Note: It is not HTTP 401 because Spring Security does not check for the headers.
        // Instead, it lets the preauthentication do it.
        assertThat(mockMvc.perform(get("/go")).andReturn().getResponse().getStatus(), is(403));
    }

    public void shouldNotUseAuthorizationHeader() throws Exception {
        // Spring Security in JEE mode does not know how to handle any Authorization headers.
        final MockHttpServletResponse response = mockMvc.perform(get("/go").
                header("Authorization", "Basic amVldXNlcjpqZWV1c2VyCg==")).
                andReturn().getResponse();
        assertThat(response.getStatus(), is(403));
    }

    @Test
    public void shouldAccessUrlSecurelyWhenRoles() throws Exception {
        // User and roles are injected into the MockMvc.
        // The Principal won't be able to inject, so Spring Security needs to inject it.

        final MockHttpServletResponse response = mockMvc.perform(get("/go").
                with(user("javaeeuser").roles("javaeeRole"))).andReturn().getResponse();
        assertThat(response.getStatus(), is(200));
        assertThat(response.getContentAsString(), is("arrived: javaeeuser"));
    }

    @Test
    public void shouldNotAccessUrlWhenNoRoles() throws Exception {
        // Spring Security in JEE mode only knows how to deal with a pre-existing Principal,
        // with no information on how to the authentication was performed.
        //
        // In this case, the Principal is injected but the Java EE Principal does not carry roles along with it.
        // Therefore, the method won't be called.
        final Principal principal = new Principal() {
            @Override
            public String getName() {
                return "jeeuser";
            }
        };

        final MockHttpServletResponse response = mockMvc.perform(get("/go").principal(principal)).andReturn().getResponse();
        assertThat(response.getStatus(), is(403));
    }

}
