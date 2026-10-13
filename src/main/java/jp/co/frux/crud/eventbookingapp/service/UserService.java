package jp.co.frux.crud.eventbookingapp.service;

import jp.co.frux.crud.eventbookingapp.dto.ProfileRequest;
import jp.co.frux.crud.eventbookingapp.dto.ProfileResponse;
import jp.co.frux.crud.eventbookingapp.dto.ReminderRequest;
import jp.co.frux.crud.eventbookingapp.dto.ReminderResponse;

public interface UserService {
    ReminderResponse<ReminderResponse.reminderData> setReminder(ReminderRequest reminderRequest);
    ProfileResponse<ProfileResponse.UserData> setProfile(ProfileRequest profileRequest);
}
