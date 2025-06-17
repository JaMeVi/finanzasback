package upc.com.finanzas.Entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "calculo_amortizacion")
public class CalculoAmortizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCalculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre; // Nombre descriptivo del cálculo

    @Column(name = "fechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    // Parámetros del cálculo (para referencia)
    @Column(name = "precioVenta", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "cuotaInicialPct", nullable = false, precision = 5, scale = 2)
    private BigDecimal cuotaInicialPct;

    @Column(name = "tea", nullable = false, precision = 5, scale = 4)
    private BigDecimal tea;

    @Column(name = "numeroAnios", nullable = false)
    private Integer numeroAnios;

    @Column(name = "frecuencia", nullable = false, length = 20)
    private String frecuencia;

    @Column(name = "plazosGracia", nullable = false, length = 500)
    private String plazosGracia; // Guardado como JSON string

    @OneToMany(mappedBy = "calculoAmortizacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cuota> cuotas = new ArrayList<>();

    // Constructores
    public CalculoAmortizacion() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getIdCalculo() { return idCalculo; }
    public void setIdCalculo(Long idCalculo) { this.idCalculo = idCalculo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

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

    public String getPlazosGracia() { return plazosGracia; }
    public void setPlazosGracia(String plazosGracia) { this.plazosGracia = plazosGracia; }

    public List<Cuota> getCuotas() { return cuotas; }
    public void setCuotas(List<Cuota> cuotas) { this.cuotas = cuotas; }
}
