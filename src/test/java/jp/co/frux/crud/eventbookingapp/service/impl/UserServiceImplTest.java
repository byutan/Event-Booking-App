package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.ProfileRequest;
import jp.co.frux.crud.eventbookingapp.dto.ProfileResponse;
import jp.co.frux.crud.eventbookingapp.dto.ReminderRequest;
import jp.co.frux.crud.eventbookingapp.dto.ReminderResponse;
import jp.co.frux.crud.eventbookingapp.entity.Reminder;
import jp.co.frux.crud.eventbookingapp.entity.User;
import jp.co.frux.crud.eventbookingapp.repository.ReminderRepository;
import jp.co.frux.crud.eventbookingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ReminderRepository reminderRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    private ReminderRequest req;

    private ProfileRequest profileReq;

    @BeforeEach
    public void setUp() {
        user = new User(
                "Jane",
                "janedoe@email.com",
                "Kh@0104",
                "avatar",
                "USER",
                LocalDateTime.of(2027, 5, 12, 10, 0)
        );
        req = new ReminderRequest(true);

        profileReq = new ProfileRequest("Jane Doe", "https://cdn.example.com/avatars/user123.jpg");
    }

    @Test
    void setReminderTrue_withNoReminderYet_shouldReturnTrue() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "janedoe@email.com", null
                )
        );
        Reminder reminder = new Reminder(
                req.getEventReminder(),
                LocalDateTime.of(2027, 5, 12, 10, 0),
                user);
        user.setReminder(reminder);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(reminderRepository.findByUserId(anyInt())).thenReturn(Optional.empty());
        when(reminderRepository.save(any(Reminder.class))).thenReturn(reminder);

        ReminderResponse<ReminderResponse.reminderData> res = userService.setReminder(req);
        assertTrue(res.isSuccess());
        assertEquals("Reminder settings updated successfully", res.getMessage());
        assertTrue(res.getData().eventReminder());
    }

    @Test
    void setReminderTrue_withCreatedReminder_shouldReturnTrue() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "janedoe@email.com", null
                )
        );
        Reminder reminder = new Reminder(
                req.getEventReminder(),
                LocalDateTime.of(2027, 5, 12, 10, 0),
                user);
        user.setReminder(reminder);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(reminderRepository.findByUserId(anyInt())).thenReturn(Optional.of(reminder));
        when(reminderRepository.save(any(Reminder.class))).thenReturn(reminder);

        ReminderResponse<ReminderResponse.reminderData> res = userService.setReminder(req);
        assertTrue(res.isSuccess());
        assertEquals("Reminder settings updated successfully", res.getMessage());
        assertTrue(res.getData().eventReminder());
    }

    @Test
    void setReminderFalse_shouldReturnFalse() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "janedoe@email.com", null
                )
        );
        Reminder reminder = new Reminder(
                req.getEventReminder(),
                LocalDateTime.of(2027, 5, 12, 10, 0),
                user);
        user.setReminder(reminder);
        req.setEventReminder(false);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(reminderRepository.findByUserId(anyInt())).thenReturn(Optional.of(reminder));
        when(reminderRepository.save(any(Reminder.class))).thenReturn(reminder);

        ReminderResponse<ReminderResponse.reminderData> res = userService.setReminder(req);
        assertTrue(res.isSuccess());
        assertEquals("Reminder settings updated successfully", res.getMessage());
        assertFalse(res.getData().eventReminder());
    }

    @Test
    void setProfile_shouldReturnSuccessMessage() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "janedoe@email.com", null
                )
        );
        user.setFullName(profileReq.getFullName());
        user.setAvatar(profileReq.getAvatar());

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        ProfileResponse<ProfileResponse.UserData> res = userService.setProfile(profileReq);
        assertTrue(res.isSuccess());
        assertEquals("Profile updated successfully", res.getMessage());
        assertEquals(0, res.getData().id());
        assertEquals("Jane Doe", res.getData().fullName());
        assertEquals("janedoe@email.com", res.getData().email());
        assertEquals("https://cdn.example.com/avatars/user123.jpg", res.getData().avatar());
    }
}
