package upc.com.finanzas.ServiceImplements;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.com.finanzas.Entities.CalculoAmortizacion;
import upc.com.finanzas.Entities.Cuota;
import upc.com.finanzas.Entities.Usuario;
import upc.com.finanzas.dto.CalculoAmortizacionResponseDTO;
import upc.com.finanzas.dto.CuotaObtenidaDTO;
import upc.com.finanzas.dto.EntradaDatosDTO;
import upc.com.finanzas.repositories.CalculoAmortizacionRepository;
import upc.com.finanzas.repositories.CuotaRepository;
import upc.com.finanzas.repositories.UsuarioRepository;
import upc.com.finanzas.servicesInterface.AmortizacionService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;



@Service
public class AmortizacionSeriviceImplements implements AmortizacionService {

    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private CalculoAmortizacionRepository calculoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<CuotaObtenidaDTO> calcularCuotas(EntradaDatosDTO datos) {
        // CORREGIDO: Validaciones agregadas
        if (datos.getPrecioVenta() == null || datos.getPrecioVenta().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio de venta debe ser mayor a 0");
        }

        if (datos.getCuotaInicialPct() == null || datos.getCuotaInicialPct().compareTo(BigDecimal.ZERO) < 0
                || datos.getCuotaInicialPct().compareTo(BigDecimal.valueOf(100)) >= 100) {
            throw new IllegalArgumentException("La cuota inicial debe estar entre 0% y 99%");
        }

        if (datos.getTea() == null || datos.getTea().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La TEA debe ser mayor a 0");
        }

        if (datos.getNumeroAnios() == null || datos.getNumeroAnios() <= 0) {
            throw new IllegalArgumentException("El número de años debe ser mayor a 0");
        }

        if (datos.getPlazosGracia() == null || datos.getPlazosGracia().isEmpty()) {
            throw new IllegalArgumentException("Los plazos de gracia son requeridos");
        }

        BigDecimal precioVenta = datos.getPrecioVenta();
        BigDecimal cuotaInicialPct = datos.getCuotaInicialPct().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal tea = datos.getTea().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        List<String> plazosGracia = datos.getPlazosGracia();

        int frecuenciaAnual = switch (datos.getFrecuencia().toLowerCase()) {
            case "cuatri", "cuatrimestral" -> 3;
            case "mensual" -> 12;
            case "trimestral" -> 4;
            case "bimestral" -> 6;
            default -> throw new IllegalArgumentException("Frecuencia inválida: " + datos.getFrecuencia());
        };

        int totalPeriodos = frecuenciaAnual * datos.getNumeroAnios();

        // CORREGIDO: Validar que el número de plazos de gracia coincida con los períodos
        if (plazosGracia.size() != totalPeriodos) {
            throw new IllegalArgumentException("El número de plazos de gracia debe coincidir con el total de períodos (" + totalPeriodos + ")");
        }

        BigDecimal prestamo = precioVenta.multiply(BigDecimal.ONE.subtract(cuotaInicialPct));
        BigDecimal tec = BigDecimal.valueOf(Math.pow(1 + tea.doubleValue(), 1.0 / frecuenciaAnual) - 1)
                .setScale(10, RoundingMode.HALF_UP);

        // Calcular cuántos períodos requieren amortización (tipo "S")
        int periodosAmortizar = (int) plazosGracia.stream().filter(p -> p.equals("S")).count();

        if (periodosAmortizar == 0) {
            throw new IllegalArgumentException("Debe haber al menos un período sin gracia para amortizar");
        }

        // CORREGIDO: Mejor cálculo del saldo acumulado y amortización
        BigDecimal saldoAcumulado = calcularSaldoAcumulado(prestamo, plazosGracia, tec);
        BigDecimal amortizacionPeriodica = saldoAcumulado.divide(BigDecimal.valueOf(periodosAmortizar), 10, RoundingMode.HALF_UP);

        List<CuotaObtenidaDTO> cuotas = new ArrayList<>();
        BigDecimal saldo = prestamo;

        for (int i = 0; i < totalPeriodos; i++) {
            CuotaObtenidaDTO cuota = new CuotaObtenidaDTO();
            cuota.setNumero(i + 1);
            cuota.setPlazoGracia(plazosGracia.get(i));
            cuota.setSaldoInicial(saldo);

            BigDecimal interes = saldo.multiply(tec).setScale(2, RoundingMode.HALF_UP);
            cuota.setInteres(interes);

            BigDecimal amort = BigDecimal.ZERO;
            BigDecimal cuotaTotal = BigDecimal.ZERO;

            switch (plazosGracia.get(i)) {
                case "T" -> {
                    // Período de gracia total: no se paga nada, interés se capitaliza
                    cuotaTotal = BigDecimal.ZERO;
                    saldo = saldo.add(interes).setScale(2, RoundingMode.HALF_UP);
                }
                case "P" -> {
                    // Período de gracia parcial: solo se paga interés
                    cuotaTotal = interes;
                    // El saldo se mantiene igual
                }
                case "S" -> {
                    // Período sin gracia: se paga interés + amortización
                    amort = amortizacionPeriodica;

                    // CORREGIDO: Para la última cuota de amortización, ajustar para que saldo final sea 0
                    if (esUltimoPeriodoAmortizacion(plazosGracia, i)) {
                        amort = saldo.setScale(2, RoundingMode.HALF_UP);
                    }

                    cuotaTotal = interes.add(amort).setScale(2, RoundingMode.HALF_UP);
                    saldo = saldo.subtract(amort).setScale(2, RoundingMode.HALF_UP);

                    // CORREGIDO: Asegurar que el saldo no sea negativo
                    if (saldo.compareTo(BigDecimal.ZERO) < 0) {
                        saldo = BigDecimal.ZERO;
                    }
                }
                default -> throw new IllegalArgumentException("Tipo de plazo de gracia inválido: " + plazosGracia.get(i));
            }

            cuota.setAmortizacion(amort.setScale(2, RoundingMode.HALF_UP));
            cuota.setCuota(cuotaTotal);
            cuota.setSaldoFinal(saldo);

            cuotas.add(cuota);
        }

        return cuotas;
    }

    // CORREGIDO: Método auxiliar mejorado
    private BigDecimal calcularSaldoAcumulado(BigDecimal prestamo, List<String> plazosGracia, BigDecimal tec) {
        BigDecimal saldoAcumulado = prestamo;
        for (String tipo : plazosGracia) {
            if ("T".equals(tipo)) {
                BigDecimal interes = saldoAcumulado.multiply(tec);
                saldoAcumulado = saldoAcumulado.add(interes);
            }
        }
        return saldoAcumulado;
    }

    private boolean esUltimoPeriodoAmortizacion(List<String> plazosGracia, int indiceActual) {
        for (int i = indiceActual + 1; i < plazosGracia.size(); i++) {
            if ("S".equals(plazosGracia.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
    public CalculoAmortizacionResponseDTO calcularYGuardarCuotas(EntradaDatosDTO datos) {
        // CORREGIDO: Validación mejorada
        if (datos.getIdUsuario() == null) {
            throw new IllegalArgumentException("El ID del usuario es requerido");
        }

        if (datos.getNombreCalculo() == null || datos.getNombreCalculo().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cálculo es requerido");
        }

        // Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(datos.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + datos.getIdUsuario()));

        // Calcular cuotas
        List<CuotaObtenidaDTO> cuotas = calcularCuotas(datos);

        // Crear y guardar el cálculo de amortización
        CalculoAmortizacion calculo = new CalculoAmortizacion();
        calculo.setNombre(datos.getNombreCalculo()); // CORREGIDO: usar getNombreCalculo()
        calculo.setUsuario(usuario);
        calculo.setPrecioVenta(datos.getPrecioVenta());
        calculo.setCuotaInicialPct(datos.getCuotaInicialPct());
        calculo.setTea(datos.getTea());
        calculo.setNumeroAnios(datos.getNumeroAnios());
        calculo.setFrecuencia(datos.getFrecuencia());
        calculo.setPlazosGracia(String.join(",", datos.getPlazosGracia()));
        calculo.setFechaCreacion(LocalDateTime.now());

        CalculoAmortizacion calculoGuardado = calculoRepository.save(calculo);

        // Guardar las cuotas
        List<Cuota> cuotasEntity = cuotas.stream()
                .map(dto -> {
                    Cuota cuota = convertirDTOaEntity(dto);
                    cuota.setCalculoAmortizacion(calculoGuardado);
                    return cuota;
                })
                .collect(Collectors.toList());

        cuotaRepository.saveAll(cuotasEntity);

        // Crear y retornar el DTO de respuesta
        CalculoAmortizacionResponseDTO response = new CalculoAmortizacionResponseDTO();
        response.setIdCalculo(calculoGuardado.getIdCalculo());
        response.setNombre(calculoGuardado.getNombre());
        response.setFechaCreacion(calculoGuardado.getFechaCreacion());
        response.setPrecioVenta(calculoGuardado.getPrecioVenta());
        response.setCuotaInicialPct(calculoGuardado.getCuotaInicialPct());
        response.setTea(calculoGuardado.getTea());
        response.setNumeroAnios(calculoGuardado.getNumeroAnios());
        response.setFrecuencia(calculoGuardado.getFrecuencia());
        response.setPlazosGracia(datos.getPlazosGracia());
        response.setCuotas(cuotas);

        return response;
    }

    @Override
    public List<CalculoAmortizacionResponseDTO> obtenerCalculosPorUsuario(Long idUsuario) {
        // CORREGIDO: Validación agregada
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario es requerido");
        }

        List<CalculoAmortizacion> calculos = calculoRepository.findByUsuarioIdUsuarioOrderByFechaCreacionDesc(idUsuario);

        return calculos.stream().map(calculo -> {
            CalculoAmortizacionResponseDTO dto = new CalculoAmortizacionResponseDTO();
            dto.setIdCalculo(calculo.getIdCalculo());
            dto.setNombre(calculo.getNombre());
            dto.setFechaCreacion(calculo.getFechaCreacion());
            dto.setPrecioVenta(calculo.getPrecioVenta());
            dto.setCuotaInicialPct(calculo.getCuotaInicialPct());
            dto.setTea(calculo.getTea());
            dto.setNumeroAnios(calculo.getNumeroAnios());
            dto.setFrecuencia(calculo.getFrecuencia());

            // CORREGIDO: Manejo seguro de plazos de gracia
            String plazosGraciaStr = calculo.getPlazosGracia();
            List<String> plazosGracia = (plazosGraciaStr != null && !plazosGraciaStr.isEmpty())
                    ? Arrays.asList(plazosGraciaStr.split(","))
                    : new ArrayList<>();
            dto.setPlazosGracia(plazosGracia);

            // Cargar las cuotas
            List<Cuota> cuotas = cuotaRepository.findByCalculoAmortizacionIdCalculoOrderByNumero(calculo.getIdCalculo());
            List<CuotaObtenidaDTO> cuotasDTO = cuotas.stream()
                    .map(this::convertirEntityaDTO)
                    .collect(Collectors.toList());
            dto.setCuotas(cuotasDTO);

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public CalculoAmortizacionResponseDTO obtenerCalculoPorId(Long idCalculo) {
        // CORREGIDO: Validación agregada
        if (idCalculo == null) {
            throw new IllegalArgumentException("El ID del cálculo es requerido");
        }

        CalculoAmortizacion calculo = calculoRepository.findById(idCalculo)
                .orElseThrow(() -> new RuntimeException("Cálculo no encontrado con ID: " + idCalculo));

        CalculoAmortizacionResponseDTO dto = new CalculoAmortizacionResponseDTO();
        dto.setIdCalculo(calculo.getIdCalculo());
        dto.setNombre(calculo.getNombre());
        dto.setFechaCreacion(calculo.getFechaCreacion());
        dto.setPrecioVenta(calculo.getPrecioVenta());
        dto.setCuotaInicialPct(calculo.getCuotaInicialPct());
        dto.setTea(calculo.getTea());
        dto.setNumeroAnios(calculo.getNumeroAnios());
        dto.setFrecuencia(calculo.getFrecuencia());

        // CORREGIDO: Manejo seguro de plazos de gracia
        String plazosGraciaStr = calculo.getPlazosGracia();
        List<String> plazosGracia = (plazosGraciaStr != null && !plazosGraciaStr.isEmpty())
                ? Arrays.asList(plazosGraciaStr.split(","))
                : new ArrayList<>();
        dto.setPlazosGracia(plazosGracia);

        List<Cuota> cuotas = cuotaRepository.findByCalculoAmortizacionIdCalculoOrderByNumero(idCalculo);
        List<CuotaObtenidaDTO> cuotasDTO = cuotas.stream()
                .map(this::convertirEntityaDTO)
                .collect(Collectors.toList());
        dto.setCuotas(cuotasDTO);

        return dto;
    }

    @Override
    @Transactional
    public void eliminarCalculo(Long idCalculo) {
        // CORREGIDO: Validación agregada
        if (idCalculo == null) {
            throw new IllegalArgumentException("El ID del cálculo es requerido");
        }

        if (!calculoRepository.existsById(idCalculo)) {
            throw new RuntimeException("Cálculo no encontrado con ID: " + idCalculo);
        }

        // Primero eliminar las cuotas asociadas
        cuotaRepository.deleteByCalculoAmortizacionIdCalculo(idCalculo);

        // Luego eliminar el cálculo
        calculoRepository.deleteById(idCalculo);
    }

    private Cuota convertirDTOaEntity(CuotaObtenidaDTO dto) {
        Cuota cuota = new Cuota();
        cuota.setNumero(dto.getNumero());
        cuota.setPlazoGracia(dto.getPlazoGracia());
        cuota.setSaldoInicial(dto.getSaldoInicial());
        cuota.setInteres(dto.getInteres());
        cuota.setCuota(dto.getCuota());
        cuota.setAmortizacion(dto.getAmortizacion());
        cuota.setSaldoFinal(dto.getSaldoFinal());
        return cuota;
    }

    private CuotaObtenidaDTO convertirEntityaDTO(Cuota cuota) {
        CuotaObtenidaDTO dto = new CuotaObtenidaDTO();
        dto.setNumero(cuota.getNumero());
        dto.setPlazoGracia(cuota.getPlazoGracia());
        dto.setSaldoInicial(cuota.getSaldoInicial());
        dto.setInteres(cuota.getInteres());
        dto.setCuota(cuota.getCuota());
        dto.setAmortizacion(cuota.getAmortizacion());
        dto.setSaldoFinal(cuota.getSaldoFinal());
        return dto;
    }
}