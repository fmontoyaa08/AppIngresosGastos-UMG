package edu.umg.controlingresosgastos.datos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.umg.controlingresosgastos.datos.GastoDao
import edu.umg.controlingresosgastos.datos.GastoEntity

@Database(entities = [GastoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gastoDao(): GastoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pistapp_database" // O el nombre que le tengas a tu base de datos
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

