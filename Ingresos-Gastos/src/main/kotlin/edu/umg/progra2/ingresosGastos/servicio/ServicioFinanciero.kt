package edu.umg.progra2.ingresosGastos.servicio

import edu.umg.progra2.ingresosGastos.datos.RepositorioGastos
import edu.umg.progra2.ingresosGastos.modelo.Gasto
import edu.umg.progra2.ingresosGastos.modelo.Ingreso

class ServicioFinanciero(private val repositorio: RepositorioGastos) {

    fun registrarGasto(id: Int, descripcion: String, monto: Double, categoria: String) {
        val gasto = Gasto(id, descripcion, monto, categoria)
        repositorio.agregarGasto(gasto)
    }

    fun registrarIngreso(id: Int, descripcion: String, monto: Double, fuente: String) {
        val ingreso = Ingreso(id, descripcion, monto, fuente)
        repositorio.agregarIngreso(ingreso)
    }

    fun calcularTotalGastos(): Double {
        return repositorio.obtenerGastos().sumOf { it.monto }
    }

    fun calcularTotalIngresos(): Double {
        return repositorio.obtenerIngresos().sumOf { it.monto }
    }

    fun calcularBalance(): Double {
        return calcularTotalIngresos() - calcularTotalGastos()
    }
}