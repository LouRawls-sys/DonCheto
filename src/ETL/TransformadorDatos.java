package ETL;

import ETL.ExtractorDatos.RegistroVenta;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Valida las líneas extraídas y las consolida por fecha y producto.
 */
public class TransformadorDatos {

    public ResultadoTransformacion transformar(List<RegistroVenta> entradas) {
        Map<ClaveDemanda, RegistroDemanda> agrupados = new LinkedHashMap<>();
        int descartados = 0;

        for (RegistroVenta venta : entradas) {
            if (!esValida(venta)) {
                descartados++;
                continue;
            }

            LocalDate fecha = venta.getFecha().toLocalDateTime().toLocalDate();
            ClaveDemanda clave = new ClaveDemanda(fecha, venta.getProductoId());

            RegistroDemanda acumulado = agrupados.get(clave);
            if (acumulado == null) {
                acumulado = new RegistroDemanda(
                        fecha,
                        venta.getProductoId(),
                        venta.getProducto().trim(),
                        normalizarCategoria(venta.getCategoria())
                );
                agrupados.put(clave, acumulado);
            }

            acumulado.agregar(
                    venta.getPedidoId(),
                    venta.getCantidad(),
                    venta.getSubtotal()
            );
        }

        return new ResultadoTransformacion(
                new ArrayList<>(agrupados.values()),
                descartados
        );
    }

    private boolean esValida(RegistroVenta venta) {
        if (venta == null
                || venta.getFecha() == null
                || venta.getPedidoId() <= 0
                || venta.getProductoId() <= 0
                || venta.getProducto() == null
                || venta.getProducto().trim().isEmpty()
                || venta.getCantidad() <= 0
                || venta.getPrecioUnitario() == null
                || venta.getSubtotal() == null) {
            return false;
        }

        return venta.getPrecioUnitario().compareTo(BigDecimal.ZERO) >= 0
                && venta.getSubtotal().compareTo(BigDecimal.ZERO) >= 0;
    }

    private String normalizarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return "General";
        }
        return categoria.trim();
    }

    private static class ClaveDemanda {
        private final LocalDate fecha;
        private final int productoId;

        private ClaveDemanda(LocalDate fecha, int productoId) {
            this.fecha = fecha;
            this.productoId = productoId;
        }

        @Override
        public boolean equals(Object objeto) {
            if (this == objeto) return true;
            if (!(objeto instanceof ClaveDemanda)) return false;
            ClaveDemanda otra = (ClaveDemanda) objeto;
            return productoId == otra.productoId
                    && Objects.equals(fecha, otra.fecha);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fecha, productoId);
        }
    }

    public static class RegistroDemanda {
        private final LocalDate fecha;
        private final int productoOrigenId;
        private final String producto;
        private final String categoria;
        private long unidades;
        private BigDecimal ingresos = BigDecimal.ZERO;
        private final Set<Integer> pedidosUnicos = new HashSet<>();

        public RegistroDemanda(LocalDate fecha, int productoOrigenId,
                String producto, String categoria) {
            this.fecha = fecha;
            this.productoOrigenId = productoOrigenId;
            this.producto = producto;
            this.categoria = categoria;
        }

        private void agregar(int pedidoId, int cantidad, BigDecimal subtotal) {
            unidades += cantidad;
            ingresos = ingresos.add(subtotal);
            pedidosUnicos.add(pedidoId);
        }

        public LocalDate getFecha() { return fecha; }
        public int getProductoOrigenId() { return productoOrigenId; }
        public String getProducto() { return producto; }
        public String getCategoria() { return categoria; }
        public long getUnidades() { return unidades; }
        public BigDecimal getIngresos() { return ingresos; }
        public int getPedidos() { return pedidosUnicos.size(); }
    }

    public static class ResultadoTransformacion {
        private final List<RegistroDemanda> registros;
        private final int descartados;

        public ResultadoTransformacion(List<RegistroDemanda> registros,
                int descartados) {
            this.registros = registros;
            this.descartados = descartados;
        }

        public List<RegistroDemanda> getRegistros() { return registros; }
        public int getDescartados() { return descartados; }
    }
}
