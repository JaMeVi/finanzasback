package upc.com.finanzas.ServiceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.com.finanzas.Entities.Rol;
import upc.com.finanzas.Entities.Usuario;
import upc.com.finanzas.dto.CrearUsuarioDTO;
import upc.com.finanzas.dto.LoginDTO;
import upc.com.finanzas.dto.UsuarioResponseDTO;
import upc.com.finanzas.repositories.RolRepository;
import upc.com.finanzas.repositories.UsuarioRepository;
import upc.com.finanzas.servicesInterface.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Override
    public UsuarioResponseDTO login(LoginDTO loginDTO) {
        // CORREGIDO: Validaciones agregadas
        if (loginDTO.getEmail() == null || loginDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }

        if (loginDTO.getPassword() == null || loginDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }

        Optional<Usuario> usuario = usuarioRepository.findByEmailAndPassword(
                loginDTO.getEmail().trim(),
                loginDTO.getPassword()
        );

        if (usuario.isPresent() && usuario.get().getActivo()) {
            return new UsuarioResponseDTO(usuario.get());
        }

        throw new RuntimeException("Credenciales inválidas o usuario inactivo");
    }

    @Override
    public UsuarioResponseDTO crearUsuario(CrearUsuarioDTO crearUsuarioDTO) {
        // CORREGIDO: Validaciones mejoradas
        if (crearUsuarioDTO.getNombre() == null || crearUsuarioDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }

        if (crearUsuarioDTO.getEmail() == null || crearUsuarioDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }

        if (crearUsuarioDTO.getPassword() == null || crearUsuarioDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }

        if (crearUsuarioDTO.getIdRol() == null) {
            throw new IllegalArgumentException("El rol es requerido");
        }

        // Verificar si el email ya está en uso
        if (usuarioRepository.findByEmail(crearUsuarioDTO.getEmail().trim()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Obtener el rol
        Rol rol = rolRepository.findById(crearUsuarioDTO.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + crearUsuarioDTO.getIdRol()));

        // Crear el usuario
        Usuario usuario = new Usuario(
                crearUsuarioDTO.getNombre().trim(),
                crearUsuarioDTO.getEmail().trim(),
                crearUsuarioDTO.getPassword(), // NOTA: En producción, la contraseña debería estar hasheada
                rol
        );

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuarioGuardado);
    }

    @Override
    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findByActivoTrue().stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        // CORREGIDO: Validación agregada
        if (id == null) {
            throw new IllegalArgumentException("El ID del usuario es requerido");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return new UsuarioResponseDTO(usuario);
    }

    @Override
    public boolean existeUsuario(Long id) {
        // CORREGIDO: Validación agregada
        if (id == null) {
            return false;
        }
        return usuarioRepository.existsById(id);
    }
}