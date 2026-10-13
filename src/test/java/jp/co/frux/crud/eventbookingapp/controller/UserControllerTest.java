package jp.co.frux.crud.eventbookingapp.controller;

import jp.co.frux.crud.eventbookingapp.dto.ProfileRequest;
import jp.co.frux.crud.eventbookingapp.dto.ReminderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc()
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void setReminder_withoutLogin_shouldReturn401() throws Exception {
        ReminderRequest req = new ReminderRequest(true);

        mockMvc.perform(put("/api/users/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "jane@example.com", roles = "MANAGER")
    @Test
    void setReminder_withoutPermission_shouldReturn403() throws Exception {
        ReminderRequest req = new ReminderRequest(true);

        mockMvc.perform(put("/api/users/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "jane@example.com", roles = "USER")
    @Test
    void setReminder_shouldReturn200() throws Exception {
        ReminderRequest req = new ReminderRequest(true);

        mockMvc.perform(put("/api/users/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Reminder settings updated successfully"))
                .andExpect(jsonPath("$.data.eventReminder").value(true));
    }

    @Test
    void setProfile_withoutLogin_shouldReturn401() throws Exception {
        ProfileRequest req = new ProfileRequest(
                "Jane Doe",
                "https://cdn.example.com/avatars/user123.jpg");

        mockMvc.perform(put("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "jane@example.com", roles = "MANAGER")
    @Test
    void setProfile_withoutPermission_shouldReturn403() throws Exception {
        ProfileRequest req = new ProfileRequest(
                "Jane Doe",
                "https://cdn.example.com/avatars/user123.jpg");

        mockMvc.perform(put("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "jane@example.com", roles = "USER")
    @Test
    void setProfile_shouldReturn200() throws Exception {
        ProfileRequest req = new ProfileRequest(
                "Jane Doe",
                "https://cdn.example.com/avatars/user123.jpg");

        mockMvc.perform(put("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile updated successfully"))
                .andExpect(jsonPath("$.data.fullName").value("Jane Doe"))
                .andExpect(jsonPath("$.data.avatar").value("https://cdn.example.com/avatars/user123.jpg"));
    }

    @WithMockUser(username = "jane@example.com", roles = "USER")
    @Test
    void setProfile_withInvalidInput_shouldReturn422() throws Exception {
        ProfileRequest req = new ProfileRequest(
                "",
                "");

        mockMvc.perform(put("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
}
