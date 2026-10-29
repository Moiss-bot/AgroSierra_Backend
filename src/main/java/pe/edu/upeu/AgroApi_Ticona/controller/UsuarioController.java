
package pe.edu.upeu.AgroApi_Ticona.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AgroApi_Ticona.dto.auth.EmpleadoResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.auth.RolResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.auth.UsuarioRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.auth.UsuarioResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.service.service.AuthService;
import pe.edu.upeu.AgroApi_Ticona.service.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthService authService;

    public UsuarioController(
            UsuarioService usuarioService,
            AuthService authService) {

        this.usuarioService = usuarioService;
        this.authService = authService;
    }

    /*
     * Perfil del usuario autenticado.
     */
    @GetMapping("/usuarios/me")
    public ResponseEntity<UsuarioResponseDTO> me(
            Authentication authentication) {

        return ResponseEntity.ok(
                authService.usuarioActual(authentication.getName())
        );
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponseDTO> crear(
            @Valid @RequestBody UsuarioRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.crear(request));
    }

    @GetMapping("/empleados")
    public ResponseEntity<List<EmpleadoResponseDTO>> empleados() {
        return ResponseEntity.ok(usuarioService.listarEmpleados());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RolResponseDTO>> roles() {
        return ResponseEntity.ok(usuarioService.listarRoles());
    }
}
