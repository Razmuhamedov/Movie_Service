package ms.movie_service.controller;

import ms.movie_service.dto.auth.AuthDto;
import ms.movie_service.dto.auth.LoginResultDto;
import ms.movie_service.dto.auth.SignUpDto;
import ms.movie_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
        private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto dto){
        String result = authService.signUp(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDto dto){
        LoginResultDto result = authService.login(dto);
        return ResponseEntity.ok(result);

    }

    @GetMapping("/verification/{token}")
    public ResponseEntity<?> verification(@PathVariable("token") String token){
        String result = authService.verification(token);
        return ResponseEntity.ok(result);
    }
}
