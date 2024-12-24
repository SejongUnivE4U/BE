package SJUCapstone.BE.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    String refreshToken;

    public LoginResponse(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
