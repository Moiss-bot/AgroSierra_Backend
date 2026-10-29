package pe.edu.upeu.AgroApi_Ticona.service.service;

import pe.edu.upeu.AgroApi_Ticona.dto.auth.LoginRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.auth.LoginResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.auth.UsuarioResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO request);

    UsuarioResponseDTO usuarioActual(String username);
}
