package edu.umg.progra2.ingresosGastos

import edu.umg.progra2.ingresosGastos.consola.MenuConsola
import edu.umg.progra2.ingresosGastos.datos.RepositorioGastos
import edu.umg.progra2.ingresosGastos.servicio.ServicioFinanciero

fun main() {
    println("=== SISTEMA DE INGRESOS Y GASTOS (CONSOLA) ===")

    // 1. Instanciamos el repositorio de datos en memoria
    val repositorio = RepositorioGastos()

    // 2. Instanciamos el servicio financiero inyectándole el repositorio
    val servicio = ServicioFinanciero(repositorio)

    // 3. Instanciamos el menú de consola inyectándole el servicio
    val menu = MenuConsola(servicio)

    // 4. Arrancamos la aplicación de consola
    menu.iniciar()
}