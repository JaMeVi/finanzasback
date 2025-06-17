package upc.com.finanzas.dto;
import java.math.BigDecimal;

public class CuotaObtenidaDTO {
    private int numero;
    private String plazoGracia;
    private BigDecimal saldoInicial;
    private BigDecimal interes;
    private BigDecimal cuota;
    private BigDecimal amortizacion;
    private BigDecimal saldoFinal;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getPlazoGracia() {
        return plazoGracia;
    }

    public void setPlazoGracia(String plazoGracia) {
        this.plazoGracia = plazoGracia;
    }

    public BigDecimal getCuota() {
        return cuota;
    }

    public void setCuota(BigDecimal cuota) {
        this.cuota = cuota;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getAmortizacion() {
        return amortizacion;
    }

    public void setAmortizacion(BigDecimal amortizacion) {
        this.amortizacion = amortizacion;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }
}
