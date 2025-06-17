package upc.com.finanzas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upc.com.finanzas.Entities.CalculoAmortizacion;

import java.util.List;

@Repository
public interface CalculoAmortizacionRepository extends JpaRepository<CalculoAmortizacion, Long> {
    List<CalculoAmortizacion> findByUsuarioIdUsuario(Long idUsuario);
    List<CalculoAmortizacion> findByUsuarioIdUsuarioOrderByFechaCreacionDesc(Long idUsuario);
}
