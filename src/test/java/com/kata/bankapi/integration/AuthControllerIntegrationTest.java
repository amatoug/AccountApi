package com.kata.bankapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

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

    @Test
    void should_return_401_with_wrong_password() throws Exception {
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("username", "user", "password", "wrongpassword"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Identifiants invalides"));
    }

    @Test
    void should_return_401_with_unknown_user() throws Exception {
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("username", "unknown", "password", "password"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Identifiants invalides"));
    }

    @Test
    void should_access_protected_endpoint_after_login() throws Exception {
        // Login first to establish session
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("username", "user", "password", "password"))))
                .andExpect(status().isOk())
                .andReturn();

        // Use session from login to access protected endpoint
        mockMvc.perform(post("/api/accounts")
                .session((org.springframework.mock.web.MockHttpSession)
                        loginResult.getRequest().getSession())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "TestUser"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestUser"));
    }

    @Test
    void should_not_access_protected_endpoint_without_login() throws Exception {
        mockMvc.perform(get("/api/accounts/" + java.util.UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_logout_successfully() throws Exception {
        // Login first
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("username", "user", "password", "password"))))
                .andExpect(status().isOk())
                .andReturn();

        org.springframework.mock.web.MockHttpSession session =
                (org.springframework.mock.web.MockHttpSession) loginResult.getRequest().getSession();

        // Logout
        mockMvc.perform(post("/api/logout")
                .session(session))
                .andExpect(status().isOk());
    }
}
