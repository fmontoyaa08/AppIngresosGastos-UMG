package edu.umg.controlingresosgastos

import android.content.Context
import androidx.room.Room
import edu.umg.programacion2.gastos.servicio.ControlGastos
import edu.umg.controlingresosgastos.datos.AppDatabase
import edu.umg.controlingresosgastos.datos.GastoRepositoryRoom

object Dependencias {

    lateinit var control: ControlGastos
        private set

    fun iniciar(context: Context) {
        val baseDeDatos = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "gastos.db"
        ).build()

        control = ControlGastos(GastoRepositoryRoom(baseDeDatos.gastoDao()))
    }
}