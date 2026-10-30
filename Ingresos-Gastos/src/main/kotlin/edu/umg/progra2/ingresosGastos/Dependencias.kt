package edu.umg.progra2.ingresosGastos

import edu.umg.progra2.ingresosGastos.datos.RepositorioGastos // O el repositorio en memoria que uses para consola
import edu.umg.progra2.ingresosGastos.servicio.ServicioFinanciero

object Dependencias {
    // Creamos una instancia del repositorio de memoria para la consola
    private val repositorioGastos = RepositorioGastos()

    val servicioFinanciero: ServicioFinanciero by lazy {
        ServicioFinanciero(repositorioGastos)
    }
}