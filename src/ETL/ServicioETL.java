package ETL;

import Conexion.Conexion;
import ETL.ExtractorDatos.RegistroVenta;
import ETL.TransformadorDatos.RegistroDemanda;
import ETL.TransformadorDatos.ResultadoTransformacion;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Coordina Extract, Transform y Load.
 */
public class ServicioETL {

    private final ExtractorDatos extractor = new ExtractorDatos();
    private final TransformadorDatos transformador = new TransformadorDatos();
    private final CargadorDataWarehouse cargador = new CargadorDataWarehouse();

    public ResultadoETL ejecutar() throws SQLException {
        try (Connection conexion = Conexion.conectar()) {
            if (conexion == null) {
                throw new SQLException(
                        "No fue posible establecer la conexión con MySQL.");
            }

            // EXTRACT
            List<RegistroVenta> extraidos = extractor.extraer(conexion);

            // TRANSFORM
            ResultadoTransformacion transformacion =
                    transformador.transformar(extraidos);

            List<RegistroDemanda> transformados =
                    transformacion.getRegistros();

            // LOAD
            int cargados = cargador.cargar(conexion, transformados);

            return new ResultadoETL(
                    extraidos.size(),
                    transformacion.getDescartados(),
                    transformados.size(),
                    cargados
            );
        }
    }

    public static class ResultadoETL {
        private final int registrosExtraidos;
        private final int registrosDescartados;
        private final int gruposTransformados;
        private final int registrosCargados;

        public ResultadoETL(int registrosExtraidos, int registrosDescartados,
                int gruposTransformados, int registrosCargados) {
            this.registrosExtraidos = registrosExtraidos;
            this.registrosDescartados = registrosDescartados;
            this.gruposTransformados = gruposTransformados;
            this.registrosCargados = registrosCargados;
        }

        public int getRegistrosExtraidos() { return registrosExtraidos; }
        public int getRegistrosDescartados() { return registrosDescartados; }
        public int getGruposTransformados() { return gruposTransformados; }
        public int getRegistrosCargados() { return registrosCargados; }
    }
}
