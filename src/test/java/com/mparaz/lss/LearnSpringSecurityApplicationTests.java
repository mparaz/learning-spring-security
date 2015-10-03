package com.mparaz.lss;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import javax.servlet.Filter;
import java.security.Principal;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LearnSpringSecurityApplication.class)
@WebAppConfiguration
public class LearnSpringSecurityApplicationTests {

	@Autowired
    private WebApplicationContext webApplicationContext;

//    @Autowired
//    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        // Need to apply(springSecurity()) to pass authentication
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void shouldNotAccessUrlInsecurely() throws Exception {
        // Without any credentials, it will return an HTTP 401 Unauthorised.
        assertThat(mockMvc.perform(get("/go")).andReturn().getResponse().getStatus(), is(401));
    }

    @Test
    public void shouldAccessUrlSecurely() throws Exception {

        // It passes two ways.

        // 1. It interprets the header.
        mockMvc.perform(get("/go").header("Authorization", "Basic aW5tZW1vcnk6aW5tZW1vcnk="));

        // 2. It uses the principal.
        final Principal principal = new Principal() {
            @Override
            public String getName() {
                return "inmemory";
            }
        };

        mockMvc.perform(get("/go").principal(principal));
    }
}
