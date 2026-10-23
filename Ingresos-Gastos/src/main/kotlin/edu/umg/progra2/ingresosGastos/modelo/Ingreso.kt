package edu.umg.progra2.ingresosGastos.modelo

data class Ingreso(
    val id: Int,
    val descripcion: String,
    val monto: Double,
    val fuente: String
)