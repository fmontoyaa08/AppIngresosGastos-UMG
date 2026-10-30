package edu.umg.controlingresosgastos

// Tipos de ingreso (venta de software a negocios, sector agropecuario, etc.)
enum class TipoIngreso {
    SALARIO,
    RENTA,
    JUBILACION,
    TRABAJO_EXTRA,
    SOFTWARE_NEGOCIOS, // Venta de programas o sistemas para negocios
    AGROPECUARIO       // Proyectos o sistemas del sector agropecuario
}

// Tipos de gasto actualizados (Supermercado, Restaurantes, Gasolina, Gas, Ropa, Cuota de carro)
enum class TipoGasto {
    PAGO_CARRO,        // Pagar un carro por pagos / cuotas
    SUPERMERCADO,      // Compras de despensa / supermercado
    RESTAURANTES,      // Comida fuera, restaurantes
    GASOLINA,          // Combustible
    GAS,               // Gas propano / hogar
    ROPA,              // Compras de ropa y calzado
    TRANSPORTE,
    EDUCACION,
    OTROS
}

// Modelo para los Ingresos
data class Ingreso(
    val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val tipo: TipoIngreso,
    val fecha: String,
    val metodoPago: String = "Efectivo"
)

// Modelo para los Gastos
data class Gasto(
    val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val tipo: TipoGasto = TipoGasto.OTROS,
    val fecha: String,
    val metodoPago: String = "Efectivo"
)

// Modelo para el Perfil de Usuario
data class PerfilUsuario(
    var nombre: String,
    var correo: String,
    var fechaNacimiento: String,
    var salario: Double
)