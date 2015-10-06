package com.mparaz.lss;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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

    // Set up the REST template to return the mock role.
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void setUp() {

        // Need to apply(springSecurity()) to pass authentication
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
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
        // Mock server shall return the credentials
        // as expected by LearningAuthenticationUserDetailsService
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo("http://localhost:8080/r/jeeuser"))
                .andRespond(MockRestResponseCreators.withSuccess("[\"ROLE_mockRole\"]", MediaType.APPLICATION_JSON));

        // Spring Security in JEE mode only knows how to deal with a pre-existing Principal,
        // with no information on how to the authentication was performed.
        final Principal principal = new Principal() {
            @Override
            public String getName() {
                return "jeeuser";
            }
        };

        final MockHttpServletResponse response1 = mockMvc.perform(get("/go").principal(principal)).andReturn().getResponse();
        assertThat(response1.getStatus(), is(200));
        assertThat(response1.getContentAsString(), is("arrived"));
    }

    @Test
    public void shouldNotAccessUrlWhenNoRoles() throws Exception {
        // Mock server shall return no credentials
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo("http://localhost:8080/r/jeeuser"))
                .andRespond(MockRestResponseCreators.withSuccess("[]", MediaType.APPLICATION_JSON));

        // Spring Security in JEE mode only knows how to deal with a pre-existing Principal,
        // with no information on how to the authentication was performed.
        final Principal principal = new Principal() {
            @Override
            public String getName() {
                return "jeeuser";
            }
        };

        final MockHttpServletResponse response1 = mockMvc.perform(get("/go").principal(principal)).andReturn().getResponse();
        assertThat(response1.getStatus(), is(403));
    }

}
