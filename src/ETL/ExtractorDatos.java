package ETL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ventas pagadas
 */
public class ExtractorDatos {

    private static final String SQL_EXTRAER =
            "SELECT p.id AS pedido_id, p.creado_en AS fecha_pedido, "
          + "pd.producto_id, pr.nombre AS producto, pr.categoria, "
          + "pd.cantidad, pd.precio_unitario, pd.subtotal "
          + "FROM pedidos p "
          + "INNER JOIN pedido_detalles pd ON pd.pedido_id = p.id "
          + "INNER JOIN productos pr ON pr.id = pd.producto_id "
          + "WHERE p.estado = 'ENTREGADO' AND p.pagado = 1 "
          + "ORDER BY p.creado_en, p.id, pd.id";

    public List<RegistroVenta> extraer(Connection conexion) throws SQLException {
        List<RegistroVenta> registros = new ArrayList<>();

        try (PreparedStatement pst = conexion.prepareStatement(SQL_EXTRAER);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Timestamp fecha = rs.getTimestamp("fecha_pedido");
                BigDecimal precio = rs.getBigDecimal("precio_unitario");
                BigDecimal subtotal = rs.getBigDecimal("subtotal");

                registros.add(new RegistroVenta(
                        rs.getInt("pedido_id"),
                        fecha,
                        rs.getInt("producto_id"),
                        rs.getString("producto"),
                        rs.getString("categoria"),
                        rs.getInt("cantidad"),
                        precio,
                        subtotal
                ));
            }
        }

        return registros;
    }

    //detalle de tps
    public static class RegistroVenta {
        private final int pedidoId;
        private final Timestamp fecha;
        private final int productoId;
        private final String producto;
        private final String categoria;
        private final int cantidad;
        private final BigDecimal precioUnitario;
        private final BigDecimal subtotal;

        public RegistroVenta(int pedidoId, Timestamp fecha, int productoId,
                String producto, String categoria, int cantidad,
                BigDecimal precioUnitario, BigDecimal subtotal) {
            this.pedidoId = pedidoId;
            this.fecha = fecha;
            this.productoId = productoId;
            this.producto = producto;
            this.categoria = categoria;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = subtotal;
        }

        public int getPedidoId() { return pedidoId; }
        public Timestamp getFecha() { return fecha; }
        public int getProductoId() { return productoId; }
        public String getProducto() { return producto; }
        public String getCategoria() { return categoria; }
        public int getCantidad() { return cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public BigDecimal getSubtotal() { return subtotal; }
    }
}
