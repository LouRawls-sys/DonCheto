package ETL;

import ETL.TransformadorDatos.RegistroDemanda;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Carga las dimensiones y la tabla de hechos Historicos.
 * El nombre se escribe en minúsculas para coincidir con MySQL en el servidor.
 */
public class CargadorDataWarehouse {

    private static final String ORIGEN = "ETL_REAL";
    private static final DateTimeFormatter FORMATO_ID =
            DateTimeFormatter.BASIC_ISO_DATE;

    public int cargar(Connection conexion, List<RegistroDemanda> registros)
            throws SQLException {

        boolean autoCommitAnterior = conexion.getAutoCommit();

        try {
            conexion.setAutoCommit(false);

            Map<Integer, Integer> productosDimensionales = new HashMap<>();

            for (RegistroDemanda registro : registros) {
                validarRango(registro);
                insertarOActualizarTiempo(conexion, registro.getFecha());

                int productoDimId = obtenerOCrearProducto(
                        conexion,
                        registro.getProductoOrigenId(),
                        registro.getProducto(),
                        registro.getCategoria(),
                        productosDimensionales
                );

                insertarOActualizarHistorico(conexion, registro, productoDimId);
            }

            conexion.commit();
            return registros.size();

        } catch (SQLException | RuntimeException ex) {
            try {
                conexion.rollback();
            } catch (SQLException rollbackEx) {
                ex.addSuppressed(rollbackEx);
            }
            throw ex;
        } finally {
            conexion.setAutoCommit(autoCommitAnterior);
        }
    }

    private void validarRango(RegistroDemanda registro) throws SQLException {
        if (registro.getUnidades() > Integer.MAX_VALUE) {
            throw new SQLException("La demanda del producto "
                    + registro.getProducto() + " supera el límite INT.");
        }
    }

    private void insertarOActualizarTiempo(Connection cn, LocalDate fecha)
            throws SQLException {

        String sql = "INSERT INTO dim_tiempo "
                + "(fecha_id, fecha, dia, numero_dia_semana, nombre_dia, "
                + "numero_mes, nombre_mes, trimestre, anio, fin_semana) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "fecha=VALUES(fecha), dia=VALUES(dia), "
                + "numero_dia_semana=VALUES(numero_dia_semana), "
                + "nombre_dia=VALUES(nombre_dia), "
                + "numero_mes=VALUES(numero_mes), "
                + "nombre_mes=VALUES(nombre_mes), "
                + "trimestre=VALUES(trimestre), anio=VALUES(anio), "
                + "fin_semana=VALUES(fin_semana)";

        DayOfWeek diaSemana = fecha.getDayOfWeek();

        try (PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, Integer.parseInt(fecha.format(FORMATO_ID)));
            pst.setDate(2, Date.valueOf(fecha));
            pst.setInt(3, fecha.getDayOfMonth());
            // Convención ISO: lunes=1 ... domingo=7.
            pst.setInt(4, diaSemana.getValue());
            pst.setString(5, nombreDia(diaSemana));
            pst.setInt(6, fecha.getMonthValue());
            pst.setString(7, nombreMes(fecha.getMonthValue()));
            pst.setInt(8, ((fecha.getMonthValue() - 1) / 3) + 1);
            pst.setInt(9, fecha.getYear());
            pst.setInt(10, (diaSemana == DayOfWeek.SATURDAY
                    || diaSemana == DayOfWeek.SUNDAY) ? 1 : 0);
            pst.executeUpdate();
        }
    }

    private int obtenerOCrearProducto(Connection cn, int productoOrigenId,
            String nombre, String categoria, Map<Integer, Integer> cache)
            throws SQLException {

        Integer existente = cache.get(productoOrigenId);
        if (existente != null) {
            return existente;
        }

        String insertar = "INSERT INTO dim_producto "
                + "(producto_origen_id, nombre, categoria) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "nombre=VALUES(nombre), categoria=VALUES(categoria)";

        try (PreparedStatement pst = cn.prepareStatement(insertar)) {
            pst.setInt(1, productoOrigenId);
            pst.setString(2, nombre);
            pst.setString(3, categoria);
            pst.executeUpdate();
        }

        String consultar = "SELECT id FROM dim_producto "
                + "WHERE producto_origen_id = ?";

        try (PreparedStatement pst = cn.prepareStatement(consultar)) {
            pst.setInt(1, productoOrigenId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    cache.put(productoOrigenId, id);
                    return id;
                }
            }
        }

        throw new SQLException("No se pudo recuperar la dimensión del producto "
                + productoOrigenId);
    }

    private void insertarOActualizarHistorico(Connection cn,
            RegistroDemanda registro, int productoDimId) throws SQLException {

        String sql = "INSERT INTO historicos "
                + "(fecha_id, producto_id, unidades_vendidas, ingresos, "
                + "pedidos, origen) VALUES (?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "unidades_vendidas=VALUES(unidades_vendidas), "
                + "ingresos=VALUES(ingresos), "
                + "pedidos=VALUES(pedidos), cargado_en=CURRENT_TIMESTAMP";

        int fechaId = Integer.parseInt(
                registro.getFecha().format(FORMATO_ID));

        try (PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, fechaId);
            pst.setInt(2, productoDimId);
            pst.setInt(3, (int) registro.getUnidades());
            pst.setBigDecimal(4, registro.getIngresos());
            pst.setInt(5, registro.getPedidos());
            pst.setString(6, ORIGEN);
            pst.executeUpdate();
        }
    }

    private String nombreDia(DayOfWeek dia) {
        switch (dia) {
            case MONDAY: return "Lunes";
            case TUESDAY: return "Martes";
            case WEDNESDAY: return "Miercoles";
            case THURSDAY: return "Jueves";
            case FRIDAY: return "Viernes";
            case SATURDAY: return "Sabado";
            default: return "Domingo";
        }
    }

    private String nombreMes(int mes) {
        switch (mes) {
            case 1: return "Enero";
            case 2: return "Febrero";
            case 3: return "Marzo";
            case 4: return "Abril";
            case 5: return "Mayo";
            case 6: return "Junio";
            case 7: return "Julio";
            case 8: return "Agosto";
            case 9: return "Septiembre";
            case 10: return "Octubre";
            case 11: return "Noviembre";
            default: return "Diciembre";
        }
    }
}
