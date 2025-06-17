package upc.com.finanzas.dto;
import java.math.BigDecimal;
import java.util.List;

public class EntradaDatosDTO {
    private Long idUsuario;
    private String nombreCalculo;
    private BigDecimal precioVenta;
    private BigDecimal cuotaInicialPct;
    private BigDecimal tea;
    private Integer numeroAnios;
    private String frecuencia;
    private List<String> plazosGracia;

    public EntradaDatosDTO() {}

     public String getNombre() {
        return nombreCalculo;
    }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCalculo() { return nombreCalculo; }
    public void setNombreCalculo(String nombreCalculo) { this.nombreCalculo = nombreCalculo; }

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
}