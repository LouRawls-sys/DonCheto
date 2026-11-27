package pruebas;

import Modelo.DemandaCategoria;
import Modelo.DemandaPeriodo;
import Modelo.DemandaProducto;
import Modelo.ResumenDemanda;
import Servicio.ServicioBI;
import java.util.List;

public class PruebaBI {
    public static void main(String[] args) {
        int simulacionId = 1;
        ServicioBI servicio = new ServicioBI();

        try {
            ResumenDemanda r = servicio.obtenerResumen(simulacionId);
            List<DemandaProducto> productos = servicio.obtenerProductos(simulacionId);
            List<DemandaCategoria> categorias = servicio.obtenerCategorias(simulacionId);
            List<DemandaPeriodo> mensual =  servicio.obtenerDemandaMensual(simulacionId);

List<DemandaPeriodo> semanal =
        servicio.obtenerDemandaPorDiaSemana(simulacionId);

            System.out.println("===== VALIDACION MODULO BI =====");
            System.out.println("Unidades: " + r.getUnidades());
            System.out.println("Ingresos: $" + r.getIngresos());
            System.out.println("Productos: " + r.getProductos());
            System.out.println("Dias: " + r.getDias());
            System.out.println("Filas por producto: " + productos.size());
            System.out.println("Categorias encontradas: " + categorias.size());

            System.out.println("\nTOP 5 PRODUCTOS");
            for (int i = 0; i < Math.min(5, productos.size()); i++) {
                DemandaProducto p = productos.get(i);
                System.out.println((i + 1) + ". " + p.getNombre()
                        + " | " + p.getUnidades() + " unidades"
                        + " | " + p.getParticipacion() + "%");
            }

            System.out.println("\nDEMANDA POR CATEGORIA");
            for (DemandaCategoria c : categorias) {
                System.out.println(c.getCategoria() + " | "
                        + c.getUnidades() + " unidades | "
                        + c.getParticipacion() + "%");
            }
            System.out.println("\nDEMANDA MENSUAL");

for (DemandaPeriodo p : mensual) {
    System.out.println(p.getPeriodo()
            + " | " + p.getUnidades() + " unidades"
            + " | $" + p.getIngresos()
            + " | Promedio diario: " + p.getPromedioDiario());
}

System.out.println("\nDEMANDA POR DIA DE LA SEMANA");

for (DemandaPeriodo p : semanal) {
    System.out.println(p.getPeriodo()
            + " | " + p.getUnidades() + " unidades"
            + " | $" + p.getIngresos()
            + " | Promedio diario: " + p.getPromedioDiario());
}
        } catch (Exception e) {
            System.err.println("Fallo la prueba BI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
