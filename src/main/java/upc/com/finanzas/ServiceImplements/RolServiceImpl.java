package upc.com.finanzas.ServiceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.com.finanzas.Entities.Rol;
import upc.com.finanzas.repositories.RolRepository;
import upc.com.finanzas.servicesInterface.RolService;

import java.util.List;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    @Override
    public Rol obtenerRolPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }

    @Override
    public Rol crearRol(String nombre, String descripcion) {
        // CORREGIDO: Usar método correcto del repositorio
        if (rolRepository.findByNombre(nombre).isPresent()) {
            throw new RuntimeException("El rol ya existe");
        }

        Rol rol = new Rol(nombre, descripcion);
        return rolRepository.save(rol);
    }
}