package Modelo;

import java.math.BigDecimal;

public class DemandaCategoria {
    private final String categoria;
    private final long unidades;
    private final BigDecimal ingresos;
    private final BigDecimal participacion;

    public DemandaCategoria(String categoria, long unidades,
            BigDecimal ingresos, BigDecimal participacion) {
        this.categoria = categoria;
        this.unidades = unidades;
        this.ingresos = ingresos;
        this.participacion = participacion;
    }

    public String getCategoria() { return categoria; }
    public long getUnidades() { return unidades; }
    public BigDecimal getIngresos() { return ingresos; }
    public BigDecimal getParticipacion() { return participacion; }
}
