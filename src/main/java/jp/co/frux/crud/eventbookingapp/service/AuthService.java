package jp.co.frux.crud.eventbookingapp.service;

import jakarta.validation.Valid;
import jp.co.frux.crud.eventbookingapp.dto.RegisterRequest;
import jp.co.frux.crud.eventbookingapp.dto.RegisterResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    RegisterResponse register(@RequestBody @Valid RegisterRequest registerRequest);
}
