package Modelo;

import java.math.BigDecimal;

public class ResumenDemanda {
    private final long unidades;
    private final BigDecimal ingresos;
    private final int productos;
    private final int dias;

    public ResumenDemanda(long unidades, BigDecimal ingresos, int productos, int dias) {
        this.unidades = unidades;
        this.ingresos = ingresos;
        this.productos = productos;
        this.dias = dias;
    }

    public long getUnidades() { return unidades; }
    public BigDecimal getIngresos() { return ingresos; }
    public int getProductos() { return productos; }
    public int getDias() { return dias; }
}
