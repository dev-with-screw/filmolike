package home.work.filmolike.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class WebSecurityConfigTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("GET /registration when authenticated - then status 403")
	@WithMockUser(username = "something user", roles={"USER"})
	void getRegistrationWhen() throws Exception {
		mockMvc.perform(get("/registration"))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("GET /notes when not authenticated - then redirect to /login")
	public void accessDeniedTest() throws Exception {
		mockMvc.perform(get("/notes"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@DisplayName("GET /notes with correct authenticating data - then redirect /notes")
	@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void correctLoginTest() throws Exception {
		mockMvc.perform(formLogin().user("u").password("p"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/notes"));
	}

	@Test
	@DisplayName("POST /login when authenticating with incorrect data - then redirect")
	public void badCredentials() throws Exception {
		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "user_not_exist")
				.param("password", "any_password")
				.with(csrf()))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?error"));
	}
}
