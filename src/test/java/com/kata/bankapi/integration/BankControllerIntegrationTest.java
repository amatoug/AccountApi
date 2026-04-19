package com.kata.bankapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BankControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void should_create_account() throws Exception {
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "Alice"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.solde").value(0));
    }

    @Test
    @WithMockUser
    void should_return_bad_request_when_creating_account_without_name() throws Exception {
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void should_get_account_by_id() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "Bob"))))
                .andExpect(status().isCreated())
                .andReturn();

        String id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(get("/api/accounts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.solde").value(0));
    }

    @Test
    @WithMockUser
    void should_return_not_found_for_unknown_account() throws Exception {
        mockMvc.perform(get("/api/accounts/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ACCOUNT_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("account not found"))
                .andExpect(jsonPath("$.requestId").exists());
    }

    @Test
    @WithMockUser
    void should_deposit_amount() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "Charlie"))))
                .andExpect(status().isCreated())
                .andReturn();

        String id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(post("/api/accounts/{id}/deposit", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("amount", 150))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(150));
    }

    @Test
    @WithMockUser
    void should_return_bad_request_when_deposit_negative_amount() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "Dave"))))
                .andExpect(status().isCreated())
                .andReturn();

        String id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(post("/api/accounts/{id}/deposit", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("amount", -50))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void should_return_not_found_when_deposit_to_unknown_account() throws Exception {
        mockMvc.perform(post("/api/accounts/{id}/deposit", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("amount", 100))))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void should_withdraw_amount() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "Eve"))))
                .andExpect(status().isCreated())
                .andReturn();

        String id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(post("/api/accounts/{id}/deposit", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("amount", 200))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/accounts/{id}/withdraw", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("amount", 75))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(125));
    }

    @Test
    @WithMockUser
    void should_return_bad_request_when_withdraw_negative_amount() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "Frank"))))
                .andExpect(status().isCreated())
                .andReturn();

        String id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(post("/api/accounts/{id}/withdraw", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("amount", -30))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void should_return_unprocessable_entity_when_overdraft() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "Grace"))))
                .andExpect(status().isCreated())
                .andReturn();

        String id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(post("/api/accounts/{id}/withdraw", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("amount", 500))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser
    void should_perform_full_account_lifecycle() throws Exception {
        // Create account
        MvcResult createResult = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "Heidi"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.solde").value(0))
                .andReturn();

        String id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        // Deposit 300
        mockMvc.perform(post("/api/accounts/{id}/deposit", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("amount", 300))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(300));

        // Deposit 200 more
        mockMvc.perform(post("/api/accounts/{id}/deposit", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("amount", 200))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(500));

        // Withdraw 150
        mockMvc.perform(post("/api/accounts/{id}/withdraw", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("amount", 150))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(350));

        // Verify final balance
        mockMvc.perform(get("/api/accounts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Heidi"))
                .andExpect(jsonPath("$.solde").value(350));
    }

    @Test
    void should_return_unauthorized_when_not_authenticated() throws Exception {
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("name", "Ivan"))))
                .andExpect(status().isUnauthorized());
    }
}
