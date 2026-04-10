package com.kata.bankapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_login_with_valid_credentials() throws Exception {
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("username", "user", "password", "password"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }

    @ParameterizedTest(name = "should return 401 for username={0}")
    @CsvSource({
            "user, wrongpassword",
            "unknown, password"
    })
    void should_return_401_with_invalid_credentials(String username, String password) throws Exception {
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("username", username, "password", password))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Identifiants invalides"));
    }

    @Test
    void should_access_protected_endpoint_after_login() throws Exception {
        MockHttpSession session = loginAndGetSession();

        mockMvc.perform(post("/api/accounts")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "TestUser"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestUser"));
    }

    @Test
    void should_not_access_protected_endpoint_without_login() throws Exception {
        mockMvc.perform(get("/api/accounts/" + UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_logout_successfully() throws Exception {
        MockHttpSession session = loginAndGetSession();

        mockMvc.perform(post("/api/logout")
                .session(session))
                .andExpect(status().isOk());
    }

    private MockHttpSession loginAndGetSession() throws Exception {
        return (MockHttpSession) mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("username", "user", "password", "password"))))
                .andExpect(status().isOk())
                .andReturn()
                .getRequest()
                .getSession();
    }
}
