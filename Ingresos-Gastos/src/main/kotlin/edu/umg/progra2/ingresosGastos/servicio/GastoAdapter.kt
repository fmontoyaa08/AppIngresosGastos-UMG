package edu.umg.progra2.ingresosGastos.servicio

import edu.umg.progra2.ingresosGastos.modelo.Gasto

class GastoAdapter {
    fun formatearGastos(listaGastos: List<Gasto>): String {
        if (listaGastos.isEmpty()) {
            return "No hay gastos registrados."
        }

        val sb = StringBuilder()
        val total = listaGastos.sumOf { it.monto }

        sb.append("=== MIS GASTOS ===\n")
        sb.append(String.format("%d gastos | Total: Q %.2f\n", listaGastos.size, total))
        sb.append("----------------------------------------\n")

        for (gasto in listaGastos) {
            sb.append("• ${gasto.descripcion}\n")
            sb.append("  Categoría: ${gasto.categoria}\n")
            sb.append(String.format("  Monto: Q %.2f\n", gasto.monto))
            sb.append("----------------------------------------\n")
        }

        return sb.toString()
    }
}