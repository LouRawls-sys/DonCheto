/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.math.BigDecimal;

/**
 *
 * @author luisg
 */
public class DemandaPeriodo {

    private final String periodo;
    private final long unidades;
    private final BigDecimal ingresos;
    private final BigDecimal promedioDiario;

    public DemandaPeriodo(String periodo, long unidades,
            BigDecimal ingresos, BigDecimal promedioDiario) {
        this.periodo = periodo;
        this.unidades = unidades;
        this.ingresos = ingresos;
        this.promedioDiario = promedioDiario;
    }

    public String getPeriodo() {
        return periodo;
    }

    public long getUnidades() {
        return unidades;
    }

    public BigDecimal getIngresos() {
        return ingresos;
    }

    public BigDecimal getPromedioDiario() {
        return promedioDiario;
    }
}

