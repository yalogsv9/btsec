package vn.edu.ltweb.springws.vidu2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class Vidu2SecurityIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void anonymousVisitorIsRedirectedToVidu2Login() throws Exception {
        mvc.perform(get("/vidu2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/vidu2/login"));
    }

    @Test
    void userCanLoginWithUsernameAndSeeProfileInHeader() throws Exception {
        MvcResult login = mvc.perform(formLogin("/vidu2/login")
                        .user("user01")
                        .password("123456"))
                .andExpect(authenticated().withUsername("user01"))
                .andExpect(redirectedUrl("/vidu2"))
                .andReturn();

        mvc.perform(get("/vidu2").session((MockHttpSession) login.getRequest().getSession()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Nguyễn Hữu Trung")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("user01@gmail.com")));
    }

    @Test
    void userCanLoginWithEmail() throws Exception {
        mvc.perform(formLogin("/vidu2/login")
                        .user("user01@gmail.com")
                        .password("123456"))
                .andExpect(authenticated().withUsername("user01"))
                .andExpect(redirectedUrl("/vidu2"));
    }

    @Test
    void regularUserCannotAccessAdminArea() throws Exception {
        mvc.perform(get("/vidu2/admin/panel")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("user01").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void logoutRedirectsToVidu2Login() throws Exception {
        MvcResult login = mvc.perform(formLogin("/vidu2/login")
                        .user("user01")
                        .password("123456"))
                .andExpect(authenticated().withUsername("user01"))
                .andReturn();

        mvc.perform(post("/vidu2/logout").with(csrf())
                        .session((MockHttpSession) login.getRequest().getSession()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vidu2/login?logout"));
    }
}
