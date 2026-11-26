/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package conexion;

import Conexion.Conexion;
import OAD.OAD_Demanda;
import java.sql.Connection;
import java.math.BigDecimal;

public class pruebas {

    public static void main(String[] args) {

        // 1. Probar conexion
        try (Connection cn = Conexion.conectar()) {

            if (cn == null) {
                System.out.println("No se pudo establecer la conexion.");
                return;
            }

            System.out.println("Conexion comprobada correctamente.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }

        // 2. Consultar indicadores
        OAD_Demanda demanda = new OAD_Demanda();

        int simulacionId = 1;

        long unidades = demanda.obtenerTotalUnidades(simulacionId);

        BigDecimal ingresos =
                demanda.obtenerIngresos(simulacionId);

        int productos =
                demanda.obtenerTotalProductos(simulacionId);

        int dias =
                demanda.obtenerTotalDias(simulacionId);

        // 3. Mostrar resultados
        System.out.println("\n===== RESUMEN SATD =====");

        System.out.println("Unidades demandadas: " + unidades);
        System.out.println("Ingresos simulados: $" + ingresos);
        System.out.println("Productos analizados: " + productos);
        System.out.println("Dias analizados: " + dias);

    }
}
