package SJUCapstone.BE.auth.controller;

import SJUCapstone.BE.auth.dto.LoginRequest;
import SJUCapstone.BE.auth.dto.LoginResponse;
import SJUCapstone.BE.auth.dto.RegisterRequest;
import SJUCapstone.BE.auth.dto.ServerLoginResponse;
import SJUCapstone.BE.auth.exception.InvalidPasswordException;
import SJUCapstone.BE.auth.exception.UserNotFoundException;
import SJUCapstone.BE.auth.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            ServerLoginResponse serverLoginResponse = loginService.login(loginRequest);

            response.addCookie(serverLoginResponse.getCookie());
            // SameSite 설정
            response.addHeader("Set-Cookie",
                    String.format("%s=%s; Path=/; HttpOnly; Secure; SameSite=None",
                            serverLoginResponse.getCookie().getName(),
                            serverLoginResponse.getCookie().getValue()));

            LoginResponse loginResponse = serverLoginResponse.getLoginResponse();

            return ResponseEntity.ok(loginResponse);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            loginService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/check-email")
    @ResponseBody
    public Boolean checkEmail(@RequestParam("email") String email) {
        return loginService.isEmailAvailable(email);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // "accessToken" 쿠키를 찾아서 삭제
        response.addCookie(loginService.logout());

        response.addHeader("Set-Cookie",
                String.format("%s=%s; Path=/; HttpOnly; Secure; SameSite=None",
                        "accessToken",
                        null));
        return ResponseEntity.noContent().build();
        // 필요한 경우, 서버에서 추가적인 처리 (예: accessToken 무효화)
        // 예: tokenService.invalidateToken(accessToken);
    }
}
