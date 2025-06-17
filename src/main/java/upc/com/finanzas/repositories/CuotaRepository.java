package upc.com.finanzas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upc.com.finanzas.Entities.Cuota;

import java.util.List;

@Repository
public interface CuotaRepository extends  JpaRepository<Cuota, Long> {
    List<Cuota> findByCalculoAmortizacionIdCalculoOrderByNumero(Long idCalculo);
    void deleteByCalculoAmortizacionIdCalculo(Long idCalculo);
}
