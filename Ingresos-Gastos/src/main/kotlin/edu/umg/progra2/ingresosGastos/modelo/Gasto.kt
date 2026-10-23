package edu.umg.progra2.ingresosGastos.modelo

data class Gasto(
    val id: Int,
    val descripcion: String,
    val monto: Double,
    val categoria: String,
    val fecha: String = "2026-10-22" // Fecha por defecto o la que ingrese el usuario
)