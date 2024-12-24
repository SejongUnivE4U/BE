package SJUCapstone.BE.auth.service;

import SJUCapstone.BE.auth.domain.Token;
import SJUCapstone.BE.auth.dto.*;
import SJUCapstone.BE.auth.exception.InvalidPasswordException;
import SJUCapstone.BE.user.domain.User;
import SJUCapstone.BE.user.domain.UserInfo;
import SJUCapstone.BE.user.service.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

    public LoginService() {
    }

    public ServerLoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userService.findByEmail(email);

        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        } else {
            TokenResponse tokens = authService.generateTokens(user.getEmail());

            Cookie cookie = getCookie(tokens);
            LoginResponse loginResponse = new LoginResponse(tokens.getRefreshToken());

            saveToken(tokens, user);

            return new ServerLoginResponse(cookie, loginResponse);
        }
    }

    public Cookie logout(){
        return deleteCookie();
    }

    private Cookie getCookie(TokenResponse tokens) {
        Cookie cookie = new Cookie("accessToken", tokens.getAccessToken());
        cookie.setHttpOnly(true);  // 자바스크립트에서 접근 불가
        cookie.setSecure(true); // HTTP에서 작동
        cookie.setMaxAge(60 * 60 * 24);  // 1일 동안 유지
        cookie.setPath("/");

        return cookie;
    }

    private Cookie deleteCookie() {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);  // HTTPS를 사용하는 경우에만 설정
        cookie.setMaxAge(0);  // 쿠키 만료 시간 설정 (0이면 즉시 삭제)
        cookie.setPath("/");  // 전체 경로에서 쿠키가 유효하도록 설정

        return cookie;
    }

    private void saveToken(TokenResponse tokens, User user) {
        Token token = new Token(user.getUserId(), tokens.getRefreshToken(), tokens.getAccessToken());
        authService.saveToken(token);
    }

    public void register(RegisterRequest registerRequest) {
        User user = new User(registerRequest);

        userService.saveUser(user);
        userService.saveUserInfo(new UserInfo(user));
    }


    public boolean isEmailAvailable(String email) {
        return !userService.checkDuplicateEmail(email);
    }
}
