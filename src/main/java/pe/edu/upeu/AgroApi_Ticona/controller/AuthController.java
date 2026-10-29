package pe.edu.upeu.AgroApi_Ticona.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AgroApi_Ticona.dto.auth.LoginRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.auth.LoginResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.service.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {

        return ResponseEntity.ok(authService.login(request));
    }
}
