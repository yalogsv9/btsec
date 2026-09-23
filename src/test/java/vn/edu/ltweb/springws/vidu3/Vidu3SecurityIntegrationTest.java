package vn.edu.ltweb.springws.vidu3;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class Vidu3SecurityIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void anonymousVisitorIsRedirectedToVidu3Login() throws Exception {
        mvc.perform(get("/vidu3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/vidu3/login"));
    }

    @Test
    void loginWithoutRememberMeDoesNotIssueCookie() throws Exception {
        mvc.perform(post("/vidu3/login").with(csrf())
                        .param("username", "rememberme")
                        .param("password", "123456"))
                .andExpect(authenticated().withUsername("rememberme"))
                .andExpect(redirectedUrl("/vidu3"))
                .andExpect(cookie().doesNotExist("remember-me"));
    }

    @Test
    void invalidLoginRedirectsToVidu3ErrorPage() throws Exception {
        mvc.perform(post("/vidu3/login").with(csrf())
                        .param("username", "rememberme")
                        .param("password", "wrong-password")
                        .param("remember-me", "on"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vidu3/login?error=true"))
                .andExpect(cookie().maxAge("remember-me", 0));
    }

    @Test
    void rememberMeCookieAuthenticatesRequestWithoutSession() throws Exception {
        mvc.perform(get("/vidu3").cookie(rememberMeCookie()))
                .andExpect(authenticated().withUsername("rememberme"))
                .andExpect(content().string(containsString("rememberme")));
    }

    @Test
    void csrfLogoutExpiresRememberMeCookie() throws Exception {
        mvc.perform(post("/vidu3/logout").with(csrf()).cookie(rememberMeCookie()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vidu3/login?logout"))
                .andExpect(cookie().maxAge("remember-me", 0));
    }

    private Cookie rememberMeCookie() throws Exception {
        MvcResult result = mvc.perform(post("/vidu3/login").with(csrf())
                        .param("username", "rememberme")
                        .param("password", "123456")
                        .param("remember-me", "on"))
                .andExpect(authenticated().withUsername("rememberme"))
                .andExpect(redirectedUrl("/vidu3"))
                .andExpect(cookie().exists("remember-me"))
                .andReturn();
        return result.getResponse().getCookie("remember-me");
    }
}
