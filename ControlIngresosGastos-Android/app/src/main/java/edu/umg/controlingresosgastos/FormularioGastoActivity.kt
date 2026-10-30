package edu.umg.controlingresosgastos

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class FormularioGastoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_gasto)

        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val etMonto = findViewById<EditText>(R.id.etMonto)
        val etFecha = findViewById<EditText>(R.id.etFecha)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarMovimiento)

        // Saber si viene de Ingreso o Gasto desde el Intent
        val tipoMovimiento = intent.getStringExtra("TIPO_MOVIMIENTO") ?: "GASTO"

        // Configurar el selector de fecha (Calendario)
        etFecha?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                etFecha.setText(formattedDate)
            }, year, month, day).show()
        }

        btnGuardar?.setOnClickListener {
            val descripcionInput = etDescripcion?.text?.toString()?.trim() ?: ""
            val montoStr = etMonto?.text?.toString()?.trim() ?: ""
            val fecha = etFecha?.text?.toString()?.trim() ?: ""

            if (descripcionInput.isEmpty() || montoStr.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos (incluyendo la fecha)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monto = montoStr.toDoubleOrNull() ?: 0.0

            // Si es ingreso, le anteponemos la etiqueta [INGRESO] para que tu sumatoria lo detecte
            val descripcionFinal = if (tipoMovimiento == "INGRESO") {
                "[INGRESO] $descripcionInput"
            } else {
                descripcionInput
            }

            val categoriaFinal = if (tipoMovimiento == "INGRESO") "Ingresos" else "General"

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val database = androidx.room.Room.databaseBuilder(
                        applicationContext,
                        edu.umg.controlingresosgastos.datos.AppDatabase::class.java,
                        "gastos.db"
                    ).build()

                    val nuevoGasto = edu.umg.controlingresosgastos.datos.GastoEntity(
                        descripcion = descripcionFinal,
                        monto = monto,
                        fecha = fecha,
                        categoria = categoriaFinal
                    )

                    database.gastoDao().insertar(nuevoGasto)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@FormularioGastoActivity, "Movimiento guardado con éxito", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@FormularioGastoActivity, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}