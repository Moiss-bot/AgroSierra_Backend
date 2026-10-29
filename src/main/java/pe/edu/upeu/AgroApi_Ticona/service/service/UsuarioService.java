package pe.edu.upeu.AgroApi_Ticona.service.service;

import pe.edu.upeu.AgroApi_Ticona.dto.auth.EmpleadoResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.auth.RolResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.auth.UsuarioRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.auth.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO crear(UsuarioRequestDTO request);

    List<UsuarioResponseDTO> listar();

    List<EmpleadoResponseDTO> listarEmpleados();

    List<RolResponseDTO> listarRoles();
}
