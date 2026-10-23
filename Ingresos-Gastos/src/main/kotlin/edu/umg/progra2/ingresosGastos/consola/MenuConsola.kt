package edu.umg.progra2.ingresosGastos.consola

import edu.umg.progra2.ingresosGastos.servicio.ServicioFinanciero

class MenuConsola(private val servicio: ServicioFinanciero) {

    fun iniciar() {
        var opcion = 0
        do {
            println("\n=== GESTIÓN DE INGRESOS Y GASTOS ===")
            println("1. Registrar Ingreso")
            println("2. Registrar Gasto")
            println("3. Ver Resumen y Balance")
            println("4. Salir")
            print("Seleccione una opción: ")

            opcion = readLine()?.toIntOrNull() ?: 0

            when (opcion) {
                1 -> {
                    print("ID del ingreso: ")
                    val id = readLine()?.toIntOrNull() ?: 0
                    print("Descripción: ")
                    val desc = readLine() ?: ""
                    print("Monto: ")
                    val monto = readLine()?.toDoubleOrNull() ?: 0.0
                    print("Fuente (Ej. Salario, Freelance): ")
                    val fuente = readLine() ?: ""

                    servicio.registrarIngreso(id, desc, monto, fuente)
                    println("¡Ingreso registrado con éxito!")
                }
                2 -> {
                    print("ID del gasto: ")
                    val id = readLine()?.toIntOrNull() ?: 0
                    print("Descripción: ")
                    val desc = readLine() ?: ""
                    print("Monto: ")
                    val monto = readLine()?.toDoubleOrNull() ?: 0.0
                    print("Categoría (Ej. Comida, Alquiler): ")
                    val categoria = readLine() ?: ""

                    servicio.registrarGasto(id, desc, monto, categoria)
                    println("¡Gasto registrado con éxito!")
                }
                3 -> {
                    println("\n--- RESUMEN FINANCIERO ---")
                    println("Total Ingresos: Q${servicio.calcularTotalIngresos()}")
                    println("Total Gastos: Q${servicio.calcularTotalGastos()}")
                    println("Balance Final: Q${servicio.calcularBalance()}")
                }
                4 -> println("Saliendo de la aplicación. ¡Adiós!")
                else -> println("Opción no válida. Intente de nuevo.")
            }
        } while (opcion != 4)
    }
}