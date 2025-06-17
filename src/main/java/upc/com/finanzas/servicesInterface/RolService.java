package upc.com.finanzas.servicesInterface;

import upc.com.finanzas.Entities.Rol;

import java.util.List;


public interface RolService {
    List<Rol> obtenerTodosLosRoles();
    Rol obtenerRolPorId(Long id);
    Rol crearRol(String nombre, String descripcion);
}
