package upc.com.finanzas.servicesInterface;

import upc.com.finanzas.dto.CalculoAmortizacionResponseDTO;
import upc.com.finanzas.dto.CuotaObtenidaDTO;
import upc.com.finanzas.dto.EntradaDatosDTO;

import java.util.List;

public interface AmortizacionService {

     List<CuotaObtenidaDTO> calcularCuotas(EntradaDatosDTO datos);
     CalculoAmortizacionResponseDTO calcularYGuardarCuotas(EntradaDatosDTO datos);
     List<CalculoAmortizacionResponseDTO> obtenerCalculosPorUsuario(Long idUsuario);
     CalculoAmortizacionResponseDTO obtenerCalculoPorId(Long idCalculo);
     void eliminarCalculo(Long idCalculo);
}
