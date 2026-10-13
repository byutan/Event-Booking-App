package jp.co.frux.crud.eventbookingapp.controller;

import jp.co.frux.crud.eventbookingapp.dto.PaymentRequest;
import jp.co.frux.crud.eventbookingapp.dto.PaymentResponse;
import jp.co.frux.crud.eventbookingapp.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc()
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentService paymentService;

    private PaymentRequest req;

    private PaymentResponse<PaymentResponse.PaymentData> res;
    @BeforeEach
    public void setup() {
        req = new PaymentRequest(
                1,
                "1234 5678 9012 3456",
                "12/25",
                "1234"
        );

        PaymentResponse.PaymentData paymentData = new PaymentResponse.PaymentData(
                0,
                1,
                50.0,
                "PAID"
        );

        res = new PaymentResponse<>(
                true,
                "Payment successful",
                paymentData
        );
    }

    @WithMockUser(username = "jane@example.com", roles = "ADMIN")
    @Test
    void createPayment_shouldReturn201() throws Exception {
        when(paymentService.createPayment(req)).thenReturn(res);
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Payment successful"))
                .andExpect(jsonPath("$.data.paymentId").value(0))
                .andExpect(jsonPath("$.data.bookingId").value(1))
                .andExpect(jsonPath("$.data.amount").value(50.0))
                .andExpect(jsonPath("$.data.status").value("PAID"));
    }

    @WithMockUser(username = "jane@example.com", roles = "MANAGER")
    @Test
    void createPayment_withoutPermission_shouldReturn403() throws Exception {
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createPayment_withoutLogin_shouldReturn401() throws Exception {
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "jane@example.com", roles = "ADMIN")
    @Test
    void createPayment_withInvalidInput_shouldReturn422() throws Exception {
        req = new PaymentRequest(
                0,
                "",
                "",
                ""
        );


        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableContent());
    }
}
