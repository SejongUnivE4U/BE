package SJUCapstone.BE.auth.dto;

import jakarta.servlet.http.Cookie;
import lombok.Getter;

@Getter
public class ServerLoginResponse {
    Cookie cookie;
    LoginResponse loginResponse;

    public ServerLoginResponse(Cookie cookie, LoginResponse loginResponse) {
        this.cookie = cookie;
        this.loginResponse = loginResponse;
    }
}
