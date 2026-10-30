package edu.umg.controlingresosgastos

import android.util.Log
import edu.umg.programacion2.gastos.servicio.ControlGastos
import edu.umg.programacion2.gastos.datos.GastoRepositoryEnMemoria

object DataRepository {
    private const val TAG = "DEBUG_FATIMA"

    // Instancia única del JAR
    val controlGastos: ControlGastos = ControlGastos(GastoRepositoryEnMemoria())

    // 💡 LISTA LOCAL PROPIA: Almacena tanto objetos Ingreso como Gasto de forma segura
    private val listaGlobalLocal = mutableListOf<Any>()

    // Método universal para registrar movimientos
    fun registrarMovimiento(objetoGasto: Any) {
        try {
            val metodo = controlGastos.javaClass.methods.firstOrNull {
                it.parameterTypes.size == 1
            }
            metodo?.invoke(controlGastos, objetoGasto)
        } catch (e: Exception) {
            Log.e(TAG, "Aviso del JAR: ${e.message}")
        }

        listaGlobalLocal.add(objetoGasto)
        Log.d(TAG, "¡Movimiento guardado con éxito! Total en lista local: ${listaGlobalLocal.size}")
    }

    // Método para obtener todos los movimientos registrados
    fun obtenerListaMovimientos(): List<*> {
        return listaGlobalLocal
    }
}