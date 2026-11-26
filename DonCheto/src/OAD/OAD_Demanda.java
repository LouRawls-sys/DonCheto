package OAD;

import Conexion.Conexion;
import Modelo.DemandaCategoria;
import Modelo.DemandaProducto;
import Modelo.ResumenDemanda;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a los datos analiticos de demanda. Todas las consultas se limitan
 * a la simulacion indicada y leen el catalogo operativo de productos.
 */
public class OAD_Demanda {

    public ResumenDemanda obtenerResumen(int simulacionId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(unidades_vendidas),0), "
                + "COALESCE(SUM(ingreso_estimado),0), "
                + "COUNT(DISTINCT producto_id), COUNT(DISTINCT fecha) "
                + "FROM satd_hechos_demanda WHERE simulacion_id = ?";

        try (Connection cn = abrirConexion();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, simulacionId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new ResumenDemanda(
                            rs.getLong(1), rs.getBigDecimal(2),
                            rs.getInt(3), rs.getInt(4));
                }
            }
        }
        return new ResumenDemanda(0, BigDecimal.ZERO, 0, 0);
    }

    public List<DemandaProducto> obtenerDemandaPorProducto(int simulacionId)
            throws SQLException {
        String sql = "SELECT p.id, p.nombre, p.categoria, "
                + "COALESCE(SUM(d.unidades_vendidas),0) AS unidades, "
                + "COALESCE(SUM(d.ingreso_estimado),0) AS ingresos, "
                + "COALESCE(AVG(d.unidades_vendidas),0) AS promedio "
                + "FROM satd_hechos_demanda d "
                + "INNER JOIN productos p ON p.id = d.producto_id "
                + "WHERE d.simulacion_id = ? "
                + "GROUP BY p.id, p.nombre, p.categoria "
                + "ORDER BY unidades DESC, p.nombre ASC";

        List<DemandaProducto> lista = new ArrayList<>();
        try (Connection cn = abrirConexion();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, simulacionId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DemandaProducto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getLong("unidades"),
                            rs.getBigDecimal("ingresos"),
                            rs.getBigDecimal("promedio"),
                            BigDecimal.ZERO));
                }
            }
        }
        return lista;
    }

    public List<DemandaCategoria> obtenerDemandaPorCategoria(int simulacionId)
            throws SQLException {
        String sql = "SELECT p.categoria, "
                + "COALESCE(SUM(d.unidades_vendidas),0) AS unidades, "
                + "COALESCE(SUM(d.ingreso_estimado),0) AS ingresos "
                + "FROM satd_hechos_demanda d "
                + "INNER JOIN productos p ON p.id = d.producto_id "
                + "WHERE d.simulacion_id = ? "
                + "GROUP BY p.categoria "
                + "ORDER BY unidades DESC, p.categoria ASC";

        List<DemandaCategoria> lista = new ArrayList<>();
        try (Connection cn = abrirConexion();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, simulacionId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DemandaCategoria(
                            rs.getString("categoria"),
                            rs.getLong("unidades"),
                            rs.getBigDecimal("ingresos"),
                            BigDecimal.ZERO));
                }
            }
        }
        return lista;
    }

    private Connection abrirConexion() throws SQLException {
        Connection cn = Conexion.conectar();
        if (cn == null) {
            throw new SQLException("No fue posible establecer la conexion con MySQL.");
        }
        return cn;
    }

    // Metodos conservados para que las pruebas anteriores sigan funcionando.
    public long obtenerTotalUnidades(int simulacionId) {
        try { return obtenerResumen(simulacionId).getUnidades(); }
        catch (SQLException e) {
            System.err.println("Error al consultar unidades: " + e.getMessage());
            return 0;
        }
    }

    public BigDecimal obtenerIngresos(int simulacionId) {
        try { return obtenerResumen(simulacionId).getIngresos(); }
        catch (SQLException e) {
            System.err.println("Error al consultar ingresos: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public int obtenerTotalProductos(int simulacionId) {
        try { return obtenerResumen(simulacionId).getProductos(); }
        catch (SQLException e) {
            System.err.println("Error al consultar productos: " + e.getMessage());
            return 0;
        }
    }

    public int obtenerTotalDias(int simulacionId) {
        try { return obtenerResumen(simulacionId).getDias(); }
        catch (SQLException e) {
            System.err.println("Error al consultar dias: " + e.getMessage());
            return 0;
        }
    }
}
