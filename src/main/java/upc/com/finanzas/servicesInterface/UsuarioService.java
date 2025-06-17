package upc.com.finanzas.servicesInterface;

import upc.com.finanzas.dto.CrearUsuarioDTO;
import upc.com.finanzas.dto.LoginDTO;
import upc.com.finanzas.dto.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO login(LoginDTO loginDTO);
    UsuarioResponseDTO crearUsuario(CrearUsuarioDTO crearUsuarioDTO);
    List<UsuarioResponseDTO> obtenerTodosLosUsuarios();
    UsuarioResponseDTO obtenerUsuarioPorId(Long id);
    boolean existeUsuario(Long id);
}