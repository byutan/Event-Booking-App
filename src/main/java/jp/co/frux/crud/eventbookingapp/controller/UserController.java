package jp.co.frux.crud.eventbookingapp.controller;

import jakarta.validation.Valid;
import jp.co.frux.crud.eventbookingapp.dto.ProfileRequest;
import jp.co.frux.crud.eventbookingapp.dto.ProfileResponse;
import jp.co.frux.crud.eventbookingapp.dto.ReminderRequest;
import jp.co.frux.crud.eventbookingapp.dto.ReminderResponse;
import jp.co.frux.crud.eventbookingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/reminders")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReminderResponse<ReminderResponse.reminderData>> setReminder(@Valid @RequestBody ReminderRequest reminderRequest) {
        ReminderResponse<ReminderResponse.reminderData> res = userService.setReminder(reminderRequest);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProfileResponse<ProfileResponse.UserData>> setProfile(@Valid @RequestBody ProfileRequest profileRequest) {
        ProfileResponse<ProfileResponse.UserData> res = userService.setProfile(profileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
