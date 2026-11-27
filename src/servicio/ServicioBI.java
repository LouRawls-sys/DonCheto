package Servicio;

import Modelo.DemandaCategoria;
import Modelo.DemandaPeriodo;
import Modelo.DemandaProducto;
import Modelo.ResumenDemanda;
import OAD.OAD_Demanda;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/** Reglas de calculo del primer modulo de inteligencia de negocios. */
public class ServicioBI {
    private final OAD_Demanda oad = new OAD_Demanda();

    public ResumenDemanda obtenerResumen(int simulacionId) throws SQLException {
        return oad.obtenerResumen(simulacionId);
    }

    public List<DemandaProducto> obtenerProductos(int simulacionId)
            throws SQLException {
        List<DemandaProducto> originales = oad.obtenerDemandaPorProducto(simulacionId);
        long total = 0;
        for (DemandaProducto p : originales) total += p.getUnidades();

        List<DemandaProducto> resultado = new ArrayList<>();
        for (DemandaProducto p : originales) {
            BigDecimal porcentaje = porcentaje(p.getUnidades(), total);
            resultado.add(new DemandaProducto(
                    p.getProductoId(), p.getNombre(), p.getCategoria(),
                    p.getUnidades(), p.getIngresos(), p.getPromedioDiario(),
                    porcentaje));
        }
        return resultado;
    }

    public List<DemandaCategoria> obtenerCategorias(int simulacionId)
            throws SQLException {
        List<DemandaCategoria> originales = oad.obtenerDemandaPorCategoria(simulacionId);
        long total = 0;
        for (DemandaCategoria c : originales) total += c.getUnidades();

        List<DemandaCategoria> resultado = new ArrayList<>();
        for (DemandaCategoria c : originales) {
            resultado.add(new DemandaCategoria(
                    c.getCategoria(), c.getUnidades(), c.getIngresos(),
                    porcentaje(c.getUnidades(), total)));
        }
        return resultado;
    }

    private BigDecimal porcentaje(long parte, long total) {
        if (total == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(parte)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    }
    // Métodos compatibles con FrmAnalisisDemanda

public List<DemandaProducto> obtenerDemandaPorProducto(int simulacionId)
        throws SQLException {
    return obtenerProductos(simulacionId);
}

public List<DemandaCategoria> obtenerDemandaPorCategoria(int simulacionId)
        throws SQLException {
    return obtenerCategorias(simulacionId);
}

public BigDecimal obtenerPromedioDiarioGeneral(int simulacionId)
        throws SQLException {

    ResumenDemanda resumen = obtenerResumen(simulacionId);

    if (resumen.getDias() == 0) {
        return BigDecimal.ZERO;
    }

    return BigDecimal.valueOf(resumen.getUnidades())
            .divide(
                    BigDecimal.valueOf(resumen.getDias()),
                    2,
                    RoundingMode.HALF_UP
            );
}
public List<DemandaPeriodo> obtenerDemandaMensual(int simulacionId)
        throws SQLException {
    return oad.obtenerDemandaMensual(simulacionId);
}

public List<DemandaPeriodo> obtenerDemandaPorDiaSemana(int simulacionId)
        throws SQLException {
    return oad.obtenerDemandaPorDiaSemana(simulacionId);
}
}
