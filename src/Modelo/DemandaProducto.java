package Modelo;

import java.math.BigDecimal;

public class DemandaProducto {
    private final int productoId;
    private final String nombre;
    private final String categoria;
    private final long unidades;
    private final BigDecimal ingresos;
    private final BigDecimal promedioDiario;
    private final BigDecimal participacion;

    public DemandaProducto(int productoId, String nombre, String categoria,
            long unidades, BigDecimal ingresos, BigDecimal promedioDiario,
            BigDecimal participacion) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.categoria = categoria;
        this.unidades = unidades;
        this.ingresos = ingresos;
        this.promedioDiario = promedioDiario;
        this.participacion = participacion;
    }

    public int getProductoId() { return productoId; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public long getUnidades() { return unidades; }
    public BigDecimal getIngresos() { return ingresos; }
    public BigDecimal getPromedioDiario() { return promedioDiario; }
    public BigDecimal getParticipacion() { return participacion; }
}
