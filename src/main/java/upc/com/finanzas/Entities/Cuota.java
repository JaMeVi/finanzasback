package upc.com.finanzas.Entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cuota")
public class Cuota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCalculo", nullable = false)
    private CalculoAmortizacion calculoAmortizacion;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "plazoGracia", nullable = false, length = 10)
    private String plazoGracia;

    @Column(name = "saldoInicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "interes", nullable = false, precision = 15, scale = 2)
    private BigDecimal interes;

    @Column(name = "cuota", nullable = false, precision = 15, scale = 2)
    private BigDecimal cuota;

    @Column(name = "amortizacion", nullable = false, precision = 15, scale = 2)
    private BigDecimal amortizacion;

    @Column(name = "saldoFinal", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoFinal;

    // Constructores
    public Cuota() {}

    // Getters y Setters
    public Long getIdCuota() { return idCuota; }
    public void setIdCuota(Long idCuota) { this.idCuota = idCuota; }

    public CalculoAmortizacion getCalculoAmortizacion() { return calculoAmortizacion; }
    public void setCalculoAmortizacion(CalculoAmortizacion calculoAmortizacion) { this.calculoAmortizacion = calculoAmortizacion; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public String getPlazoGracia() { return plazoGracia; }
    public void setPlazoGracia(String plazoGracia) { this.plazoGracia = plazoGracia; }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public BigDecimal getInteres() { return interes; }
    public void setInteres(BigDecimal interes) { this.interes = interes; }

    public BigDecimal getCuota() { return cuota; }
    public void setCuota(BigDecimal cuota) { this.cuota = cuota; }

    public BigDecimal getAmortizacion() { return amortizacion; }
    public void setAmortizacion(BigDecimal amortizacion) { this.amortizacion = amortizacion; }

    public BigDecimal getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(BigDecimal saldoFinal) { this.saldoFinal = saldoFinal; }
}