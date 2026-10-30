package edu.umg.progra2.ingresosGastos.servicio

import edu.umg.progra2.ingresosGastos.modelo.Gasto

class GastoAdapter {
    fun formatearGastos(listaGastos: List<Gasto>): String {
        if (listaGastos.isEmpty()) {
            return "No hay gastos registrados."
        }

        // Códigos ANSI para el color rojo y resetear el estilo
        val rojo = "\u001B[31m"
        val reset = "\u001B[0m"

        val sb = StringBuilder()
        val total = listaGastos.sumOf { it.monto }

        sb.append("=== MIS GASTOS ===\n")
        sb.append(String.format("%s%d gastos | Total: Q %.2f%s\n", rojo, listaGastos.size, total, reset))
        sb.append("----------------------------------------\n")

        for (gasto in listaGastos) {
            sb.append("• ${gasto.descripcion}\n")
            sb.append("  Categoría: ${gasto.categoria}\n")
            sb.append(String.format("  Monto: %sQ %.2f%s\n", rojo, gasto.monto, reset))
            sb.append("----------------------------------------\n")
        }

        return sb.toString()
    }
}