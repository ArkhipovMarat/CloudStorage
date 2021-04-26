package ru.netology.cloud_storage;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.netology.cloud_storage.dto.JwtRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationTest {
    private static final String WRONG_USER_LOGIN = "wrong_user";
    private static final String USER_LOGIN = "user1";
    private static final String USER_PASSWORD = "password1";
    private static final String REQUEST = "/list";
    private static final String LOGIN = "/login";

    private static final Gson gson = new Gson();

    @Autowired
    MockMvc mockMvc;

    @Test
    void givenCorrectCredentials_whenLogin_shouldGetOkStatus() throws Exception {
        JwtRequest validJwtRequest = new JwtRequest(USER_LOGIN,USER_PASSWORD);

        mockMvc.perform(post(LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(validJwtRequest))).andExpect(status().isOk());
    }

    @Test
    void givenBadCredentials_whenLogin_shouldThrowBadRequest() throws Exception {
        JwtRequest badJwtRequest = new JwtRequest(WRONG_USER_LOGIN,USER_PASSWORD);

        mockMvc.perform(post(LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(badJwtRequest))).andExpect(status().isBadRequest());
    }

    @Test
    void givenUnauthorizedRequest_whenRequestSecuredURL_ShouldThrowUnauthorizedStatusCode() throws Exception {
        mockMvc.perform(get(REQUEST)).andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = USER_LOGIN, password = USER_PASSWORD)
    @Test
    void givenAuthorizedRequest_whenRequestSecuredURL_ShouldGetOkStatus() throws Exception {
        mockMvc.perform(get(REQUEST)).andExpect(status().isOk());
    }
}


