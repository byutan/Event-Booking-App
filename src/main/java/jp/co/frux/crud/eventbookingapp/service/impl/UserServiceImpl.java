package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.ProfileRequest;
import jp.co.frux.crud.eventbookingapp.dto.ProfileResponse;
import jp.co.frux.crud.eventbookingapp.dto.ReminderRequest;
import jp.co.frux.crud.eventbookingapp.dto.ReminderResponse;
import jp.co.frux.crud.eventbookingapp.entity.Reminder;
import jp.co.frux.crud.eventbookingapp.entity.User;
import jp.co.frux.crud.eventbookingapp.exception.BusinessValidationException;
import jp.co.frux.crud.eventbookingapp.repository.ReminderRepository;
import jp.co.frux.crud.eventbookingapp.repository.UserRepository;
import jp.co.frux.crud.eventbookingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;

    @Override
    public ReminderResponse<ReminderResponse.reminderData> setReminder(ReminderRequest reminderRequest) {
        boolean eventReminder = reminderRequest.getEventReminder();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessValidationException("User not found", HttpStatus.NOT_FOUND));

        LocalDateTime createdAt = LocalDateTime.now();
        Reminder reminder;

        if (reminderRepository.findByUserId(user.getId()).isPresent()) {
            reminder = reminderRepository.findByUserId(user.getId()).get();
            reminder.setEventReminder(eventReminder);
        } else {
            reminder = new Reminder(eventReminder, createdAt, user);
        }
        reminderRepository.save(reminder);

        boolean isSuccess = true;
        String returnMessage = "Reminder settings updated successfully";

        return ReminderResponse.<ReminderResponse.reminderData>builder()
                .success(isSuccess)
                .message(returnMessage)
                .data(new ReminderResponse.reminderData(reminder.isEventReminder()))
                .build();
    }

    @Override
    public ProfileResponse<ProfileResponse.UserData> setProfile(ProfileRequest profileRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessValidationException("User not found", HttpStatus.NOT_FOUND));

        user.setFullName(profileRequest.getFullName());
        user.setAvatar(profileRequest.getAvatar());

        User savedUser = userRepository.save(user);
        boolean isSuccess = true;
        String returnMessage = "Profile updated successfully";

        return ProfileResponse.<ProfileResponse.UserData>builder()
                .success(isSuccess)
                .message(returnMessage)
                .data(new ProfileResponse.UserData(
                        savedUser.getId(),
                        savedUser.getFullName(),
                        savedUser.getEmail(),
                        savedUser.getAvatar()
                ))
                .build();
    }
}
