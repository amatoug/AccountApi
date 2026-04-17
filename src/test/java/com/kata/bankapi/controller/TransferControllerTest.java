package com.kata.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.bankapi.config.SecurityConfig;
import com.kata.bankapi.dto.TransferRequest;
import com.kata.bankapi.dto.TransferResponse;
import com.kata.bankapi.exception.handler.GlobalExceptionHandler;
import com.kata.bankapi.services.TransferService;
import com.kata.bankapi.web.RequestIdFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransferController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class, RequestIdFilter.class})
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferService transferService;

    @Test
    @WithMockUser(authorities = "TRANSFER_CREATE")
    void should_create_transfer() throws Exception {
        UUID transferId = UUID.randomUUID();
        when(transferService.createTransfer(any())).thenReturn(new TransferResponse(transferId, "COMPLETED"));

        TransferRequest request = new TransferRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("10.50"),
                "EUR",
                "invoice-1"
        );

        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transferId").value(transferId.toString()))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @WithMockUser(authorities = "TRANSFER_CREATE")
    void should_return_validation_error_with_request_id() throws Exception {
        TransferRequest invalidRequest = new TransferRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                BigDecimal.ZERO,
                "EURO",
                "ref"
        );

        mockMvc.perform(post("/api/transfers")
                        .header("X-Request-Id", "req-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.requestId").value("req-123"));
    }

    @Test
    void should_return_unauthorized_when_not_authenticated() throws Exception {
        TransferRequest request = new TransferRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("10.00"),
                "EUR",
                "ref"
        );

        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void should_return_forbidden_when_missing_authority() throws Exception {
        TransferRequest request = new TransferRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("10.00"),
                "EUR",
                "ref"
        );

        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
