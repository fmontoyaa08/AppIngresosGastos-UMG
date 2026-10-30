package edu.umg.progra2.ingresosGastos.consola

import edu.umg.progra2.ingresosGastos.servicio.ServicioFinanciero

class MenuConsola(private val servicio: ServicioFinanciero) {

    fun iniciar() {
        var opcion = 0

        // Códigos ANSI para colores en la consola
        val rojo = "\u001B[31m"
        val reset = "\u001B[0m"

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
                    print("Seleccione la moneda (1. Quetzales Q / 2. Dólares $): ")
                    val opcionMoneda = readLine()?.toIntOrNull() ?: 1
                    val simbolo = if (opcionMoneda == 2) "$" else "Q"

                    print("ID del ingreso: ")
                    val id = readLine()?.toIntOrNull() ?: 0
                    print("Descripción: ")
                    val desc = readLine() ?: ""
                    print("Monto ($simbolo): ")
                    val monto = readLine()?.toDoubleOrNull() ?: 0.0
                    print("Fuente (Ej. Salario, Freelance): ")
                    val fuente = readLine() ?: ""

                    servicio.registrarIngreso(id, desc, monto, fuente)
                    println("¡Ingreso registrado con éxito en $simbolo!")
                }
                2 -> {
                    print("Seleccione la moneda (1. Quetzales Q / 2. Dólares $): ")
                    val opcionMoneda = readLine()?.toIntOrNull() ?: 1
                    val simbolo = if (opcionMoneda == 2) "$" else "Q"

                    print("ID del gasto: ")
                    val id = readLine()?.toIntOrNull() ?: 0
                    print("Descripción: ")
                    val desc = readLine() ?: ""
                    print("Monto ($simbolo): ")
                    val monto = readLine()?.toDoubleOrNull() ?: 0.0
                    print("Categoría (Ej. Comida, Alquiler): ")
                    val categoria = readLine() ?: ""
                    print("Fecha (Ej. 2026-10-28): ")
                    val fecha = readLine() ?: ""

                    servicio.registrarGasto(id, desc, monto, categoria, fecha)
                    println("¡Gasto registrado con éxito en $simbolo!")
                }
                3 -> {
                    println("\n--- RESUMEN FINANCIERO ---")
                    println("Total Ingresos: Q${servicio.calcularTotalIngresos()}")
                    // Aquí se imprime el total de gastos en ROJO brillante
                    println("${rojo}Total Gastos: Q${servicio.calcularTotalGastos()}${reset}")
                    println("Balance Final: Q${servicio.calcularBalance()}")
                }
                4 -> println("Saliendo de la aplicación. ¡Adiós!")
                else -> println("Opción no válida. Intente de nuevo.")
            }
        } while (opcion != 4)
    }
}