package vn.edu.ltweb.springws.vidu1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class Vidu1SecurityIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void publicVidu1HomeIsAvailable() throws Exception {
        mvc.perform(get("/vidu1"))
                .andExpect(status().isOk());
    }

    @Test
    void anonymousVisitorIsRedirectedFromPrivatePageToLogin() throws Exception {
        mvc.perform(get("/vidu1/private"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void studentCanLoginAndOpenPrivatePage() throws Exception {
        MvcResult login = mvc.perform(formLogin("/vidu1/login")
                        .user("student")
                        .password("123456"))
                .andExpect(authenticated().withUsername("student"))
                .andReturn();

        mvc.perform(get("/vidu1/private")
                        .session((MockHttpSession) login.getRequest().getSession()))
                .andExpect(status().isOk());
    }

    @Test
    void logoutInvalidatesTheAuthenticatedSession() throws Exception {
        MvcResult login = mvc.perform(formLogin("/vidu1/login")
                        .user("student")
                        .password("123456"))
                .andExpect(authenticated().withUsername("student"))
                .andReturn();
        MockHttpSession session = (MockHttpSession) login.getRequest().getSession();

        mvc.perform(post("/vidu1/logout").with(csrf()).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vidu1"));

        mvc.perform(get("/vidu1/private").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
