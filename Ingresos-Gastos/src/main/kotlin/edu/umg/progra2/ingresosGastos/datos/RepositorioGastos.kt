package edu.umg.progra2.ingresosGastos.datos

import edu.umg.progra2.ingresosGastos.modelo.Gasto
import edu.umg.progra2.ingresosGastos.modelo.Ingreso

class RepositorioGastos {
    // Listas en memoria para almacenar los registros
    private val listaGastos = mutableListOf<Gasto>()
    private val listaIngresos = mutableListOf<Ingreso>()

    // Métodos para Gastos
    fun agregarGasto(gasto: Gasto) {
        listaGastos.add(gasto)
    }

    fun obtenerGastos(): List<Gasto> {
        return listaGastos
    }

    // Métodos para Ingresos
    fun agregarIngreso(ingreso: Ingreso) {
        listaIngresos.add(ingreso)
    }

    fun obtenerIngresos(): List<Ingreso> {
        return listaIngresos
    }
}