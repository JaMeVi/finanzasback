package upc.com.finanzas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CalculoAmortizacionResponseDTO {
    private Long idCalculo;
    private String nombre;
    private LocalDateTime fechaCreacion;
    private BigDecimal precioVenta;
    private BigDecimal cuotaInicialPct;
    private BigDecimal tea;
    private Integer numeroAnios;
    private String frecuencia;
    private List<String> plazosGracia;
    private List<CuotaObtenidaDTO> cuotas;

    public CalculoAmortizacionResponseDTO() {}

    public Long getIdCalculo() { return idCalculo; }
    public void setIdCalculo(Long idCalculo) { this.idCalculo = idCalculo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public BigDecimal getCuotaInicialPct() { return cuotaInicialPct; }
    public void setCuotaInicialPct(BigDecimal cuotaInicialPct) { this.cuotaInicialPct = cuotaInicialPct; }

    public BigDecimal getTea() { return tea; }
    public void setTea(BigDecimal tea) { this.tea = tea; }

    public Integer getNumeroAnios() { return numeroAnios; }
    public void setNumeroAnios(Integer numeroAnios) { this.numeroAnios = numeroAnios; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public List<String> getPlazosGracia() { return plazosGracia; }
    public void setPlazosGracia(List<String> plazosGracia) { this.plazosGracia = plazosGracia; }

    public List<CuotaObtenidaDTO> getCuotas() { return cuotas; }
    public void setCuotas(List<CuotaObtenidaDTO> cuotas) { this.cuotas = cuotas; }
}