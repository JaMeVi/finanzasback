package upc.com.finanzas.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.com.finanzas.dto.CalculoAmortizacionResponseDTO;
import upc.com.finanzas.dto.CuotaObtenidaDTO;
import upc.com.finanzas.dto.EntradaDatosDTO;
import upc.com.finanzas.servicesInterface.AmortizacionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/amortizacion")
@CrossOrigin
public class AmortizacionController {

    @Autowired
    private AmortizacionService amortizacionService;

    @PostMapping("/calcular")
    public ResponseEntity<?> calcularCuotas(@RequestBody EntradaDatosDTO datos) {
        try {
            List<CuotaObtenidaDTO> cuotas = amortizacionService.calcularCuotas(datos);
            return ResponseEntity.ok(cuotas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Datos inválidos", "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @PostMapping("/calcular-y-guardar")
    public ResponseEntity<?> calcularYGuardarCuotas(@RequestBody EntradaDatosDTO datos) {
        try {
            CalculoAmortizacionResponseDTO resultado = amortizacionService.calcularYGuardarCuotas(datos);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Datos inválidos", "mensaje", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Recurso no encontrado", "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerCalculosPorUsuario(@PathVariable Long idUsuario) {
        try {
            List<CalculoAmortizacionResponseDTO> calculos = amortizacionService.obtenerCalculosPorUsuario(idUsuario);
            return ResponseEntity.ok(calculos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Datos inválidos", "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @GetMapping("/{idCalculo}")
    public ResponseEntity<?> obtenerCalculoPorId(@PathVariable Long idCalculo) {
        try {
            CalculoAmortizacionResponseDTO calculo = amortizacionService.obtenerCalculoPorId(idCalculo);
            return ResponseEntity.ok(calculo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Datos inválidos", "mensaje", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Recurso no encontrado", "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }

    @DeleteMapping("/{idCalculo}")
    public ResponseEntity<?> eliminarCalculo(@PathVariable Long idCalculo) {
        try {
            amortizacionService.eliminarCalculo(idCalculo);
            return ResponseEntity.ok(Map.of("mensaje", "Cálculo eliminado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Datos inválidos", "mensaje", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Recurso no encontrado", "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }
}
