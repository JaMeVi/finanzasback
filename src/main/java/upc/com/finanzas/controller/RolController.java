package upc.com.finanzas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.com.finanzas.Entities.Rol;
import upc.com.finanzas.dto.CrearRolDTO;
import upc.com.finanzas.servicesInterface.RolService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<?> obtenerTodosLosRoles() {
        try {
            List<Rol> roles = rolService.obtenerTodosLosRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRolPorId(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Datos inválidos", "mensaje", "El ID del rol es requerido"));
            }

            Rol rol = rolService.obtenerRolPorId(id);
            return ResponseEntity.ok(rol);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Recurso no encontrado", "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crearRol(@RequestBody CrearRolDTO crearRolDTO) {
        try {
            // Validaciones
            if (crearRolDTO.getNombre() == null || crearRolDTO.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Datos inválidos", "mensaje", "El nombre del rol es requerido"));
            }

            if (crearRolDTO.getDescripcion() == null || crearRolDTO.getDescripcion().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Datos inválidos", "mensaje", "La descripción del rol es requerida"));
            }

            Rol rol = rolService.crearRol(crearRolDTO.getNombre().trim(), crearRolDTO.getDescripcion().trim());
            return ResponseEntity.status(HttpStatus.CREATED).body(rol);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Conflicto", "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }
}